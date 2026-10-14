package ec.edu.monster.wsconversorweb;

import ec.edu.monster.wsconversorweb.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppControladorTest {

    @Test
    @DisplayName("Debe validar modelo UsuarioSession")
    void testUsuarioSession() {
        UsuarioSession usuario = new UsuarioSession("MONSTER", "TOKEN-TEST-999");
        assertEquals("MONSTER", usuario.getUsuario());
        assertEquals("TOKEN-TEST-999", usuario.getToken());
        assertNotNull(usuario.getHoraIngreso());

        usuario.setUsuario("ADMIN");
        assertEquals("ADMIN", usuario.getUsuario());
    }

    @Test
    @DisplayName("Debe validar modelo HistorialItem")
    void testHistorialItem() {
        HistorialItem item = new HistorialItem("LONGITUD", 10.0, "METRO", 1000.0, "CENTIMETRO");
        assertEquals("LONGITUD", item.getMagnitud());
        assertEquals(10.0, item.getValorOrigen());
        assertEquals("METRO", item.getUnidadOrigen());
        assertEquals(1000.0, item.getValorDestino());
        assertEquals("CENTIMETRO", item.getUnidadDestino());
        assertNotNull(item.getHora());
        assertNotNull(item.getId());
    }

    @Test
    @DisplayName("Debe validar enums de magnitudes (Longitud, Masa, Temperatura)")
    void testEnumsMagnitudes() {
        assertEquals(5, UnidadLongitud.values().length);
        assertEquals("Metro (m)", UnidadLongitud.METRO.getEtiqueta());

        assertEquals(5, UnidadMasa.values().length);
        assertEquals("Kilogramo (kg)", UnidadMasa.KILOGRAMO.getEtiqueta());

        assertEquals(4, UnidadTemperatura.values().length);
        assertEquals("Celsius (°C)", UnidadTemperatura.CELSIUS.getEtiqueta());
    }
}
