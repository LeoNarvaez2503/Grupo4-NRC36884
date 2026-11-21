package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP para consumir la API pública de países (REST Countries v3.1
 * con respaldo automático en apicountries.com si v3.1 está deprecada o no disponible).
 */
public class ConsumoAPI {

    private static final String BASE_PRIMARY = "https://restcountries.com/v3.1";
    private static final String BASE_FALLBACK = "https://www.apicountries.com";
    private static final int TIMEOUT_MS = 12000;

    private static volatile String ultimaFuente = BASE_PRIMARY;

    /**
     * Obtiene el JSON de todos los países desde la API pública.
     */
    public static String obtenerDatos() {
        return consultarConRespaldo(
                BASE_PRIMARY + "/all?fields=name,capital,region,population,currencies",
                BASE_FALLBACK + "/countries"
        );
    }

    /**
     * Consulta un país por nombre en la API pública (Actividad 1).
     */
    public static String obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "";
        }
        String encoded = codificarSegmento(nombre.trim());
        return consultarConRespaldo(
                BASE_PRIMARY + "/name/" + encoded,
                BASE_FALLBACK + "/name/" + encoded
        );
    }

    /**
     * Consulta países por región en la API pública (Actividad 4).
     */
    public static String obtenerPorRegion(String region) {
        if (region == null || region.isBlank()) {
            return "";
        }
        String encoded = codificarSegmento(region.trim().toLowerCase());
        return consultarConRespaldo(
                BASE_PRIMARY + "/region/" + encoded,
                BASE_FALLBACK + "/region/" + encoded
        );
    }

    public static String getUltimaFuente() {
        return ultimaFuente;
    }

    /**
     * Parsea una respuesta JSON (formato REST Countries v3.1 o v2/apicountries) a lista de {@link Pais}.
     */
    public static List<Pais> parsearPaises(String json) {
        List<Pais> lista = new ArrayList<>();
        if (json == null || json.isBlank()) {
            return lista;
        }
        JsonElement root = JsonParser.parseString(json);
        if (root.isJsonArray()) {
            for (JsonElement elem : root.getAsJsonArray()) {
                if (elem.isJsonObject()) {
                    Pais p = parsearObjetoPais(elem.getAsJsonObject());
                    if (p != null && p.getNombre() != null && !p.getNombre().isBlank()) {
                        lista.add(p);
                    }
                }
            }
        } else if (root.isJsonObject()) {
            JsonObject obj = root.getAsJsonObject();
            if (obj.has("success") && !obj.get("success").getAsBoolean()) {
                return lista;
            }
            Pais p = parsearObjetoPais(obj);
            if (p != null && p.getNombre() != null && !p.getNombre().isBlank()) {
                lista.add(p);
            }
        }
        return lista;
    }

    private static Pais parsearObjetoPais(JsonObject obj) {
        String nombre = extraerNombre(obj);
        String capital = extraerCapital(obj);
        String region = extraerTexto(obj, "region", "Sin región");
        long poblacion = obj.has("population") && !obj.get("population").isJsonNull()
                ? obj.get("population").getAsLong()
                : 0L;
        String moneda = extraerMoneda(obj);

        return new Pais(
                truncar(nombre, 100),
                truncar(capital, 100),
                truncar(region, 100),
                poblacion,
                truncar(moneda, 100)
        );
    }

    private static String extraerNombre(JsonObject obj) {
        if (!obj.has("name") || obj.get("name").isJsonNull()) {
            return "";
        }
        JsonElement nameEl = obj.get("name");
        if (nameEl.isJsonPrimitive()) {
            return nameEl.getAsString();
        }
        if (nameEl.isJsonObject()) {
            JsonObject nameObj = nameEl.getAsJsonObject();
            if (nameObj.has("common") && !nameObj.get("common").isJsonNull()) {
                return nameObj.get("common").getAsString();
            }
            if (nameObj.has("official") && !nameObj.get("official").isJsonNull()) {
                return nameObj.get("official").getAsString();
            }
        }
        return "";
    }

    private static String extraerCapital(JsonObject obj) {
        if (!obj.has("capital") || obj.get("capital").isJsonNull()) {
            return "N/A";
        }
        JsonElement capEl = obj.get("capital");
        if (capEl.isJsonPrimitive()) {
            String val = capEl.getAsString();
            return val.isBlank() ? "N/A" : val;
        }
        if (capEl.isJsonArray()) {
            JsonArray arr = capEl.getAsJsonArray();
            if (!arr.isEmpty() && !arr.get(0).isJsonNull()) {
                return arr.get(0).getAsString();
            }
        }
        return "N/A";
    }

    private static String extraerMoneda(JsonObject obj) {
        if (!obj.has("currencies") || obj.get("currencies").isJsonNull()) {
            return "N/A";
        }
        JsonElement curEl = obj.get("currencies");
        List<String> monedas = new ArrayList<>();

        if (curEl.isJsonArray()) {
            // Formato v2 / apicountries: [{"code":"USD","name":"United States dollar","symbol":"$"}]
            for (JsonElement item : curEl.getAsJsonArray()) {
                if (item.isJsonObject()) {
                    JsonObject curObj = item.getAsJsonObject();
                    String nombre = extraerTexto(curObj, "name", "");
                    String codigo = extraerTexto(curObj, "code", "");
                    String texto = formatearMoneda(nombre, codigo);
                    if (!texto.isBlank()) {
                        monedas.add(texto);
                    }
                }
            }
        } else if (curEl.isJsonObject()) {
            // Formato v3.1: {"USD": {"name":"United States dollar","symbol":"$"}}
            for (Map.Entry<String, JsonElement> entry : curEl.getAsJsonObject().entrySet()) {
                if (entry.getValue().isJsonObject()) {
                    JsonObject curObj = entry.getValue().getAsJsonObject();
                    String nombre = extraerTexto(curObj, "name", "");
                    String texto = formatearMoneda(nombre, entry.getKey());
                    if (!texto.isBlank()) {
                        monedas.add(texto);
                    }
                }
            }
        }
        return monedas.isEmpty() ? "N/A" : String.join(", ", monedas);
    }

    private static String formatearMoneda(String nombre, String codigo) {
        if (!nombre.isBlank() && !codigo.isBlank()) {
            return nombre + " (" + codigo + ")";
        }
        return !nombre.isBlank() ? nombre : codigo;
    }

    private static String extraerTexto(JsonObject obj, String key, String defaultVal) {
        if (obj.has(key) && !obj.get(key).isJsonNull() && obj.get(key).isJsonPrimitive()) {
            String val = obj.get(key).getAsString();
            return val.isBlank() ? defaultVal : val;
        }
        return defaultVal;
    }

    private static String consultarConRespaldo(String primaryUrl, String fallbackUrl) {
        HttpResult primary = ejecutarGet(primaryUrl);
        if (esRespuestaValida(primary)) {
            ultimaFuente = "restcountries.com (v3.1)";
            return primary.body;
        }
        HttpResult fallback = ejecutarGet(fallbackUrl);
        if (esRespuestaValida(fallback)) {
            ultimaFuente = "apicountries.com (mirror v3/v2)";
            return fallback.body;
        }
        ultimaFuente = "No disponible";
        return "";
    }

    private static boolean esRespuestaValida(HttpResult res) {
        if (res == null || res.statusCode < 200 || res.statusCode >= 300 || res.body == null || res.body.isBlank()) {
            return false;
        }
        String trimmed = res.body.trim();
        if (trimmed.startsWith("{") && (trimmed.contains("\"success\":false") || trimmed.contains("\"success\": false"))) {
            return false;
        }
        return trimmed.startsWith("[") || trimmed.startsWith("{");
    }

    private static HttpResult ejecutarGet(String urlStr) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(TIMEOUT_MS);
            con.setReadTimeout(TIMEOUT_MS);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("User-Agent", "Java-MVC-Paises/1.0");

            int status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        sb.append(linea);
                    }
                    return new HttpResult(status, sb.toString());
                }
            }
            return new HttpResult(status, "");
        } catch (IOException e) {
            return new HttpResult(-1, "");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static String codificarSegmento(String texto) {
        return URLEncoder.encode(texto, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String truncar(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max);
    }

    private record HttpResult(int statusCode, String body) {
    }
}
