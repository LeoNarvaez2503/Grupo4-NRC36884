package ec.edu.monster.seguridad;

/**
 * Adaptador/Alias para AdministradorToken para compatibilidad con ManejadorAutenticacion.
 * 
 * @author MeatPuppets
 */
public class AdministradorTokens {

    private AdministradorTokens() {
        // Constructor privado para clase utilitaria
    }

    public static String generarToken() {
        return AdministradorToken.generarToken();
    }

    public static boolean validarToken(String token) {
        return AdministradorToken.validarToken(token);
    }

    public static void invalidarToken(String token) {
        AdministradorToken.invalidarToken(token);
    }

    public static void limpiar() {
        AdministradorToken.limpiar();
    }
}
