package ec.edu.monster.dao;

import ec.edu.monster.modelo.Credito;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreditoDAO {

    public int registrarCreditoConAmortizacion(Credito credito, List<CuotaAmortizacion> tabla) throws SQLException {
        String sqlCred = "INSERT INTO CREDITO (COD_CLIENTE, MONTO, NUM_CUOTAS, TASA_INTERES_ANUAL, VALOR_CUOTA, FECHA_CREACION, ESTADO) " +
                         "VALUES (?, ?, ?, ?, ?, NOW(), ?)";
        String sqlAmort = "INSERT INTO TABLA_AMORTIZACION (COD_CREDITO, NUM_CUOTA, VALOR_CUOTA, INTERES_PAGADO, CAPITAL_PAGADO, SALDO) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);
            int codCreditoGenerado = 0;
            try {
                try (PreparedStatement psCred = conn.prepareStatement(sqlCred, Statement.RETURN_GENERATED_KEYS)) {
                    psCred.setInt(1, credito.getCodCliente());
                    psCred.setDouble(2, credito.getMonto());
                    psCred.setInt(3, credito.getNumCuotas());
                    psCred.setDouble(4, credito.getTasaInteresAnual());
                    psCred.setDouble(5, credito.getValorCuota());
                    psCred.setString(6, credito.getEstado() != null ? credito.getEstado() : "ACTIVO");
                    psCred.executeUpdate();

                    try (ResultSet rsKeys = psCred.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            codCreditoGenerado = rsKeys.getInt(1);
                            credito.setCodCredito(codCreditoGenerado);
                        }
                    }
                }

                try (PreparedStatement psAmort = conn.prepareStatement(sqlAmort)) {
                    for (CuotaAmortizacion cuota : tabla) {
                        psAmort.setInt(1, codCreditoGenerado);
                        psAmort.setInt(2, cuota.getNumCuota());
                        psAmort.setDouble(3, cuota.getValorCuota());
                        psAmort.setDouble(4, cuota.getInteresPagado());
                        psAmort.setDouble(5, cuota.getCapitalPagado());
                        psAmort.setDouble(6, cuota.getSaldo());
                        psAmort.addBatch();
                    }
                    psAmort.executeBatch();
                }

                conn.commit();
                return codCreditoGenerado;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public List<CuotaAmortizacion> obtenerTablaPorCredito(int codCredito) throws SQLException {
        List<CuotaAmortizacion> lista = new ArrayList<>();
        String sql = "SELECT COD_AMORTIZACION, COD_CREDITO, NUM_CUOTA, VALOR_CUOTA, INTERES_PAGADO, CAPITAL_PAGADO, SALDO " +
                     "FROM TABLA_AMORTIZACION WHERE COD_CREDITO = ? ORDER BY NUM_CUOTA ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCredito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CuotaAmortizacion c = new CuotaAmortizacion();
                    c.setCodAmortizacion(rs.getInt("COD_AMORTIZACION"));
                    c.setCodCredito(rs.getInt("COD_CREDITO"));
                    c.setNumCuota(rs.getInt("NUM_CUOTA"));
                    c.setValorCuota(rs.getDouble("VALOR_CUOTA"));
                    c.setInteresPagado(rs.getDouble("INTERES_PAGADO"));
                    c.setCapitalPagado(rs.getDouble("CAPITAL_PAGADO"));
                    c.setSaldo(rs.getDouble("SALDO"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public Credito obtenerCreditoActivoPorCliente(int codCliente) throws SQLException {
        String sql = "SELECT COD_CREDITO, COD_CLIENTE, MONTO, NUM_CUOTAS, TASA_INTERES_ANUAL, VALOR_CUOTA, FECHA_CREACION, ESTADO " +
                     "FROM CREDITO WHERE COD_CLIENTE = ? ORDER BY COD_CREDITO DESC LIMIT 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Credito c = new Credito();
                    c.setCodCredito(rs.getInt("COD_CREDITO"));
                    c.setCodCliente(rs.getInt("COD_CLIENTE"));
                    c.setMonto(rs.getDouble("MONTO"));
                    c.setNumCuotas(rs.getInt("NUM_CUOTAS"));
                    c.setTasaInteresAnual(rs.getDouble("TASA_INTERES_ANUAL"));
                    c.setValorCuota(rs.getDouble("VALOR_CUOTA"));
                    c.setFechaCreacion(rs.getString("FECHA_CREACION"));
                    c.setEstado(rs.getString("ESTADO"));
                    return c;
                }
            }
        }
        return null;
    }
}
