import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.FrmLogin;

/**
 * Punto de entrada de la aplicación MVC de Países.
 */
public class Main {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // Usa el Look & Feel por defecto si Nimbus no está disponible
        }

        SwingUtilities.invokeLater(() -> {
            FrmLogin login = new FrmLogin();
            login.setVisible(true);
        });
    }
}
