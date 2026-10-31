package ec.edu.monster.cliesc;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.view.DesktopView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class WS_Comercializadora_DesktopClient {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            AppControlador controlador = new AppControlador();
            DesktopView view = new DesktopView(controlador);
            view.setVisible(true);
        });
    }
}
