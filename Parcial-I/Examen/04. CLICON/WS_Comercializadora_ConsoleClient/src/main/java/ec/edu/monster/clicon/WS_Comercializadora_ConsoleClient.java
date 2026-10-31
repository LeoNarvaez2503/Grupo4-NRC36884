package ec.edu.monster.clicon;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.view.ConsolaView;

public class WS_Comercializadora_ConsoleClient {

    public static void main(String[] args) {
        AppControlador controlador = new AppControlador();
        ConsolaView vista = new ConsolaView(controlador);

        // Si se pasa el argumento --demo o --test, ejecuta la batería automática directa
        if (args.length > 0 && ("--demo".equalsIgnoreCase(args[0]) || "--test".equalsIgnoreCase(args[0]))) {
            vista.ejecutarBateriaCuatroClientes();
        } else {
            vista.iniciar();
        }
    }
}
