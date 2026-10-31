package ec.edu.monster.dao;

import ec.edu.monster.modelo.TelefonoCelular;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO {

    public List<TelefonoCelular> listar(boolean soloDisponibles) throws SQLException {
        List<TelefonoCelular> lista = new ArrayList<>();
        String sql = "SELECT COD_TELEFONO, MARCA, MODELO, PRECIO, CARACTERISTICAS, IMAGEN_URL, ESTADO FROM TELEFONO_CELULAR ";
        if (soloDisponibles) sql += "WHERE ESTADO = 'DISPONIBLE' ";
        sql += "ORDER BY COD_TELEFONO ASC";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearTelefono(rs));
            }
        }
        return lista;
    }

    public TelefonoCelular buscarPorId(int id) throws SQLException {
        String sql = "SELECT COD_TELEFONO, MARCA, MODELO, PRECIO, CARACTERISTICAS, IMAGEN_URL, ESTADO FROM TELEFONO_CELULAR WHERE COD_TELEFONO = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTelefono(rs);
                }
            }
        }
        return null;
    }

    public int insertar(TelefonoCelular tel) throws SQLException {
        String sql = "INSERT INTO TELEFONO_CELULAR (MARCA, MODELO, PRECIO, CARACTERISTICAS, IMAGEN_URL, ESTADO) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tel.getMarca());
            ps.setString(2, tel.getModelo());
            ps.setDouble(3, tel.getPrecio());
            ps.setString(4, tel.getCaracteristicas() != null ? tel.getCaracteristicas() : "");
            ps.setString(5, tel.getImagenUrl() != null ? tel.getImagenUrl() : "");
            ps.setString(6, tel.getEstado() != null ? tel.getEstado() : "DISPONIBLE");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean actualizar(TelefonoCelular tel) throws SQLException {
        String sql = "UPDATE TELEFONO_CELULAR SET MARCA = ?, MODELO = ?, PRECIO = ?, CARACTERISTICAS = ?, IMAGEN_URL = ?, ESTADO = ? WHERE COD_TELEFONO = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tel.getMarca());
            ps.setString(2, tel.getModelo());
            ps.setDouble(3, tel.getPrecio());
            ps.setString(4, tel.getCaracteristicas() != null ? tel.getCaracteristicas() : "");
            ps.setString(5, tel.getImagenUrl() != null ? tel.getImagenUrl() : "");
            ps.setString(6, tel.getEstado() != null ? tel.getEstado() : "DISPONIBLE");
            ps.setInt(7, tel.getCodTelefono());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean desactivar(int id) throws SQLException {
        String sql = "UPDATE TELEFONO_CELULAR SET ESTADO = 'INACTIVO' WHERE COD_TELEFONO = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private TelefonoCelular mapearTelefono(ResultSet rs) throws SQLException {
        TelefonoCelular t = new TelefonoCelular();
        t.setCodTelefono(rs.getInt("COD_TELEFONO"));
        t.setMarca(rs.getString("MARCA"));
        t.setModelo(rs.getString("MODELO"));
        t.setPrecio(rs.getDouble("PRECIO"));
        t.setCaracteristicas(rs.getString("CARACTERISTICAS"));
        t.setImagenUrl(rs.getString("IMAGEN_URL"));
        t.setEstado(rs.getString("ESTADO"));
        return t;
    }
}
