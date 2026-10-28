package ec.edu.monster.wsconversormov.model;

public class ConfiguracionConexion {

    // Unico lugar donde se define el servidor por defecto. Cambia solo SERVIDOR_HOST.
    // En emulador de Android usa 10.0.2.2 para llegar al localhost de la PC.
    private static final String SERVIDOR_HOST = "10.234.96.150";
    private static final String SERVIDOR_PUERTO = "8080";
    private static final String SERVIDOR_CONTEXTO = "WS_ConversorUnidades_Java_REST/api";

    private static String endpointUrl = "http://" + SERVIDOR_HOST + ":" + SERVIDOR_PUERTO + "/" + SERVIDOR_CONTEXTO;

    public static String getEndpointUrl() {
        return endpointUrl;
    }

    public static void setEndpointUrl(String url) {
        if (url != null && !url.trim().isEmpty()) {
            endpointUrl = url.trim();
        }
    }
}
