package ec.edu.monster;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.TelefonoCelular;
import ec.edu.monster.servicio.BanQuitoRestClient;
import ec.edu.monster.servicio.FacturacionService;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Aplicación Web de la Comercializadora de Teléfonos Celulares (Java / RESTful Client).
 * Integra catálogo de celulares, facturación (Efectivo -42% y Crédito Directo BanQuito)
 * y consulta de amortización.
 * NRC: 36884 | Grupo: 04 | Puerto: 8090
 */
public class ComercializadoraApp {

    private static final int PUERTO = 8090;
    private static final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private static final FacturaDAO facturaDAO = new FacturaDAO();
    private static final FacturacionService facturacionService = new FacturacionService();
    private static final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);

        // Rutas Web y APIs
        server.createContext("/", new PaginaHandler());
        server.createContext("/api/telefonos", new TelefonosApiHandler());
        server.createContext("/api/facturar", new FacturarApiHandler());
        server.createContext("/api/verificar-credito", new VerificarCreditoApiHandler());
        server.createContext("/api/amortizacion", new AmortizacionApiHandler());
        server.createContext("/api/facturas", new FacturasListApiHandler());

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();

        System.out.println("=======================================================================");
        System.out.println("  COMERCIALIZADORA DE TELÉFONOS CELULARES (CLIENTE WEB JAVA RESTFUL)");
        System.out.println("  NRC: 36884 | Grupo: 04 | Puerto: " + PUERTO);
        System.out.println("  URL de Acceso: http://localhost:" + PUERTO + "/");
        System.out.println("=======================================================================");
    }

    // Handler para servir la aplicación Web interactiva moderna
    static class PaginaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/api/")) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] bytes = cargarHtmlPagina();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static byte[] cargarHtmlPagina() {
        try {
            // 1. Intentar desde classpath
            InputStream is = ComercializadoraApp.class.getResourceAsStream("/static/index.html");
            if (is != null) {
                try (is) {
                    return is.readAllBytes();
                }
            }
            // 2. Intentar desde disco local
            Path pathDisk = Path.of("src/main/resources/static/index.html");
            if (Files.exists(pathDisk)) {
                return Files.readAllBytes(pathDisk);
            }
            Path pathWebapp = Path.of("src/main/webapp/index.html");
            if (Files.exists(pathWebapp)) {
                return Files.readAllBytes(pathWebapp);
            }
        } catch (Exception ignored) {}
        return "<h1>Error cargando index.html</h1>".getBytes(StandardCharsets.UTF_8);
    }

    // Handler de verificación de crédito en vivo (AJAX)
    static class VerificarCreditoApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String cedula = params.getOrDefault("cedula", "");
            int codTelefono = Integer.parseInt(params.getOrDefault("codTelefono", "0"));
            int cuotas = Integer.parseInt(params.getOrDefault("cuotas", "6"));

            try {
                TelefonoCelular tel = catalogoDAO.buscarPorId(codTelefono);
                double precio = tel != null ? tel.getPrecio() : 0.0;

                // 1. Validar sujeto de crédito en BanQuito REST API
                SujetoCreditoResponse sujeto = banquitoClient.validarSujetoCredito(cedula);

                // 2. Obtener cupo si es cliente
                MontoMaximoResponse capacidad = null;
                boolean capacidadSuficiente = false;
                List<CuotaAmortizacion> amortizacion = List.of();

                if (sujeto.codCliente > 0) {
                    capacidad = banquitoClient.obtenerMontoMaximo(sujeto.codCliente);
                    capacidadSuficiente = (precio <= capacidad.montoMaximo);
                    if (sujeto.esSujeto && capacidadSuficiente && cuotas >= 3 && cuotas <= 18) {
                        amortizacion = banquitoClient.simularAmortizacion(precio, cuotas);
                    }
                }

                Map<String, Object> resp = new HashMap<>();
                resp.put("exito", true);
                resp.put("sujeto", sujeto);
                resp.put("capacidad", capacidad);
                resp.put("precioTelefono", precio);
                resp.put("capacidadSuficiente", capacidadSuficiente);
                resp.put("tablaAmortizacion", amortizacion);

                responderJson(exchange, 200, resp);
            } catch (Exception ex) {
                responderJson(exchange, 500, Map.of("exito", false, "mensaje", ex.getMessage()));
            }
        }
    }

    // Handler de emisión de Facturas (Efectivo / Crédito)
    static class FacturarApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Map<String, Object> body = gson.fromJson(reader, Map.class);

                String cedula = (String) body.getOrDefault("cedula", "");
                String nombre = (String) body.getOrDefault("nombre", "");
                int codTelefono = ((Double) body.getOrDefault("codTelefono", 0.0)).intValue();
                String formaPago = (String) body.getOrDefault("formaPago", "EFECTIVO");
                int cuotas = ((Double) body.getOrDefault("cuotas", 6.0)).intValue();

                FacturacionService.ResultadoFactura res;
                if ("EFECTIVO".equalsIgnoreCase(formaPago)) {
                    res = facturacionService.facturarEfectivo(cedula, nombre, codTelefono);
                } else {
                    res = facturacionService.facturarCreditoDirecto(cedula, nombre, codTelefono, cuotas);
                }

                responderJson(exchange, res.exito() ? 200 : 400, res);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // Handler CRUD Catálogo de Teléfonos
    static class TelefonosApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String method = exchange.getRequestMethod();
            try {
                if ("GET".equalsIgnoreCase(method)) {
                    List<TelefonoCelular> telefonos = catalogoDAO.listar(false);
                    responderJson(exchange, 200, telefonos);
                } else if ("POST".equalsIgnoreCase(method)) {
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    TelefonoCelular tel = gson.fromJson(reader, TelefonoCelular.class);
                    int id = catalogoDAO.insertar(tel);
                    tel.setCodTelefono(id);
                    responderJson(exchange, 201, tel);
                } else if ("PUT".equalsIgnoreCase(method)) {
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    TelefonoCelular tel = gson.fromJson(reader, TelefonoCelular.class);
                    catalogoDAO.actualizar(tel);
                    responderJson(exchange, 200, tel);
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    Map<String, String> q = parseQuery(exchange.getRequestURI().getQuery());
                    int id = Integer.parseInt(q.getOrDefault("id", "0"));
                    catalogoDAO.desactivar(id);
                    responderJson(exchange, 200, Map.of("exito", true));
                }
            } catch (Exception ex) {
                responderJson(exchange, 500, Map.of("error", ex.getMessage()));
            }
        }
    }

    // Handler Consulta de Amortización
    static class AmortizacionApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> q = parseQuery(exchange.getRequestURI().getQuery());
            int codCredito = Integer.parseInt(q.getOrDefault("codCredito", "0"));
            String cedula = q.getOrDefault("cedula", "").trim();

            if (codCredito > 0) {
                List<CuotaAmortizacion> tabla = banquitoClient.obtenerTablaAmortizacion(codCredito);
                responderJson(exchange, 200, tabla);
            } else if (!cedula.isBlank()) {
                SujetoCreditoResponse sujeto = banquitoClient.validarSujetoCredito(cedula);
                if (sujeto.codCliente > 0) {
                    Factura fac = null;
                    try {
                        for (Factura f : facturaDAO.listar()) {
                            if (cedula.equals(f.getCedulaCliente()) && f.getCodCreditoBanco() != null) {
                                fac = f;
                                break;
                            }
                        }
                    } catch (Exception ignored) {}

                    int credId = fac != null && fac.getCodCreditoBanco() != null ? fac.getCodCreditoBanco() : 1;
                    List<CuotaAmortizacion> tabla = banquitoClient.obtenerTablaAmortizacion(credId);
                    responderJson(exchange, 200, Map.of("cliente", sujeto, "creditoId", credId, "tabla", tabla));
                } else {
                    responderJson(exchange, 404, Map.of("error", "Cliente no encontrado con la cédula " + cedula));
                }
            } else {
                responderJson(exchange, 400, Map.of("error", "Especifique codCredito o cedula"));
            }
        }
    }

    // Handler de Lista de Facturas
    static class FacturasListApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            agregarHeadersCORS(exchange);
            try {
                List<Factura> list = facturacionService.listarFacturas();
                responderJson(exchange, 200, list);
            } catch (Exception ex) {
                responderJson(exchange, 500, Map.of("error", ex.getMessage()));
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
            try {
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String val = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
                result.put(key, val);
            } catch (Exception ignored) {}
        }
        return result;
    }
}
