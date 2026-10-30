package ec.edu.monster.ws;

import ec.edu.monster.controlador.OperacionesController;
import ec.edu.monster.modelo.AuthRespuestaDTO;
import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.modelo.MovimientoDTO;
import ec.edu.monster.modelo.OperacionRespuestaDTO;
import ec.edu.monster.modelo.SucursalDTO;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

/**
 * Servicio Web SOAP de EurekaBank - Grupo 04 (GR04).
 * Dominio unificado: ec.edu.monster | TargetNamespace: http://ws.monster.edu.ec/
 * Expone todas las operaciones bancarias, consultas y semáforo MashUp para los 4 clientes Java.
 */
@WebService(serviceName = "EurekaBankWS", targetNamespace = "http://ws.monster.edu.ec/")
public class EurekaBankWS {

    private final OperacionesController controlador = new OperacionesController();

    @WebMethod(operationName = "autenticarUsuario")
    public AuthRespuestaDTO autenticarUsuario(
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "clave") String clave) {
        return controlador.autenticarUsuario(usuario, clave);
    }

    @WebMethod(operationName = "listarSucursales")
    public List<SucursalDTO> listarSucursales() {
        return controlador.listarSucursales();
    }

    @WebMethod(operationName = "listarCuentasPorSucursal")
    public List<CuentaDetalleDTO> listarCuentasPorSucursal(
            @WebParam(name = "sucursalId") int sucursalId) {
        return controlador.listarCuentasPorSucursal(sucursalId);
    }

    @WebMethod(operationName = "obtenerCuentaPorNumero")
    public CuentaDetalleDTO obtenerCuentaPorNumero(
            @WebParam(name = "numeroCuenta") String numeroCuenta) {
        return controlador.obtenerCuentaPorNumero(numeroCuenta);
    }

    @WebMethod(operationName = "consultarCuentasPorCliente")
    public List<CuentaDetalleDTO> consultarCuentasPorCliente(
            @WebParam(name = "dni") String dni) {
        return controlador.consultarCuentasPorCliente(dni);
    }

    @WebMethod(operationName = "consultarExtracto")
    public List<MovimientoDTO> consultarExtracto(
            @WebParam(name = "numeroCuenta") String numeroCuenta) {
        return controlador.consultarExtracto(numeroCuenta);
    }

    @WebMethod(operationName = "depositar")
    public OperacionRespuestaDTO depositar(
            @WebParam(name = "numeroCuenta") String numeroCuenta,
            @WebParam(name = "monto") double monto,
            @WebParam(name = "empleadoId") int empleadoId,
            @WebParam(name = "ventanilla") int ventanilla) {
        return controlador.depositar(numeroCuenta, monto, empleadoId, ventanilla);
    }

    @WebMethod(operationName = "retirar")
    public OperacionRespuestaDTO retirar(
            @WebParam(name = "numeroCuenta") String numeroCuenta,
            @WebParam(name = "monto") double monto,
            @WebParam(name = "empleadoId") int empleadoId,
            @WebParam(name = "ventanilla") int ventanilla) {
        return controlador.retirar(numeroCuenta, monto, empleadoId, ventanilla);
    }

    @WebMethod(operationName = "transferir")
    public OperacionRespuestaDTO transferir(
            @WebParam(name = "cuentaOrigen") String cuentaOrigen,
            @WebParam(name = "cuentaDestino") String cuentaDestino,
            @WebParam(name = "monto") double monto,
            @WebParam(name = "empleadoId") int empleadoId,
            @WebParam(name = "ventanilla") int ventanilla) {
        return controlador.transferir(cuentaOrigen, cuentaDestino, monto, empleadoId, ventanilla);
    }

    @WebMethod(operationName = "consultarEstadoMashUp")
    public List<CuentaDetalleDTO> consultarEstadoMashUp() {
        return controlador.listarCuentasPorSucursal(0);
    }

    @WebMethod(operationName = "desbloquearCuenta")
    public OperacionRespuestaDTO desbloquearCuenta(
            @WebParam(name = "numeroCuenta") String numeroCuenta,
            @WebParam(name = "usuarioAdmin") String usuarioAdmin) {
        return controlador.desbloquearCuenta(numeroCuenta, usuarioAdmin);
    }

    @WebMethod(operationName = "bloquearCuentaManual")
    public OperacionRespuestaDTO bloquearCuentaManual(
            @WebParam(name = "numeroCuenta") String numeroCuenta,
            @WebParam(name = "ventanilla") int ventanilla,
            @WebParam(name = "usuario") String usuario) {
        return controlador.bloquearCuentaManual(numeroCuenta, ventanilla, usuario);
    }
}
