package ec.edu.monster.cliweb.view;

import ec.edu.monster.cliweb.controller.AppControlador;
import ec.edu.monster.cliweb.model.CuentaModel;
import ec.edu.monster.cliweb.model.MovimientoModel;
import ec.edu.monster.cliweb.model.ResultadoOperacion;
import ec.edu.monster.cliweb.model.SucursalModel;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Capa Vista (Patrón MVC Tradicional): Backing Bean JSF para dashboard.xhtml.
 * Gestiona presentación del Semáforo MashUp (Verde/Rojo), extractos y formularios transaccionales.
 */
@Named("dashboardView")
@SessionScoped
public class DashboardView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginView loginView;

    private int sucursalSeleccionada = 0;
    private String filtroBusqueda = "";
    private String cuentaSeleccionada = "001-0001";
    private String cuentaDestino = "001-0002";
    private double montoOperacion = 50.00;
    private String mensajeAlerta = "";
    private boolean exitoAlerta = true;

    private List<SucursalModel> sucursales = new ArrayList<>();
    private List<CuentaModel> cuentas = new ArrayList<>();
    private List<MovimientoModel> movimientos = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (loginView != null && loginView.isAutenticado()) {
            cargarDatosIniciales();
        }
    }

    public void verificarSesion() throws IOException {
        if (loginView == null || !loginView.isAutenticado()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }
        if (sucursales.isEmpty()) {
            cargarDatosIniciales();
        }
    }

    public void cargarDatosIniciales() {
        try {
            AppControlador ctrl = loginView.getControlador();
            this.sucursales = ctrl.obtenerSucursales();
            this.cuentas = ctrl.obtenerCuentasPorSucursal(sucursalSeleccionada);
            if (cuentaSeleccionada != null && !cuentaSeleccionada.isBlank()) {
                this.movimientos = ctrl.obtenerExtracto(cuentaSeleccionada);
            }
        } catch (Exception e) {
            this.mensajeAlerta = "Error al consultar servidor: " + e.getMessage();
            this.exitoAlerta = false;
        }
    }

    public void sincronizarMashup() {
        if (loginView == null || !loginView.isAutenticado()) {
            return;
        }
        try {
            AppControlador ctrl = loginView.getControlador();
            if (filtroBusqueda != null && !filtroBusqueda.trim().isEmpty()) {
                String f = filtroBusqueda.trim();
                if (f.contains("-")) {
                    CuentaModel c = ctrl.buscarCuentaPorNumero(f);
                    this.cuentas = c != null ? List.of(c) : List.of();
                } else {
                    this.cuentas = ctrl.buscarCuentasPorDni(f);
                }
            } else {
                this.cuentas = ctrl.obtenerCuentasPorSucursal(sucursalSeleccionada);
            }
        } catch (Exception ignored) {
        }
    }

    public void filtrarSucursal() {
        this.filtroBusqueda = "";
        sincronizarMashup();
    }

    public void buscarCuenta() {
        sincronizarMashup();
        if (!cuentas.isEmpty()) {
            seleccionarCuenta(cuentas.get(0).getNumeroCuenta());
        }
    }

    public void limpiarFiltro() {
        this.filtroBusqueda = "";
        this.sucursalSeleccionada = 0;
        sincronizarMashup();
    }

    public void seleccionarCuenta(String numeroCuenta) {
        this.cuentaSeleccionada = numeroCuenta;
        try {
            this.movimientos = loginView.getControlador().obtenerExtracto(numeroCuenta);
        } catch (Exception e) {
            this.mensajeAlerta = "No se pudo cargar extracto: " + e.getMessage();
            this.exitoAlerta = false;
        }
    }

    public void ejecutarDeposito() {
        procesar(loginView.getControlador().depositar(cuentaSeleccionada, montoOperacion));
    }

    public void ejecutarRetiro() {
        procesar(loginView.getControlador().retirar(cuentaSeleccionada, montoOperacion));
    }

    public void ejecutarTransferencia() {
        procesar(loginView.getControlador().transferir(cuentaSeleccionada, cuentaDestino, montoOperacion));
    }

    public void ejecutarDesbloqueo() {
        procesar(loginView.getControlador().desbloquearCuenta(cuentaSeleccionada));
    }

    public void simularConcurrenciaTresVentanillas() {
        if (loginView == null || !loginView.isAutenticado()) {
            return;
        }
        final String cta = (cuentaSeleccionada != null && !cuentaSeleccionada.isBlank()) ? cuentaSeleccionada.trim() : "001-0001";
        AppControlador ctrl = loginView.getControlador();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            List<Future<String>> futuros = new ArrayList<>();
            for (int v = 1; v <= 3; v++) {
                final int vent = v;
                futuros.add(pool.submit(() -> {
                    if (vent > 1) {
                        Thread.sleep(220L * vent);
                    }
                    ResultadoOperacion r = ctrl.depositarDesdeVentanilla(cta, 20.00, vent);
                    return "[Ventanilla " + vent + "] " + (r.isExito() ? "ÉXITO: " : "BLOQUEADA: ") + r.getMensaje();
                }));
            }
            StringBuilder resumen = new StringBuilder("Escenario Crítico 3 Ventanillas sobre " + cta + ":\n");
            for (Future<String> f : futuros) {
                resumen.append(f.get()).append("\n");
            }
            this.mensajeAlerta = resumen.toString();
            this.exitoAlerta = true;
        } catch (Exception ex) {
            this.mensajeAlerta = "Error en simulación: " + ex.getMessage();
            this.exitoAlerta = false;
        } finally {
            pool.shutdownNow();
        }
        sincronizarMashup();
        if (cuentaSeleccionada != null && !cuentaSeleccionada.isBlank()) {
            this.movimientos = loginView.getControlador().obtenerExtracto(cuentaSeleccionada);
        }
    }

    private void procesar(ResultadoOperacion res) {
        if (res != null) {
            this.exitoAlerta = res.isExito();
            this.mensajeAlerta = res.getMensaje();
        }
        sincronizarMashup();
        if (cuentaSeleccionada != null && !cuentaSeleccionada.isBlank()) {
            this.movimientos = loginView.getControlador().obtenerExtracto(cuentaSeleccionada);
        }
    }

    public long getTotalCuentasRojas() {
        return cuentas.stream().filter(c -> "ROJO".equalsIgnoreCase(c.getDisponibilidadMashup())).count();
    }

    public long getTotalCuentasVerdes() {
        return cuentas.stream().filter(c -> !"ROJO".equalsIgnoreCase(c.getDisponibilidadMashup())).count();
    }

    public int getSucursalSeleccionada() { return sucursalSeleccionada; }
    public void setSucursalSeleccionada(int sucursalSeleccionada) { this.sucursalSeleccionada = sucursalSeleccionada; }

    public String getFiltroBusqueda() { return filtroBusqueda; }
    public void setFiltroBusqueda(String filtroBusqueda) { this.filtroBusqueda = filtroBusqueda; }

    public String getCuentaSeleccionada() { return cuentaSeleccionada; }
    public void setCuentaSeleccionada(String cuentaSeleccionada) { this.cuentaSeleccionada = cuentaSeleccionada; }

    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public double getMontoOperacion() { return montoOperacion; }
    public void setMontoOperacion(double montoOperacion) { this.montoOperacion = montoOperacion; }

    public String getMensajeAlerta() { return mensajeAlerta; }
    public boolean isExitoAlerta() { return exitoAlerta; }

    public List<SucursalModel> getSucursales() { return sucursales; }
    public List<CuentaModel> getCuentas() { return cuentas; }
    public List<MovimientoModel> getMovimientos() { return movimientos; }
}
