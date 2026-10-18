package ec.edu.monster.wsconversorcon.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversorService {

    private static final String BASE_URL = "http://localhost:8080/WS_ConversorUnidades_Java_REST/api";
    private static String tokenSesion;

    public static String getTokenSesion() {
        return tokenSesion;
    }

    public static void setTokenSesion(String token) {
        tokenSesion = token;
    }

    public String login(String usuario, String contrasenia) {
        try {
            String jsonInput = String.format("{\"usuario\":\"%s\",\"contrasenia\":\"%s\"}",
                    escapeJson(usuario), escapeJson(contrasenia));

            String respuesta = postJson(BASE_URL + "/auth/login", jsonInput);
            String token = extraerCampoJson(respuesta, "token");
            if (token != null && !token.isBlank()) {
                tokenSesion = token;
                return token;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        return ejecutarConversion("longitud", valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        return ejecutarConversion("masa", valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        return ejecutarConversion("temperatura", valor, unidadInicial, unidadFinal);
    }

    public String cambiarContrasenia(String actual, String nueva) {
        try {
            String jsonInput = String.format("{\"contraseniaActual\":\"%s\",\"contraseniaNueva\":\"%s\"}",
                    escapeJson(actual), escapeJson(nueva));
            return postJson(BASE_URL + "/auth/cambiar-contrasenia", jsonInput);
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar contraseña: " + e.getMessage(), e);
        }
    }

    private double ejecutarConversion(String tipo, double valor, String inicial, String finalU) {
        try {
            String urlStr = String.format("%s/conversor/%s?valor=%s&unidadInicial=%s&unidadFinal=%s",
                    BASE_URL, tipo, valor,
                    URLEncoder.encode(inicial, StandardCharsets.UTF_8),
                    URLEncoder.encode(finalU, StandardCharsets.UTF_8));

            String respuesta = getJson(urlStr);
            String valorFinalStr = extraerCampoJson(respuesta, "valorFinal");
            if (valorFinalStr != null) {
                return Double.parseDouble(valorFinalStr);
            }
            throw new RuntimeException("No se encontró 'valorFinal' en la respuesta del servidor: " + respuesta);
        } catch (Exception e) {
            throw new RuntimeException("Error en conversión de " + tipo + ": " + e.getMessage(), e);
        }
    }

    private String getJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (tokenSesion != null) {
            conn.setRequestProperty("Authorization", "Bearer " + tokenSesion);
        }

        int code = conn.getResponseCode();
        if (code != 200) {
            String errorMsg = leerStream(conn.getErrorStream());
            String cleanMsg = extraerCampoJson(errorMsg, "mensaje");
            if (cleanMsg != null && !cleanMsg.isBlank()) {
                throw new RuntimeException(cleanMsg);
            }
            throw new RuntimeException("HTTP " + code + ": " + errorMsg);
        }
        return leerStream(conn.getInputStream());
    }

    private String postJson(String urlStr, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        if (tokenSesion != null) {
            conn.setRequestProperty("Authorization", "Bearer " + tokenSesion);
        }
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        if (code >= 400) {
            String errorMsg = leerStream(conn.getErrorStream());
            String cleanMsg = extraerCampoJson(errorMsg, "mensaje");
            if (cleanMsg != null && !cleanMsg.isBlank()) {
                throw new RuntimeException(cleanMsg);
            }
            throw new RuntimeException("HTTP " + code + ": " + errorMsg);
        }
        return leerStream(conn.getInputStream());
    }

    private String leerStream(java.io.InputStream is) throws Exception {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea.trim());
            }
            return sb.toString();
        }
    }

    private String extraerCampoJson(String json, String campo) {
        Pattern patternNumero = Pattern.compile("\"" + campo + "\"\\s*:\\s*([0-9.E+-]+)");
        Matcher matcherNumero = patternNumero.matcher(json);
        if (matcherNumero.find()) {
            return matcherNumero.group(1);
        }

        Pattern patternTexto = Pattern.compile("\"" + campo + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcherTexto = patternTexto.matcher(json);
        if (matcherTexto.find()) {
            return matcherTexto.group(1);
        }
        return null;
    }

    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
