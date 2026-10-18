package ec.edu.monster.wsconversormov;

import ec.edu.monster.wsconversormov.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppControladorTest {

    @Test
    public void testUnidadLongitud() {
        assertEquals(5, UnidadLongitud.values().length);
        assertEquals("Milímetro (mm)", UnidadLongitud.MILIMETRO.getEtiqueta());
        assertEquals("Metro (m)", UnidadLongitud.METRO.getEtiqueta());
    }

    @Test
    public void testUnidadMasa() {
        assertEquals(5, UnidadMasa.values().length);
        assertEquals("Gramo (g)", UnidadMasa.GRAMO.getEtiqueta());
        assertEquals("Kilogramo (kg)", UnidadMasa.KILOGRAMO.getEtiqueta());
    }

    @Test
    public void testUnidadTemperatura() {
        assertEquals(4, UnidadTemperatura.values().length);
        assertEquals("Celsius (°C)", UnidadTemperatura.CELSIUS.getEtiqueta());
        assertEquals("Fahrenheit (°F)", UnidadTemperatura.FAHRENHEIT.getEtiqueta());
    }

    @Test
    public void testUsuarioSession() {
        UsuarioSession session = new UsuarioSession("MONSTER", "TOKEN-TEST-123");
        assertEquals("MONSTER", session.getUsuario());
        assertEquals("TOKEN-TEST-123", session.getToken());
        assertNotNull(session.getHoraIngreso());
    }

    @Test
    public void testConfiguracionConexion() {
        String original = ConfiguracionConexion.getEndpointUrl();
        assertNotNull(original);

        ConfiguracionConexion.setEndpointUrl("http://192.168.1.50:8080/WS_Conversor");
        assertEquals("http://192.168.1.50:8080/WS_Conversor", ConfiguracionConexion.getEndpointUrl());

        // Restaurar
        ConfiguracionConexion.setEndpointUrl(original);
    }
}
