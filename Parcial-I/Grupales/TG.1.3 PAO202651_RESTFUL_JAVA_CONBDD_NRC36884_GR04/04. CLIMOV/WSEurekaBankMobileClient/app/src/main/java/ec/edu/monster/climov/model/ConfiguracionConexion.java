package ec.edu.monster.climov.model;

/**
 * Modelo de configuración de conexión de red hacia el Servidor EurekaBank.
 * En emulador Android 10.0.2.2 apunta al localhost del host Windows (Payara 7 :8080).
 */
public class ConfiguracionConexion {

    private static String host = "10.0.2.2";
    private static int puerto = 8080;

    private ConfiguracionConexion() {
    }

    public static void configurar(String nuevoHost, int nuevoPuerto) {
        if (nuevoHost != null && !nuevoHost.trim().isEmpty()) {
            host = nuevoHost.trim();
        }
        if (nuevoPuerto > 0) {
            puerto = nuevoPuerto;
        }
    }

    public static String getHost() {
        return host;
    }

    public static int getPuerto() {
        return puerto;
    }

    public static String getSoapEndpoint() {
        return "http://" + host + ":" + puerto + "/WS_EurekaBank_Java_SOAP/EurekaBankWS";
    }

    public static String getRestBaseUrl() {
        return "http://" + host + ":" + puerto + "/WS_EurekaBank_Java_REST/api";
    }
}
