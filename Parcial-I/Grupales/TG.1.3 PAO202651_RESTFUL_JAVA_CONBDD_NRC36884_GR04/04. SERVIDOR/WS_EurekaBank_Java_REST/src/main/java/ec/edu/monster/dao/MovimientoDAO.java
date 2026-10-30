package ec.edu.monster.dao;

import ec.edu.monster.modelo.MovimientoDTO;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Movimientos Financieros para Auditoría e Historial Cronológico (RQF-003).
 */
public class MovimientoDAO {

    public boolean registrarMovimiento(Connection con, MovimientoDTO mov) throws SQLException {
        String sql = "INSERT INTO movimiento (numero_operacion, numero_cuenta, id_empleado, ventanilla, "
                + "tipo, monto, saldo_resultante, cuenta_referencia, descripcion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mov.getNumeroOperacion());
            ps.setString(2, mov.getNumeroCuenta());
            ps.setInt(3, mov.getIdEmpleado() > 0 ? mov.getIdEmpleado() : 1);
            ps.setInt(4, mov.getVentanilla() > 0 ? mov.getVentanilla() : 1);
            ps.setString(5, mov.getTipo());
            ps.setDouble(6, mov.getMonto());
            ps.setDouble(7, mov.getSaldoResultante());
            ps.setString(8, mov.getCuentaReferencia());
            ps.setString(9, mov.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }

    public List<MovimientoDTO> listarExtracto(String numeroCuenta) {
        List<MovimientoDTO> lista = new ArrayList<>();
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return lista;
        }
        String sql = "SELECT m.id_movimiento, m.numero_operacion, m.numero_cuenta, m.id_empleado, "
                + "COALESCE(e.nombre_completo, 'Sistema Monster') AS nombre_empleado, "
                + "COALESCE(m.ventanilla, 1) AS ventanilla, m.tipo, m.monto, m.saldo_resultante, "
                + "m.cuenta_referencia, m.descripcion, m.fecha_hora "
                + "FROM movimiento m "
                + "LEFT JOIN empleado e ON m.id_empleado = e.id_empleado "
                + "WHERE m.numero_cuenta = ? "
                + "ORDER BY m.fecha_hora DESC, m.id_movimiento DESC";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoDTO dto = new MovimientoDTO();
                    dto.setIdMovimiento(rs.getInt("id_movimiento"));
                    dto.setNumeroOperacion(rs.getString("numero_operacion"));
                    dto.setNumeroCuenta(rs.getString("numero_cuenta"));
                    dto.setIdEmpleado(rs.getInt("id_empleado"));
                    dto.setNombreEmpleado(rs.getString("nombre_empleado"));
                    dto.setVentanilla(rs.getInt("ventanilla"));
                    dto.setTipo(rs.getString("tipo"));
                    dto.setMonto(rs.getDouble("monto"));
                    dto.setSaldoResultante(rs.getDouble("saldo_resultante"));
                    dto.setCuentaReferencia(rs.getString("cuenta_referencia"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    java.sql.Timestamp ts = rs.getTimestamp("fecha_hora");
                    dto.setFechaHora(ts != null ? sdf.format(ts) : "");
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("[MovimientoDAO] Error listarExtracto: " + e.getMessage());
        }
        return lista;
    }
}
