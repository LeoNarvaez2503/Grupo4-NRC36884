package ec.edu.monster.cliweb.view;

import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.TelefonoCelular;
import ec.edu.monster.servicio.BanQuitoRestClient;
import ec.edu.monster.servicio.FacturacionService;
import ec.edu.monster.servicio.FacturacionService.ResultadoFactura;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed Bean JSF (Jakarta Faces) para la Vista Web de la Comercializadora.
 * Controla la facturación (Efectivo -42% y Crédito Directo BanQuito),
 * el catálogo de teléfonos (CRUD) y la consulta de amortización.
 * NRC: 36884 | Grupo: 04
 */
@Named("comercializadoraView")
@SessionScoped
public class ComercializadoraView implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final FacturacionService facturacionService = new FacturacionService();
    private final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();

    // Pestaña activa: "facturacion", "catalogo", "amortizacion", "historial"
    private String tabActiva = "facturacion";

    // --- Formulario Facturación ---
    private String cedula = "1710000001";
    private String nombre = "Carlos Andrade";
    private int codTelefono = 1;
    private String formaPago = "CREDITO_DIRECTO"; // "EFECTIVO" o "CREDITO_DIRECTO"
    private int cuotas = 6;

    private double precioTelefono = 450.00;
    private double descuentoEfectivo = 0.00;
    private double totalPagarEfectivo = 0.00;
    private double cuotaEstimada = 0.00;

    // Alertas y Resultados de Facturación
    private String mensajeAlerta = "";
    private String tipoAlerta = "info"; // "success", "danger", "warning", "info"
    private boolean verificado = false;
    private SujetoCreditoResponse sujetoCredito = new SujetoCreditoResponse();
    private MontoMaximoResponse capacidadCredito = new MontoMaximoResponse();
    private List<CuotaAmortizacion> tablaSimulada = new ArrayList<>();
    private List<CuotaAmortizacion> tablaFactura = new ArrayList<>();
    private ResultadoFactura ultimoResultado;

    // --- Catálogo CRUD ---
    private List<TelefonoCelular> telefonos = new ArrayList<>();
    private String nuevoMarca = "";
    private String nuevoModelo = "";
    private double nuevoPrecio = 350.00;
    private String nuevoCaracteristicas = "";

    // --- Consulta de Amortización ---
    private Integer busqCredito;
    private String busqCedula = "";
    private List<CuotaAmortizacion> tablaConsultada = new ArrayList<>();
    private String infoClienteConsultado = "";

    // --- Historial ---
    private List<Factura> historialFacturas = new ArrayList<>();

    @PostConstruct
    public void init() {
        recargarTelefonos();
        recargarHistorial();
        actualizarCalculosPrecios();
    }

    public void recargarTelefonos() {
        try {
            this.telefonos = catalogoDAO.listar(false);
            if (!this.telefonos.isEmpty() && codTelefono == 0) {
                codTelefono = this.telefonos.get(0).getCodTelefono();
            }
        } catch (Exception e) {
            this.telefonos = new ArrayList<>();
        }
    }

    public void recargarHistorial() {
        try {
            this.historialFacturas = facturacionService.listarFacturas();
        } catch (Exception e) {
            this.historialFacturas = new ArrayList<>();
        }
    }

    /**
     * Sincronización reactiva automática cada 2.5s (Poller similar a EurekaBank).
     * Recarga el historial de facturas y el catálogo de teléfonos desde la base de datos.
     */
    public void sincronizarDatos() {
        recargarHistorial();
        recargarTelefonos();
    }

    public void actualizarCalculosPrecios() {
        TelefonoCelular sel = buscarTelefonoPorId(this.codTelefono);
        if (sel != null) {
            this.precioTelefono = sel.getPrecio();
        } else {
            this.precioTelefono = 0.0;
        }
        this.descuentoEfectivo = Math.round(this.precioTelefono * 0.42 * 100.0) / 100.0;
        this.totalPagarEfectivo = Math.round((this.precioTelefono - this.descuentoEfectivo) * 100.0) / 100.0;
    }

    public void onTelefonoChange() {
        actualizarCalculosPrecios();
        if ("CREDITO_DIRECTO".equals(this.formaPago) && this.cedula != null && !this.cedula.isBlank()) {
            verificarCredito();
        }
    }

    public void onFormaPagoChange() {
        actualizarCalculosPrecios();
        if ("CREDITO_DIRECTO".equals(this.formaPago)) {
            verificarCredito();
        } else {
            this.mensajeAlerta = "Pago en Efectivo seleccionado. Aplica un 42% de descuento directo.";
            this.tipoAlerta = "info";
            this.tablaSimulada.clear();
            this.cuotaEstimada = 0.0;
        }
    }

    // =========================================================================
    // BOTONERA DE ACCESO RÁPIDO: 4 CLIENTES DE PRUEBA OFICIALES (RÚBRICA)
    // =========================================================================

    public void seleccionarCliente1() {
        this.tabActiva = "facturacion";
        this.cedula = "1710000001";
        this.nombre = "Carlos Andrade";
        this.formaPago = "CREDITO_DIRECTO";
        this.cuotas = 6;
        seleccionarTelefonoPorModeloOPrecio("Motorola", 450.00);
        actualizarCalculosPrecios();
        verificarCredito();
    }

    public void seleccionarCliente2() {
        this.tabActiva = "facturacion";
        this.cedula = "1710000002";
        this.nombre = "Andrea Morales";
        this.formaPago = "CREDITO_DIRECTO";
        this.cuotas = 6;
        seleccionarTelefonoPorModeloOPrecio("Samsung Galaxy S24", 850.00);
        actualizarCalculosPrecios();
        verificarCredito();
    }

    public void seleccionarCliente3() {
        this.tabActiva = "facturacion";
        this.cedula = "1710000003";
        this.nombre = "Mateo Salazar";
        this.formaPago = "CREDITO_DIRECTO";
        this.cuotas = 6;
        seleccionarTelefonoPorModeloOPrecio("Motorola", 450.00);
        actualizarCalculosPrecios();
        verificarCredito();
    }

    public void seleccionarCliente4() {
        this.tabActiva = "facturacion";
        this.cedula = "1710000004";
        this.nombre = "Elena Castro";
        this.formaPago = "EFECTIVO";
        this.cuotas = 6;
        seleccionarTelefonoPorModeloOPrecio("Xiaomi", 450.00);
        actualizarCalculosPrecios();
        this.mensajeAlerta = String.format("Cliente 4 (Elena Castro) seleccionada para compra en Efectivo. Aplica 42%% de descuento: Total $%.2f (Ahorro: $%.2f).", this.totalPagarEfectivo, this.descuentoEfectivo);
        this.tipoAlerta = "success";
        this.tablaSimulada.clear();
        this.cuotaEstimada = 0.0;
    }

    private void seleccionarTelefonoPorModeloOPrecio(String keyword, double precioFallback) {
        for (TelefonoCelular t : this.telefonos) {
            if ((t.getModelo() != null && t.getModelo().toLowerCase().contains(keyword.toLowerCase()))
                    || (t.getMarca() != null && t.getMarca().toLowerCase().contains(keyword.toLowerCase()))
                    || Math.abs(t.getPrecio() - precioFallback) < 0.01) {
                this.codTelefono = t.getCodTelefono();
                return;
            }
        }
    }

    // =========================================================================
    // VERIFICACIÓN DE CRÉDITO DIRECTO EN VIVO CON BANQUITO RESTFUL
    // =========================================================================

    public void verificarCredito() {
        if (this.cedula == null || this.cedula.trim().isBlank()) {
            this.mensajeAlerta = "Ingrese el número de cédula para consultar en BanQuito.";
            this.tipoAlerta = "warning";
            this.verificado = false;
            return;
        }

        actualizarCalculosPrecios();

        try {
            // 1. Invocar BanQuito REST API: es sujeto de crédito?
            this.sujetoCredito = banquitoClient.validarSujetoCredito(this.cedula.trim());

            if (this.sujetoCredito == null || !this.sujetoCredito.esSujeto) {
                String motivo = this.sujetoCredito != null ? this.sujetoCredito.motivo : "Cliente no registrado o no sujeto.";
                this.mensajeAlerta = "BanQuito Denegó el Crédito: " + motivo;
                this.tipoAlerta = "danger";
                this.verificado = false;
                this.tablaSimulada.clear();
                this.cuotaEstimada = 0.0;
                return;
            }

            // 2. Invocar BanQuito REST API: calcular cupo máximo
            this.capacidadCredito = banquitoClient.obtenerMontoMaximo(this.sujetoCredito.codCliente);
            double cupoMax = this.capacidadCredito != null ? this.capacidadCredito.montoMaximo : 0.0;

            if (this.precioTelefono > cupoMax) {
                this.mensajeAlerta = String.format("Capacidad Insuficiente: El teléfono cuesta $%.2f pero el cupo máximo aprobado por BanQuito es de $%.2f.", this.precioTelefono, cupoMax);
                this.tipoAlerta = "danger";
                this.verificado = false;
                this.tablaSimulada.clear();
                this.cuotaEstimada = 0.0;
                return;
            }

            // 3. Invocar BanQuito REST API: simulación amortización francesa
            this.tablaSimulada = banquitoClient.simularAmortizacion(this.precioTelefono, this.cuotas);
            if (this.tablaSimulada != null && this.tablaSimulada.size() > 1) {
                this.cuotaEstimada = this.tablaSimulada.get(1).valorCuota;
            } else {
                this.cuotaEstimada = 0.0;
            }

            this.mensajeAlerta = String.format("Crédito Aprobado por BanQuito: Cliente calificado con cupo de $%.2f. Cuota fija estimada: $%.2f a %d meses.", cupoMax, this.cuotaEstimada, this.cuotas);
            this.tipoAlerta = "success";
            this.verificado = true;

        } catch (Exception ex) {
            this.mensajeAlerta = "Error de comunicación con el Core BanQuito: " + ex.getMessage();
            this.tipoAlerta = "danger";
            this.verificado = false;
        }
    }

    // =========================================================================
    // EMISIÓN DE FACTURA
    // =========================================================================

    public void emitirFactura() {
        if (this.cedula == null || this.cedula.isBlank() || this.nombre == null || this.nombre.isBlank()) {
            this.mensajeAlerta = "Complete los datos obligatorios del cliente (Cédula y Nombre).";
            this.tipoAlerta = "warning";
            return;
        }

        try {
            if ("EFECTIVO".equalsIgnoreCase(this.formaPago)) {
                this.ultimoResultado = facturacionService.facturarEfectivo(this.cedula, this.nombre, this.codTelefono);
                this.tablaFactura.clear();
            } else {
                this.ultimoResultado = facturacionService.facturarCreditoDirecto(this.cedula, this.nombre, this.codTelefono, this.cuotas);
                if (this.ultimoResultado.exito()) {
                    this.tablaFactura = this.ultimoResultado.tabla();
                } else {
                    this.tablaFactura.clear();
                }
            }

            if (this.ultimoResultado.exito()) {
                this.mensajeAlerta = this.ultimoResultado.mensaje();
                this.tipoAlerta = "success";
                recargarHistorial();
            } else {
                this.mensajeAlerta = this.ultimoResultado.mensaje();
                this.tipoAlerta = "danger";
            }
        } catch (Exception ex) {
            this.mensajeAlerta = "Error al procesar la factura: " + ex.getMessage();
            this.tipoAlerta = "danger";
        }
    }

    // =========================================================================
    // CATÁLOGO CRUD
    // =========================================================================

    public void crearTelefono() {
        if (this.nuevoMarca.isBlank() || this.nuevoModelo.isBlank() || this.nuevoPrecio <= 0) {
            this.mensajeAlerta = "Complete marca, modelo y precio mayor a 0 para el nuevo teléfono.";
            this.tipoAlerta = "warning";
            return;
        }

        TelefonoCelular t = new TelefonoCelular();
        t.setMarca(this.nuevoMarca.trim());
        t.setModelo(this.nuevoModelo.trim());
        t.setPrecio(this.nuevoPrecio);
        t.setCaracteristicas(this.nuevoCaracteristicas.trim());
        t.setEstado("ACTIVO");

        try {
            int id = catalogoDAO.insertar(t);
            recargarTelefonos();
            this.nuevoMarca = "";
            this.nuevoModelo = "";
            this.nuevoPrecio = 350.00;
            this.nuevoCaracteristicas = "";
            this.mensajeAlerta = "Teléfono #" + id + " registrado correctamente en el catálogo.";
            this.tipoAlerta = "success";
        } catch (Exception ex) {
            this.mensajeAlerta = "Error al registrar teléfono: " + ex.getMessage();
            this.tipoAlerta = "danger";
        }
    }

    public void desactivarTelefono(int id) {
        try {
            catalogoDAO.desactivar(id);
            recargarTelefonos();
            this.mensajeAlerta = "Teléfono #" + id + " retirado de la venta.";
            this.tipoAlerta = "info";
        } catch (Exception ex) {
            this.mensajeAlerta = "Error al desactivar teléfono: " + ex.getMessage();
            this.tipoAlerta = "danger";
        }
    }

    // =========================================================================
    // CONSULTA DE AMORTIZACIÓN BANQUITO
    // =========================================================================

    public void consultarAmortizacion() {
        this.tablaConsultada.clear();
        this.infoClienteConsultado = "";

        try {
            boolean buscoPorCredito = false;
            if (this.busqCredito != null && this.busqCredito > 0) {
                this.tablaConsultada = banquitoClient.obtenerTablaAmortizacion(this.busqCredito);
                if (!this.tablaConsultada.isEmpty()) {
                    this.infoClienteConsultado = "Crédito BanQuito #" + this.busqCredito + " encontrado exitosamente (" + this.tablaConsultada.size() + " cuotas).";
                    buscoPorCredito = true;
                }
            }

            if (!buscoPorCredito && this.busqCedula != null && !this.busqCedula.trim().isBlank()) {
                SujetoCreditoResponse sujeto = banquitoClient.validarSujetoCredito(this.busqCedula.trim());
                if (sujeto != null && sujeto.codCliente > 0) {
                    Factura fEncontrada = null;
                    for (Factura f : this.historialFacturas) {
                        if (this.busqCedula.trim().equals(f.getCedulaCliente()) && f.getCodCreditoBanco() != null) {
                            fEncontrada = f;
                            break;
                        }
                    }
                    int credId = fEncontrada != null && fEncontrada.getCodCreditoBanco() != null ? fEncontrada.getCodCreditoBanco() : 1;
                    this.tablaConsultada = banquitoClient.obtenerTablaAmortizacion(credId);
                    this.infoClienteConsultado = String.format("Cliente: %s | Cédula: %s | Crédito Asociado: #%d (%d cuotas)", sujeto.nombre, sujeto.cedula, credId, this.tablaConsultada.size());
                } else {
                    this.infoClienteConsultado = "No se encontró ningún cliente bancario con la cédula " + this.busqCedula;
                    return;
                }
            } else if (!buscoPorCredito) {
                if (this.busqCredito != null && this.busqCredito > 0) {
                    this.infoClienteConsultado = "No se encontró el Crédito #" + this.busqCredito + ". Ingrese un código de crédito válido (ej: 1, 52, 53, 54, 55) o borre ese campo para buscar por cédula.";
                } else {
                    this.infoClienteConsultado = "Ingrese un número de crédito bancario o la cédula del cliente a consultar.";
                }
                return;
            }

            if (this.tablaConsultada.isEmpty()) {
                this.infoClienteConsultado = "No se registraron cuotas de amortización para la búsqueda solicitada.";
            }
        } catch (Exception ex) {
            this.infoClienteConsultado = "Error al consultar amortización en BanQuito: " + ex.getMessage();
        }
    }

    private TelefonoCelular buscarTelefonoPorId(int id) {
        for (TelefonoCelular t : this.telefonos) {
            if (t.getCodTelefono() == id) {
                return t;
            }
        }
        return !this.telefonos.isEmpty() ? this.telefonos.get(0) : null;
    }

    public void verificarSujeto() {
        verificarCredito();
    }

    public void simularCreditoBanQuito() {
        if ("CREDITO_DIRECTO".equals(this.formaPago)) {
            verificarCredito();
        }
    }

    public void seleccionarTelefonoParaFactura(int codTel) {
        this.codTelefono = codTel;
        this.tabActiva = "facturacion";
        actualizarCalculosPrecios();
        if ("CREDITO_DIRECTO".equals(this.formaPago)) {
            verificarCredito();
        }
    }

    public double getTotalMontoFacturas() {
        double sum = 0.0;
        for (Factura f : this.historialFacturas) {
            sum += f.getTotalPagar();
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    public int getCantidadFacturasCredito() {
        int c = 0;
        for (Factura f : this.historialFacturas) {
            if ("CREDITO_DIRECTO".equalsIgnoreCase(f.getFormaPago())) c++;
        }
        return c;
    }

    public int getCantidadFacturasEfectivo() {
        int c = 0;
        for (Factura f : this.historialFacturas) {
            if ("EFECTIVO".equalsIgnoreCase(f.getFormaPago())) c++;
        }
        return c;
    }

    // --- Getters y Setters ---

    public String getTabActiva() { return tabActiva; }
    public void setTabActiva(String tabActiva) { this.tabActiva = tabActiva; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCodTelefono() { return codTelefono; }
    public void setCodTelefono(int codTelefono) { this.codTelefono = codTelefono; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public int getCuotas() { return cuotas; }
    public void setCuotas(int cuotas) { this.cuotas = cuotas; }

    public double getPrecioTelefono() { return precioTelefono; }
    public void setPrecioTelefono(double precioTelefono) { this.precioTelefono = precioTelefono; }

    public double getDescuentoEfectivo() { return descuentoEfectivo; }
    public double getTotalPagarEfectivo() { return totalPagarEfectivo; }
    public double getCuotaEstimada() { return cuotaEstimada; }

    public String getMensajeAlerta() { return mensajeAlerta; }
    public String getTipoAlerta() { return tipoAlerta; }
    public boolean isVerificado() { return verificado; }

    public SujetoCreditoResponse getSujetoCredito() { return sujetoCredito; }
    public double getCupoAprobado() {
        return this.capacidadCredito != null ? this.capacidadCredito.getMontoMaximo() : 0.0;
    }

    public MontoMaximoResponse getCapacidadCredito() { return capacidadCredito; }
    public List<CuotaAmortizacion> getTablaSimulada() { return tablaSimulada; }
    public List<CuotaAmortizacion> getTablaFactura() { return tablaFactura; }

    public List<TelefonoCelular> getTelefonos() { return telefonos; }

    public String getNuevoMarca() { return nuevoMarca; }
    public void setNuevoMarca(String nuevoMarca) { this.nuevoMarca = nuevoMarca; }

    public String getNuevoModelo() { return nuevoModelo; }
    public void setNuevoModelo(String nuevoModelo) { this.nuevoModelo = nuevoModelo; }

    public double getNuevoPrecio() { return nuevoPrecio; }
    public void setNuevoPrecio(double nuevoPrecio) { this.nuevoPrecio = nuevoPrecio; }

    public String getNuevoCaracteristicas() { return nuevoCaracteristicas; }
    public void setNuevoCaracteristicas(String nuevoCaracteristicas) { this.nuevoCaracteristicas = nuevoCaracteristicas; }

    public Integer getBusqCredito() { return busqCredito; }
    public void setBusqCredito(Integer busqCredito) { this.busqCredito = busqCredito; }

    public String getBusqCedula() { return busqCedula; }
    public void setBusqCedula(String busqCedula) { this.busqCedula = busqCedula; }

    public List<CuotaAmortizacion> getTablaConsultada() { return tablaConsultada; }
    public String getInfoClienteConsultado() { return infoClienteConsultado; }

    public List<Factura> getHistorialFacturas() { return historialFacturas; }
}
