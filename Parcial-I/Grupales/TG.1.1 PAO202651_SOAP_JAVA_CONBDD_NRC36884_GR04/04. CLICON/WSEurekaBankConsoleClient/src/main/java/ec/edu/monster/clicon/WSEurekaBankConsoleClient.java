package ec.edu.monster.clicon;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.view.ConsolaView;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Punto de entrada del Cliente Consola Java de EurekaBank (Grupo 04).
 * Dominio: ec.edu.monster | Arquitectura MVC Tradicional.
 */
public class WSEurekaBankConsoleClient {

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
        }

        AppControlador controlador = new AppControlador();
        ConsolaView vista = new ConsolaView(controlador);
        if (args != null && args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            vista.ejecutarDiagnosticoAutomatico();
        } else {
            vista.iniciar();
        }
    }
}