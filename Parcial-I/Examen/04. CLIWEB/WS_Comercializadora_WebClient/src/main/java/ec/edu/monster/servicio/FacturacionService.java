package ec.edu.monster.servicio;

import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.FacturaDetalle;
import ec.edu.monster.modelo.TelefonoCelular;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Logica de facturacion de la Comercializadora con CARRITO DE COMPRAS:
 * una factura agrupa varios telefonos (con cantidad) y se paga en Efectivo (42% de descuento
 * sobre todo el carrito) o a Credito Directo BanQuito (el credito financia el total del carrito).
 */
public class FacturacionService {

    public static final double DESCUENTO_EFECTIVO = 42.00; // 42% segun enunciado
    public static final int MAX_CANTIDAD_POR_ITEM = 10;

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();

    /** Linea solicitada del carrito: telefono + cantidad. */
    public record ItemCarrito(int codTelefono, int cantidad) {}

    public record ResultadoFactura(boolean exito, String mensaje, int facturaId, List<CuotaAmortizacion> tabla, Factura factura) {
        public ResultadoFactura(boolean exito, String mensaje, int facturaId, List<CuotaAmortizacion> tabla) {
            this(exito, mensaje, facturaId, tabla, null);
        }
    }

    /** Carrito ya validado contra el catalogo: renglones + subtotal. */
    public record CarritoResuelto(List<FacturaDetalle> items, double subtotal, int totalArticulos, String error) {
        public boolean ok() { return error == null; }
    }

    // ------------------------------------------------------------------------------------
    // Carrito
    // ------------------------------------------------------------------------------------

    /** Valida el carrito, une telefonos repetidos y calcula el subtotal con los precios del catalogo. */
    public CarritoResuelto resolverCarrito(List<ItemCarrito> solicitados) throws SQLException {
        if (solicitados == null || solicitados.isEmpty()) {
            return new CarritoResuelto(new ArrayList<>(), 0, 0, "El carrito está vacío. Agregue al menos un teléfono.");
        }
        Map<Integer, Integer> unidos = new LinkedHashMap<>();
        for (ItemCarrito it : solicitados) {
            if (it.cantidad() <= 0) {
                return new CarritoResuelto(new ArrayList<>(), 0, 0, "La cantidad de cada artículo debe ser mayor a cero.");
            }
            unidos.merge(it.codTelefono(), it.cantidad(), Integer::sum);
        }

        List<FacturaDetalle> items = new ArrayList<>();
        double subtotal = 0;
        int articulos = 0;
        for (Map.Entry<Integer, Integer> e : unidos.entrySet()) {
            TelefonoCelular tel = catalogoDAO.buscarPorId(e.getKey());
            if (tel == null) {
                return new CarritoResuelto(new ArrayList<>(), 0, 0, "El teléfono #" + e.getKey() + " no existe en el catálogo.");
            }
            if ("INACTIVO".equalsIgnoreCase(tel.getEstado())) {
                return new CarritoResuelto(new ArrayList<>(), 0, 0, "El teléfono " + tel.getMarca() + " " + tel.getModelo() + " ya no está disponible para la venta.");
            }
            if (e.getValue() > MAX_CANTIDAD_POR_ITEM) {
                return new CarritoResuelto(new ArrayList<>(), 0, 0, "Máximo " + MAX_CANTIDAD_POR_ITEM + " unidades por artículo (" + tel.getMarca() + " " + tel.getModelo() + ").");
            }
            FacturaDetalle d = new FacturaDetalle(tel.getCodTelefono(), tel.getMarca() + " " + tel.getModelo(), tel.getPrecio(), e.getValue());
            items.add(d);
            subtotal += d.getSubtotal();
            articulos += d.getCantidad();
        }
        return new CarritoResuelto(items, redondear(subtotal), articulos, null);
    }

    // ------------------------------------------------------------------------------------
    // Facturacion
    // ------------------------------------------------------------------------------------

    /** Compatibilidad: factura de un solo telefono (cantidad 1). */
    public ResultadoFactura facturarEfectivo(String cedula, String nombre, int codTelefono) {
        return facturarEfectivo(cedula, nombre, List.of(new ItemCarrito(codTelefono, 1)));
    }

    /** Compatibilidad: factura de un solo telefono (cantidad 1). */
    public ResultadoFactura facturarCreditoDirecto(String cedula, String nombre, int codTelefono, int plazoCuotas) {
        return facturarCreditoDirecto(cedula, nombre, List.of(new ItemCarrito(codTelefono, 1)), plazoCuotas);
    }

    // Venta con Pago en Efectivo (42% de descuento sobre el total del carrito)
    public ResultadoFactura facturarEfectivo(String cedula, String nombre, List<ItemCarrito> carrito) {
        try {
            CarritoResuelto c = resolverCarrito(carrito);
            if (!c.ok()) {
                return new ResultadoFactura(false, c.error(), 0, new ArrayList<>());
            }

            double valorDesc = redondear(c.subtotal() * (DESCUENTO_EFECTIVO / 100.0));
            double total = redondear(c.subtotal() - valorDesc);

            Factura f = nuevaCabecera("FAC-EFEC-", cedula, nombre, null, c);
            f.setFormaPago("EFECTIVO");
            f.setPorcentajeDescuento(DESCUENTO_EFECTIVO);
            f.setValorDescuento(valorDesc);
            f.setTotalPagar(total);
            f.setObservacion("Venta en Efectivo con descuento comercial del 42% (" + c.totalArticulos() + " artículo(s))");

            int facId = facturaDAO.insertar(f);
            f.setNumFactura(facId);
            return new ResultadoFactura(
                true,
                String.format("Factura #%d emitida exitosamente en Efectivo: %d artículo(s), subtotal $%.2f, descuento 42%% ($%.2f). Total a pagar: $%.2f.",
                        facId, c.totalArticulos(), c.subtotal(), valorDesc, total),
                facId,
                new ArrayList<>(),
                f
            );

        } catch (SQLException ex) {
            return new ResultadoFactura(false, "Error al registrar la factura: " + ex.getMessage(), 0, new ArrayList<>());
        }
    }

    // Venta con Credito Directo (Alianza con BanQuito): el credito financia el total del carrito
    public ResultadoFactura facturarCreditoDirecto(String cedula, String nombre, List<ItemCarrito> carrito, int plazoCuotas) {
        try {
            CarritoResuelto c = resolverCarrito(carrito);
            if (!c.ok()) {
                return new ResultadoFactura(false, c.error(), 0, new ArrayList<>());
            }
            double totalCarrito = c.subtotal();

            // 1. Invocar BanQuito REST API para validar si es sujeto de credito
            SujetoCreditoResponse calif = banquitoClient.validarSujetoCredito(cedula);
            if (calif == null || !calif.esSujeto) {
                String mot = (calif != null && calif.motivo != null) ? calif.motivo : "No calificado";
                return new ResultadoFactura(false, "Crédito Denegado por BanQuito: " + mot, 0, new ArrayList<>());
            }

            // 2. Invocar BanQuito REST API para verificar monto maximo aprobado frente al TOTAL del carrito
            MontoMaximoResponse capacidad = banquitoClient.obtenerMontoMaximo(calif.codCliente);
            if (capacidad == null) {
                return new ResultadoFactura(false, "No se pudo consultar el monto máximo en BanQuito.", 0, new ArrayList<>());
            }
            if (totalCarrito > capacidad.montoMaximo) {
                return new ResultadoFactura(
                    false,
                    String.format("Crédito Denegado: El total del carrito ($%.2f por %d artículo(s)) excede el monto máximo aprobado por BanQuito ($%.2f). Capacidad insuficiente.",
                        totalCarrito, c.totalArticulos(), capacidad.montoMaximo),
                    0,
                    new ArrayList<>()
                );
            }

            // 3. Invocar BanQuito REST API para otorgar el credito y generar la tabla de amortizacion
            OtorgarCreditoResponse otorgarRes = banquitoClient.otorgarCredito(calif.codCliente, totalCarrito, plazoCuotas);
            if (otorgarRes == null || !otorgarRes.exito || otorgarRes.credito == null) {
                String m = otorgarRes != null ? otorgarRes.mensaje : "sin respuesta";
                return new ResultadoFactura(false, "Error al otorgar crédito en BanQuito: " + m, 0, new ArrayList<>());
            }

            // 4. Registrar Factura (cabecera + items) en la Comercializadora
            Factura f = nuevaCabecera("FAC-CRED-", cedula, nombre, calif, c);
            f.setFormaPago("CREDITO_DIRECTO");
            f.setPorcentajeDescuento(0.0);
            f.setValorDescuento(0.0);
            f.setTotalPagar(totalCarrito);
            f.setCodCreditoBanco(otorgarRes.credito.codCredito);
            f.setNumCuotas(plazoCuotas);
            f.setValorCuota(otorgarRes.credito.valorCuota);
            f.setObservacion("Venta financiada con Crédito Directo BanQuito #" + otorgarRes.credito.codCredito + " (" + c.totalArticulos() + " artículo(s))");

            int facId = facturaDAO.insertar(f);
            f.setNumFactura(facId);

            return new ResultadoFactura(
                true,
                String.format("Factura #%d emitida exitosamente: %d artículo(s) por $%.2f. Crédito BanQuito #%d aprobado a %d cuotas de $%.2f.",
                    facId, c.totalArticulos(), totalCarrito, otorgarRes.credito.codCredito, plazoCuotas, otorgarRes.credito.valorCuota),
                facId,
                otorgarRes.tablaAmortizacion,
                f
            );

        } catch (SQLException ex) {
            return new ResultadoFactura(false, "Error en base de datos al facturar: " + ex.getMessage(), 0, new ArrayList<>());
        }
    }

    private Factura nuevaCabecera(String prefijo, String cedula, String nombre, SujetoCreditoResponse calif, CarritoResuelto c) {
        Factura f = new Factura();
        f.setNumeroComprobante(prefijo + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        if (calif != null) f.setCodClienteBanco(calif.codCliente);
        f.setCedulaCliente(cedula.trim());
        String porDefecto = calif != null && calif.nombre != null ? calif.nombre : "Consumidor Final";
        f.setNombreCliente(nombre != null && !nombre.isBlank() ? nombre.trim() : porDefecto);
        f.setItems(c.items());
        f.setSubtotal(c.subtotal());
        f.setPrecioUnitario(c.subtotal());
        FacturaDetalle primero = c.items().get(0);
        f.setCodTelefono(primero.getCodTelefono());
        f.setModeloTelefono(resumenItems(c.items()));
        return f;
    }

    /** Texto corto para la columna "dispositivo" de la cabecera: "Samsung Galaxy A55 x2 + 1 mas". */
    public static String resumenItems(List<FacturaDetalle> items) {
        if (items == null || items.isEmpty()) return "";
        FacturaDetalle p = items.get(0);
        String s = p.getModeloTelefono() + (p.getCantidad() > 1 ? " x" + p.getCantidad() : "");
        if (items.size() > 1) s += " + " + (items.size() - 1) + " más";
        return s.length() > 250 ? s.substring(0, 250) : s;
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

    private static double redondear(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
