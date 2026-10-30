package ec.edu.monster.climov.controller;

import ec.edu.monster.climov.model.ConfiguracionConexion;
import ec.edu.monster.climov.model.CuentaModel;
import ec.edu.monster.climov.model.EurekaBankService;
import ec.edu.monster.climov.model.MovimientoModel;
import ec.edu.monster.climov.model.ResultadoOperacion;
import ec.edu.monster.climov.model.UsuarioSesion;
import java.util.List;

/**
 * Capa Controlador (Patrón MVC Tradicional) para el Cliente Móvil Android.
 */
public class AppControlador {

    private static final AppControlador INSTANCIA = new AppControlador();

    private final EurekaBankService servicio = new EurekaBankService();
    private UsuarioSesion sesionActual;
    private int ventanillaActiva = 1;

    private AppControlador() {
    }

    public static AppControlador getInstancia() {
        return INSTANCIA;
    }

    public void configurarServidor(String host, int puerto) {
        ConfiguracionConexion.configurar(host, puerto);
    }

    public UsuarioSesion iniciarSesion(String usuario, String clave) {
        UsuarioSesion res = servicio.login(usuario, clave);
        if (res.isAutenticado()) {
            this.sesionActual = res;
        }
        return res;
    }

    public void cerrarSesion() {
        this.sesionActual = null;
    }

    public UsuarioSesion getSesionActual() {
        return sesionActual;
    }

    public int getVentanillaActiva() {
        return ventanillaActiva;
    }

    public void setVentanillaActiva(int ventanillaActiva) {
        if (ventanillaActiva >= 1 && ventanillaActiva <= 4) {
            this.ventanillaActiva = ventanillaActiva;
        }
    }

    public List<CuentaModel> obtenerTodasLasCuentas() {
        return servicio.listarCuentasPorSucursal(0);
    }

    public List<MovimientoModel> obtenerExtracto(String numeroCuenta) {
        return servicio.consultarExtracto(numeroCuenta);
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.depositar(numeroCuenta, monto, empId, ventanillaActiva);
    }

    public ResultadoOperacion retirar(String numeroCuenta, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.retirar(numeroCuenta, monto, empId, ventanillaActiva);
    }

    public ResultadoOperacion transferir(String cuentaOrigen, String cuentaDestino, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.transferir(cuentaOrigen, cuentaDestino, monto, empId, ventanillaActiva);
    }

    public ResultadoOperacion desbloquearCuenta(String numeroCuenta) {
        String usr = sesionActual != null ? sesionActual.getUsuario() : "Monster";
        return servicio.desbloquearCuenta(numeroCuenta, usr);
    }
}
