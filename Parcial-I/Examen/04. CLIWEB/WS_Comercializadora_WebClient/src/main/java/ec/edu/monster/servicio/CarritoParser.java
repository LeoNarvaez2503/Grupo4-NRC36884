package ec.edu.monster.servicio;

import ec.edu.monster.servicio.FacturacionService.ItemCarrito;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Convierte lo que envian los clientes (JSON o query-string) en una lista de ItemCarrito.
 *  - JSON:  {"items":[{"codTelefono":1,"cantidad":2},{"codTelefono":5,"cantidad":1}], ...}
 *  - Query: items=1:2,5:1   (codTelefono:cantidad separados por coma)
 *  - Compatibilidad: {"codTelefono":1} equivale a un carrito con 1 unidad de ese telefono.
 */
public final class CarritoParser {

    private CarritoParser() {}

    public static List<ItemCarrito> desdeBody(Map<String, Object> body) {
        List<ItemCarrito> lista = new ArrayList<>();
        if (body == null) return lista;

        Object raw = body.get("items");
        if (raw instanceof List<?> l) {
            for (Object o : l) {
                if (o instanceof Map<?, ?> m) {
                    int cod = numero(m.get("codTelefono"), 0);
                    int cant = numero(m.get("cantidad"), 1);
                    if (cod > 0) lista.add(new ItemCarrito(cod, cant));
                }
            }
        }
        if (lista.isEmpty()) {
            int cod = numero(body.get("codTelefono"), 0);
            if (cod > 0) lista.add(new ItemCarrito(cod, 1));
        }
        return lista;
    }

    public static List<ItemCarrito> desdeQuery(String items, int codTelefonoUnico) {
        List<ItemCarrito> lista = new ArrayList<>();
        if (items != null && !items.isBlank()) {
            for (String par : items.split(",")) {
                String[] p = par.trim().split(":");
                int cod = numero(p[0], 0);
                int cant = p.length > 1 ? numero(p[1], 1) : 1;
                if (cod > 0) lista.add(new ItemCarrito(cod, cant));
            }
        }
        if (lista.isEmpty() && codTelefonoUnico > 0) {
            lista.add(new ItemCarrito(codTelefonoUnico, 1));
        }
        return lista;
    }

    private static int numero(Object o, int def) {
        if (o instanceof Number n) return n.intValue();
        if (o != null) {
            try { return Integer.parseInt(o.toString().trim()); } catch (NumberFormatException ignored) {}
        }
        return def;
    }
}
