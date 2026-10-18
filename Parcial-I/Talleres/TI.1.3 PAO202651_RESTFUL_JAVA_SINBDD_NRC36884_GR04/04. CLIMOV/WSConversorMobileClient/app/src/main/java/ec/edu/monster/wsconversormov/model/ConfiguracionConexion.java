package ec.edu.monster.wsconversormov.model;

public class ConfiguracionConexion {

    // 10.0.2.2 es la IP por defecto para acceder al localhost de la máquina host desde el emulador de Android
    private static String endpointUrl = "http://10.0.2.2:8080/WS_ConversorUnidades_Java_REST/api";

    public static String getEndpointUrl() {
        return endpointUrl;
    }

    public static void setEndpointUrl(String url) {
        if (url != null && !url.trim().isEmpty()) {
            endpointUrl = url.trim();
        }
    }
}
