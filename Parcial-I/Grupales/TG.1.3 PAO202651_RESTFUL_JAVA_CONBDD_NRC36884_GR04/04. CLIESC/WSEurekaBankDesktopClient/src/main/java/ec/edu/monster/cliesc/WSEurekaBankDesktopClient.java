package ec.edu.monster.cliesc;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada del Cliente Escritorio Swing de EurekaBank (Grupo 04).
 * Dominio: ec.edu.monster | Arquitectura MVC Tradicional.
 */
public class WSEurekaBankDesktopClient {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            AppControlador controlador = new AppControlador();
            LoginView vista = new LoginView(controlador);
            vista.setVisible(true);
        });
    }
}
