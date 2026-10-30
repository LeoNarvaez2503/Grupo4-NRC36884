package ec.edu.monster.climov.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Capa Modelo (Patrón MVC Tradicional): Consumidor nativo Android de la API RESTful de EurekaBank.
 * Cero lógica de negocio local; toda operación se delega vía HTTP/JSON al servidor central.
 */
public class EurekaBankService {

    public String getEndpointActual() {
        return ConfiguracionConexion.getRestBaseUrl();
    }

    public UsuarioSesion login(String usuario, String clave) {
        try {
            JSONObject req = new JSONObject();
            req.put("usuario", usuario);
            req.put("clave", clave);
            JSONObject obj = new JSONObject(enviarPost("/auth/login", req.toString()));
            UsuarioSesion s = new UsuarioSesion();
            s.setAutenticado(obj.optBoolean("autenticado", false));
            s.setMensaje(obj.optString("mensaje", ""));
            s.setToken(obj.optString("token", ""));
            s.setIdEmpleado(obj.optInt("idEmpleado", 1));
            s.setUsuario(obj.optString("usuario", ""));
            s.setNombreCompleto(obj.optString("nombreCompleto", ""));
            s.setRol(obj.optString("rol", ""));
            s.setIdSucursal(obj.optInt("idSucursal", 1));
            s.setNombreSucursal(obj.optString("nombreSucursal", ""));
            s.setVentanilla(obj.optInt("ventanilla", 1));
            return s;
        } catch (Exception e) {
            throw new RuntimeException("Error REST Login: " + e.getMessage(), e);
        }
    }

    public List<SucursalModel> listarSucursales() {
        try {
            JSONArray arr = new JSONArray(enviarGet("/sucursales"));
            List<SucursalModel> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                SucursalModel s = new SucursalModel();
                s.setIdSucursal(o.optInt("idSucursal"));
                s.setCodigo(o.optString("codigo"));
                s.setNombre(o.optString("nombre"));
                s.setCiudad(o.optString("ciudad"));
                s.setDireccion(o.optString("direccion"));
                s.setTotalCuentas(o.optInt("totalCuentas"));
                lista.add(s);
            }
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error REST Sucursales: " + e.getMessage(), e);
        }
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        try {
            JSONArray arr = new JSONArray(enviarGet("/cuentas?sucursal=" + sucursalId));
            List<CuentaModel> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                lista.add(mapearCuenta(arr.getJSONObject(i)));
            }
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error REST Cuentas: " + e.getMessage(), e);
        }
    }

    public List<MovimientoModel> consultarExtracto(String numeroCuenta) {
        try {
            String ctaEnc = URLEncoder.encode(numeroCuenta.trim(), "UTF-8");
            JSONArray arr = new JSONArray(enviarGet("/cuentas/" + ctaEnc + "/movimientos"));
            List<MovimientoModel> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                MovimientoModel m = new MovimientoModel();
                m.setIdMovimiento(o.optInt("idMovimiento"));
                m.setNumeroOperacion(o.optString("numeroOperacion"));
                m.setNumeroCuenta(o.optString("numeroCuenta"));
                m.setIdEmpleado(o.optInt("idEmpleado"));
                m.setNombreEmpleado(o.optString("nombreEmpleado"));
                m.setVentanilla(o.optInt("ventanilla"));
                m.setTipo(o.optString("tipo"));
                m.setMonto(o.optDouble("monto"));
                m.setSaldoResultante(o.optDouble("saldoResultante"));
                m.setCuentaReferencia(o.optString("cuentaReferencia"));
                m.setDescripcion(o.optString("descripcion"));
                m.setFechaHora(o.optString("fechaHora"));
                lista.add(m);
            }
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error REST Extracto: " + e.getMessage(), e);
        }
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        try {
            JSONObject req = new JSONObject();
            req.put("numeroCuenta", numeroCuenta);
            req.put("monto", monto);
            req.put("empleadoId", empleadoId);
            req.put("ventanilla", ventanilla);
            return mapearOperacion(new JSONObject(enviarPost("/movimientos/deposito", req.toString())));
        } catch (Exception e) {
            throw new RuntimeException("Error REST Depósito: " + e.getMessage(), e);
        }
    }

    public ResultadoOperacion retirar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        try {
            JSONObject req = new JSONObject();
            req.put("numeroCuenta", numeroCuenta);
            req.put("monto", monto);
            req.put("empleadoId", empleadoId);
            req.put("ventanilla", ventanilla);
            return mapearOperacion(new JSONObject(enviarPost("/movimientos/retiro", req.toString())));
        } catch (Exception e) {
            throw new RuntimeException("Error REST Retiro: " + e.getMessage(), e);
        }
    }

    public ResultadoOperacion transferir(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId, int ventanilla) {
        try {
            JSONObject req = new JSONObject();
            req.put("cuentaOrigen", cuentaOrigen);
            req.put("cuentaDestino", cuentaDestino);
            req.put("monto", monto);
            req.put("empleadoId", empleadoId);
            req.put("ventanilla", ventanilla);
            return mapearOperacion(new JSONObject(enviarPost("/movimientos/transferencia", req.toString())));
        } catch (Exception e) {
            throw new RuntimeException("Error REST Transferencia: " + e.getMessage(), e);
        }
    }

    public ResultadoOperacion desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        try {
            String ctaEnc = URLEncoder.encode(numeroCuenta.trim(), "UTF-8");
            JSONObject req = new JSONObject();
            req.put("usuarioAdmin", usuarioAdmin);
            return mapearOperacion(new JSONObject(enviarPost("/cuentas/" + ctaEnc + "/desbloquear", req.toString())));
        } catch (Exception e) {
            throw new RuntimeException("Error REST Desbloqueo: " + e.getMessage(), e);
        }
    }

    private CuentaModel mapearCuenta(JSONObject o) {
        CuentaModel c = new CuentaModel();
        c.setNumeroCuenta(o.optString("numeroCuenta"));
        c.setMoneda(o.optString("moneda"));
        c.setSaldo(o.optDouble("saldo"));
        c.setEstadoCuenta(o.optString("estadoCuenta"));
        c.setDisponibilidadMashup(o.optString("disponibilidadMashup", "VERDE"));
        c.setVentanillaBloqueo(o.optInt("ventanillaBloqueo", 0));
        c.setBloqueadoPor(o.optString("bloqueadoPor", ""));
        c.setIdCliente(o.optInt("idCliente"));
        c.setDniCliente(o.optString("dniCliente"));
        c.setNombreCliente(o.optString("nombreCliente"));
        c.setApellidoCliente(o.optString("apellidoCliente"));
        c.setEmailCliente(o.optString("emailCliente"));
        c.setIdSucursal(o.optInt("idSucursal"));
        c.setNombreSucursal(o.optString("nombreSucursal"));
        c.setCiudadSucursal(o.optString("ciudadSucursal"));
        return c;
    }

    private ResultadoOperacion mapearOperacion(JSONObject o) {
        ResultadoOperacion r = new ResultadoOperacion();
        r.setExito(o.optBoolean("exito", false));
        r.setCodigo(o.optString("codigo", ""));
        r.setMensaje(o.optString("mensaje", ""));
        r.setNumeroOperacion(o.optString("numeroOperacion", ""));
        r.setNumeroCuenta(o.optString("numeroCuenta", ""));
        r.setSaldoAnterior(o.optDouble("saldoAnterior", 0.0));
        r.setMonto(o.optDouble("monto", 0.0));
        r.setSaldoActual(o.optDouble("saldoActual", 0.0));
        r.setFechaHora(o.optString("fechaHora", ""));
        return r;
    }

    private String enviarGet(String ruta) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(ConfiguracionConexion.getRestBaseUrl() + ruta);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Accept", "application/json");
            InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
                return sc.hasNext() ? sc.next() : "[]";
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String enviarPost(String ruta, String jsonBody) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(ConfiguracionConexion.getRestBaseUrl() + ruta);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }
            InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
                return sc.hasNext() ? sc.next() : "{}";
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
