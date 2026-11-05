package ec.edu.monster.clicon;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.view.ConsolaView;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class WS_Comercializadora_ConsoleClient {

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {}

        AppControlador controlador = new AppControlador();
        ConsolaView vista = new ConsolaView(controlador);

        // Si se pasa el argumento --demo o --test, ejecuta la bateria automatica directa
        if (args.length > 0 && ("--demo".equalsIgnoreCase(args[0]) || "--test".equalsIgnoreCase(args[0]))) {
            vista.ejecutarBateriaCuatroClientes();
        } else {
            vista.iniciar();
        }
    }
}
