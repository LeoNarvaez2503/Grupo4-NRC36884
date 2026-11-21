package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Data Access Object (DAO) para las operaciones CRUD y reportes estadísticos
 * sobre la tabla {@code pais} en MySQL.
 */
public class PaisDAO {

    /**
     * Inserta un nuevo país en la base de datos y asigna el ID generado al objeto.
     */
    public boolean guardar(Pais pais) {
        String sql = "INSERT INTO pais(nombre, capital, region, poblacion, moneda) VALUES(?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, pais.getNombre());
                ps.setString(2, pais.getCapital());
                ps.setString(3, pais.getRegion());
                ps.setLong(4, pais.getPoblacion());
                ps.setString(5, pais.getMoneda());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pais.setId(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar país: " + e.getMessage());
        }
        return false;
    }

    /**
     * Importa una lista de países evitando duplicados por nombre (inserta nuevos y actualiza existentes).
     *
     * @return arreglo de 2 enteros: [insertados, actualizados]
     */
    public int[] importarLote(List<Pais> paises) {
        int insertados = 0;
        int actualizados = 0;
        if (paises == null || paises.isEmpty()) {
            return new int[]{0, 0};
        }

        String selectSql = "SELECT id, LOWER(nombre) AS nombre_lower FROM pais";
        String insertSql = "INSERT INTO pais(nombre, capital, region, poblacion, moneda) VALUES(?, ?, ?, ?, ?)";
        String updateSql = "UPDATE pais SET nombre = ?, capital = ?, region = ?, poblacion = ?, moneda = ? WHERE id = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return new int[]{0, 0};
            }
            con.setAutoCommit(false);
            try {
                Map<String, Integer> existentes = new HashMap<>();
                try (PreparedStatement psSel = con.prepareStatement(selectSql);
                     ResultSet rs = psSel.executeQuery()) {
                    while (rs.next()) {
                        existentes.put(rs.getString("nombre_lower"), rs.getInt("id"));
                    }
                }

                try (PreparedStatement psIns = con.prepareStatement(insertSql);
                     PreparedStatement psUpd = con.prepareStatement(updateSql)) {
                    for (Pais pais : paises) {
                        if (pais.getNombre() == null || pais.getNombre().isBlank()) {
                            continue;
                        }
                        String key = pais.getNombre().trim().toLowerCase(Locale.ROOT);
                        Integer existingId = existentes.get(key);
                        if (existingId == null) {
                            psIns.setString(1, pais.getNombre());
                            psIns.setString(2, pais.getCapital());
                            psIns.setString(3, pais.getRegion());
                            psIns.setLong(4, pais.getPoblacion());
                            psIns.setString(5, pais.getMoneda());
                            psIns.addBatch();
                            insertados++;
                            existentes.put(key, -1);
                        } else if (existingId > 0) {
                            psUpd.setString(1, pais.getNombre());
                            psUpd.setString(2, pais.getCapital());
                            psUpd.setString(3, pais.getRegion());
                            psUpd.setLong(4, pais.getPoblacion());
                            psUpd.setString(5, pais.getMoneda());
                            psUpd.setInt(6, existingId);
                            psUpd.addBatch();
                            actualizados++;
                        }
                    }
                    if (insertados > 0) {
                        psIns.executeBatch();
                    }
                    if (actualizados > 0) {
                        psUpd.executeBatch();
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al importar lote de países: " + e.getMessage());
            return new int[]{0, 0};
        }
        return new int[]{insertados, actualizados};
    }

    /**
     * Obtiene todos los países registrados en MySQL ordenados alfabéticamente.
     */
    public List<Pais> listarTodos() {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capital, region, poblacion, moneda FROM pais ORDER BY nombre ASC";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return lista;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPais(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar países: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un país por su ID.
     */
    public Pais buscarPorId(int id) {
        String sql = "SELECT id, nombre, capital, region, poblacion, moneda FROM pais WHERE id = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearPais(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar país por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca países cuyo nombre contenga el texto indicado (insensible a mayúsculas).
     */
    public List<Pais> buscarPorNombre(String nombre) {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capital, region, poblacion, moneda FROM pais "
                + "WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre ASC";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return lista;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + nombre.trim() + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearPais(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar países por nombre: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca países pertenecientes a una región específica (Actividad 4).
     */
    public List<Pais> buscarPorRegion(String region) {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capital, region, poblacion, moneda FROM pais "
                + "WHERE LOWER(region) = LOWER(?) ORDER BY nombre ASC";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return lista;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, region.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearPais(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar países por región: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza los datos de un país existente (Actividad 2 - Actualizar).
     */
    public boolean actualizar(Pais pais) {
        String sql = "UPDATE pais SET nombre = ?, capital = ?, region = ?, poblacion = ?, moneda = ? WHERE id = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pais.getNombre());
                ps.setString(2, pais.getCapital());
                ps.setString(3, pais.getRegion());
                ps.setLong(4, pais.getPoblacion());
                ps.setString(5, pais.getMoneda());
                ps.setInt(6, pais.getId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar país: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un país por su ID (Actividad 2 - Eliminar).
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM pais WHERE id = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar país: " + e.getMessage());
        }
        return false;
    }

    /**
     * Reporte estadístico: Número de países por región (Actividad 5).
     */
    public Map<String, Integer> contarPaisesPorRegion() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(NULLIF(TRIM(region), ''), 'Sin región') AS region_nombre, "
                + "COUNT(*) AS total FROM pais GROUP BY region_nombre ORDER BY total DESC, region_nombre ASC";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return conteo;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    conteo.put(rs.getString("region_nombre"), rs.getInt("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar países por región: " + e.getMessage());
        }
        return conteo;
    }

    /**
     * Reporte estadístico: País más poblado registrado (Actividad 5).
     */
    public Pais obtenerPaisMasPoblado() {
        String sql = "SELECT id, nombre, capital, region, poblacion, moneda FROM pais "
                + "ORDER BY poblacion DESC, nombre ASC LIMIT 1";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPais(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener país más poblado: " + e.getMessage());
        }
        return null;
    }

    /**
     * Reporte estadístico: Región con mayor número de países (Actividad 5).
     */
    public Map.Entry<String, Integer> obtenerRegionConMasPaises() {
        String sql = "SELECT COALESCE(NULLIF(TRIM(region), ''), 'Sin región') AS region_nombre, "
                + "COUNT(*) AS total FROM pais GROUP BY region_nombre ORDER BY total DESC, region_nombre ASC LIMIT 1";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AbstractMap.SimpleEntry<>(rs.getString("region_nombre"), rs.getInt("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener región con más países: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve el total de países almacenados en la tabla.
     */
    public int contarTotalPaises() {
        String sql = "SELECT COUNT(*) FROM pais";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                return 0;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar países: " + e.getMessage());
        }
        return 0;
    }

    private Pais mapearPais(ResultSet rs) throws SQLException {
        return new Pais(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("capital"),
                rs.getString("region"),
                rs.getLong("poblacion"),
                rs.getString("moneda")
        );
    }
}
