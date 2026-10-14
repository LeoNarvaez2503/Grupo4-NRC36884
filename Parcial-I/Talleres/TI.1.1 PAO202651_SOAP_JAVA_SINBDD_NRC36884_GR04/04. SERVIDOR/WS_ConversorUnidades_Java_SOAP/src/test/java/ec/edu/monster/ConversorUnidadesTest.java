package ec.edu.monster;

import ec.edu.monster.controladores.ws.WSConversorUnidades;
import ec.edu.monster.modelos.utilidades.enums.UnidadLongitud;
import ec.edu.monster.modelos.utilidades.enums.UnidadMasa;
import ec.edu.monster.modelos.utilidades.enums.UnidadTemperatura;
import ec.edu.monster.modelos.utilidades.mapeadores.UnidadMapper;
import ec.edu.monster.seguridad.AdministradorCredenciales;
import ec.edu.monster.seguridad.AdministradorToken;
import ec.edu.monster.seguridad.AdministradorTokens;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversorUnidadesTest {

    private WSConversorUnidades servicio;

    @BeforeEach
    void setUp() {
        AdministradorCredenciales.reiniciar();
        AdministradorToken.limpiar();
        servicio = new WSConversorUnidades();
    }

    @Test
    void testConversionLongitud() {
        // 1 metro = 100 centímetros
        double resultado = servicio.convertirLongitud(1.0, "METRO", "CENTIMETRO");
        assertEquals(100.0, resultado, 0.001);

        // 1000 metros = 1 kilómetro
        resultado = servicio.convertirLongitud(1000.0, "m", "km");
        assertEquals(1.0, resultado, 0.001);
    }

    @Test
    void testConversionMasa() {
        // 1 kilogramo = 1000 gramos
        double resultado = servicio.convertirMasa(1.0, "KILOGRAMO", "GRAMO");
        assertEquals(1000.0, resultado, 0.001);

        // 1 tonelada = 1000 kilogramos
        resultado = servicio.convertirMasa(1.0, "TONELADA", "KILOGRAMO");
        assertEquals(1000.0, resultado, 0.001);
    }

    @Test
    void testConversionTemperatura() {
        // 0 °C = 32 °F
        double resultado = servicio.convertirTemperatura(0.0, "CELSIUS", "FAHRENHEIT");
        assertEquals(32.0, resultado, 0.001);

        // 100 °C = 212 °F
        resultado = servicio.convertirTemperatura(100.0, "c", "f");
        assertEquals(212.0, resultado, 0.001);

        // 0 °C = 273.15 K
        resultado = servicio.convertirTemperatura(0.0, "CELSIUS", "KELVIN");
        assertEquals(273.15, resultado, 0.001);
    }

    @Test
    void testLoginYCredenciales() {
        // Login exitoso con la única credencial válida: MONSTER / MONSTER9
        String token = servicio.login("MONSTER", "MONSTER9");
        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(AdministradorToken.validarToken(token));
        assertTrue(AdministradorTokens.validarToken(token));

        // Login exitoso insensible a mayúsculas en el usuario
        String token2 = servicio.login("monster", "MONSTER9");
        assertNotNull(token2);
        assertTrue(AdministradorToken.validarToken(token2));

        // Login fallido con contraseña incorrecta (las contraseñas sí son sensibles a mayúsculas)
        assertThrows(RuntimeException.class, () -> servicio.login("MONSTER", "clave_invalida"));
        assertThrows(RuntimeException.class, () -> servicio.login("MONSTER", "monster9"));

        // Login fallido con credenciales anteriores (ya no deben existir)
        assertThrows(RuntimeException.class, () -> servicio.login("admin", "admin"));
        assertThrows(RuntimeException.class, () -> servicio.login("espe", "espe"));
        assertThrows(RuntimeException.class, () -> servicio.login("usuario", "1234"));
    }

    @Test
    void testCambiarContrasenia() {
        // Validar credencial predeterminada
        assertTrue(AdministradorCredenciales.validarUsuario("MONSTER", "MONSTER9"));

        // Cambiar contraseña de MONSTER
        AdministradorCredenciales.cambiarContrasenia("MONSTER", "MONSTER9", "NUEVA_CLAVE_99");
        assertTrue(AdministradorCredenciales.validarUsuario("MONSTER", "NUEVA_CLAVE_99"));
        assertFalse(AdministradorCredenciales.validarUsuario("MONSTER", "MONSTER9"));

        // Restaurar estado
        AdministradorCredenciales.reiniciar();
        assertTrue(AdministradorCredenciales.validarUsuario("MONSTER", "MONSTER9"));
    }

    @Test
    void testUnidadMapper() {
        assertEquals(UnidadLongitud.METRO, UnidadMapper.toLongitud("m"));
        assertEquals(UnidadLongitud.KILOMETRO, UnidadMapper.toLongitud("KILOMETROS"));
        assertEquals(UnidadMasa.KILOGRAMO, UnidadMapper.toMasa("kg"));
        assertEquals(UnidadTemperatura.CELSIUS, UnidadMapper.toTemperatura("celsius"));

        assertThrows(IllegalArgumentException.class, () -> UnidadMapper.toLongitud("unidad_falsa"));
    }
}
