package vista;

import controlador.LoginController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Vista de inicio de sesión (Login) del sistema MVC de Países.
 */
public class FrmLogin extends JFrame {

    private final LoginController loginController;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JCheckBox chkMostrarPassword;
    private JLabel lblMensajeError;
    private JButton btnIngresar;
    private JButton btnSalir;
    private char defaultEchoChar;

    public FrmLogin() {
        this.loginController = new LoginController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Inicio de Sesión — Gestión de Países (MVC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Color.WHITE);

        root.add(crearPanelImagen(), BorderLayout.WEST);
        root.add(crearPanelFormulario(), BorderLayout.CENTER);

        setContentPane(root);
        getRootPane().setDefaultButton(btnIngresar);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelImagen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(14, 116, 144));
        panel.setPreferredSize(new Dimension(300, 360));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = cargarImagenLogin(268, 280);
        if (icon != null) {
            lblImagen.setIcon(icon);
        } else {
            lblImagen.setText("API Países MVC");
            lblImagen.setForeground(Color.WHITE);
            lblImagen.setFont(new Font("Segoe UI", Font.BOLD, 20));
        }

        JLabel lblPie = new JLabel("Bienvenido al Sistema", SwingConstants.CENTER);
        lblPie.setForeground(new Color(224, 242, 254));
        lblPie.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPie.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(lblImagen, BorderLayout.CENTER);
        panel.add(lblPie, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(360, 360));
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(26, 54, 93));
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Consumo de API REST • Java + MySQL + MVC");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 4, 16, 4);
        panel.add(lblSubtitulo, gbc);

        gbc.insets = new Insets(4, 4, 4, 4);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(260, 34));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridy = 3;
        panel.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 4, 4, 4);
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(260, 34));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        defaultEchoChar = txtPassword.getEchoChar();
        gbc.gridy = 5;
        gbc.insets = new Insets(4, 4, 4, 4);
        panel.add(txtPassword, gbc);

        chkMostrarPassword = new JCheckBox("Mostrar contraseña");
        chkMostrarPassword.setBackground(Color.WHITE);
        chkMostrarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkMostrarPassword.addActionListener(e -> {
            txtPassword.setEchoChar(chkMostrarPassword.isSelected() ? (char) 0 : defaultEchoChar);
        });
        gbc.gridy = 6;
        panel.add(chkMostrarPassword, gbc);

        lblMensajeError = new JLabel(" ");
        lblMensajeError.setForeground(new Color(220, 38, 38));
        lblMensajeError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 7;
        gbc.insets = new Insets(6, 4, 10, 4);
        panel.add(lblMensajeError, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(Color.WHITE);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(37, 99, 235));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(120, 36));
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> iniciarSesion());

        btnSalir = new JButton("Salir");
        btnSalir.setBackground(new Color(100, 116, 139));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(120, 36));
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);

        gbc.gridy = 8;
        gbc.insets = new Insets(4, 4, 0, 4);
        panel.add(panelBotones, gbc);

        return panel;
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensajeError.setText("Ingrese usuario y contraseña.");
            return;
        }

        if (loginController.autenticar(usuario, password)) {
            lblMensajeError.setText(" ");
            FrmPais frmPais = new FrmPais(usuario);
            frmPais.setVisible(true);
            dispose();
        } else {
            lblMensajeError.setText("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private ImageIcon cargarImagenLogin(int maxW, int maxH) {
        BufferedImage img = null;
        try (InputStream is = getClass().getResourceAsStream("/images/login.jpeg")) {
            if (is != null) {
                img = ImageIO.read(is);
            }
        } catch (IOException ignored) {
        }
        if (img == null) {
            File localFile = new File("foto para el login.jpeg");
            if (localFile.exists()) {
                try {
                    img = ImageIO.read(localFile);
                } catch (IOException ignored) {
                }
            }
        }
        if (img == null) {
            return null;
        }
        double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
        int w = Math.max(1, (int) Math.round(img.getWidth() * scale));
        int h = Math.max(1, (int) Math.round(img.getHeight() * scale));
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
