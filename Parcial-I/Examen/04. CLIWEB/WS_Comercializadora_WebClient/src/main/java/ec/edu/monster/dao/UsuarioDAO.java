package ec.edu.monster.dao;

import ec.edu.monster.util.ConexionDB;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DAO para la autenticación de usuarios contra la tabla USUARIO en comercializadora_db.
 */
public class UsuarioDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class UsuarioDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        private boolean autenticado;
        private String usuario;
        private String nombre;
        private String rol;
        private String mensaje;

        public boolean isAutenticado() { return autenticado; }
        public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }

    public UsuarioDTO autenticar(String usuario, String clave) {
        UsuarioDTO dto = new UsuarioDTO();
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            dto.setAutenticado(false);
            dto.setMensaje("Usuario y contraseña requeridos.");
            return dto;
        }

        String sql = "SELECT COD_USUARIO, USUARIO, CLAVE, NOMBRE, ROL, ESTADO FROM USUARIO WHERE LOWER(USUARIO) = LOWER(?) AND ESTADO = 'ACTIVO'";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passDb = rs.getString("CLAVE");
                    if (clave.equals(passDb)) {
                        dto.setAutenticado(true);
                        dto.setUsuario(rs.getString("USUARIO"));
                        dto.setNombre(rs.getString("NOMBRE"));
                        dto.setRol(rs.getString("ROL"));
                        dto.setMensaje("Autenticación exitosa desde Base de Datos (comercializadora_db.USUARIO)");
                        return dto;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[UsuarioDAO Error BDD]: " + e.getMessage());
            // Fallback de contingencia si la conexión a BDD se interrumpe
            if ("Monster".equalsIgnoreCase(usuario.trim()) && "Monster9".equals(clave)) {
                dto.setAutenticado(true);
                dto.setUsuario("Monster");
                dto.setNombre("Administrador Monster Core (Offline)");
                dto.setRol("ADMIN");
                dto.setMensaje("Autenticado con credenciales maestras Monster/Monster9");
                return dto;
            }
        }

        dto.setAutenticado(false);
        dto.setMensaje("Credenciales incorrectas (Requerido: Monster / Monster9).");
        return dto;
    }
}
