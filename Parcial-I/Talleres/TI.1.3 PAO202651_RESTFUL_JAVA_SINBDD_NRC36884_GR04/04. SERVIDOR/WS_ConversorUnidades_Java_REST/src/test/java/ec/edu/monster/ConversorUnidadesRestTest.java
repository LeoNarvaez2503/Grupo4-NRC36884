package ec.edu.monster;

import ec.edu.monster.controladores.rest.AutenticacionResource;
import ec.edu.monster.controladores.rest.ConversorResource;
import ec.edu.monster.modelos.dto.CambioContraseniaRequest;
import ec.edu.monster.modelos.dto.ConversionRequest;
import ec.edu.monster.modelos.dto.ConversionResponse;
import ec.edu.monster.modelos.dto.LoginRequest;
import ec.edu.monster.modelos.dto.LoginResponse;
import ec.edu.monster.seguridad.AdministradorCredenciales;
import ec.edu.monster.seguridad.AdministradorToken;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConversorUnidadesRestTest {

    private ConversorResource conversorResource;
    private AutenticacionResource authResource;

    @BeforeEach
    public void setUp() {
        conversorResource = new ConversorResource();
        authResource = new AutenticacionResource();
        AdministradorCredenciales.reiniciar();
    }

    @Test
    @DisplayName("Test Conversión Longitud: 1000 Metros -> 1 Kilómetro")
    public void testConvertirLongitud() {
        Response resp = conversorResource.convertirLongitudGet(1000.0, "METRO", "KILOMETRO");
        assertEquals(200, resp.getStatus());
        ConversionResponse body = (ConversionResponse) resp.getEntity();
        assertTrue(body.isExito());
        assertEquals(1.0, body.getValorFinal(), 0.0001);
    }

    @Test
    @DisplayName("Test Conversión Longitud POST: 5000 Milímetros -> 5 Metros")
    public void testConvertirLongitudPost() {
        ConversionRequest req = new ConversionRequest(5000.0, "MILIMETROS", "METROS");
        Response resp = conversorResource.convertirLongitudPost(req);
        assertEquals(200, resp.getStatus());
        ConversionResponse body = (ConversionResponse) resp.getEntity();
        assertTrue(body.isExito());
        assertEquals(5.0, body.getValorFinal(), 0.0001);
    }

    @Test
    @DisplayName("Test Conversión Masa: 5000 Gramos -> 5 Kilogramos")
    public void testConvertirMasa() {
        Response resp = conversorResource.convertirMasaGet(5000.0, "GRAMO", "KILOGRAMO");
        assertEquals(200, resp.getStatus());
        ConversionResponse body = (ConversionResponse) resp.getEntity();
        assertTrue(body.isExito());
        assertEquals(5.0, body.getValorFinal(), 0.0001);
    }

    @Test
    @DisplayName("Test Conversión Temperatura: 100 Celsius -> 212 Fahrenheit")
    public void testConvertirTemperatura() {
        Response resp = conversorResource.convertirTemperaturaGet(100.0, "CELSIUS", "FAHRENHEIT");
        assertEquals(200, resp.getStatus());
        ConversionResponse body = (ConversionResponse) resp.getEntity();
        assertTrue(body.isExito());
        assertEquals(212.0, body.getValorFinal(), 0.0001);
    }

    @Test
    @DisplayName("Test Login con Credenciales Oficiales (MONSTER / MONSTER9)")
    public void testLoginExitoso() {
        LoginRequest req = new LoginRequest("MONSTER", "MONSTER9");
        Response resp = authResource.loginJson(req);
        assertEquals(200, resp.getStatus());
        LoginResponse body = (LoginResponse) resp.getEntity();
        assertTrue(body.isExito());
        assertNotNull(body.getToken());
        assertTrue(AdministradorToken.validarToken(body.getToken()));
    }

    @Test
    @DisplayName("Test Login Fallido con Credenciales Incorrectas")
    public void testLoginFallido() {
        LoginRequest req = new LoginRequest("INCORRECTO", "PASSWORD_MALA");
        Response resp = authResource.loginJson(req);
        assertEquals(401, resp.getStatus());
        LoginResponse body = (LoginResponse) resp.getEntity();
        assertFalse(body.isExito());
        assertNull(body.getToken());
    }

    @Test
    @DisplayName("Test Cambio de Contraseña y Login posterior")
    public void testCambioContrasenia() {
        CambioContraseniaRequest req = new CambioContraseniaRequest("MONSTER9", "NUEVA_CLAVE_2026");
        Response resp = authResource.cambiarContrasenia(req);
        assertEquals(200, resp.getStatus());

        // La clave vieja ya no debe funcionar
        Response respVieja = authResource.loginJson(new LoginRequest("MONSTER", "MONSTER9"));
        assertEquals(401, respVieja.getStatus());

        // La clave nueva sí debe funcionar
        Response respNueva = authResource.loginJson(new LoginRequest("MONSTER", "NUEVA_CLAVE_2026"));
        assertEquals(200, respNueva.getStatus());
    }
}
