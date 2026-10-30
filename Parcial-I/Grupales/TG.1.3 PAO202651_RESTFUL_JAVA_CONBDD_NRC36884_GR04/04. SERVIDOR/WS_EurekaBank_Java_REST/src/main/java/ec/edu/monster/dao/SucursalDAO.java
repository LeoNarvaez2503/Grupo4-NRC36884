package ec.edu.monster.dao;

import ec.edu.monster.modelo.SucursalDTO;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para consulta de Sucursales de EurekaBank.
 */
public class SucursalDAO {

    public List<SucursalDTO> listarSucursales() {
        List<SucursalDTO> lista = new ArrayList<>();
        String sql = "SELECT s.id_sucursal, s.codigo, s.nombre, s.ciudad, s.direccion, "
                + "COUNT(c.numero_cuenta) AS total_cuentas "
                + "FROM sucursal s "
                + "LEFT JOIN cuenta c ON s.id_sucursal = c.id_sucursal "
                + "GROUP BY s.id_sucursal, s.codigo, s.nombre, s.ciudad, s.direccion "
                + "ORDER BY s.id_sucursal ASC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new SucursalDTO(
                        rs.getInt("id_sucursal"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("ciudad"),
                        rs.getString("direccion"),
                        rs.getInt("total_cuentas")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[SucursalDAO] Error listarSucursales: " + e.getMessage());
        }
        return lista;
    }
}
