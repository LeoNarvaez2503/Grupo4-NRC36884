package ec.edu.monster.servicio;

import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.TelefonoCelular;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FacturacionService {

    public static final double DESCUENTO_EFECTIVO = 42.00; // 42% según enunciado
    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();

    public record ResultadoFactura(boolean exito, String mensaje, int facturaId, List<CuotaAmortizacion> tabla) {}

    // ÍTEM 9: Venta con Pago en Efectivo (42% de descuento)
    public ResultadoFactura facturarEfectivo(String cedula, String nombre, int codTelefono) {
        try {
            TelefonoCelular tel = catalogoDAO.buscarPorId(codTelefono);
            if (tel == null) {
                return new ResultadoFactura(false, "El teléfono seleccionado no existe.", 0, new ArrayList<>());
            }

            double precio = tel.getPrecio();
            double descPorc = DESCUENTO_EFECTIVO;
            double valorDesc = Math.round(precio * (descPorc / 100.0) * 100.0) / 100.0;
            double total = Math.round((precio - valorDesc) * 100.0) / 100.0;

            String comp = "FAC-EFEC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            Factura f = new Factura();
            f.setNumeroComprobante(comp);
            f.setCedulaCliente(cedula.trim());
            f.setNombreCliente(nombre != null && !nombre.isBlank() ? nombre.trim() : "Consumidor Final");
            f.setCodTelefono(tel.getCodTelefono());
            f.setModeloTelefono(tel.getMarca() + " " + tel.getModelo());
            f.setPrecioUnitario(precio);
            f.setFormaPago("EFECTIVO");
            f.setPorcentajeDescuento(descPorc);
            f.setValorDescuento(valorDesc);
            f.setTotalPagar(total);
            f.setObservacion("Venta en Efectivo con descuento comercial del 42%");

            int facId = facturaDAO.insertar(f);
            return new ResultadoFactura(
                true,
                String.format("Factura #%d emitida exitosamente en Efectivo con 42%% de descuento ($%.2f). Total a pagar: $%.2f.", facId, valorDesc, total),
                facId,
                new ArrayList<>()
            );

        } catch (SQLException ex) {
            return new ResultadoFactura(false, "Error al registrar la factura: " + ex.getMessage(), 0, new ArrayList<>());
        }
    }

    // ÍTEM 9: Venta con Crédito Directo (Alianza con BanQuito)
    public ResultadoFactura facturarCreditoDirecto(String cedula, String nombre, int codTelefono, int plazoCuotas) {
        try {
            TelefonoCelular tel = catalogoDAO.buscarPorId(codTelefono);
            if (tel == null) {
                return new ResultadoFactura(false, "El teléfono seleccionado no existe.", 0, new ArrayList<>());
            }

            // 1. Invocar BanQuito REST API para validar si es sujeto de crédito
            SujetoCreditoResponse calif = banquitoClient.validarSujetoCredito(cedula);
            if (!calif.esSujeto) {
                return new ResultadoFactura(false, "Crédito Denegado por BanQuito: " + calif.motivo, 0, new ArrayList<>());
            }

            // 2. Invocar BanQuito REST API para verificar monto máximo aprobado
            MontoMaximoResponse capacidad = banquitoClient.obtenerMontoMaximo(calif.codCliente);
            if (tel.getPrecio() > capacidad.montoMaximo) {
                return new ResultadoFactura(
                    false,
                    String.format("Crédito Denegado: El valor del celular ($%.2f) excede el monto máximo aprobado por BanQuito ($%.2f). Capacidad insuficiente.",
                        tel.getPrecio(), capacidad.montoMaximo),
                    0,
                    new ArrayList<>()
                );
            }

            // 3. Invocar BanQuito REST API para otorgar el crédito y generar la tabla de amortización
            OtorgarCreditoResponse otorgarRes = banquitoClient.otorgarCredito(calif.codCliente, tel.getPrecio(), plazoCuotas);
            if (!otorgarRes.exito || otorgarRes.credito == null) {
                return new ResultadoFactura(false, "Error al otorgar crédito en BanQuito: " + otorgarRes.mensaje, 0, new ArrayList<>());
            }

            // 4. Registrar Factura en la Comercializadora
            String comp = "FAC-CRED-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            Factura f = new Factura();
            f.setNumeroComprobante(comp);
            f.setCodClienteBanco(calif.codCliente);
            f.setCedulaCliente(cedula.trim());
            f.setNombreCliente(nombre != null && !nombre.isBlank() ? nombre.trim() : calif.nombre);
            f.setCodTelefono(tel.getCodTelefono());
            f.setModeloTelefono(tel.getMarca() + " " + tel.getModelo());
            f.setPrecioUnitario(tel.getPrecio());
            f.setFormaPago("CREDITO_DIRECTO");
            f.setPorcentajeDescuento(0.0);
            f.setValorDescuento(0.0);
            f.setTotalPagar(tel.getPrecio());
            f.setCodCreditoBanco(otorgarRes.credito.codCredito);
            f.setNumCuotas(plazoCuotas);
            f.setValorCuota(otorgarRes.credito.valorCuota);
            f.setObservacion("Venta financiada con Crédito Directo BanQuito #" + otorgarRes.credito.codCredito);

            int facId = facturaDAO.insertar(f);

            return new ResultadoFactura(
                true,
                String.format("Factura #%d emitida exitosamente. Crédito BanQuito #%d aprobado a %d cuotas de $%.2f.",
                    facId, otorgarRes.credito.codCredito, plazoCuotas, otorgarRes.credito.valorCuota),
                facId,
                otorgarRes.tablaAmortizacion
            );

        } catch (SQLException ex) {
            return new ResultadoFactura(false, "Error en base de datos al facturar: " + ex.getMessage(), 0, new ArrayList<>());
        }
    }

    public List<Factura> listarFacturas() {
        try {
            return facturaDAO.listar();
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }

    public Factura buscarFacturaPorId(int id) {
        try {
            return facturaDAO.buscarPorId(id);
        } catch (SQLException ex) {
            return null;
        }
    }
}
