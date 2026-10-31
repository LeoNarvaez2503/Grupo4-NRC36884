package ec.edu.monster.dao;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public Cliente buscarPorCedula(String cedula) throws SQLException {
        if (cedula == null || cedula.isBlank()) return null;
        String sql = "SELECT COD_CLIENTE, CEDULA, NOMBRE, GENERO, FECHA_NACIMIENTO FROM CLIENTE WHERE CEDULA = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    public Cliente buscarPorId(int codCliente) throws SQLException {
        String sql = "SELECT COD_CLIENTE, CEDULA, NOMBRE, GENERO, FECHA_NACIMIENTO FROM CLIENTE WHERE COD_CLIENTE = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT COD_CLIENTE, CEDULA, NOMBRE, GENERO, FECHA_NACIMIENTO FROM CLIENTE ORDER BY COD_CLIENTE ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        }
        return lista;
    }

    // Regla 2: Verificar que el cliente al menos posea una transacción de depósito en el último mes (30 días)
    public boolean tieneDepositoUltimoMes(int codCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MOVIMIENTO m " +
                     "INNER JOIN CUENTA c ON m.NUM_CUENTA = c.NUM_CUENTA " +
                     "WHERE c.COD_CLIENTE = ? AND m.TIPO = 'DEP' AND m.FECHA >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Regla 4: Verificar que el cliente actualmente no tenga un crédito activo en el banco
    public boolean tieneCreditoActivo(int codCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CREDITO WHERE COD_CLIENTE = ? AND ESTADO = 'ACTIVO'";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obtener promedio de todos los depósitos de los últimos 3 meses (90 días)
    public double promedioDepositosUltimos3Meses(int codCliente) throws SQLException {
        String sql = "SELECT COALESCE(AVG(m.VALOR), 0.0) FROM MOVIMIENTO m " +
                     "INNER JOIN CUENTA c ON m.NUM_CUENTA = c.NUM_CUENTA " +
                     "WHERE c.COD_CLIENTE = ? AND m.TIPO = 'DEP' AND m.FECHA >= DATE_SUB(CURDATE(), INTERVAL 90 DAY)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 100.0) / 100.0;
                }
            }
        }
        return 0.0;
    }

    // Obtener promedio de todos los retiros de los últimos 3 meses (90 días)
    public double promedioRetirosUltimos3Meses(int codCliente) throws SQLException {
        String sql = "SELECT COALESCE(AVG(m.VALOR), 0.0) FROM MOVIMIENTO m " +
                     "INNER JOIN CUENTA c ON m.NUM_CUENTA = c.NUM_CUENTA " +
                     "WHERE c.COD_CLIENTE = ? AND m.TIPO = 'RET' AND m.FECHA >= DATE_SUB(CURDATE(), INTERVAL 90 DAY)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 100.0) / 100.0;
                }
            }
        }
        return 0.0;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodCliente(rs.getInt("COD_CLIENTE"));
        c.setCedula(rs.getString("CEDULA"));
        c.setNombre(rs.getString("NOMBRE"));
        c.setGenero(rs.getString("GENERO") != null ? rs.getString("GENERO").trim().toUpperCase() : "M");
        c.setFechaNacimiento(rs.getString("FECHA_NACIMIENTO"));
        return c;
    }
}
