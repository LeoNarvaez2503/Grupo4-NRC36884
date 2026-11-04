package ec.edu.monster.climov.model;

import android.content.Context;
import android.content.SharedPreferences;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Gestor de Configuración de Conexión y Endpoints para Android.
 * Permite cambiar la dirección IP y puerto del servidor remoto (Payara / BanQuito)
 * para conectarse desde un emulador o un teléfono físico en la red local.
 */
public class ConfiguracionConexion {

    private static final String PREF_NAME = "MonsterConfig";
    private static final String KEY_HOST = "server_host";
    private static final String KEY_PORT = "server_port";

    public static final String DEFAULT_HOST = "10.0.2.2"; // Emulador oficial Android
    public static final int DEFAULT_PORT = 8080;

    private static String host = DEFAULT_HOST;
    private static int port = DEFAULT_PORT;
    private static boolean inicializado = false;

    private String comEndpoint;
    private String banquitoEndpoint;
    private String resolvedCom = null;
    private String resolvedBanquito = null;

    public ConfiguracionConexion() {
        actualizarEndpoints();
    }

    public static synchronized void init(Context context) {
        if (context != null && !inicializado) {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            host = sp.getString(KEY_HOST, DEFAULT_HOST);
            port = sp.getInt(KEY_PORT, DEFAULT_PORT);
            inicializado = true;
        }
    }

    public static synchronized void guardar(Context context, String nuevoHost, int nuevoPuerto) {
        String h = normalizarHost(nuevoHost);
        int p = nuevoPuerto > 0 ? nuevoPuerto : DEFAULT_PORT;
        if (h.contains(":")) {
            String[] parts = h.split(":");
            h = parts[0];
            try { p = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
        }
        host = h;
        port = p;
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(KEY_HOST, host).putInt(KEY_PORT, port).apply();
        }
    }

    public static String getHost() { return host; }
    public static int getPort() { return port; }
    public static String getUrlBase() { return "http://" + host + ":" + port; }

    public static String normalizarHost(String input) {
        if (input == null || input.trim().isEmpty()) return DEFAULT_HOST;
        input = input.trim();
        if (input.startsWith("http://")) input = input.substring(7);
        if (input.startsWith("https://")) input = input.substring(8);
        if (input.endsWith("/")) input = input.substring(0, input.length() - 1);
        return input;
    }

    private void actualizarEndpoints() {
        this.comEndpoint = "http://" + host + ":" + port + "/WS_Comercializadora_WebClient/api";
        this.banquitoEndpoint = "http://" + host + ":" + port + "/WS_BanQuito_Java_REST/api/credito";
        this.resolvedCom = null;
        this.resolvedBanquito = null;
    }

    public String getComEndpoint() {
        if (resolvedCom != null) return resolvedCom;
        actualizarEndpoints();
        String[] candidatos = new String[]{
            "http://" + host + ":" + port + "/WS_Comercializadora_WebClient/api",
            "http://" + host + ":8090/api",
            "http://" + host + ":" + port + "/api"
        };
        for (String urlStr : candidatos) {
            try {
                URL u = new URL(urlStr + "/telefonos");
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(800);
                conn.setReadTimeout(800);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
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
        actualizarEndpoints();
        String[] candidatos = new String[]{
            "http://" + host + ":" + port + "/WS_BanQuito_Java_REST/api/credito",
            "http://" + host + ":" + port + "/api/credito"
        };
        for (String urlStr : candidatos) {
            try {
                URL u = new URL(urlStr + "/verificar/1710000001");
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(800);
                conn.setReadTimeout(800);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
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
     * Prueba síncrona para ejecutar en hilo secundario.
     */
    public static ResultadoPrueba probarConexion(String testHost, int testPort) {
        String h = normalizarHost(testHost);
        int p = testPort > 0 ? testPort : DEFAULT_PORT;
        if (h.contains(":")) {
            String[] parts = h.split(":");
            h = parts[0];
            try { p = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
        }

        boolean banquitoOk = false;
        boolean comercializadoraOk = false;
        String detalle = "";

        // Probar BanQuito REST
        String banquitoUrl = "http://" + h + ":" + p + "/WS_BanQuito_Java_REST/api/credito/verificar/1710000001";
        try {
            URL u = new URL(banquitoUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(1500);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) banquitoOk = true;
        } catch (Exception e) {
            detalle += "[BanQuito: " + e.getMessage() + "] ";
        }

        // Probar Comercializadora REST
        String comUrl = "http://" + h + ":" + p + "/WS_Comercializadora_WebClient/api/telefonos";
        try {
            URL u = new URL(comUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(1500);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) comercializadoraOk = true;
        } catch (Exception e) {
            detalle += "[Comercializadora: " + e.getMessage() + "]";
        }

        boolean exito = banquitoOk && comercializadoraOk;
        String mensaje;
        if (exito) {
            mensaje = "✔ ¡Conexión Exitosa con Payara Server (" + h + ":" + p + ")!";
        } else if (banquitoOk || comercializadoraOk) {
            mensaje = "⚠ Parcial: " + (banquitoOk ? "BanQuito OK" : "Comercializadora OK");
        } else {
            mensaje = "❌ No responde http://" + h + ":" + p + " " + detalle;
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
