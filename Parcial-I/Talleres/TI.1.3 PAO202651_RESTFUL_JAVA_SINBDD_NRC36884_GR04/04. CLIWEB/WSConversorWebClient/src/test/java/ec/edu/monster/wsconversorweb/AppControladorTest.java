package ec.edu.monster.wsconversorweb;

import ec.edu.monster.wsconversorweb.model.ConfiguracionServidor;

import ec.edu.monster.wsconversorweb.controller.AppControlador;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class AppControladorTest {

    private final AppControlador control = new AppControlador();
    private boolean servidorDisponible = false;

    @BeforeEach
    public void verificarServidor() {
        try {
            URL url = new URL(ConfiguracionServidor.getUrlBase() + "/conversor/unidades");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(1500);
            servidorDisponible = (conn.getResponseCode() == 200);
        } catch (Exception e) {
            servidorDisponible = false;
        }
    }

    @Test
    @DisplayName("CLIWEB: Instanciación correcta del controlador")
    public void testControladorInstancia() {
        assertNotNull(control);
    }

    @Test
    @DisplayName("CLIWEB: Test Login con servidor activo")
    public void testLogin() {
        Assumptions.assumeTrue(servidorDisponible, "Servidor REST no disponible (ver servidor.properties)");
        String token = control.login("MONSTER", "MONSTER9");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("CLIWEB: Test Conversión Longitud 1000m -> 1km con servidor activo")
    public void testConvertirLongitud() {
        Assumptions.assumeTrue(servidorDisponible, "Servidor REST no disponible (ver servidor.properties)");
        double res = control.convertirLongitud(1000.0, "METRO", "KILOMETRO");
        assertEquals(1.0, res, 0.0001);
    }

    @Test
    @DisplayName("CLIWEB: Test Conversión Temperatura 100C -> 212F con servidor activo")
    public void testConvertirTemperatura() {
        Assumptions.assumeTrue(servidorDisponible, "Servidor REST no disponible (ver servidor.properties)");
        double res = control.convertirTemperatura(100.0, "CELSIUS", "FAHRENHEIT");
        assertEquals(212.0, res, 0.0001);
    }
}
