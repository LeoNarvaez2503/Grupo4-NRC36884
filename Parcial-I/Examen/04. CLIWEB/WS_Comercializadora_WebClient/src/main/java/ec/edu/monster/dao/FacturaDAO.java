package ec.edu.monster.dao;

import ec.edu.monster.modelo.Factura;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public int insertar(Factura f) throws SQLException {
        String sql = "INSERT INTO FACTURA (NUMERO_COMPROBANTE, FECHA, COD_CLIENTE_BANCO, CEDULA_CLIENTE, " +
                     "NOMBRE_CLIENTE, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, FORMA_PAGO, " +
                     "PORCENTAJE_DESCUENTO, VALOR_DESCUENTO, TOTAL_PAGAR, COD_CREDITO_BANCO, NUM_CUOTAS, " +
                     "VALOR_CUOTA, OBSERVACION) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNumeroComprobante());
            if (f.getCodClienteBanco() != null) ps.setInt(2, f.getCodClienteBanco()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, f.getCedulaCliente());
            ps.setString(4, f.getNombreCliente());
            ps.setInt(5, f.getCodTelefono());
            ps.setString(6, f.getModeloTelefono());
            ps.setDouble(7, f.getPrecioUnitario());
            ps.setString(8, f.getFormaPago());
            ps.setDouble(9, f.getPorcentajeDescuento());
            ps.setDouble(10, f.getValorDescuento());
            ps.setDouble(11, f.getTotalPagar());
            if (f.getCodCreditoBanco() != null) ps.setInt(12, f.getCodCreditoBanco()); else ps.setNull(12, Types.INTEGER);
            if (f.getNumCuotas() != null) ps.setInt(13, f.getNumCuotas()); else ps.setNull(13, Types.INTEGER);
            if (f.getValorCuota() != null) ps.setDouble(14, f.getValorCuota()); else ps.setNull(14, Types.DECIMAL);
            ps.setString(15, f.getObservacion());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Factura> listar() throws SQLException {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT NUM_FACTURA, NUMERO_COMPROBANTE, FECHA, COD_CLIENTE_BANCO, CEDULA_CLIENTE, " +
                     "NOMBRE_CLIENTE, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, FORMA_PAGO, " +
                     "PORCENTAJE_DESCUENTO, VALOR_DESCUENTO, TOTAL_PAGAR, COD_CREDITO_BANCO, NUM_CUOTAS, " +
                     "VALOR_CUOTA, OBSERVACION FROM FACTURA ORDER BY NUM_FACTURA DESC";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearFactura(rs));
            }
        }
        return lista;
    }

    public Factura buscarPorId(int id) throws SQLException {
        String sql = "SELECT NUM_FACTURA, NUMERO_COMPROBANTE, FECHA, COD_CLIENTE_BANCO, CEDULA_CLIENTE, " +
                     "NOMBRE_CLIENTE, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, FORMA_PAGO, " +
                     "PORCENTAJE_DESCUENTO, VALOR_DESCUENTO, TOTAL_PAGAR, COD_CREDITO_BANCO, NUM_CUOTAS, " +
                     "VALOR_CUOTA, OBSERVACION FROM FACTURA WHERE NUM_FACTURA = ? LIMIT 1";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFactura(rs);
                }
            }
        }
        return null;
    }

    private Factura mapearFactura(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setNumFactura(rs.getInt("NUM_FACTURA"));
        f.setNumeroComprobante(rs.getString("NUMERO_COMPROBANTE"));
        f.setFecha(rs.getString("FECHA"));
        int codCli = rs.getInt("COD_CLIENTE_BANCO");
        if (!rs.wasNull()) f.setCodClienteBanco(codCli);
        f.setCedulaCliente(rs.getString("CEDULA_CLIENTE"));
        f.setNombreCliente(rs.getString("NOMBRE_CLIENTE"));
        f.setCodTelefono(rs.getInt("COD_TELEFONO"));
        f.setModeloTelefono(rs.getString("MODELO_TELEFONO"));
        f.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
        f.setFormaPago(rs.getString("FORMA_PAGO"));
        f.setPorcentajeDescuento(rs.getDouble("PORCENTAJE_DESCUENTO"));
        f.setValorDescuento(rs.getDouble("VALOR_DESCUENTO"));
        f.setTotalPagar(rs.getDouble("TOTAL_PAGAR"));
        int codCred = rs.getInt("COD_CREDITO_BANCO");
        if (!rs.wasNull()) f.setCodCreditoBanco(codCred);
        int nCuotas = rs.getInt("NUM_CUOTAS");
        if (!rs.wasNull()) f.setNumCuotas(nCuotas);
        double vCuota = rs.getDouble("VALOR_CUOTA");
        if (!rs.wasNull()) f.setValorCuota(vCuota);
        f.setObservacion(rs.getString("OBSERVACION"));
        return f;
    }
}
