package ec.edu.monster.wsconversorcon;

import ec.edu.monster.wsconversorcon.model.UnidadLongitud;
import ec.edu.monster.wsconversorcon.model.UnidadMasa;
import ec.edu.monster.wsconversorcon.model.UnidadTemperatura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppControladorTest {

    @Test
    @DisplayName("Debe validar cantidad y etiquetas de UnidadLongitud")
    void testUnidadLongitud() {
        assertEquals(5, UnidadLongitud.values().length);
        assertEquals("Milímetro (mm)", UnidadLongitud.MILIMETRO.getEtiqueta());
        assertEquals("Metro (m)", UnidadLongitud.METRO.getEtiqueta());
    }

    @Test
    @DisplayName("Debe validar cantidad y etiquetas de UnidadMasa")
    void testUnidadMasa() {
        assertEquals(5, UnidadMasa.values().length);
        assertEquals("Gramo (g)", UnidadMasa.GRAMO.getEtiqueta());
        assertEquals("Kilogramo (kg)", UnidadMasa.KILOGRAMO.getEtiqueta());
    }

    @Test
    @DisplayName("Debe validar cantidad y etiquetas de UnidadTemperatura")
    void testUnidadTemperatura() {
        assertEquals(4, UnidadTemperatura.values().length);
        assertEquals("Celsius (°C)", UnidadTemperatura.CELSIUS.getEtiqueta());
        assertEquals("Fahrenheit (°F)", UnidadTemperatura.FAHRENHEIT.getEtiqueta());
    }
}
