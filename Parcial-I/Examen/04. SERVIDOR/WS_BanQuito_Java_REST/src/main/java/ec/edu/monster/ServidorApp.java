package ec.edu.monster;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.MontoMaximoDTO;
import ec.edu.monster.modelo.OtorgarCreditoDTO;
import ec.edu.monster.modelo.SujetoCreditoDTO;
import ec.edu.monster.servicio.CreditoService;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Servidor Web RESTful Embebido de BanQuito Core - Módulo de Crédito.
 * Permite ejecutar el servicio REST en Java de manera inmediata en el puerto 8080.
 * NRC: 36884 - Grupo 04
 */
public class ServidorApp {

    private static final int PUERTO = 8080;
    private static final CreditoService service = new CreditoService();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);
        server.createContext("/api/credito", new CreditoHandler());
        server.createContext("/", new RootHandler());
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();

        System.out.println("=======================================================================");
        System.out.println("  BANQUITO CORE - MÓDULO DE CRÉDITO (RESTFUL WEB SERVICE EN JAVA)");
        System.out.println("  NRC: 36884 | Grupo: 04 | Puerto: " + PUERTO);
        System.out.println("  Base URI: http://localhost:" + PUERTO + "/api/credito");
        System.out.println("=======================================================================");
        System.out.println("  Endpoints disponibles:");
        System.out.println("  - GET  /api/credito/es-sujeto/{cedula}");
        System.out.println("  - GET  /api/credito/monto-maximo/{codCliente}");
        System.out.println("  - POST /api/credito/otorgar");
        System.out.println("  - GET  /api/credito/tabla-amortizacion/{codCredito}");
        System.out.println("  - GET  /api/credito/tabla-amortizacion/cliente/{codCliente}");
        System.out.println("  - GET  /api/credito/clientes");
        System.out.println("  - GET  /api/credito/simular?monto=450&cuotas=6");
        System.out.println("=======================================================================");
    }

    static class CreditoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            String method = exchange.getRequestMethod();

            try {
                // GET /api/credito/es-sujeto/{cedula} o /api/credito/verificar/{cedula}
                if ("GET".equalsIgnoreCase(method) && (path.contains("/es-sujeto/") || path.contains("/verificar/"))) {
                    String[] parts = path.split("/");
                    String cedula = parts[parts.length - 1];
                    SujetoCreditoDTO resp = service.esSujetoCredito(cedula);
                    responderJson(exchange, 200, resp);
                    return;
                }

                // GET /api/credito/monto-maximo/{codCliente}
                if ("GET".equalsIgnoreCase(method) && path.contains("/monto-maximo/")) {
                    String[] parts = path.split("/");
                    int codCliente = Integer.parseInt(parts[parts.length - 1]);
                    MontoMaximoDTO resp = service.calcularMontoMaximo(codCliente);
                    responderJson(exchange, 200, resp);
                    return;
                }

                // POST /api/credito/otorgar o /api/credito/crear-tabla-amortizacion
                if ("POST".equalsIgnoreCase(method) && (path.endsWith("/otorgar") || path.endsWith("/crear-tabla-amortizacion"))) {
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    OtorgarCreditoDTO req = gson.fromJson(reader, OtorgarCreditoDTO.class);
                    OtorgarCreditoDTO resp = service.otorgarCredito(req.getCodCliente(), req.getPrecioTelefono(), req.getNumeroCuotas());
                    responderJson(exchange, resp.isExito() ? 200 : 400, resp);
                    return;
                }

                // GET /api/credito/tabla-amortizacion/cliente/{codCliente}
                if ("GET".equalsIgnoreCase(method) && path.contains("/tabla-amortizacion/cliente/")) {
                    String[] parts = path.split("/");
                    int codCliente = Integer.parseInt(parts[parts.length - 1]);
                    OtorgarCreditoDTO resp = service.obtenerCreditoActivoPorCliente(codCliente);
                    responderJson(exchange, resp.isExito() ? 200 : 404, resp);
                    return;
                }

                // GET /api/credito/tabla-amortizacion/{codCredito} o /api/credito/obtener-tabla-amortizacion/{codCredito}
                if ("GET".equalsIgnoreCase(method) && (path.contains("/tabla-amortizacion/") || path.contains("/obtener-tabla-amortizacion/"))) {
                    String[] parts = path.split("/");
                    int codCredito = Integer.parseInt(parts[parts.length - 1]);
                    List<CuotaAmortizacion> tabla = service.obtenerTablaAmortizacion(codCredito);
                    if (tabla.isEmpty()) {
                        responderJson(exchange, 404, Map.of("error", "No existe tabla de amortización para el crédito #" + codCredito));
                    } else {
                        responderJson(exchange, 200, tabla);
                    }
                    return;
                }

                // GET /api/credito/clientes
                if ("GET".equalsIgnoreCase(method) && path.endsWith("/clientes")) {
                    List<Cliente> clientes = service.listarClientes();
                    responderJson(exchange, 200, clientes);
                    return;
                }

                // GET /api/credito/simular?monto=450&cuotas=6
                if ("GET".equalsIgnoreCase(method) && path.contains("/simular")) {
                    Map<String, String> queryParams = parseQuery(uri.getQuery());
                    double monto = Double.parseDouble(queryParams.getOrDefault("monto", "450"));
                    int cuotas = Integer.parseInt(queryParams.getOrDefault("cuotas", "6"));
                    List<CuotaAmortizacion> tabla = service.generarTablaAmortizacion(monto, cuotas, CreditoService.TASA_INTERES_ANUAL);
                    responderJson(exchange, 200, tabla);
                    return;
                }

                // Ruta no encontrada
                responderJson(exchange, 404, Map.of("error", "Recurso no encontrado: " + path));

            } catch (Exception ex) {
                responderJson(exchange, 500, Map.of("error", "Error interno en el servidor: " + ex.getMessage()));
            }
        }
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset='utf-8'/>
                    <title>BanQuito Core - RESTful API</title>
                    <style>
                        body { font-family: sans-serif; background: #f4f6f9; padding: 40px; color: #333; }
                        .card { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); max-width: 800px; margin: auto; }
                        h1 { color: #003366; }
                        code { background: #eef2f7; padding: 3px 6px; border-radius: 4px; font-weight: bold; }
                        li { margin: 10px 0; }
                    </style>
                </head>
                <body>
                    <div class='card'>
                        <h1>BanQuito Core - Módulo de Crédito (Java RESTful)</h1>
                        <p>Servicios Web RESTful en ejecución activa para el Examen Complexivo Práctico (NRC 36884 - Grupo 04).</p>
                        <h3>Endpoints Públicos:</h3>
                        <ul>
                            <li><code>GET /api/credito/es-sujeto/{cedula}</code> - Valida si el cliente es sujeto de crédito</li>
                            <li><code>GET /api/credito/monto-maximo/{codCliente}</code> - Retorna cupo máximo aprobado</li>
                            <li><code>POST /api/credito/otorgar</code> - Otorga crédito y registra tabla de amortización</li>
                            <li><code>GET /api/credito/tabla-amortizacion/{codCredito}</code> - Retorna cuotas amortizadas</li>
                            <li><code>GET /api/credito/clientes</code> - Lista los clientes del banco</li>
                            <li><code>GET /api/credito/simular?monto=450&cuotas=6</code> - Simulación sistema francés</li>
                        </ul>
                    </div>
                </body>
                </html>
                """;
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static void responderJson(HttpExchange exchange, int status, Object data) throws IOException {
        String json = gson.toJson(data);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void agregarHeadersCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isBlank()) return result;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else if (pair.length == 1) {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
