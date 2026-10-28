package ec.edu.monster.wsconversorweb.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Punto unico de configuracion del servidor REST.
 * Orden de prioridad: -Dservidor.host=... , servidor.properties junto al
 * .jar o en el directorio actual, servidor.properties dentro del classpath.
 */
public final class ConfiguracionServidor {

    private static final String ARCHIVO = "servidor.properties";
    private static final Properties PROPS = cargar();

    private ConfiguracionServidor() {
    }

    private static Properties cargar() {
        Properties p = new Properties();
        p.setProperty("servidor.host", "10.234.96.150");
        p.setProperty("servidor.puerto", "8080");
        p.setProperty("servidor.contexto", "WS_ConversorUnidades_Java_REST/api");

        try (InputStream in = ConfiguracionServidor.class.getResourceAsStream("/" + ARCHIVO)) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException ex) {
            System.err.println("No se pudo leer " + ARCHIVO + " del classpath: " + ex.getMessage());
        }

        File externo = new File(ARCHIVO);
        if (externo.isFile()) {
            try (InputStream in = new FileInputStream(externo)) {
                p.load(in);
            } catch (IOException ex) {
                System.err.println("No se pudo leer " + externo.getAbsolutePath() + ": " + ex.getMessage());
            }
        }
        return p;
    }

    private static String get(String clave) {
        return System.getProperty(clave, PROPS.getProperty(clave)).trim();
    }

    public static String getHost() {
        return get("servidor.host");
    }

    /** Ej.: http://10.234.96.150:8080/WS_ConversorUnidades_Java_REST/api */
    public static String getUrlBase() {
        return "http://" + getHost() + ":" + get("servidor.puerto") + "/" + get("servidor.contexto");
    }
}
