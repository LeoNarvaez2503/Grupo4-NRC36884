package ec.edu.monster.dao;

import ec.edu.monster.modelo.AuthRespuestaDTO;
import ec.edu.monster.util.ConexionDB;
import ec.edu.monster.util.SeguridadUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * DAO para autenticación y consulta de empleados / clientes contra MariaDB/MySQL.
 */
public class EmpleadoDAO {

    public AuthRespuestaDTO autenticar(String usuario, String clave) {
        AuthRespuestaDTO resp = new AuthRespuestaDTO();
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            resp.setAutenticado(false);
            resp.setMensaje("Debe ingresar usuario y contraseña.");
            return resp;
        }

        String sqlEmp = "SELECT e.id_empleado, e.usuario, e.password, e.nombre_completo, e.rol, "
                + "e.id_sucursal, COALESCE(e.ventanilla, 1) AS ventanilla, s.nombre AS nombre_sucursal "
                + "FROM empleado e "
                + "INNER JOIN sucursal s ON e.id_sucursal = s.id_sucursal "
                + "WHERE LOWER(e.usuario) = LOWER(?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlEmp)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password");
                    if (SeguridadUtil.verificarPassword(clave, hash)) {
                        resp.setAutenticado(true);
                        resp.setIdEmpleado(rs.getInt("id_empleado"));
                        resp.setUsuario(rs.getString("usuario"));
                        resp.setNombreCompleto(rs.getString("nombre_completo"));
                        resp.setRol(rs.getString("rol"));
                        resp.setIdSucursal(rs.getInt("id_sucursal"));
                        resp.setNombreSucursal(rs.getString("nombre_sucursal"));
                        resp.setVentanilla(rs.getInt("ventanilla"));
                        resp.setToken("MONSTER-TK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
                        resp.setMensaje("Bienvenido " + resp.getNombreCompleto() + " [" + resp.getRol() + "]");
                        return resp;
                    } else {
                        resp.setAutenticado(false);
                        resp.setMensaje("Contraseña incorrecta para el usuario " + usuario.trim() + ".");
                        return resp;
                    }
                }
            }

            // Permitir también consulta a titulares por DNI (clave = DNI o Monster9)
            String sqlCli = "SELECT id_cliente, dni, nombre, apellido FROM cliente WHERE dni = ?";
            try (PreparedStatement psCli = con.prepareStatement(sqlCli)) {
                psCli.setString(1, usuario.trim());
                try (ResultSet rsCli = psCli.executeQuery()) {
                    if (rsCli.next() && (clave.equals(usuario.trim()) || "Monster9".equals(clave))) {
                        resp.setAutenticado(true);
                        resp.setIdEmpleado(1);
                        resp.setUsuario(rsCli.getString("dni"));
                        resp.setNombreCompleto(rsCli.getString("nombre") + " " + rsCli.getString("apellido"));
                        resp.setRol("Cliente");
                        resp.setIdSucursal(1);
                        resp.setNombreSucursal("Banca Digital Clientes");
                        resp.setVentanilla(0);
                        resp.setToken("CLIENTE-TK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
                        resp.setMensaje("Bienvenido " + resp.getNombreCompleto() + " [Cliente Titular]");
                        return resp;
                    }
                }
            }
        } catch (SQLException e) {
            resp.setAutenticado(false);
            resp.setMensaje("Error de conexión con Base de Datos: " + e.getMessage());
            return resp;
        }

        resp.setAutenticado(false);
        resp.setMensaje("Credenciales inválidas. Usuario no registrado.");
        return resp;
    }

    public String obtenerNombreUsuarioPorId(int idEmpleado) {
        String sql = "SELECT usuario, nombre_completo FROM empleado WHERE id_empleado = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("usuario");
                }
            }
        } catch (SQLException ignored) {
        }
        return "Monster";
    }
}
