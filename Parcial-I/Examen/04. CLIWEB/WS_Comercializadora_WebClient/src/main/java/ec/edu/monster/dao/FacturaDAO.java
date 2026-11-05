package ec.edu.monster.dao;

import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.FacturaDetalle;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acceso a datos de FACTURA (cabecera) y FACTURA_DETALLE (items del carrito).
 * La cabecera y todos sus renglones se guardan en UNA sola transaccion.
 */
public class FacturaDAO {

    private static final String COLS_CABECERA =
            "NUM_FACTURA, NUMERO_COMPROBANTE, FECHA, COD_CLIENTE_BANCO, CEDULA_CLIENTE, NOMBRE_CLIENTE, " +
            "COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, SUBTOTAL, FORMA_PAGO, PORCENTAJE_DESCUENTO, " +
            "VALOR_DESCUENTO, TOTAL_PAGAR, COD_CREDITO_BANCO, NUM_CUOTAS, VALOR_CUOTA, OBSERVACION";

    private static volatile boolean esquemaListo = false;

    /**
     * Migracion automatica e idempotente: si la base aun tiene el esquema anterior
     * (1 telefono por factura) crea FACTURA_DETALLE y adapta FACTURA.
     */
    public static synchronized void asegurarEsquema() throws SQLException {
        if (esquemaListo) return;
        try (Connection conn = ConexionDB.getConexion(); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS FACTURA_DETALLE (" +
                    "COD_DETALLE INT NOT NULL AUTO_INCREMENT, NUM_FACTURA INT NOT NULL, COD_TELEFONO INT NOT NULL, " +
                    "MODELO_TELEFONO VARCHAR(160) NOT NULL, PRECIO_UNITARIO NUMERIC(10,2) NOT NULL, " +
                    "CANTIDAD INT NOT NULL DEFAULT 1, SUBTOTAL NUMERIC(10,2) NOT NULL, PRIMARY KEY (COD_DETALLE), " +
                    "CONSTRAINT FK_DETALLE_FACTURA FOREIGN KEY (NUM_FACTURA) REFERENCES FACTURA (NUM_FACTURA) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "CONSTRAINT FK_DETALLE_TELEFONO FOREIGN KEY (COD_TELEFONO) REFERENCES TELEFONO_CELULAR (COD_TELEFONO) " +
                    "ON DELETE RESTRICT ON UPDATE CASCADE) ENGINE=InnoDB");

            if (!existeColumna(conn, "FACTURA", "SUBTOTAL")) {
                st.executeUpdate("ALTER TABLE FACTURA ADD COLUMN SUBTOTAL NUMERIC(10,2) NOT NULL DEFAULT 0.00 AFTER PRECIO_UNITARIO");
            }
            if (existeFk(conn, "FACTURA", "FK_FACTURA_TELEFONO")) {
                st.executeUpdate("ALTER TABLE FACTURA DROP FOREIGN KEY FK_FACTURA_TELEFONO");
            }
            st.executeUpdate("ALTER TABLE FACTURA MODIFY COD_TELEFONO INT NULL, " +
                    "MODIFY MODELO_TELEFONO VARCHAR(255) NULL, MODIFY PRECIO_UNITARIO NUMERIC(10,2) NULL");

            // Facturas antiguas sin detalle -> un renglon con cantidad 1
            st.executeUpdate("INSERT INTO FACTURA_DETALLE (NUM_FACTURA, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL) " +
                    "SELECT F.NUM_FACTURA, F.COD_TELEFONO, F.MODELO_TELEFONO, F.PRECIO_UNITARIO, 1, F.PRECIO_UNITARIO FROM FACTURA F " +
                    "WHERE F.COD_TELEFONO IS NOT NULL AND NOT EXISTS (SELECT 1 FROM FACTURA_DETALLE D WHERE D.NUM_FACTURA = F.NUM_FACTURA)");
            st.executeUpdate("UPDATE FACTURA SET SUBTOTAL = PRECIO_UNITARIO WHERE SUBTOTAL = 0 AND PRECIO_UNITARIO IS NOT NULL");
        }
        esquemaListo = true;
    }

    private static boolean existeColumna(Connection conn, String tabla, String columna) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tabla);
            ps.setString(2, columna);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
        }
    }

    private static boolean existeFk(Connection conn, String tabla, String fk) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA = DATABASE() " +
                     "AND TABLE_NAME = ? AND CONSTRAINT_NAME = ? AND CONSTRAINT_TYPE = 'FOREIGN KEY'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tabla);
            ps.setString(2, fk);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
        }
    }

    /** Inserta la factura y TODOS sus items de forma atomica. Devuelve el NUM_FACTURA generado. */
    public int insertar(Factura f) throws SQLException {
        asegurarEsquema();
        String sqlCab = "INSERT INTO FACTURA (NUMERO_COMPROBANTE, FECHA, COD_CLIENTE_BANCO, CEDULA_CLIENTE, " +
                     "NOMBRE_CLIENTE, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, SUBTOTAL, FORMA_PAGO, " +
                     "PORCENTAJE_DESCUENTO, VALOR_DESCUENTO, TOTAL_PAGAR, COD_CREDITO_BANCO, NUM_CUOTAS, " +
                     "VALOR_CUOTA, OBSERVACION) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDet = "INSERT INTO FACTURA_DETALLE (NUM_FACTURA, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);
            try {
                int numFactura = 0;
                try (PreparedStatement ps = conn.prepareStatement(sqlCab, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, f.getNumeroComprobante());
                    if (f.getCodClienteBanco() != null) ps.setInt(2, f.getCodClienteBanco()); else ps.setNull(2, Types.INTEGER);
                    ps.setString(3, f.getCedulaCliente());
                    ps.setString(4, f.getNombreCliente());
                    if (f.getCodTelefono() > 0) ps.setInt(5, f.getCodTelefono()); else ps.setNull(5, Types.INTEGER);
                    ps.setString(6, f.getModeloTelefono());
                    ps.setDouble(7, f.getPrecioUnitario());
                    ps.setDouble(8, f.getSubtotal());
                    ps.setString(9, f.getFormaPago());
                    ps.setDouble(10, f.getPorcentajeDescuento());
                    ps.setDouble(11, f.getValorDescuento());
                    ps.setDouble(12, f.getTotalPagar());
                    if (f.getCodCreditoBanco() != null) ps.setInt(13, f.getCodCreditoBanco()); else ps.setNull(13, Types.INTEGER);
                    if (f.getNumCuotas() != null) ps.setInt(14, f.getNumCuotas()); else ps.setNull(14, Types.INTEGER);
                    if (f.getValorCuota() != null) ps.setDouble(15, f.getValorCuota()); else ps.setNull(15, Types.DECIMAL);
                    ps.setString(16, f.getObservacion());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) numFactura = rs.getInt(1);
                    }
                }

                try (PreparedStatement pd = conn.prepareStatement(sqlDet)) {
                    for (FacturaDetalle d : f.getItems()) {
                        pd.setInt(1, numFactura);
                        pd.setInt(2, d.getCodTelefono());
                        pd.setString(3, d.getModeloTelefono());
                        pd.setDouble(4, d.getPrecioUnitario());
                        pd.setInt(5, d.getCantidad());
                        pd.setDouble(6, d.getSubtotal());
                        pd.addBatch();
                    }
                    pd.executeBatch();
                }
                conn.commit();
                return numFactura;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public List<Factura> listar() throws SQLException {
        asegurarEsquema();
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT " + COLS_CABECERA + " FROM FACTURA ORDER BY NUM_FACTURA DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearFactura(rs));
            }
            cargarItems(conn, lista);
        }
        return lista;
    }

    public Factura buscarPorId(int id) throws SQLException {
        asegurarEsquema();
        String sql = "SELECT " + COLS_CABECERA + " FROM FACTURA WHERE NUM_FACTURA = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Factura f = mapearFactura(rs);
                    cargarItems(conn, List.of(f));
                    return f;
                }
            }
        }
        return null;
    }

    /** Carga los renglones de todas las facturas dadas con una sola consulta. */
    private void cargarItems(Connection conn, List<Factura> facturas) throws SQLException {
        if (facturas.isEmpty()) return;
        Map<Integer, Factura> porId = new HashMap<>();
        StringBuilder in = new StringBuilder();
        for (Factura f : facturas) {
            porId.put(f.getNumFactura(), f);
            if (in.length() > 0) in.append(',');
            in.append(f.getNumFactura());
        }
        String sql = "SELECT COD_DETALLE, NUM_FACTURA, COD_TELEFONO, MODELO_TELEFONO, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL " +
                     "FROM FACTURA_DETALLE WHERE NUM_FACTURA IN (" + in + ") ORDER BY COD_DETALLE ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FacturaDetalle d = new FacturaDetalle();
                d.setCodDetalle(rs.getInt("COD_DETALLE"));
                d.setNumFactura(rs.getInt("NUM_FACTURA"));
                d.setCodTelefono(rs.getInt("COD_TELEFONO"));
                d.setModeloTelefono(rs.getString("MODELO_TELEFONO"));
                d.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                d.setCantidad(rs.getInt("CANTIDAD"));
                d.setSubtotal(rs.getDouble("SUBTOTAL"));
                Factura f = porId.get(d.getNumFactura());
                if (f != null) f.getItems().add(d);
            }
        }
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
        f.setSubtotal(rs.getDouble("SUBTOTAL"));
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
