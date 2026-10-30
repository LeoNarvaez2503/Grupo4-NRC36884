package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.UsuarioSesion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 * Capa Vista (Patrón MVC Tradicional): Ventana de Autenticación de Escritorio Swing.
 */
public class LoginView extends JFrame {

    private final AppControlador controlador;
    private final JTextField txtUsuario = new JTextField("Monster", 18);
    private final JPasswordField txtClave = new JPasswordField("Monster9", 18);
    private final JComboBox<String> cmbVentanilla = new JComboBox<>(new String[]{
        "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4"
    });
    private final JButton btnIngresar = new JButton("Ingresar al Sistema");
    private final JLabel lblEstado = new JLabel("Listo para conectar al servidor", SwingConstants.CENTER);

    public LoginView(AppControlador controlador) {
        this.controlador = controlador;
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("EurekaBank - Autenticación Centralizada (GR04 - ec.edu.monster)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(470, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(15, 23, 42));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(new Color(15, 23, 42));
        header.setBorder(BorderFactory.createEmptyBorder(22, 20, 14, 20));

        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        URL imgUrl = getClass().getClassLoader().getResource("images/sullivan.png");
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage().getScaledInstance(105, 105, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        }
        header.add(lblLogo, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("EUREKABANK - TERMINAL BANCARIA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblTitulo.setForeground(new Color(56, 189, 248));
        header.add(lblTitulo, BorderLayout.CENTER);

        JLabel lblSub = new JLabel("Patrón MVC Tradicional | Dominio: ec.edu.monster", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184));
        header.add(lblSub, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font fontLbl = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel l1 = new JLabel("Usuario (Monster / cajero1..4 / gerente1):");
        l1.setFont(fontLbl);
        card.add(l1, gbc);

        gbc.gridy = 1;
        txtUsuario.setFont(fontInput);
        txtUsuario.setPreferredSize(new Dimension(280, 34));
        card.add(txtUsuario, gbc);

        gbc.gridy = 2;
        JLabel l2 = new JLabel("Contraseña:");
        l2.setFont(fontLbl);
        card.add(l2, gbc);

        gbc.gridy = 3;
        txtClave.setFont(fontInput);
        txtClave.setPreferredSize(new Dimension(280, 34));
        card.add(txtClave, gbc);

        gbc.gridy = 4;
        JLabel l3 = new JLabel("Terminal / Ventanilla de Operación:");
        l3.setFont(fontLbl);
        card.add(l3, gbc);

        gbc.gridy = 5;
        cmbVentanilla.setFont(fontInput);
        cmbVentanilla.setPreferredSize(new Dimension(280, 34));
        card.add(cmbVentanilla, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(16, 6, 6, 6);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(37, 99, 235));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(280, 40));
        btnIngresar.addActionListener(e -> autenticar());
        card.add(btnIngresar, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(6, 6, 2, 6);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(100, 116, 139));
        lblEstado.setText("Endpoint: " + controlador.getEndpointActual());
        card.add(lblEstado, gbc);

        root.add(card, BorderLayout.CENTER);
        setContentPane(root);
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void autenticar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();
        int ventanilla = cmbVentanilla.getSelectedIndex() + 1;

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete usuario y contraseña.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnIngresar.setEnabled(false);
        lblEstado.setText("Validando credenciales con el servidor...");

        new SwingWorker<UsuarioSesion, Void>() {
            @Override
            protected UsuarioSesion doInBackground() {
                UsuarioSesion s = controlador.iniciarSesion(usuario, clave);
                controlador.setVentanillaActiva(ventanilla);
                return s;
            }

            @Override
            protected void done() {
                btnIngresar.setEnabled(true);
                try {
                    UsuarioSesion s = get();
                    if (s != null && s.isAutenticado()) {
                        DashboardView dash = new DashboardView(controlador);
                        dash.setVisible(true);
                        dispose();
                    } else {
                        lblEstado.setText("Autenticación rechazada");
                        JOptionPane.showMessageDialog(LoginView.this,
                                s != null ? s.getMensaje() : "Credenciales inválidas.",
                                "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    lblEstado.setText("Error de red con el servidor");
                    JOptionPane.showMessageDialog(LoginView.this,
                            "No se pudo conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
