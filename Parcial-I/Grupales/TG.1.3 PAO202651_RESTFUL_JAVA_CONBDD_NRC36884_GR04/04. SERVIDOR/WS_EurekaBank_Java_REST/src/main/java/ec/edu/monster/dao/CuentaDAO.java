package ec.edu.monster.dao;

import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Cuentas Bancarias con INNER JOIN a Cliente/Sucursal y bloqueo pesimista SELECT FOR UPDATE.
 */
public class CuentaDAO {

    private static final String BASE_SELECT =
            "SELECT cu.numero_cuenta, cu.moneda, cu.saldo, cu.estado, cu.id_cliente, cu.id_sucursal, "
            + "cl.dni, cl.nombre AS nombre_cliente, cl.apellido AS apellido_cliente, cl.email, "
            + "su.nombre AS nombre_sucursal, su.ciudad AS ciudad_sucursal, "
            + "COALESCE(cm.estado_mashup, 'VERDE') AS estado_mashup, "
            + "COALESCE(cm.ventanilla, 0) AS ventanilla_bloqueo, "
            + "cm.bloqueado_por "
            + "FROM cuenta cu "
            + "INNER JOIN cliente cl ON cu.id_cliente = cl.id_cliente "
            + "INNER JOIN sucursal su ON cu.id_sucursal = su.id_sucursal "
            + "LEFT JOIN control_mashup cm ON cu.numero_cuenta = cm.numero_cuenta ";

    public CuentaDetalleDTO obtenerCuentaForUpdate(Connection con, String numeroCuenta) throws SQLException {
        String sql = "SELECT numero_cuenta, moneda, saldo, estado, id_cliente, id_sucursal "
                + "FROM cuenta WHERE numero_cuenta = ? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CuentaDetalleDTO dto = new CuentaDetalleDTO();
                    dto.setNumeroCuenta(rs.getString("numero_cuenta"));
                    dto.setMoneda(rs.getString("moneda"));
                    dto.setSaldo(rs.getDouble("saldo"));
                    dto.setEstadoCuenta(rs.getString("estado"));
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setIdSucursal(rs.getInt("id_sucursal"));
                    return dto;
                }
            }
        }
        return null;
    }

    public boolean actualizarSaldo(Connection con, String numeroCuenta, double nuevoSaldo) throws SQLException {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta.trim());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstadoCuenta(String numeroCuenta, String nuevoEstado) {
        String sql = "UPDATE cuenta SET estado = ? WHERE numero_cuenta = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, numeroCuenta.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<CuentaDetalleDTO> listarCuentasPorSucursal(int sucursalId) {
        List<CuentaDetalleDTO> lista = new ArrayList<>();
        String sql = sucursalId <= 0
                ? BASE_SELECT + "ORDER BY cu.numero_cuenta ASC"
                : BASE_SELECT + "WHERE cu.id_sucursal = ? ORDER BY cu.numero_cuenta ASC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (sucursalId > 0) {
                ps.setInt(1, sucursalId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCuentaDetalle(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CuentaDAO] Error listarCuentasPorSucursal: " + e.getMessage());
        }
        return lista;
    }

    public CuentaDetalleDTO obtenerCuentaPorNumero(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return null;
        }
        String sql = BASE_SELECT + "WHERE cu.numero_cuenta = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCuentaDetalle(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CuentaDAO] Error obtenerCuentaPorNumero: " + e.getMessage());
        }
        return null;
    }

    public List<CuentaDetalleDTO> obtenerCuentasPorDni(String dni) {
        List<CuentaDetalleDTO> lista = new ArrayList<>();
        if (dni == null || dni.isBlank()) {
            return lista;
        }
        String sql = BASE_SELECT + "WHERE cl.dni = ? ORDER BY cu.numero_cuenta ASC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCuentaDetalle(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CuentaDAO] Error obtenerCuentasPorDni: " + e.getMessage());
        }
        return lista;
    }

    private CuentaDetalleDTO mapearCuentaDetalle(ResultSet rs) throws SQLException {
        CuentaDetalleDTO dto = new CuentaDetalleDTO();
        dto.setNumeroCuenta(rs.getString("numero_cuenta"));
        dto.setMoneda(rs.getString("moneda"));
        dto.setSaldo(rs.getDouble("saldo"));
        dto.setEstadoCuenta(rs.getString("estado"));
        String mashup = rs.getString("estado_mashup");
        if ("Bloqueada".equalsIgnoreCase(dto.getEstadoCuenta())) {
            mashup = "ROJO";
        }
        dto.setDisponibilidadMashup(mashup != null ? mashup : "VERDE");
        dto.setVentanillaBloqueo(rs.getInt("ventanilla_bloqueo"));
        dto.setBloqueadoPor(rs.getString("bloqueado_por"));
        dto.setIdCliente(rs.getInt("id_cliente"));
        dto.setDniCliente(rs.getString("dni"));
        dto.setNombreCliente(rs.getString("nombre_cliente"));
        dto.setApellidoCliente(rs.getString("apellido_cliente"));
        dto.setEmailCliente(rs.getString("email"));
        dto.setIdSucursal(rs.getInt("id_sucursal"));
        dto.setNombreSucursal(rs.getString("nombre_sucursal"));
        dto.setCiudadSucursal(rs.getString("ciudad_sucursal"));
        return dto;
    }
}
