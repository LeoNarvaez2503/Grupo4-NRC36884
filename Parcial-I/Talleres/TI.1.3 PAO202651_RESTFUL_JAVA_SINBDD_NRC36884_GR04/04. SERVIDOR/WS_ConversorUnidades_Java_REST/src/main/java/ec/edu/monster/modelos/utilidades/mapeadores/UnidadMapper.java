package ec.edu.monster.modelos.utilidades.mapeadores;

import ec.edu.monster.modelos.utilidades.enums.UnidadLongitud;
import ec.edu.monster.modelos.utilidades.enums.UnidadMasa;
import ec.edu.monster.modelos.utilidades.enums.UnidadTemperatura;
import java.text.Normalizer;

/**
 * Mapeador para convertir cadenas de texto a los respectivos enumeradores de unidad.
 * Soporta nombres en mayúsculas/minúsculas, con/sin tildes, plurales y abreviaturas comunes.
 * 
 * @author MeatPuppets
 */
public class UnidadMapper {

    private UnidadMapper() {
        // Constructor privado para clase utilitaria
    }

    private static String normalizar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad no puede estar vacía.");
        }
        String limpio = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase();
        return limpio;
    }

    /**
     * Mapea una cadena de texto a UnidadLongitud.
     * 
     * @param valor Cadena representativa de la unidad.
     * @return UnidadLongitud correspondiente.
     */
    public static UnidadLongitud toLongitud(String valor) {
        String u = normalizar(valor);
        return switch (u) {
            case "MILIMETRO", "MILIMETROS", "MM" -> UnidadLongitud.MILIMETRO;
            case "CENTIMETRO", "CENTIMETROS", "CM" -> UnidadLongitud.CENTIMETRO;
            case "METRO", "METROS", "M" -> UnidadLongitud.METRO;
            case "KILOMETRO", "KILOMETROS", "KM" -> UnidadLongitud.KILOMETRO;
            case "YARDA", "YARDAS", "YD" -> UnidadLongitud.YARDA;
            default -> {
                try {
                    yield UnidadLongitud.valueOf(u);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unidad de longitud no soportada: " + valor);
                }
            }
        };
    }

    /**
     * Mapea una cadena de texto a UnidadMasa.
     * 
     * @param valor Cadena representativa de la unidad.
     * @return UnidadMasa correspondiente.
     */
    public static UnidadMasa toMasa(String valor) {
        String u = normalizar(valor);
        return switch (u) {
            case "MILIGRAMO", "MILIGRAMOS", "MG" -> UnidadMasa.MILIGRAMO;
            case "GRAMO", "GRAMOS", "G" -> UnidadMasa.GRAMO;
            case "KILOGRAMO", "KILOGRAMOS", "KG" -> UnidadMasa.KILOGRAMO;
            case "TONELADA", "TONELADAS", "T" -> UnidadMasa.TONELADA;
            case "ONZA", "ONZAS", "OZ" -> UnidadMasa.ONZA;
            default -> {
                try {
                    yield UnidadMasa.valueOf(u);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unidad de masa no soportada: " + valor);
                }
            }
        };
    }

    /**
     * Mapea una cadena de texto a UnidadTemperatura.
     * 
     * @param valor Cadena representativa de la unidad.
     * @return UnidadTemperatura correspondiente.
     */
    public static UnidadTemperatura toTemperatura(String valor) {
        String u = normalizar(valor);
        return switch (u) {
            case "CELSIUS", "CENTIGRADO", "CENTIGRADOS", "C", "GRADOS CELSIUS" -> UnidadTemperatura.CELSIUS;
            case "FAHRENHEIT", "F", "GRADOS FAHRENHEIT" -> UnidadTemperatura.FAHRENHEIT;
            case "KELVIN", "K", "GRADOS KELVIN" -> UnidadTemperatura.KELVIN;
            case "RANKINE", "R", "GRADOS RANKINE" -> UnidadTemperatura.RANKINE;
            default -> {
                try {
                    yield UnidadTemperatura.valueOf(u);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unidad de temperatura no soportada: " + valor);
                }
            }
        };
    }
}
