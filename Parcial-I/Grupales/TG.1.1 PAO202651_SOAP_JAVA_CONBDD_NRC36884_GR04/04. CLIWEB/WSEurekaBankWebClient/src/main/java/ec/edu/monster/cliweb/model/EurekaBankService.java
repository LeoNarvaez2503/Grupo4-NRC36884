package ec.edu.monster.cliweb.model;

import ec.edu.monster.ws.AuthRespuestaDTO;
import ec.edu.monster.ws.CuentaDetalleDTO;
import ec.edu.monster.ws.EurekaBankWS;
import ec.edu.monster.ws.EurekaBankWS_Service;
import ec.edu.monster.ws.MovimientoDTO;
import ec.edu.monster.ws.OperacionRespuestaDTO;
import ec.edu.monster.ws.SucursalDTO;
import jakarta.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa Modelo (Patrón MVC): Consumidor exclusivo del Servicio Web SOAP EurekaBankWS.
 * Cero lógica de negocio local; toda operación se delega al servidor central.
 */
public class EurekaBankService implements java.io.Serializable {

    private final ConfiguracionServidor config = new ConfiguracionServidor();

    private EurekaBankWS obtenerPuerto() {
        EurekaBankWS_Service service = new EurekaBankWS_Service();
        EurekaBankWS port = service.getEurekaBankWSPort();
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getEndpointUrl());
        return port;
    }

    public String getEndpointActual() {
        return config.getEndpointUrl();
    }

    public UsuarioSesion login(String usuario, String clave) {
        AuthRespuestaDTO dto = obtenerPuerto().autenticarUsuario(usuario, clave);
        UsuarioSesion s = new UsuarioSesion();
        s.setAutenticado(dto.isAutenticado());
        s.setMensaje(dto.getMensaje());
        s.setToken(dto.getToken());
        s.setIdEmpleado(dto.getIdEmpleado());
        s.setUsuario(dto.getUsuario());
        s.setNombreCompleto(dto.getNombreCompleto());
        s.setRol(dto.getRol());
        s.setIdSucursal(dto.getIdSucursal());
        s.setNombreSucursal(dto.getNombreSucursal());
        s.setVentanilla(dto.getVentanilla());
        return s;
    }

    public List<SucursalModel> listarSucursales() {
        List<SucursalModel> res = new ArrayList<>();
        for (SucursalDTO dto : obtenerPuerto().listarSucursales()) {
            SucursalModel m = new SucursalModel();
            m.setIdSucursal(dto.getIdSucursal());
            m.setCodigo(dto.getCodigo());
            m.setNombre(dto.getNombre());
            m.setCiudad(dto.getCiudad());
            m.setDireccion(dto.getDireccion());
            m.setTotalCuentas(dto.getTotalCuentas());
            res.add(m);
        }
        return res;
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        List<CuentaModel> res = new ArrayList<>();
        for (CuentaDetalleDTO dto : obtenerPuerto().listarCuentasPorSucursal(sucursalId)) {
            res.add(mapearCuenta(dto));
        }
        return res;
    }

    public CuentaModel obtenerCuentaPorNumero(String numeroCuenta) {
        CuentaDetalleDTO dto = obtenerPuerto().obtenerCuentaPorNumero(numeroCuenta);
        return dto != null ? mapearCuenta(dto) : null;
    }

    public List<CuentaModel> consultarCuentasPorCliente(String dni) {
        List<CuentaModel> res = new ArrayList<>();
        for (CuentaDetalleDTO dto : obtenerPuerto().consultarCuentasPorCliente(dni)) {
            res.add(mapearCuenta(dto));
        }
        return res;
    }

    public List<MovimientoModel> consultarExtracto(String numeroCuenta) {
        List<MovimientoModel> res = new ArrayList<>();
        for (MovimientoDTO dto : obtenerPuerto().consultarExtracto(numeroCuenta)) {
            MovimientoModel m = new MovimientoModel();
            m.setIdMovimiento(dto.getIdMovimiento());
            m.setNumeroOperacion(dto.getNumeroOperacion());
            m.setNumeroCuenta(dto.getNumeroCuenta());
            m.setIdEmpleado(dto.getIdEmpleado());
            m.setNombreEmpleado(dto.getNombreEmpleado());
            m.setVentanilla(dto.getVentanilla());
            m.setTipo(dto.getTipo());
            m.setMonto(dto.getMonto());
            m.setSaldoResultante(dto.getSaldoResultante());
            m.setCuentaReferencia(dto.getCuentaReferencia());
            m.setDescripcion(dto.getDescripcion());
            m.setFechaHora(dto.getFechaHora());
            res.add(m);
        }
        return res;
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        return mapearOperacion(obtenerPuerto().depositar(numeroCuenta, monto, empleadoId, ventanilla));
    }

    public ResultadoOperacion retirar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        return mapearOperacion(obtenerPuerto().retirar(numeroCuenta, monto, empleadoId, ventanilla));
    }

    public ResultadoOperacion transferir(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId, int ventanilla) {
        return mapearOperacion(obtenerPuerto().transferir(cuentaOrigen, cuentaDestino, monto, empleadoId, ventanilla));
    }

    public ResultadoOperacion desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        return mapearOperacion(obtenerPuerto().desbloquearCuenta(numeroCuenta, usuarioAdmin));
    }

    public ResultadoOperacion bloquearCuentaManual(String numeroCuenta, int ventanilla, String usuario) {
        return mapearOperacion(obtenerPuerto().bloquearCuentaManual(numeroCuenta, ventanilla, usuario));
    }

    private CuentaModel mapearCuenta(CuentaDetalleDTO dto) {
        CuentaModel c = new CuentaModel();
        c.setNumeroCuenta(dto.getNumeroCuenta());
        c.setMoneda(dto.getMoneda());
        c.setSaldo(dto.getSaldo());
        c.setEstadoCuenta(dto.getEstadoCuenta());
        c.setDisponibilidadMashup(dto.getDisponibilidadMashup());
        c.setVentanillaBloqueo(dto.getVentanillaBloqueo());
        c.setBloqueadoPor(dto.getBloqueadoPor());
        c.setIdCliente(dto.getIdCliente());
        c.setDniCliente(dto.getDniCliente());
        c.setNombreCliente(dto.getNombreCliente());
        c.setApellidoCliente(dto.getApellidoCliente());
        c.setEmailCliente(dto.getEmailCliente());
        c.setIdSucursal(dto.getIdSucursal());
        c.setNombreSucursal(dto.getNombreSucursal());
        c.setCiudadSucursal(dto.getCiudadSucursal());
        return c;
    }

    private ResultadoOperacion mapearOperacion(OperacionRespuestaDTO dto) {
        ResultadoOperacion r = new ResultadoOperacion();
        r.setExito(dto.isExito());
        r.setCodigo(dto.getCodigo());
        r.setMensaje(dto.getMensaje());
        r.setNumeroOperacion(dto.getNumeroOperacion());
        r.setNumeroCuenta(dto.getNumeroCuenta());
        r.setSaldoAnterior(dto.getSaldoAnterior());
        r.setMonto(dto.getMonto());
        r.setSaldoActual(dto.getSaldoActual());
        r.setFechaHora(dto.getFechaHora());
        return r;
    }
}
