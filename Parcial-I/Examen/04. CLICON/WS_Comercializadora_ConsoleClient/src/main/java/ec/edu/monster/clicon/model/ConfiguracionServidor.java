package ec.edu.monster.clicon.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConfiguracionServidor {

    /** Host central: -Dexamen.host, variable EXAMEN_HOST o "04. CONFIG/servidor.properties" (copiado al classpath al compilar). */
    private static String cargarHostCentral() {
        String host = "localhost";
        try (java.io.InputStream in = ConfiguracionServidor.class.getClassLoader().getResourceAsStream("servidor.properties")) {
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

    private static final String PREF_KEY_HOST = "server_host";
    private final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(ConfiguracionServidor.class);
    private String host;
    private String comEndpoint;
    private String banquitoEndpoint;
    private String resolvedCom = null;
    private String resolvedBanquito = null;

    /** Host guardado desde el menu de la consola; si no hay, el host central (04. CONFIG/servidor.properties). */
    public ConfiguracionServidor() {
        this.host = prefs.get(PREF_KEY_HOST, cargarHostCentral());
        actualizarEndpoints();
    }

    private void actualizarEndpoints() {
        this.comEndpoint = "http://" + host + ":8080/WS_Comercializadora_WebClient/api";
        this.banquitoEndpoint = "http://" + host + ":8080/WS_BanQuito_Java_REST/api/credito";
        this.resolvedCom = null;
        this.resolvedBanquito = null;
    }

    public String getHost() {
        return host;
    }

    /** Cambia el host del servidor (IP o nombre) y lo recuerda para la proxima ejecucion. */
    public synchronized void configurarHost(String nuevoHost) {
        String h = nuevoHost == null ? "" : nuevoHost.trim();
        if (h.startsWith("http://")) h = h.substring(7);
        if (h.startsWith("https://")) h = h.substring(8);
        int corte = h.indexOf('/');
        if (corte >= 0) h = h.substring(0, corte);
        int dosPuntos = h.indexOf(':');
        if (dosPuntos >= 0) h = h.substring(0, dosPuntos);
        if (h.isEmpty()) return;
        this.host = h;
        prefs.put(PREF_KEY_HOST, h);
        actualizarEndpoints();
    }

    public String getComEndpoint() {
        if (resolvedCom != null) {
            return resolvedCom;
        }
        // 1. Probar Payara Oficial (puerto 8080)
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(comEndpoint + "/telefonos")).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedCom = comEndpoint;
                return resolvedCom;
            }
        } catch (Exception ignored) {}

        // 2. Probar Servidor Standalone (puerto 8090)
        String fallback8090 = "http://" + host + ":8090/api";
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(fallback8090 + "/telefonos")).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedCom = fallback8090;
                return resolvedCom;
            }
        } catch (Exception ignored) {}

        resolvedCom = comEndpoint;
        return resolvedCom;
    }

    public void setComEndpoint(String comEndpoint) {
        this.comEndpoint = comEndpoint;
        this.resolvedCom = comEndpoint;
    }

    public String getBanquitoEndpoint() {
        if (resolvedBanquito != null) {
            return resolvedBanquito;
        }
        // 1. Probar Payara Oficial
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(banquitoEndpoint)).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBanquito = banquitoEndpoint;
                return resolvedBanquito;
            }
        } catch (Exception ignored) {}

        // 2. Probar ruta directa
        String cortaUrl = "http://" + host + ":8080/api/credito";
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(cortaUrl)).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBanquito = cortaUrl;
                return resolvedBanquito;
            }
        } catch (Exception ignored) {}

        resolvedBanquito = banquitoEndpoint;
        return resolvedBanquito;
    }

    public void setBanquitoEndpoint(String banquitoEndpoint) {
        this.banquitoEndpoint = banquitoEndpoint;
        this.resolvedBanquito = banquitoEndpoint;
    }
}
