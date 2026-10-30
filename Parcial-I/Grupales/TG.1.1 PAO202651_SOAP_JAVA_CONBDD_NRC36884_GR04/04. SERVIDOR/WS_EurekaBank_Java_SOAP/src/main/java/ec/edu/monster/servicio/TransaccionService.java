package ec.edu.monster.servicio;

import ec.edu.monster.dao.CuentaDAO;
import ec.edu.monster.dao.EmpleadoDAO;
import ec.edu.monster.dao.MovimientoDAO;
import ec.edu.monster.dao.SucursalDAO;
import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.modelo.MovimientoDTO;
import ec.edu.monster.modelo.OperacionRespuestaDTO;
import ec.edu.monster.modelo.SucursalDTO;
import ec.edu.monster.util.ConexionDB;
import ec.edu.monster.util.GestorConcurrencia;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio Transaccional Bancario ACID con Exclusión Mutua Pesimista (SELECT FOR UPDATE)
 * y Sincronización MashUp en Tiempo Real (VERDE / ROJO) - SRS RQF-002 a RQF-005.
 */
public class TransaccionService {

    private static final AtomicLong CORRELATIVO = new AtomicLong(System.currentTimeMillis() % 100000);

    private final CuentaDAO cuentaDAO = new CuentaDAO();
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final GestorConcurrencia gestorConcurrencia = GestorConcurrencia.getInstancia();

    private long obtenerLatenciaBloqueoMs() {
        String val = System.getProperty("eurekabank.lock.delay.ms", "3500");
        try {
            return Math.max(0L, Long.parseLong(val));
        } catch (NumberFormatException e) {
            return 3500L;
        }
    }

    private String generarNumeroOperacion(String prefijo) {
        return prefijo + "-" + (System.currentTimeMillis() / 1000) + "-" + CORRELATIVO.incrementAndGet();
    }

    private String ahora() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public List<SucursalDTO> listarSucursales() {
        return sucursalDAO.listarSucursales();
    }

    public List<CuentaDetalleDTO> listarCuentasPorSucursal(int sucursalId) {
        return cuentaDAO.listarCuentasPorSucursal(sucursalId);
    }

    public CuentaDetalleDTO obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaDAO.obtenerCuentaPorNumero(numeroCuenta);
    }

    public List<CuentaDetalleDTO> consultarCuentasPorCliente(String dni) {
        return cuentaDAO.obtenerCuentasPorDni(dni);
    }

    public List<MovimientoDTO> consultarExtracto(String numeroCuenta) {
        return movimientoDAO.listarExtracto(numeroCuenta);
    }

    public OperacionRespuestaDTO realizarDeposito(String numeroCuenta, double monto, int idEmpleado, int ventanilla) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "Debe especificar el número de cuenta.");
        }
        if (monto <= 0) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "El monto del depósito debe ser mayor a $0.00.");
        }

        String cta = numeroCuenta.trim();
        int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
        int empId = idEmpleado <= 0 ? 1 : idEmpleado;
        String usuarioEmp = empleadoDAO.obtenerNombreUsuarioPorId(empId);

        if (!gestorConcurrencia.bloquearCuenta(cta, nroVentanilla, usuarioEmp)) {
            GestorConcurrencia.InfoBloqueo info = gestorConcurrencia.obtenerInfoBloqueo(cta);
            return OperacionRespuestaDTO.error("CONCURRENCIA_BLOQUEADA",
                    "CONCURRENCIA [ROJO]: La cuenta " + cta + " está siendo operada actualmente en Ventanilla "
                    + info.getVentanilla() + " (" + info.getUsuario() + "). Intente en unos segundos.");
        }

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            CuentaDetalleDTO cuenta = cuentaDAO.obtenerCuentaForUpdate(con, cta);
            if (cuenta == null) {
                con.rollback();
                return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta " + cta + " no existe.");
            }
            if ("Bloqueada".equalsIgnoreCase(cuenta.getEstadoCuenta())) {
                con.rollback();
                return OperacionRespuestaDTO.error("CUENTA_BLOQUEADA",
                        "La cuenta " + cta + " tiene bloqueo administrativo. Solicite desbloqueo al Superadmin Monster.");
            }

            long delay = obtenerLatenciaBloqueoMs();
            if (delay > 0) {
                Thread.sleep(delay);
            }

            double saldoAnterior = cuenta.getSaldo();
            double nuevoSaldo = Math.round((saldoAnterior + monto) * 100.0) / 100.0;
            cuentaDAO.actualizarSaldo(con, cta, nuevoSaldo);

            String nroOp = generarNumeroOperacion("DEP");
            MovimientoDTO mov = new MovimientoDTO();
            mov.setNumeroOperacion(nroOp);
            mov.setNumeroCuenta(cta);
            mov.setIdEmpleado(empId);
            mov.setVentanilla(nroVentanilla);
            mov.setTipo("Deposito");
            mov.setMonto(monto);
            mov.setSaldoResultante(nuevoSaldo);
            mov.setDescripcion("Depósito en Ventanilla " + nroVentanilla + " por " + usuarioEmp);
            movimientoDAO.registrarMovimiento(con, mov);

            con.commit();

            OperacionRespuestaDTO ok = new OperacionRespuestaDTO();
            ok.setExito(true);
            ok.setCodigo("OK");
            ok.setNumeroOperacion(nroOp);
            ok.setNumeroCuenta(cta);
            ok.setSaldoAnterior(saldoAnterior);
            ok.setMonto(monto);
            ok.setSaldoActual(nuevoSaldo);
            ok.setFechaHora(ahora());
            ok.setMensaje(String.format("Depósito exitoso en cuenta %s. Op: %s | Nuevo saldo: $%.2f", cta, nroOp, nuevoSaldo));
            return ok;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            rollbackSilencioso(con);
            return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Operación interrumpida: " + ie.getMessage());
        } catch (Exception e) {
            rollbackSilencioso(con);
            return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Fallo transaccional (ROLLBACK aplicado): " + e.getMessage());
        } finally {
            cerrarConexion(con);
            gestorConcurrencia.liberarCuenta(cta);
        }
    }

    public OperacionRespuestaDTO realizarRetiro(String numeroCuenta, double monto, int idEmpleado, int ventanilla) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "Debe especificar el número de cuenta.");
        }
        if (monto <= 0) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "El monto del retiro debe ser mayor a $0.00.");
        }

        String cta = numeroCuenta.trim();
        int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
        int empId = idEmpleado <= 0 ? 1 : idEmpleado;
        String usuarioEmp = empleadoDAO.obtenerNombreUsuarioPorId(empId);

        if (!gestorConcurrencia.bloquearCuenta(cta, nroVentanilla, usuarioEmp)) {
            GestorConcurrencia.InfoBloqueo info = gestorConcurrencia.obtenerInfoBloqueo(cta);
            return OperacionRespuestaDTO.error("CONCURRENCIA_BLOQUEADA",
                    "CONCURRENCIA [ROJO]: La cuenta " + cta + " está siendo operada actualmente en Ventanilla "
                    + info.getVentanilla() + " (" + info.getUsuario() + "). Intente en unos segundos.");
        }

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            CuentaDetalleDTO cuenta = cuentaDAO.obtenerCuentaForUpdate(con, cta);
            if (cuenta == null) {
                con.rollback();
                return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta " + cta + " no existe.");
            }
            if ("Bloqueada".equalsIgnoreCase(cuenta.getEstadoCuenta())) {
                con.rollback();
                return OperacionRespuestaDTO.error("CUENTA_BLOQUEADA",
                        "La cuenta " + cta + " tiene bloqueo administrativo. Solicite desbloqueo al Superadmin Monster.");
            }
            if (cuenta.getSaldo() < monto) {
                con.rollback();
                return OperacionRespuestaDTO.error("SALDO_INSUFICIENTE",
                        String.format("SALDO INSUFICIENTE: Disponible $%.2f, solicitado $%.2f.", cuenta.getSaldo(), monto));
            }

            long delay = obtenerLatenciaBloqueoMs();
            if (delay > 0) {
                Thread.sleep(delay);
            }

            double saldoAnterior = cuenta.getSaldo();
            double nuevoSaldo = Math.round((saldoAnterior - monto) * 100.0) / 100.0;
            cuentaDAO.actualizarSaldo(con, cta, nuevoSaldo);

            String nroOp = generarNumeroOperacion("RET");
            MovimientoDTO mov = new MovimientoDTO();
            mov.setNumeroOperacion(nroOp);
            mov.setNumeroCuenta(cta);
            mov.setIdEmpleado(empId);
            mov.setVentanilla(nroVentanilla);
            mov.setTipo("Retiro");
            mov.setMonto(monto);
            mov.setSaldoResultante(nuevoSaldo);
            mov.setDescripcion("Retiro en Ventanilla " + nroVentanilla + " por " + usuarioEmp);
            movimientoDAO.registrarMovimiento(con, mov);

            con.commit();

            OperacionRespuestaDTO ok = new OperacionRespuestaDTO();
            ok.setExito(true);
            ok.setCodigo("OK");
            ok.setNumeroOperacion(nroOp);
            ok.setNumeroCuenta(cta);
            ok.setSaldoAnterior(saldoAnterior);
            ok.setMonto(monto);
            ok.setSaldoActual(nuevoSaldo);
            ok.setFechaHora(ahora());
            ok.setMensaje(String.format("Retiro exitoso en cuenta %s. Op: %s | Nuevo saldo: $%.2f", cta, nroOp, nuevoSaldo));
            return ok;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            rollbackSilencioso(con);
            return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Operación interrumpida: " + ie.getMessage());
        } catch (Exception e) {
            rollbackSilencioso(con);
            return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Fallo transaccional (ROLLBACK aplicado): " + e.getMessage());
        } finally {
            cerrarConexion(con);
            gestorConcurrencia.liberarCuenta(cta);
        }
    }

    public OperacionRespuestaDTO realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto, int idEmpleado, int ventanilla) {
        if (cuentaOrigen == null || cuentaOrigen.isBlank() || cuentaDestino == null || cuentaDestino.isBlank()) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "Debe indicar cuenta origen y cuenta destino.");
        }
        String orig = cuentaOrigen.trim();
        String dest = cuentaDestino.trim();
        if (orig.equalsIgnoreCase(dest)) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "La cuenta origen y destino no pueden ser la misma.");
        }
        if (monto <= 0) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "El monto de transferencia debe ser mayor a $0.00.");
        }

        int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
        int empId = idEmpleado <= 0 ? 1 : idEmpleado;
        String usuarioEmp = empleadoDAO.obtenerNombreUsuarioPorId(empId);

        String primera = orig.compareTo(dest) < 0 ? orig : dest;
        String segunda = primera.equals(orig) ? dest : orig;

        if (!gestorConcurrencia.bloquearCuenta(primera, nroVentanilla, usuarioEmp)) {
            GestorConcurrencia.InfoBloqueo info = gestorConcurrencia.obtenerInfoBloqueo(primera);
            return OperacionRespuestaDTO.error("CONCURRENCIA_BLOQUEADA",
                    "CONCURRENCIA [ROJO]: La cuenta " + primera + " está en proceso en Ventanilla " + info.getVentanilla() + ".");
        }
        try {
            if (!gestorConcurrencia.bloquearCuenta(segunda, nroVentanilla, usuarioEmp)) {
                GestorConcurrencia.InfoBloqueo info = gestorConcurrencia.obtenerInfoBloqueo(segunda);
                return OperacionRespuestaDTO.error("CONCURRENCIA_BLOQUEADA",
                        "CONCURRENCIA [ROJO]: La cuenta " + segunda + " está en proceso en Ventanilla " + info.getVentanilla() + ".");
            }
            Connection con = null;
            try {
                con = ConexionDB.getConexion();
                con.setAutoCommit(false);

                // Bloqueo pesimista ordenado para prevenir interbloqueos (Deadlocks)
                CuentaDetalleDTO lock1 = cuentaDAO.obtenerCuentaForUpdate(con, primera);
                CuentaDetalleDTO lock2 = cuentaDAO.obtenerCuentaForUpdate(con, segunda);
                CuentaDetalleDTO cOrig = primera.equals(orig) ? lock1 : lock2;
                CuentaDetalleDTO cDest = primera.equals(dest) ? lock1 : lock2;

                if (cOrig == null) {
                    con.rollback();
                    return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta origen " + orig + " no existe.");
                }
                if (cDest == null) {
                    con.rollback();
                    return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta destino " + dest + " no existe.");
                }
                if ("Bloqueada".equalsIgnoreCase(cOrig.getEstadoCuenta()) || "Bloqueada".equalsIgnoreCase(cDest.getEstadoCuenta())) {
                    con.rollback();
                    return OperacionRespuestaDTO.error("CUENTA_BLOQUEADA", "Una de las cuentas posee bloqueo administrativo.");
                }
                if (cOrig.getSaldo() < monto) {
                    con.rollback();
                    return OperacionRespuestaDTO.error("SALDO_INSUFICIENTE",
                            String.format("SALDO INSUFICIENTE en %s: Disponible $%.2f, solicitado $%.2f.", orig, cOrig.getSaldo(), monto));
                }

                long delay = obtenerLatenciaBloqueoMs();
                if (delay > 0) {
                    Thread.sleep(delay);
                }

                double saldoAnteriorOrig = cOrig.getSaldo();
                double nuevoSaldoOrig = Math.round((saldoAnteriorOrig - monto) * 100.0) / 100.0;
                double nuevoSaldoDest = Math.round((cDest.getSaldo() + monto) * 100.0) / 100.0;

                cuentaDAO.actualizarSaldo(con, orig, nuevoSaldoOrig);
                cuentaDAO.actualizarSaldo(con, dest, nuevoSaldoDest);

                String nroBase = generarNumeroOperacion("TRF");

                MovimientoDTO movSalida = new MovimientoDTO();
                movSalida.setNumeroOperacion(nroBase + "-DB");
                movSalida.setNumeroCuenta(orig);
                movSalida.setIdEmpleado(empId);
                movSalida.setVentanilla(nroVentanilla);
                movSalida.setTipo("Transferencia-Salida");
                movSalida.setMonto(monto);
                movSalida.setSaldoResultante(nuevoSaldoOrig);
                movSalida.setCuentaReferencia(dest);
                movSalida.setDescripcion("Transferencia enviada a " + dest + " (Ventanilla " + nroVentanilla + ")");
                movimientoDAO.registrarMovimiento(con, movSalida);

                MovimientoDTO movEntrada = new MovimientoDTO();
                movEntrada.setNumeroOperacion(nroBase + "-CR");
                movEntrada.setNumeroCuenta(dest);
                movEntrada.setIdEmpleado(empId);
                movEntrada.setVentanilla(nroVentanilla);
                movEntrada.setTipo("Transferencia-Entrada");
                movEntrada.setMonto(monto);
                movEntrada.setSaldoResultante(nuevoSaldoDest);
                movEntrada.setCuentaReferencia(orig);
                movEntrada.setDescripcion("Transferencia recibida de " + orig + " (Ventanilla " + nroVentanilla + ")");
                movimientoDAO.registrarMovimiento(con, movEntrada);

                con.commit();

                OperacionRespuestaDTO ok = new OperacionRespuestaDTO();
                ok.setExito(true);
                ok.setCodigo("OK");
                ok.setNumeroOperacion(nroBase);
                ok.setNumeroCuenta(orig);
                ok.setSaldoAnterior(saldoAnteriorOrig);
                ok.setMonto(monto);
                ok.setSaldoActual(nuevoSaldoOrig);
                ok.setFechaHora(ahora());
                ok.setMensaje(String.format("Transferencia exitosa %s -> %s. Op: %s | Nuevo saldo origen: $%.2f",
                        orig, dest, nroBase, nuevoSaldoOrig));
                return ok;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                rollbackSilencioso(con);
                return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Transferencia interrumpida: " + ie.getMessage());
            } catch (Exception e) {
                rollbackSilencioso(con);
                return OperacionRespuestaDTO.error("ERROR_TRANSACCION", "Fallo en transferencia (ROLLBACK aplicado): " + e.getMessage());
            } finally {
                cerrarConexion(con);
                gestorConcurrencia.liberarCuenta(segunda);
            }
        } finally {
            gestorConcurrencia.liberarCuenta(primera);
        }
    }

    /**
     * Desbloqueo administrativo ejecutado por Superadmin Monster (SRS Sección 9.1).
     */
    public OperacionRespuestaDTO desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "Debe indicar el número de cuenta a desbloquear.");
        }
        String cta = numeroCuenta.trim();
        CuentaDetalleDTO existe = cuentaDAO.obtenerCuentaPorNumero(cta);
        if (existe == null) {
            return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta " + cta + " no existe.");
        }
        gestorConcurrencia.liberarCuenta(cta);
        cuentaDAO.actualizarEstadoCuenta(cta, "Activa");

        OperacionRespuestaDTO ok = new OperacionRespuestaDTO();
        ok.setExito(true);
        ok.setCodigo("OK");
        ok.setNumeroCuenta(cta);
        ok.setSaldoActual(existe.getSaldo());
        ok.setFechaHora(ahora());
        ok.setMensaje("Cuenta " + cta + " liberada y restablecida a VERDE (Libre) por "
                + (usuarioAdmin != null && !usuarioAdmin.isBlank() ? usuarioAdmin : "Monster") + ".");
        return ok;
    }

    /**
     * Bloqueo manual administrativo o de prueba de semáforo MashUp.
     */
    public OperacionRespuestaDTO bloquearCuentaManual(String numeroCuenta, int ventanilla, String usuario) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return OperacionRespuestaDTO.error("ERROR_VALIDACION", "Debe indicar el número de cuenta a bloquear.");
        }
        String cta = numeroCuenta.trim();
        CuentaDetalleDTO existe = cuentaDAO.obtenerCuentaPorNumero(cta);
        if (existe == null) {
            return OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta " + cta + " no existe.");
        }
        boolean okLock = gestorConcurrencia.bloquearCuenta(cta, ventanilla <= 0 ? 1 : ventanilla,
                (usuario == null || usuario.isBlank()) ? "Monster" : usuario);
        if (!okLock) {
            return OperacionRespuestaDTO.error("CONCURRENCIA_BLOQUEADA",
                    "La cuenta " + cta + " ya se encuentra bloqueada en ROJO.");
        }
        OperacionRespuestaDTO ok = new OperacionRespuestaDTO();
        ok.setExito(true);
        ok.setCodigo("OK");
        ok.setNumeroCuenta(cta);
        ok.setSaldoActual(existe.getSaldo());
        ok.setFechaHora(ahora());
        ok.setMensaje("Cuenta " + cta + " marcada en ROJO (En proceso en Ventanilla " + (ventanilla <= 0 ? 1 : ventanilla) + ").");
        return ok;
    }

    private void rollbackSilencioso(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    private void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
