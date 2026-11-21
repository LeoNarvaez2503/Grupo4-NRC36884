package modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión JDBC hacia MySQL.
 */
public class Conexion {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/api_paises?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "admin";

    private static final Properties PROPS = cargarPropiedades();

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream is = Conexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer db.properties, se usarán valores por defecto: " + e.getMessage());
        }
        return props;
    }

    private static String obtenerConfig(String envKey, String propKey, String defaultVal) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isBlank()) {
            return envVal.trim();
        }
        String propVal = PROPS.getProperty(propKey);
        if (propVal != null && !propVal.isBlank()) {
            return propVal.trim();
        }
        return defaultVal;
    }

    /**
     * Abre una conexión JDBC a la base de datos configurada.
     *
     * @return instancia de {@link Connection} o {@code null} si falla la conexión.
     */
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(
                    obtenerConfig("DB_URL", "db.url", DEFAULT_URL),
                    obtenerConfig("DB_USER", "db.user", DEFAULT_USER),
                    obtenerConfig("DB_PASS", "db.password", DEFAULT_PASS)
            );
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si la base de datos responde correctamente.
     */
    public static boolean probarConexion() {
        try (Connection con = conectar()) {
            return con != null && !con.isClosed() && con.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }
}
