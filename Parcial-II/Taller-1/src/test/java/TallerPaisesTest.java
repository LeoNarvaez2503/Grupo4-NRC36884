import controlador.LoginController;
import controlador.PaisController;
import controlador.PaisController.ReporteEstadistico;
import controlador.PaisController.ResultadoImportacion;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import modelo.Conexion;
import modelo.ConsumoAPI;
import modelo.Pais;
import modelo.PaisDAO;
import modelo.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vista.FrmLogin;
import vista.FrmPais;

/**
 * Pruebas integrales de API REST, parser JSON, DAO MySQL, CRUD y reportes estadísticos.
 */
class TallerPaisesTest {

    @Test
    void testParseoAmbosEsquemasJson() {
        String jsonV3 = """
                [
                  {
                    "name": { "common": "Ecuador", "official": "Republic of Ecuador" },
                    "capital": ["Quito"],
                    "region": "Americas",
                    "population": 17643060,
                    "currencies": { "USD": { "name": "United States dollar", "symbol": "$" } }
                  }
                ]
                """;
        List<Pais> v3 = ConsumoAPI.parsearPaises(jsonV3);
        Assertions.assertEquals(1, v3.size());
        Assertions.assertEquals("Ecuador", v3.get(0).getNombre());
        Assertions.assertEquals("Quito", v3.get(0).getCapital());
        Assertions.assertEquals("Americas", v3.get(0).getRegion());
        Assertions.assertEquals(17643060L, v3.get(0).getPoblacion());
        Assertions.assertEquals("United States dollar (USD)", v3.get(0).getMoneda());

        String jsonV2 = """
                [
                  {
                    "name": "Ecuador",
                    "capital": "Quito",
                    "region": "Americas",
                    "population": 17643060,
                    "currencies": [ { "code": "USD", "name": "United States dollar", "symbol": "$" } ]
                  }
                ]
                """;
        List<Pais> v2 = ConsumoAPI.parsearPaises(jsonV2);
        Assertions.assertEquals(1, v2.size());
        Assertions.assertEquals("Ecuador", v2.get(0).getNombre());
        Assertions.assertEquals("Quito", v2.get(0).getCapital());
        Assertions.assertEquals("Americas", v2.get(0).getRegion());
        Assertions.assertEquals(17643060L, v2.get(0).getPoblacion());
        Assertions.assertEquals("United States dollar (USD)", v2.get(0).getMoneda());
    }

    @Test
    void testConsumoApiPorNombreYTodos() {
        PaisController controller = new PaisController();

        // Actividad 1: Consulta por nombre
        List<Pais> ecuadorList = controller.consultarApiPorNombre("ecuador");
        Assertions.assertFalse(ecuadorList.isEmpty(), "La API debe retornar Ecuador");
        Pais ec = ecuadorList.get(0);
        Assertions.assertEquals("Ecuador", ec.getNombre());
        Assertions.assertEquals("Quito", ec.getCapital());
        Assertions.assertEquals("Americas", ec.getRegion());
        Assertions.assertTrue(ec.getPoblacion() > 1_000_000L);
        Assertions.assertTrue(ec.getMoneda().contains("USD"));

        // Endpoint general
        String jsonAll = ConsumoAPI.obtenerDatos();
        List<Pais> todos = ConsumoAPI.parsearPaises(jsonAll);
        Assertions.assertTrue(todos.size() >= 190, "Se esperaban al menos 190 países, se obtuvieron " + todos.size());
    }

    @Test
    void testConexionMySQLYCrudCompleto() {
        Assertions.assertTrue(Conexion.probarConexion(), "MySQL debe estar conectado en localhost:3306/api_paises");
        PaisDAO dao = new PaisDAO();

        // Crear
        Pais nuevo = new Pais("PaisPruebaMVC", "CapitalPrueba", "Americas", 123456L, "TestCoin (TST)");
        Assertions.assertTrue(dao.guardar(nuevo));
        Assertions.assertTrue(nuevo.getId() > 0);

        try {
            // Leer
            Pais leido = dao.buscarPorId(nuevo.getId());
            Assertions.assertNotNull(leido);
            Assertions.assertEquals("PaisPruebaMVC", leido.getNombre());

            List<Pais> porNombre = dao.buscarPorNombre("PaisPruebaMVC");
            Assertions.assertFalse(porNombre.isEmpty());

            // Actualizar
            leido.setCapital("NuevaCapital");
            leido.setPoblacion(654321L);
            Assertions.assertTrue(dao.actualizar(leido));

            Pais actualizado = dao.buscarPorId(nuevo.getId());
            Assertions.assertEquals("NuevaCapital", actualizado.getCapital());
            Assertions.assertEquals(654321L, actualizado.getPoblacion());
        } finally {
            // Eliminar
            Assertions.assertTrue(dao.eliminar(nuevo.getId()));
            Assertions.assertNull(dao.buscarPorId(nuevo.getId()));
        }
    }

    @Test
    void testImportacionCompletaFiltrosYReportesEstadisticos() {
        PaisController controller = new PaisController();

        ResultadoImportacion res = controller.importarPaises();
        Assertions.assertTrue(res.exito(), res.mensaje());
        Assertions.assertTrue(res.totalApi() >= 190);

        // Actividad 4: Búsqueda por región
        List<Pais> americas = controller.buscarEnBdPorRegion("América");
        List<Pais> europa = controller.buscarEnBdPorRegion("Europa");
        List<Pais> asia = controller.buscarEnBdPorRegion("Asia");
        List<Pais> africa = controller.buscarEnBdPorRegion("África");
        List<Pais> oceania = controller.buscarEnBdPorRegion("Oceanía");

        Assertions.assertFalse(americas.isEmpty());
        Assertions.assertFalse(europa.isEmpty());
        Assertions.assertFalse(asia.isEmpty());
        Assertions.assertFalse(africa.isEmpty());
        Assertions.assertFalse(oceania.isEmpty());

        // Actividad 5: Reportes estadísticos
        ReporteEstadistico rep = controller.generarReporteEstadistico();
        Assertions.assertTrue(rep.totalPaises() >= 190);
        Assertions.assertNotNull(rep.paisMasPoblado());
        Assertions.assertTrue(rep.paisMasPoblado().getPoblacion() > 1_000_000_000L);
        Assertions.assertNotNull(rep.regionConMasPaises());

        Map<String, Integer> porRegion = rep.paisesPorRegion();
        Assertions.assertTrue(porRegion.containsKey("Africa"));
        Assertions.assertTrue(porRegion.containsKey("Americas"));
        Assertions.assertTrue(porRegion.containsKey("Europe"));
    }

    @Test
    void testAutenticacionYVistaLogin() throws Exception {
        LoginController login = new LoginController();
        Assertions.assertTrue(login.autenticar("Monster", "Monster9"));
        Assertions.assertTrue(login.autenticar(new Usuario("Monster", "Monster9")));
        Assertions.assertFalse(login.autenticar("Monster", "incorrecta"));
        Assertions.assertFalse(login.autenticar("otro", "Monster9"));
        Assertions.assertFalse(login.autenticar(null, null));

        SwingUtilities.invokeAndWait(() -> {
            FrmLogin frmLogin = new FrmLogin();
            Assertions.assertTrue(frmLogin.getTitle().contains("Inicio de Sesión"));
            frmLogin.dispose();
        });
    }

    @Test
    void testInicializacionVistaSwing() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            FrmPais frm = new FrmPais("Monster");
            Assertions.assertTrue(frm.getTitle().contains("REST Countries"));
            frm.dispose();
        });
    }
}
