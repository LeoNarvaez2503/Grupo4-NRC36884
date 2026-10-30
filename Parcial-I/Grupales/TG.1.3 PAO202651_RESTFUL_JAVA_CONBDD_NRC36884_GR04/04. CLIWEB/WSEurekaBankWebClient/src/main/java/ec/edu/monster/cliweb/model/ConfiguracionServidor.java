package ec.edu.monster.cliweb.model;

import java.io.InputStream;
import java.util.Properties;

/**
 * Modelo de configuración de red hacia el Servidor RESTful EurekaBank.
 */
public class ConfiguracionServidor implements java.io.Serializable {

    private String host = "localhost";
    private int puerto = 8080;
    private String endpointUrl = "http://localhost:8080/WS_EurekaBank_Java_REST/api";

    public ConfiguracionServidor() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("servidor.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                this.host = props.getProperty("servidor.host", this.host).trim();
                this.puerto = Integer.parseInt(props.getProperty("servidor.puerto", "8080").trim());
                this.endpointUrl = props.getProperty("servidor.endpoint",
                        "http://" + host + ":" + puerto + "/WS_EurekaBank_Java_REST/api").trim();
            }
        } catch (Exception ignored) {
        }
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
