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

    /** Host de BanQuito compartido por todas las instancias: 04. CONFIG/servidor.properties o el campo del login. */
    private static volatile String hostActual = cargarHostCentral();
    private static volatile String resolvedBaseUrl;
    private static volatile String resolvedHost;
    private final transient HttpClient client;
    private final transient Gson gson;


    /** Host central: -Dexamen.host, variable EXAMEN_HOST o "04. CONFIG/servidor.properties" (copiado al classpath al compilar). */
    private static String cargarHostCentral() {
        String host = "localhost";
        try (java.io.InputStream in = BanQuitoRestClient.class.getClassLoader().getResourceAsStream("servidor.properties")) {
            if (in != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(in);
                host = p.getProperty("servidor.host", host).trim();
            }
        } catch (Exception ignored) {
        }
        String env = System.getenv("EXAMEN_HOST");
        if (env != null && !env.isBlank()) host = env.trim();
        String sys = System.getProperty("examen.host");
        if (sys != null && !sys.isBlank()) host = sys.trim();
        return host;
    }

    public static String getHost() {
        return hostActual;
    }

    /** Cambia el host de BanQuito para toda la aplicacion (por ejemplo desde el login). */
    public static synchronized void configurarHost(String nuevoHost) {
        String h = nuevoHost == null ? "" : nuevoHost.trim();
        if (h.startsWith("http://")) h = h.substring(7);
        if (h.startsWith("https://")) h = h.substring(8);
        int corte = h.indexOf('/');
        if (corte >= 0) h = h.substring(0, corte);
        int dosPuntos = h.indexOf(':');
        if (dosPuntos >= 0) h = h.substring(0, dosPuntos);
        if (h.isEmpty()) return;
        hostActual = h;
        resolvedBaseUrl = null;
        resolvedHost = null;
    }

    public BanQuitoRestClient() {
        this((String) null);
    }

    public BanQuitoRestClient(String baseUrl) {
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.gson = new Gson();
    }

    private String getBaseUrl() {
        String host = hostActual;
        if (resolvedBaseUrl != null && host.equals(resolvedHost)) {
            return resolvedBaseUrl;
        }
        String directa = "http://" + host + ":8080/api/credito";
        String payara = "http://" + host + ":8080/WS_BanQuito_Java_REST/api/credito";
        // Intentar la ruta directa (servidor embebido) y luego la de Payara/GlassFish
        for (String candidata : new String[]{directa, payara}) {
            try {
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(candidata)).timeout(Duration.ofSeconds(2)).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() == 200) {
                    resolvedHost = host;
                    resolvedBaseUrl = candidata;
                    return resolvedBaseUrl;
                }
            } catch (Exception ignored) {}
        }
        resolvedHost = host;
        resolvedBaseUrl = directa;
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
