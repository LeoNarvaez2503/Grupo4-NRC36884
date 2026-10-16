package ec.edu.monster.wsconversormov.service;

import ec.edu.monster.wsconversormov.model.ConfiguracionConexion;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversorService {

    private static final String TARGET_NAMESPACE = "http://ws.controladores.monster.edu.ec/";

    public String login(String usuario, String contrasenia) throws Exception {
        String soapBody = String.format(
            "<ws:login xmlns:ws=\"%s\">" +
            "<usuario>%s</usuario>" +
            "<contrasenia>%s</contrasenia>" +
            "</ws:login>",
            TARGET_NAMESPACE, escapeXml(usuario), escapeXml(contrasenia)
        );

        return invocarSoap(soapBody);
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) throws Exception {
        String soapBody = String.format(
            "<ws:convertirLongitud xmlns:ws=\"%s\">" +
            "<valor>%s</valor>" +
            "<unidadInicial>%s</unidadInicial>" +
            "<unidadFinal>%s</unidadFinal>" +
            "</ws:convertirLongitud>",
            TARGET_NAMESPACE, valor, escapeXml(unidadInicial), escapeXml(unidadFinal)
        );

        String respuesta = invocarSoap(soapBody);
        return Double.parseDouble(respuesta.trim());
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) throws Exception {
        String soapBody = String.format(
            "<ws:convertirMasa xmlns:ws=\"%s\">" +
            "<valor>%s</valor>" +
            "<unidadInicial>%s</unidadInicial>" +
            "<unidadFinal>%s</unidadFinal>" +
            "</ws:convertirMasa>",
            TARGET_NAMESPACE, valor, escapeXml(unidadInicial), escapeXml(unidadFinal)
        );

        String respuesta = invocarSoap(soapBody);
        return Double.parseDouble(respuesta.trim());
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) throws Exception {
        String soapBody = String.format(
            "<ws:convertirTemperatura xmlns:ws=\"%s\">" +
            "<valor>%s</valor>" +
            "<unidadInicial>%s</unidadInicial>" +
            "<unidadFinal>%s</unidadFinal>" +
            "</ws:convertirTemperatura>",
            TARGET_NAMESPACE, valor, escapeXml(unidadInicial), escapeXml(unidadFinal)
        );

        String respuesta = invocarSoap(soapBody);
        return Double.parseDouble(respuesta.trim());
    }

    private String invocarSoap(String cuerpoXml) throws Exception {
        String soapEnvelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                cuerpoXml +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        java.net.URL url = java.net.URI.create(ConfiguracionConexion.getEndpointUrl()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", "\"\"");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = soapEnvelope.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int statusCode = conn.getResponseCode();
        InputStream is = (statusCode >= 200 && statusCode < 400) ? conn.getInputStream() : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        String rawXml = response.toString();

        if (statusCode >= 400 || rawXml.contains("faultstring") || rawXml.contains("Fault")) {
            String fault = extraerEtiqueta(rawXml, "faultstring");
            if (fault == null || fault.isEmpty()) {
                fault = "Error en el servidor SOAP (HTTP " + statusCode + ").";
            }
            throw new RuntimeException(fault);
        }

        String resultado = extraerEtiqueta(rawXml, "return");
        if (resultado == null) {
            throw new RuntimeException("Respuesta vacía del servidor SOAP.");
        }

        return resultado;
    }

    private String extraerEtiqueta(String xml, String tagName) {
        Pattern pattern = Pattern.compile("<(?:\\w+:)?(" + tagName + ")[^>]*>(.*?)</(?:\\w+:)?\\1>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    private String escapeXml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
}
