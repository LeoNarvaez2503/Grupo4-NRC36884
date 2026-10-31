package ec.edu.monster.servicio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import java.io.Serializable;
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
 * Cliente Consumidor RESTful hacia el Banco BanQuito (JAX-RS / Jersey).
 * Base URL por defecto: http://localhost:8080/api/credito
 */
public class BanQuitoRestClient implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_BASE_URL = "http://localhost:8080/api/credito";
    private static final String PAYARA_BASE_URL = "http://localhost:8080/WS_BanQuito_Java_REST/api/credito";
    private final String baseUrl;
    private volatile String resolvedBaseUrl;
    private final transient HttpClient client;
    private final transient Gson gson;

    public BanQuitoRestClient() {
        this(DEFAULT_BASE_URL);
    }

    public BanQuitoRestClient(String baseUrl) {
        this.baseUrl = baseUrl != null ? baseUrl : DEFAULT_BASE_URL;
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.gson = new Gson();
    }

    private String getBaseUrl() {
        if (resolvedBaseUrl != null) {
            return resolvedBaseUrl;
        }
        // Intentar baseUrl inicial (ej: http://localhost:8080/api/credito)
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(baseUrl)).timeout(Duration.ofSeconds(2)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBaseUrl = baseUrl;
                return resolvedBaseUrl;
            }
        } catch (Exception ignored) {}

        // Si falló o dio 404, probar la ruta en Payara/GlassFish (/WS_BanQuito_Java_REST/api/credito)
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(PAYARA_BASE_URL)).timeout(Duration.ofSeconds(2)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBaseUrl = PAYARA_BASE_URL;
                return resolvedBaseUrl;
            }
        } catch (Exception ignored) {}

        resolvedBaseUrl = baseUrl;
        return resolvedBaseUrl;
    }

    // ÍTEM 5: Invocar servicio Web que valida si una persona es sujeto de crédito
    public SujetoCreditoResponse validarSujetoCredito(String cedula) {
        try {
            String url = getBaseUrl() + "/es-sujeto/" + URLEncoder.encode(cedula.trim(), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), SujetoCreditoResponse.class);
            } else {
                SujetoCreditoResponse err = new SujetoCreditoResponse();
                err.esSujeto = false;
                err.motivo = "Error HTTP " + response.statusCode() + " al consultar BanQuito.";
                return err;
            }
        } catch (Exception ex) {
            SujetoCreditoResponse err = new SujetoCreditoResponse();
            err.esSujeto = false;
            err.motivo = "Error de comunicación con el Core Bancario: " + ex.getMessage();
            return err;
        }
    }

    // ÍTEM 6: Invocar servicio Web que retorna el monto máximo de crédito
    public MontoMaximoResponse obtenerMontoMaximo(int codCliente) {
        try {
            String url = getBaseUrl() + "/monto-maximo/" + codCliente;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), MontoMaximoResponse.class);
            }
        } catch (Exception ignored) {}
        MontoMaximoResponse def = new MontoMaximoResponse();
        def.codCliente = codCliente;
        def.montoMaximo = 0.0;
        def.detalleCalculo = "No se pudo obtener la capacidad de crédito.";
        return def;
    }

    // ÍTEM 7: Invocar servicio Web que otorga el crédito y crea la tabla de amortización
    public OtorgarCreditoResponse otorgarCredito(int codCliente, double precioTelefono, int numeroCuotas) {
        try {
            String url = getBaseUrl() + "/otorgar";
            Map<String, Object> body = new HashMap<>();
            body.put("codCliente", codCliente);
            body.put("precioTelefono", precioTelefono);
            body.put("numeroCuotas", numeroCuotas);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return gson.fromJson(response.body(), OtorgarCreditoResponse.class);
        } catch (Exception ex) {
            OtorgarCreditoResponse err = new OtorgarCreditoResponse();
            err.exito = false;
            err.mensaje = "Error al solicitar crédito en BanQuito: " + ex.getMessage();
            return err;
        }
    }

    // ÍTEM 10 & 11: Invocar servicio Web que retorna la tabla de amortización
    public List<CuotaAmortizacion> obtenerTablaAmortizacion(int codCredito) {
        try {
            String url = getBaseUrl() + "/tabla-amortizacion/" + codCredito;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<CuotaAmortizacion>>(){}.getType());
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    public List<CuotaAmortizacion> simularAmortizacion(double monto, int cuotas) {
        try {
            String url = getBaseUrl() + "/simular?monto=" + monto + "&cuotas=" + cuotas;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<CuotaAmortizacion>>(){}.getType());
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }
}
