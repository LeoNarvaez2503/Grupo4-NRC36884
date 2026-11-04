package ec.edu.monster.cliesc.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.prefs.Preferences;

/**
 * Gestor de Configuración de Conexión y Endpoints del Servidor Payara / BanQuito.
 * Permite cambiar la dirección IP y puerto en caliente para conectarse desde cualquier equipo en la red.
 */
public class ConfiguracionServidor {

    private static final String PREF_KEY_HOST = "server_host";
    private static final String PREF_KEY_PORT = "server_port";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private final Preferences prefs;
    private String host;
    private int port;

    private String comEndpoint;
    private String banquitoEndpoint;
    private String resolvedCom = null;
    private String resolvedBanquito = null;

    public ConfiguracionServidor() {
        this.prefs = Preferences.userNodeForPackage(ConfiguracionServidor.class);
        this.host = prefs.get(PREF_KEY_HOST, DEFAULT_HOST);
        this.port = prefs.getInt(PREF_KEY_PORT, DEFAULT_PORT);
        actualizarEndpoints();
    }

    private void actualizarEndpoints() {
        this.comEndpoint = "http://" + host + ":" + port + "/WS_Comercializadora_WebClient/api";
        this.banquitoEndpoint = "http://" + host + ":" + port + "/WS_BanQuito_Java_REST/api/credito";
        this.resolvedCom = null;
        this.resolvedBanquito = null;
    }

    public static String normalizarHost(String input) {
        if (input == null || input.isBlank()) return DEFAULT_HOST;
        input = input.trim();
        if (input.startsWith("http://")) input = input.substring(7);
        if (input.startsWith("https://")) input = input.substring(8);
        if (input.endsWith("/")) input = input.substring(0, input.length() - 1);
        return input;
    }

    public synchronized void configurar(String nuevoHost, int nuevoPuerto) {
        String h = normalizarHost(nuevoHost);
        int p = nuevoPuerto > 0 ? nuevoPuerto : DEFAULT_PORT;
        if (h.contains(":")) {
            String[] parts = h.split(":");
            h = parts[0];
            try {
                p = Integer.parseInt(parts[1]);
            } catch (Exception ignored) {}
        }
        this.host = h;
        this.port = p;
        prefs.put(PREF_KEY_HOST, this.host);
        prefs.putInt(PREF_KEY_PORT, this.port);
        actualizarEndpoints();
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getUrlBase() { return "http://" + host + ":" + port; }

    public String getComEndpoint() {
        if (resolvedCom != null) return resolvedCom;
        String[] candidatos = new String[]{
            "http://" + host + ":" + port + "/WS_Comercializadora_WebClient/api",
            "http://" + host + ":8090/api",
            "http://" + host + ":" + port + "/api"
        };
        for (String urlStr : candidatos) {
            try {
                HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(900)).build();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(urlStr + "/telefonos")).timeout(Duration.ofMillis(900)).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() == 200) {
                    resolvedCom = urlStr;
                    return resolvedCom;
                }
            } catch (Exception ignored) {}
        }
        resolvedCom = comEndpoint;
        return resolvedCom;
    }

    public void setComEndpoint(String comEndpoint) {
        this.comEndpoint = comEndpoint;
        this.resolvedCom = comEndpoint;
    }

    public String getBanquitoEndpoint() {
        if (resolvedBanquito != null) return resolvedBanquito;
        String[] candidatos = new String[]{
            "http://" + host + ":" + port + "/WS_BanQuito_Java_REST/api/credito",
            "http://" + host + ":" + port + "/api/credito"
        };
        for (String urlStr : candidatos) {
            try {
                HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(900)).build();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(urlStr + "/verificar/1710000001")).timeout(Duration.ofMillis(900)).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() == 200) {
                    resolvedBanquito = urlStr;
                    return resolvedBanquito;
                }
            } catch (Exception ignored) {}
        }
        resolvedBanquito = banquitoEndpoint;
        return resolvedBanquito;
    }

    public void setBanquitoEndpoint(String banquitoEndpoint) {
        this.banquitoEndpoint = banquitoEndpoint;
        this.resolvedBanquito = banquitoEndpoint;
    }

    /**
     * Prueba la conexión a la IP y puerto especificados y retorna el diagnóstico.
     */
    public static ResultadoPrueba probarConexion(String testHost, int testPort) {
        String h = normalizarHost(testHost);
        int p = testPort > 0 ? testPort : DEFAULT_PORT;
        if (h.contains(":")) {
            String[] parts = h.split(":");
            h = parts[0];
            try { p = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
        }

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1500)).build();
        boolean banquitoOk = false;
        boolean comercializadoraOk = false;
        String detalle = "";

        // Probar BanQuito REST
        String banquitoUrl = "http://" + h + ":" + p + "/WS_BanQuito_Java_REST/api/credito/verificar/1710000001";
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(banquitoUrl)).timeout(Duration.ofMillis(1500)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) banquitoOk = true;
        } catch (Exception e) {
            detalle += "[BanQuito: " + e.getMessage() + "] ";
        }

        // Probar Comercializadora REST
        String comUrl = "http://" + h + ":" + p + "/WS_Comercializadora_WebClient/api/telefonos";
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(comUrl)).timeout(Duration.ofMillis(1500)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) comercializadoraOk = true;
        } catch (Exception e) {
            detalle += "[Comercializadora: " + e.getMessage() + "]";
        }

        boolean exito = banquitoOk && comercializadoraOk;
        String mensaje;
        if (exito) {
            mensaje = "✔ ¡Conexión Exitosa! Ambos servicios responden en " + h + ":" + p;
        } else if (banquitoOk || comercializadoraOk) {
            mensaje = "⚠ Parcial: " + (banquitoOk ? "BanQuito OK" : "Comercializadora OK") + ". Verifique el otro servicio.";
        } else {
            mensaje = "❌ No se pudo conectar a http://" + h + ":" + p + " " + detalle;
        }
        return new ResultadoPrueba(exito, mensaje, h, p);
    }

    public static class ResultadoPrueba {
        public final boolean exitoso;
        public final String mensaje;
        public final String host;
        public final int puerto;

        public ResultadoPrueba(boolean exitoso, String mensaje, String host, int puerto) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.host = host;
            this.puerto = puerto;
        }
    }
}
