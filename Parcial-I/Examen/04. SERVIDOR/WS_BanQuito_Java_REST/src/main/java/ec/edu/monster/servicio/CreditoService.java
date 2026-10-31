package ec.edu.monster.servicio;

import ec.edu.monster.dao.ClienteDAO;
import ec.edu.monster.dao.CreditoDAO;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Credito;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.MontoMaximoDTO;
import ec.edu.monster.modelo.OtorgarCreditoDTO;
import ec.edu.monster.modelo.SujetoCreditoDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditoService {

    public static final double TASA_INTERES_ANUAL = 16.50; // 16.5% anual según enunciado
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final CreditoDAO creditoDAO = new CreditoDAO();

    // =========================================================================
    // ÍTEM 5: Valida si una persona es sujeto de crédito en base a su cédula
    // =========================================================================
    public SujetoCreditoDTO esSujetoCredito(String cedula) {
        SujetoCreditoDTO dto = new SujetoCreditoDTO();
        dto.setCedula(cedula != null ? cedula.trim() : "");

        try {
            // Regla 1: Verificar si el solicitante es cliente del banco
            Cliente cliente = clienteDAO.buscarPorCedula(dto.getCedula());
            if (cliente == null) {
                dto.setEsSujeto(false);
                dto.setCumpleCliente(false);
                dto.setMotivo("Rechazado: La persona solicitante no se encuentra registrada como cliente de BanQuito.");
                return dto;
            }

            dto.setCodCliente(cliente.getCodCliente());
            dto.setNombre(cliente.getNombre());
            dto.setGenero(cliente.getGenero());
            dto.setEdad(cliente.calcularEdad());
            dto.setCumpleCliente(true);

            // Regla 3: Verificar que el cliente no sea menor de 25 años si género es masculino
            if ("M".equalsIgnoreCase(cliente.getGenero()) && dto.getEdad() < 25) {
                dto.setCumpleEdadGenero(false);
            } else {
                dto.setCumpleEdadGenero(true);
            }

            // Regla 2: Verificar que el cliente al menos posea una transacción de depósito en el último mes
            boolean tieneDeposito = clienteDAO.tieneDepositoUltimoMes(cliente.getCodCliente());
            dto.setCumpleDepositoUltimoMes(tieneDeposito);

            // Regla 4: Verificar que el cliente actualmente no tenga un crédito activo en el banco
            boolean tieneCredito = clienteDAO.tieneCreditoActivo(cliente.getCodCliente());
            dto.setCumpleSinCreditoActivo(!tieneCredito);

            // Conclusión
            boolean califica = dto.isCumpleCliente() &&
                               dto.isCumpleDepositoUltimoMes() &&
                               dto.isCumpleEdadGenero() &&
                               dto.isCumpleSinCreditoActivo();

            dto.setEsSujeto(califica);

            if (califica) {
                dto.setMotivo("Aprobado: El cliente cumple con todas las políticas crediticias de BanQuito.");
            } else {
                List<String> motivos = new ArrayList<>();
                if (!dto.isCumpleEdadGenero()) {
                    motivos.add("Restricción de edad: El solicitante masculino tiene " + dto.getEdad() + " años (debe ser mayor o igual a 25 años)");
                }
                if (!dto.isCumpleDepositoUltimoMes()) {
                    motivos.add("Falta de depósitos: El cliente no registra ningún depósito en los últimos 30 días");
                }
                if (!dto.isCumpleSinCreditoActivo()) {
                    motivos.add("Crédito vigente: El cliente ya mantiene un crédito activo en el banco");
                }
                dto.setMotivo("Rechazado: " + String.join("; ", motivos) + ".");
            }

        } catch (SQLException ex) {
            dto.setEsSujeto(false);
            dto.setMotivo("Error en base de datos al validar sujeto de crédito: " + ex.getMessage());
        }

        return dto;
    }

    // =========================================================================
    // ÍTEM 6: Retorna el monto máximo de crédito en base al código de cliente
    // Fórmula: ((Promedio Depósitos - Promedio Retiros) * 30%) * 6
    // =========================================================================
    public MontoMaximoDTO calcularMontoMaximo(int codCliente) {
        MontoMaximoDTO dto = new MontoMaximoDTO();
        dto.setCodCliente(codCliente);

        try {
            Cliente cli = clienteDAO.buscarPorId(codCliente);
            if (cli == null) {
                dto.setMontoMaximo(0.0);
                dto.setDetalleCalculo("Cliente no encontrado en el sistema BanQuito.");
                return dto;
            }

            dto.setNombre(cli.getNombre());
            dto.setCedula(cli.getCedula());

            double avgDep = clienteDAO.promedioDepositosUltimos3Meses(codCliente);
            double avgRet = clienteDAO.promedioRetirosUltimos3Meses(codCliente);
            double dif = Math.round((avgDep - avgRet) * 100.0) / 100.0;

            dto.setPromedioDepositos(avgDep);
            dto.setPromedioRetiros(avgRet);
            dto.setDiferencia(dif);

            if (dif > 0) {
                // Monto Máximo = ((Promedio Depósitos - Promedio Retiros) * 30%) * 6
                double monto = Math.round(((dif * 0.30) * 6.0) * 100.0) / 100.0;
                dto.setMontoMaximo(monto);
                dto.setDetalleCalculo(String.format(
                    "Promedio Depósitos (3 meses): $%.2f - Promedio Retiros (3 meses): $%.2f = Diferencia: $%.2f. Monto Máximo Aprobado = ($%.2f * 30%%) * 6 = $%.2f.",
                    avgDep, avgRet, dif, dif, monto
                ));
            } else {
                dto.setMontoMaximo(0.0);
                dto.setDetalleCalculo(String.format(
                    "Promedio Depósitos: $%.2f es menor o igual a Promedio Retiros: $%.2f. Capacidad crediticia insuficiente ($0.00).",
                    avgDep, avgRet
                ));
            }

        } catch (SQLException ex) {
            dto.setMontoMaximo(0.0);
            dto.setDetalleCalculo("Error al calcular capacidad de crédito: " + ex.getMessage());
        }

        return dto;
    }

    // =========================================================================
    // Generador de Tabla de Amortización Cuota Fija (Sistema Francés)
    // Tasa Mensual = 16.5% / 12 = 0.01375
    // Comprobado al centavo con el caso oficial: Préstamo $450, 6 cuotas = $78.65
    // =========================================================================
    public List<CuotaAmortizacion> generarTablaAmortizacion(double valorPrestamo, int cuotas, double tasaAnual) {
        List<CuotaAmortizacion> tabla = new ArrayList<>();
        if (valorPrestamo <= 0 || cuotas <= 0) return tabla;

        double i = (tasaAnual / 100.0) / 12.0; // 0.01375 mensual
        double n = cuotas;

        // Cuota = P * (i / (1 - (1+i)^-n))
        double cuotaCalc = valorPrestamo * (i / (1.0 - Math.pow(1.0 + i, -n)));
        double cuotaFija = Math.round(cuotaCalc * 100.0) / 100.0;

        // Cuota 0
        double saldo = valorPrestamo;
        tabla.add(new CuotaAmortizacion(0, 0.0, 0.0, 0.0, saldo));

        for (int k = 1; k <= cuotas; k++) {
            double interes = Math.round((saldo * i) * 100.0) / 100.0;
            double capital;
            double cuotaReal = cuotaFija;

            if (k == cuotas) {
                // Ajuste de centavos en la última cuota para saldar exactamente en 0.00
                capital = saldo;
                cuotaReal = Math.round((capital + interes) * 100.0) / 100.0;
                saldo = 0.0;
            } else {
                capital = Math.round((cuotaFija - interes) * 100.0) / 100.0;
                saldo = Math.round((saldo - capital) * 100.0) / 100.0;
            }

            tabla.add(new CuotaAmortizacion(k, cuotaReal, interes, capital, saldo));
        }

        return tabla;
    }

    // =========================================================================
    // ÍTEM 7: Otorga el crédito y crea la tabla de amortización en BDD
    // Parámetros: Código Cliente, Precio Celular, Número Cuotas
    // =========================================================================
    public OtorgarCreditoDTO otorgarCredito(int codCliente, double precioTelefono, int numeroCuotas) {
        OtorgarCreditoDTO res = new OtorgarCreditoDTO();
        res.setCodCliente(codCliente);
        res.setPrecioTelefono(precioTelefono);
        res.setNumeroCuotas(numeroCuotas);

        if (numeroCuotas < 3 || numeroCuotas > 18) {
            res.setExito(false);
            res.setMensaje("El número de cuotas (" + numeroCuotas + ") debe ser mayor o igual a 3 meses y menor o igual a 18 meses.");
            return res;
        }

        if (precioTelefono <= 0) {
            res.setExito(false);
            res.setMensaje("El precio del teléfono celular debe ser mayor a $0.00.");
            return res;
        }

        try {
            Cliente cli = clienteDAO.buscarPorId(codCliente);
            if (cli == null) {
                res.setExito(false);
                res.setMensaje("El cliente #" + codCliente + " no existe en el sistema BanQuito.");
                return res;
            }

            // 1. Validar sujeto de crédito
            SujetoCreditoDTO calif = esSujetoCredito(cli.getCedula());
            if (!calif.isEsSujeto()) {
                res.setExito(false);
                res.setMensaje("Crédito Denegado: " + calif.getMotivo());
                return res;
            }

            // 2. Validar monto máximo aprobado
            MontoMaximoDTO capacidad = calcularMontoMaximo(codCliente);
            if (precioTelefono > capacidad.getMontoMaximo()) {
                res.setExito(false);
                res.setMensaje(String.format(
                    "Crédito Denegado: El valor del teléfono ($%.2f) excede el monto máximo aprobado de crédito ($%.2f).",
                    precioTelefono, capacidad.getMontoMaximo()
                ));
                return res;
            }

            // 3. Generar tabla de amortización
            List<CuotaAmortizacion> tabla = generarTablaAmortizacion(precioTelefono, numeroCuotas, TASA_INTERES_ANUAL);
            double valorCuota = tabla.size() > 1 ? tabla.get(1).getValorCuota() : 0.0;

            // 4. Persistir Crédito y Tabla en Base de Datos
            Credito credito = new Credito();
            credito.setCodCliente(codCliente);
            credito.setMonto(precioTelefono);
            credito.setNumCuotas(numeroCuotas);
            credito.setTasaInteresAnual(TASA_INTERES_ANUAL);
            credito.setValorCuota(valorCuota);
            credito.setEstado("ACTIVO");

            int idCredito = creditoDAO.registrarCreditoConAmortizacion(credito, tabla);

            res.setExito(true);
            res.setMensaje(String.format(
                "Crédito No. %d aprobado y registrado exitosamente por un valor de $%.2f a %d meses.",
                idCredito, precioTelefono, numeroCuotas
            ));
            res.setCredito(credito);
            res.setTablaAmortizacion(tabla);

        } catch (SQLException ex) {
            res.setExito(false);
            res.setMensaje("Error al otorgar crédito en base de datos: " + ex.getMessage());
        }

        return res;
    }

    // =========================================================================
    // ÍTEM 10: Retorna la tabla de amortización para un crédito otorgado
    // =========================================================================
    public List<CuotaAmortizacion> obtenerTablaAmortizacion(int codCredito) {
        try {
            return creditoDAO.obtenerTablaPorCredito(codCredito);
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }

    public OtorgarCreditoDTO obtenerCreditoActivoPorCliente(int codCliente) {
        OtorgarCreditoDTO dto = new OtorgarCreditoDTO();
        try {
            Credito c = creditoDAO.obtenerCreditoActivoPorCliente(codCliente);
            if (c == null) {
                dto.setExito(false);
                dto.setMensaje("No se encontró ningún crédito registrado para el cliente #" + codCliente);
                return dto;
            }
            List<CuotaAmortizacion> tabla = creditoDAO.obtenerTablaPorCredito(c.getCodCredito());
            dto.setExito(true);
            dto.setMensaje("Crédito activo recuperado.");
            dto.setCredito(c);
            dto.setTablaAmortizacion(tabla);
        } catch (SQLException ex) {
            dto.setExito(false);
            dto.setMensaje("Error: " + ex.getMessage());
        }
        return dto;
    }

    public List<Cliente> listarClientes() {
        try {
            return clienteDAO.listarTodos();
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }
}
