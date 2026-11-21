package controlador;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import modelo.Conexion;
import modelo.ConsumoAPI;
import modelo.Pais;
import modelo.PaisDAO;

/**
 * Controlador MVC que coordina la vista {@link vista.FrmPais} con el modelo
 * ({@link ConsumoAPI} y {@link PaisDAO}).
 */
public class PaisController {

    private final PaisDAO dao;

    public PaisController() {
        this.dao = new PaisDAO();
    }

    public PaisController(PaisDAO dao) {
        this.dao = dao;
    }

    /**
     * Resultado de una operación de importación desde la API REST hacia MySQL.
     */
    public record ResultadoImportacion(
            boolean exito,
            int totalApi,
            int insertados,
            int actualizados,
            String fuente,
            String mensaje,
            List<Pais> paisesObtenidos
    ) {
    }

    /**
     * Resumen estadístico requerido en la Actividad 5.
     */
    public record ReporteEstadistico(
            int totalPaises,
            Map<String, Integer> paisesPorRegion,
            Pais paisMasPoblado,
            Map.Entry<String, Integer> regionConMasPaises
    ) {
    }

    public boolean verificarConexionBD() {
        return Conexion.probarConexion();
    }

    /**
     * Consume el endpoint general de países y los guarda/actualiza en MySQL.
     */
    public ResultadoImportacion importarPaises() {
        try {
            String json = ConsumoAPI.obtenerDatos();
            if (json == null || json.isBlank()) {
                return new ResultadoImportacion(
                        false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                        "No se pudo obtener respuesta de la API pública.", List.of()
                );
            }
            List<Pais> paises = ConsumoAPI.parsearPaises(json);
            if (paises.isEmpty()) {
                return new ResultadoImportacion(
                        false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                        "La API no devolvió registros válidos.", List.of()
                );
            }
            if (!Conexion.probarConexion()) {
                return new ResultadoImportacion(
                        false, paises.size(), 0, 0, ConsumoAPI.getUltimaFuente(),
                        "Se obtuvieron " + paises.size() + " países de la API, pero no hay conexión con MySQL.",
                        paises
                );
            }
            int[] conteo = dao.importarLote(paises);
            return new ResultadoImportacion(
                    true,
                    paises.size(),
                    conteo[0],
                    conteo[1],
                    ConsumoAPI.getUltimaFuente(),
                    String.format("Importación finalizada (%s): %d nuevos, %d actualizados.",
                            ConsumoAPI.getUltimaFuente(), conteo[0], conteo[1]),
                    paises
            );
        } catch (Exception ex) {
            return new ResultadoImportacion(
                    false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                    "Error durante la importación: " + ex.getMessage(), List.of()
            );
        }
    }

    /**
     * Actividad 1: Consulta un país por nombre directamente en la API pública.
     */
    public List<Pais> consultarApiPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return List.of();
        }
        String json = ConsumoAPI.obtenerPorNombre(nombre);
        return ConsumoAPI.parsearPaises(json);
    }

    /**
     * Actividad 1: Consulta un país por nombre en la API y opcionalmente lo persiste en MySQL.
     */
    public ResultadoImportacion importarPorNombreDesdeApi(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return new ResultadoImportacion(false, 0, 0, 0, "", "Ingrese el nombre de un país.", List.of());
        }
        try {
            List<Pais> encontrados = consultarApiPorNombre(nombre);
            if (encontrados.isEmpty()) {
                return new ResultadoImportacion(
                        false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                        "No se encontró ningún país en la API con el nombre: " + nombre, List.of()
                );
            }
            int[] conteo = dao.importarLote(encontrados);
            return new ResultadoImportacion(
                    true,
                    encontrados.size(),
                    conteo[0],
                    conteo[1],
                    ConsumoAPI.getUltimaFuente(),
                    String.format("Consulta API exitosa (%s): %d encontrado(s), %d nuevo(s), %d actualizado(s).",
                            ConsumoAPI.getUltimaFuente(), encontrados.size(), conteo[0], conteo[1]),
                    encontrados
            );
        } catch (Exception ex) {
            return new ResultadoImportacion(
                    false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                    "Error consultando API por nombre: " + ex.getMessage(), List.of()
            );
        }
    }

    /**
     * Actividad 4: Importa países de una región específica desde la API pública.
     */
    public ResultadoImportacion importarPorRegionDesdeApi(String regionSeleccionada) {
        String regionApi = normalizarRegionApi(regionSeleccionada);
        if (regionApi.isBlank()) {
            return importarPaises();
        }
        try {
            String json = ConsumoAPI.obtenerPorRegion(regionApi);
            List<Pais> paises = ConsumoAPI.parsearPaises(json);
            if (paises.isEmpty()) {
                return new ResultadoImportacion(
                        false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                        "No se encontraron países en la API para la región: " + regionSeleccionada, List.of()
                );
            }
            int[] conteo = dao.importarLote(paises);
            return new ResultadoImportacion(
                    true,
                    paises.size(),
                    conteo[0],
                    conteo[1],
                    ConsumoAPI.getUltimaFuente(),
                    String.format("Región '%s' importada (%s): %d nuevos, %d actualizados.",
                            regionApi, ConsumoAPI.getUltimaFuente(), conteo[0], conteo[1]),
                    paises
            );
        } catch (Exception ex) {
            return new ResultadoImportacion(
                    false, 0, 0, 0, ConsumoAPI.getUltimaFuente(),
                    "Error al importar región: " + ex.getMessage(), List.of()
            );
        }
    }

    // --- Operaciones CRUD sobre MySQL (Actividad 2 y 3) ---

    public List<Pais> listarPaises() {
        return dao.listarTodos();
    }

    public List<Pais> buscarEnBdPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return dao.listarTodos();
        }
        return dao.buscarPorNombre(nombre);
    }

    /**
     * Actividad 4: Filtra países registrados en MySQL por región.
     */
    public List<Pais> buscarEnBdPorRegion(String regionSeleccionada) {
        String regionApi = normalizarRegionApi(regionSeleccionada);
        if (regionApi.isBlank()) {
            return dao.listarTodos();
        }
        return dao.buscarPorRegion(regionApi);
    }

    public String crearPais(Pais pais) {
        String error = validarPais(pais);
        if (error != null) {
            return error;
        }
        boolean ok = dao.guardar(pais);
        return ok ? null : "No se pudo guardar el país en MySQL.";
    }

    public String actualizarPais(Pais pais) {
        if (pais == null || pais.getId() <= 0) {
            return "Seleccione primero un país válido de la tabla para actualizar.";
        }
        String error = validarPais(pais);
        if (error != null) {
            return error;
        }
        boolean ok = dao.actualizar(pais);
        return ok ? null : "No se pudo actualizar el país (ID " + pais.getId() + ").";
    }

    public String eliminarPais(int id) {
        if (id <= 0) {
            return "Seleccione primero un país de la tabla para eliminar.";
        }
        boolean ok = dao.eliminar(id);
        return ok ? null : "No se pudo eliminar el país (ID " + id + ").";
    }

    /**
     * Actividad 5: Genera los reportes estadísticos solicitados.
     */
    public ReporteEstadistico generarReporteEstadistico() {
        int total = dao.contarTotalPaises();
        Map<String, Integer> porRegion = dao.contarPaisesPorRegion();
        Pais masPoblado = dao.obtenerPaisMasPoblado();
        Map.Entry<String, Integer> regionTop = dao.obtenerRegionConMasPaises();
        return new ReporteEstadistico(total, porRegion, masPoblado, regionTop);
    }

    /**
     * Convierte etiquetas en español ("América", "Europa", etc.) al nombre estándar de región de la API.
     */
    public static String normalizarRegionApi(String region) {
        if (region == null) {
            return "";
        }
        String r = region.trim();
        if (r.isEmpty() || r.equalsIgnoreCase("Todas") || r.equalsIgnoreCase("All")) {
            return "";
        }
        // Si viene con formato "América (Americas)", extraer o mapear
        String lower = r.toLowerCase(Locale.ROOT);
        if (lower.contains("américa") || lower.contains("america")) {
            return "Americas";
        }
        if (lower.contains("europa") || lower.contains("europe")) {
            return "Europe";
        }
        if (lower.contains("áfrica") || lower.contains("africa")) {
            return "Africa";
        }
        if (lower.contains("oceanía") || lower.contains("oceania")) {
            return "Oceania";
        }
        if (lower.contains("asia")) {
            return "Asia";
        }
        if (lower.contains("polar")) {
            return "Polar";
        }
        if (lower.contains("antarctic")) {
            return "Antarctic";
        }
        return r;
    }

    private String validarPais(Pais pais) {
        if (pais == null) {
            return "Los datos del país son inválidos.";
        }
        if (pais.getNombre() == null || pais.getNombre().isBlank()) {
            return "El nombre del país es obligatorio.";
        }
        if (pais.getRegion() == null || pais.getRegion().isBlank()) {
            return "La región del país es obligatoria.";
        }
        if (pais.getPoblacion() < 0) {
            return "La población no puede ser negativa.";
        }
        return null;
    }
}
