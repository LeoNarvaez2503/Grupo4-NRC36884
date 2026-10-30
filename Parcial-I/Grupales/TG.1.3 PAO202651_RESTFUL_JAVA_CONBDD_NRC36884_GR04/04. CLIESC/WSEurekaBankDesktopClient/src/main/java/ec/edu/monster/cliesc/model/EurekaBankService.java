package ec.edu.monster.cliesc.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa Modelo (Patrón MVC): Consumidor exclusivo de la API RESTful de EurekaBank.
 * Cero lógica de negocio local; toda operación se delega por HTTP/JSON al servidor central.
 */
public class EurekaBankService {

    private final ConfiguracionServidor config = new ConfiguracionServidor();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final Gson gson = new Gson();

    public String getEndpointActual() {
        return config.getEndpointUrl();
    }

    public UsuarioSesion login(String usuario, String clave) {
        Map<String, Object> body = new HashMap<>();
        body.put("usuario", usuario);
        body.put("clave", clave);
        String jsonResp = enviarPost("/auth/login", gson.toJson(body));
        return gson.fromJson(jsonResp, UsuarioSesion.class);
    }

    public List<SucursalModel> listarSucursales() {
        String jsonResp = enviarGet("/sucursales");
        List<SucursalModel> list = gson.fromJson(jsonResp, new TypeToken<List<SucursalModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        String jsonResp = enviarGet("/cuentas?sucursal=" + sucursalId);
        List<CuentaModel> list = gson.fromJson(jsonResp, new TypeToken<List<CuentaModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public CuentaModel obtenerCuentaPorNumero(String numeroCuenta) {
        try {
            HttpResponse<String> resp = enviarGetRaw("/cuentas/" + enc(numeroCuenta));
            if (resp.statusCode() == 404) {
                return null;
            }
            return gson.fromJson(resp.body(), CuentaModel.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CuentaModel> consultarCuentasPorCliente(String dni) {
        String jsonResp = enviarGet("/clientes/" + enc(dni) + "/cuentas");
        List<CuentaModel> list = gson.fromJson(jsonResp, new TypeToken<List<CuentaModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public List<MovimientoModel> consultarExtracto(String numeroCuenta) {
        String jsonResp = enviarGet("/cuentas/" + enc(numeroCuenta) + "/movimientos");
        List<MovimientoModel> list = gson.fromJson(jsonResp, new TypeToken<List<MovimientoModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", numeroCuenta);
        body.put("monto", monto);
        body.put("empleadoId", empleadoId);
        body.put("ventanilla", ventanilla);
        return gson.fromJson(enviarPost("/movimientos/deposito", gson.toJson(body)), ResultadoOperacion.class);
    }

    public ResultadoOperacion retirar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", numeroCuenta);
        body.put("monto", monto);
        body.put("empleadoId", empleadoId);
        body.put("ventanilla", ventanilla);
        return gson.fromJson(enviarPost("/movimientos/retiro", gson.toJson(body)), ResultadoOperacion.class);
    }

    public ResultadoOperacion transferir(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId, int ventanilla) {
        Map<String, Object> body = new HashMap<>();
        body.put("cuentaOrigen", cuentaOrigen);
        body.put("cuentaDestino", cuentaDestino);
        body.put("monto", monto);
        body.put("empleadoId", empleadoId);
        body.put("ventanilla", ventanilla);
        return gson.fromJson(enviarPost("/movimientos/transferencia", gson.toJson(body)), ResultadoOperacion.class);
    }

    public ResultadoOperacion desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        Map<String, Object> body = new HashMap<>();
        body.put("usuarioAdmin", usuarioAdmin);
        return gson.fromJson(enviarPost("/cuentas/" + enc(numeroCuenta) + "/desbloquear", gson.toJson(body)), ResultadoOperacion.class);
    }

    public ResultadoOperacion bloquearCuentaManual(String numeroCuenta, int ventanilla, String usuario) {
        Map<String, Object> body = new HashMap<>();
        body.put("ventanilla", ventanilla);
        body.put("usuarioAdmin", usuario);
        return gson.fromJson(enviarPost("/cuentas/" + enc(numeroCuenta) + "/bloquear", gson.toJson(body)), ResultadoOperacion.class);
    }

    private HttpResponse<String> enviarGetRaw(String ruta) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(config.getEndpointUrl() + ruta))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error HTTP GET " + ruta + ": " + e.getMessage(), e);
        }
    }

    private String enviarGet(String ruta) {
        return enviarGetRaw(ruta).body();
    }

    private String enviarPost(String ruta, String jsonBody) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(config.getEndpointUrl() + ruta))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return resp.body();
        } catch (Exception e) {
            throw new RuntimeException("Error HTTP POST " + ruta + ": " + e.getMessage(), e);
        }
    }

    private String enc(String v) {
        return URLEncoder.encode(v == null ? "" : v.trim(), StandardCharsets.UTF_8);
    }
}
