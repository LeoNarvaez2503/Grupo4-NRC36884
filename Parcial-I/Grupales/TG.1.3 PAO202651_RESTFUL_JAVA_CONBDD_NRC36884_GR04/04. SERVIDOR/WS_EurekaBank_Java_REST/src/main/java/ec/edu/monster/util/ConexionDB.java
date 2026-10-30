package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexiones JDBC hacia el contenedor local MySQL/MariaDB (Docker / Podman).
 * Puerto local: 3306 | Base de datos: eurekabank_db
 */
public class ConexionDB {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/eurekabank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontró el driver JDBC de MySQL: " + e.getMessage());
        }
    }

    private ConexionDB() {
    }

    public static Connection getConexion() throws SQLException {
        String url = obtenerPropiedad("EUREKABANK_DB_URL", "eurekabank.db.url", DEFAULT_URL);
        String user = obtenerPropiedad("EUREKABANK_DB_USER", "eurekabank.db.user", DEFAULT_USER);
        String pass = obtenerPropiedad("EUREKABANK_DB_PASS", "eurekabank.db.pass", DEFAULT_PASS);
        Connection conn = DriverManager.getConnection(url, user, pass);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    private static String obtenerPropiedad(String envKey, String sysKey, String valorDefecto) {
        String sys = System.getProperty(sysKey);
        if (sys != null && !sys.isBlank()) {
            return sys.trim();
        }
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return valorDefecto;
    }
}
