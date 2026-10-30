package ec.edu.monster.controlador;

import ec.edu.monster.modelo.AuthRespuestaDTO;
import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.modelo.MovimientoDTO;
import ec.edu.monster.modelo.OperacionRespuestaDTO;
import ec.edu.monster.modelo.SucursalDTO;
import ec.edu.monster.servicio.SeguridadService;
import ec.edu.monster.servicio.TransaccionService;
import java.util.List;

/**
 * Controlador Orquestador Central del Servidor EurekaBank (Capa de Negocio).
 */
public class OperacionesController {

    private final SeguridadService seguridadService = new SeguridadService();
    private final TransaccionService transaccionService = new TransaccionService();

    public AuthRespuestaDTO autenticarUsuario(String usuario, String clave) {
        return seguridadService.autenticar(usuario, clave);
    }

    public List<SucursalDTO> listarSucursales() {
        return transaccionService.listarSucursales();
    }

    public List<CuentaDetalleDTO> listarCuentasPorSucursal(int sucursalId) {
        return transaccionService.listarCuentasPorSucursal(sucursalId);
    }

    public CuentaDetalleDTO obtenerCuentaPorNumero(String numeroCuenta) {
        return transaccionService.obtenerCuentaPorNumero(numeroCuenta);
    }

    public List<CuentaDetalleDTO> consultarCuentasPorCliente(String dni) {
        return transaccionService.consultarCuentasPorCliente(dni);
    }

    public List<MovimientoDTO> consultarExtracto(String numeroCuenta) {
        return transaccionService.consultarExtracto(numeroCuenta);
    }

    public OperacionRespuestaDTO depositar(String numeroCuenta, double monto, int idEmpleado, int ventanilla) {
        return transaccionService.realizarDeposito(numeroCuenta, monto, idEmpleado, ventanilla);
    }

    public OperacionRespuestaDTO retirar(String numeroCuenta, double monto, int idEmpleado, int ventanilla) {
        return transaccionService.realizarRetiro(numeroCuenta, monto, idEmpleado, ventanilla);
    }

    public OperacionRespuestaDTO transferir(String cuentaOrigen, String cuentaDestino, double monto, int idEmpleado, int ventanilla) {
        return transaccionService.realizarTransferencia(cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla);
    }

    public OperacionRespuestaDTO desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        return transaccionService.desbloquearCuenta(numeroCuenta, usuarioAdmin);
    }

    public OperacionRespuestaDTO bloquearCuentaManual(String numeroCuenta, int ventanilla, String usuario) {
        return transaccionService.bloquearCuentaManual(numeroCuenta, ventanilla, usuario);
    }
}
