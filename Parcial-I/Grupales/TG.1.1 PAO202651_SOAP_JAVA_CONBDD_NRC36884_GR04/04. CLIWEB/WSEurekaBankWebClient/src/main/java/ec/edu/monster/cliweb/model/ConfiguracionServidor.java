package ec.edu.monster.cliweb.model;

import java.io.InputStream;
import java.util.Properties;

/**
 * Modelo de configuración de red hacia el Servidor EurekaBank.
 * El host se lee de la configuración central del proyecto
 * ("04. CONFIG/servidor.properties"), copiada al classpath al compilar.
 * Puerto y ruta del servicio son fijos de este proyecto.
 */
public class ConfiguracionServidor implements java.io.Serializable {

    private static final int PUERTO_SERVIDOR = 8080;
    private static final String RUTA_SERVICIO = "/WS_EurekaBank_Java_SOAP/EurekaBankWS";

    private String host = "localhost";
    private int puerto = PUERTO_SERVIDOR;
    private String endpointUrl;

    public ConfiguracionServidor() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("servidor.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                this.host = props.getProperty("servidor.host", this.host).trim();
            }
        } catch (Exception ignored) {
        }
        String sysHost = System.getProperty("eurekabank.host");
        if (sysHost != null && !sysHost.isBlank()) {
            this.host = sysHost.trim();
        }
        this.endpointUrl = "http://" + host + ":" + puerto + RUTA_SERVICIO;
        String sysEndpoint = System.getProperty("eurekabank.endpoint");
        if (sysEndpoint != null && !sysEndpoint.isBlank()) {
            this.endpointUrl = sysEndpoint.trim();
        }
    }

    public String getHost() { return host; }
    public int getPuerto() { return puerto; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
}
