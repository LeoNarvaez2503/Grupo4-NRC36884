package ec.edu.monster.cliesc.controller;

import ec.edu.monster.cliesc.model.CuentaModel;
import ec.edu.monster.cliesc.model.EurekaBankService;
import ec.edu.monster.cliesc.model.MovimientoModel;
import ec.edu.monster.cliesc.model.ResultadoOperacion;
import ec.edu.monster.cliesc.model.SucursalModel;
import ec.edu.monster.cliesc.model.UsuarioSesion;
import java.util.List;

/**
 * Capa Controlador (Patrón MVC Tradicional) para el Cliente de Consola.
 * Coordina la interacción entre ConsolaView (Vista) y EurekaBankService (Modelo).
 */
public class AppControlador {

    private final EurekaBankService servicio = new EurekaBankService();
    private UsuarioSesion sesionActual;
    private int ventanillaActiva = 1;

    public UsuarioSesion iniciarSesion(String usuario, String clave) {
        UsuarioSesion res = servicio.login(usuario, clave);
        if (res.isAutenticado()) {
            this.sesionActual = res;
            if (res.getVentanilla() >= 1 && res.getVentanilla() <= 4) {
                this.ventanillaActiva = res.getVentanilla();
            }
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

    public String getEndpointActual() {
        return servicio.getEndpointActual();
    }

    public List<SucursalModel> obtenerSucursales() {
        return servicio.listarSucursales();
    }

    public List<CuentaModel> obtenerCuentasPorSucursal(int sucursalId) {
        return servicio.listarCuentasPorSucursal(sucursalId);
    }

    public CuentaModel buscarCuentaPorNumero(String numeroCuenta) {
        return servicio.obtenerCuentaPorNumero(numeroCuenta);
    }

    public List<CuentaModel> buscarCuentasPorDni(String dni) {
        return servicio.consultarCuentasPorCliente(dni);
    }

    public List<MovimientoModel> obtenerExtracto(String numeroCuenta) {
        return servicio.consultarExtracto(numeroCuenta);
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.depositar(numeroCuenta, monto, empId, ventanillaActiva);
    }

    public ResultadoOperacion depositarDesdeVentanilla(String numeroCuenta, double monto, int ventanilla) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.depositar(numeroCuenta, monto, empId, ventanilla);
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

    public ResultadoOperacion bloquearCuentaManual(String numeroCuenta) {
        String usr = sesionActual != null ? sesionActual.getUsuario() : "Monster";
        return servicio.bloquearCuentaManual(numeroCuenta, ventanillaActiva, usr);
    }
}
