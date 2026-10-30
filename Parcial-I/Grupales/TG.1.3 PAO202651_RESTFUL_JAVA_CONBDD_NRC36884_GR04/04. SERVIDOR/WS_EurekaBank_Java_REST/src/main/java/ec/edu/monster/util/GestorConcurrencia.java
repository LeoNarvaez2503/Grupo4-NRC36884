package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de Concurrencia y Sincronización MashUp (Semáforo Verde / Rojo).
 * Combina control atómico en memoria (ConcurrentHashMap) con persistencia inmediata
 * en la tabla control_mashup para sincronizar los 4 clientes (Escritorio, Web, Móvil, Consola)
 * e interoperar entre servicios sin interferir con el bloqueo pesimista SELECT FOR UPDATE de cuenta.
 */
public class GestorConcurrencia {

    public static final String VERDE = "VERDE";
    public static final String ROJO = "ROJO";

    private static final GestorConcurrencia INSTANCIA = new GestorConcurrencia();

    public static class InfoBloqueo {
        private final String estado;
        private final int ventanilla;
        private final String usuario;
        private final long timestamp;

        public InfoBloqueo(String estado, int ventanilla, String usuario) {
            this.estado = estado;
            this.ventanilla = ventanilla;
            this.usuario = usuario;
            this.timestamp = System.currentTimeMillis();
        }

        public String getEstado() { return estado; }
        public int getVentanilla() { return ventanilla; }
        public String getUsuario() { return usuario; }
        public long getTimestamp() { return timestamp; }
    }

    private final ConcurrentHashMap<String, InfoBloqueo> bloqueosMemoria = new ConcurrentHashMap<>();

    private GestorConcurrencia() {
    }

    public static GestorConcurrencia getInstancia() {
        return INSTANCIA;
    }

    /**
     * Intenta adquirir el candado exclusivo de concurrencia (pasa la cuenta de VERDE a ROJO).
     */
    public synchronized boolean bloquearCuenta(String numeroCuenta, int ventanilla, String usuario) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return false;
        }
        String key = numeroCuenta.trim();
        String responsable = (usuario == null || usuario.isBlank()) ? "Cajero" : usuario.trim();
        int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;

        InfoBloqueo actual = obtenerInfoBloqueo(key);
        if (ROJO.equalsIgnoreCase(actual.getEstado())) {
            return false;
        }

        String sql = "INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por, actualizado_en) "
                + "VALUES (?, 'ROJO', ?, ?, CURRENT_TIMESTAMP) "
                + "ON DUPLICATE KEY UPDATE "
                + "estado_mashup = IF(estado_mashup = 'VERDE', 'ROJO', estado_mashup), "
                + "ventanilla = IF(estado_mashup = 'ROJO', VALUES(ventanilla), ventanilla), "
                + "bloqueado_por = IF(estado_mashup = 'ROJO', VALUES(bloqueado_por), bloqueado_por), "
                + "actualizado_en = CURRENT_TIMESTAMP";

        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psSel = con.prepareStatement(
                    "SELECT estado_mashup FROM control_mashup WHERE numero_cuenta = ? FOR UPDATE")) {
                psSel.setString(1, key);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next() && ROJO.equalsIgnoreCase(rs.getString("estado_mashup"))) {
                        con.rollback();
                        return false;
                    }
                }
            }
            try (PreparedStatement psUp = con.prepareStatement(
                    "INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por, actualizado_en) "
                    + "VALUES (?, 'ROJO', ?, ?, CURRENT_TIMESTAMP) "
                    + "ON DUPLICATE KEY UPDATE estado_mashup = 'ROJO', ventanilla = VALUES(ventanilla), "
                    + "bloqueado_por = VALUES(bloqueado_por), actualizado_en = CURRENT_TIMESTAMP")) {
                psUp.setString(1, key);
                psUp.setInt(2, nroVentanilla);
                psUp.setString(3, responsable);
                psUp.executeUpdate();
            }
            con.commit();
            bloqueosMemoria.put(key, new InfoBloqueo(ROJO, nroVentanilla, responsable));
            return true;
        } catch (SQLException e) {
            // Respaldo en memoria si aún no existe control_mashup
            InfoBloqueo prev = bloqueosMemoria.get(key);
            if (prev != null && ROJO.equalsIgnoreCase(prev.getEstado())) {
                return false;
            }
            bloqueosMemoria.put(key, new InfoBloqueo(ROJO, nroVentanilla, responsable));
            return true;
        }
    }

    /**
     * Libera el bloqueo de la cuenta restableciendo su estado MashUp a VERDE.
     */
    public synchronized void liberarCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return;
        }
        String key = numeroCuenta.trim();
        bloqueosMemoria.put(key, new InfoBloqueo(VERDE, 0, null));
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por, actualizado_en) "
                     + "VALUES (?, 'VERDE', NULL, NULL, CURRENT_TIMESTAMP) "
                     + "ON DUPLICATE KEY UPDATE estado_mashup = 'VERDE', ventanilla = NULL, bloqueado_por = NULL, actualizado_en = CURRENT_TIMESTAMP")) {
            ps.setString(1, key);
            ps.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    /**
     * Consulta la información de bloqueo en tiempo real.
     */
    public InfoBloqueo obtenerInfoBloqueo(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return new InfoBloqueo(VERDE, 0, null);
        }
        String key = numeroCuenta.trim();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT estado_mashup, ventanilla, bloqueado_por FROM control_mashup WHERE numero_cuenta = ?")) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String est = rs.getString("estado_mashup");
                    int vent = rs.getInt("ventanilla");
                    String bloq = rs.getString("bloqueado_por");
                    InfoBloqueo info = new InfoBloqueo(est != null ? est : VERDE, vent, bloq);
                    bloqueosMemoria.put(key, info);
                    return info;
                }
            }
        } catch (SQLException ignored) {
        }
        return bloqueosMemoria.getOrDefault(key, new InfoBloqueo(VERDE, 0, null));
    }

    public String getEstadoCuenta(String numeroCuenta) {
        return obtenerInfoBloqueo(numeroCuenta).getEstado();
    }
}
