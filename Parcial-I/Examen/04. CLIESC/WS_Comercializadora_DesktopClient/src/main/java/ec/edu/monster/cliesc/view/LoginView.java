package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Ventana de Autenticación Segura (Swing).
 * Diseño Split-Card con Mascota Sullivan en alta definición.
 * Credenciales oficiales:
 * Usuario: Monster
 * Contraseña: Monster9
 * NRC: 36884 | Grupo 04
 */
public class LoginView extends JFrame {

    private final AppControlador controlador;

    private static final Color COLOR_NAVY_DARK = new Color(9, 37, 64);
    private static final Color COLOR_PRIMARY = new Color(15, 76, 129);
    private static final Color COLOR_ACCENT = new Color(245, 158, 11);
    private static final Color COLOR_BG_PANEL = new Color(248, 250, 252);
    private static final Color COLOR_BORDER = new Color(226, 232, 240);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    private static final Color COLOR_DANGER = new Color(239, 68, 68);

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JLabel lblError;
    private Image imgSullivan = null;

    public LoginView(AppControlador controlador) {
        this.controlador = controlador;
        initUI();
    }

    private Image getImagenSullivan() {
        if (imgSullivan != null) return imgSullivan;
        try {
            java.net.URL resUrl = getClass().getResource("/images/sullivan.png");
            if (resUrl != null) {
                imgSullivan = ImageIO.read(resUrl);
                return imgSullivan;
            }
        } catch (Exception ignored) {}
        String[] posiblesRutas = {
            "images/sullivan.png",
            "src/main/resources/images/sullivan.png",
            "target/classes/images/sullivan.png",
            "../04. CLIWEB/WS_Comercializadora_WebClient/src/main/webapp/images/sullivan.png",
            "/tmp/sullivan_perfect.png"
        };
        for (String r : posiblesRutas) {
            try {
                File f = new File(r);
                if (f.exists()) {
                    imgSullivan = ImageIO.read(f);
                    return imgSullivan;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private void initUI() {
        setTitle("Iniciar Sesión — CelStore & BanQuito (Grupo 04 - Monster)");
        setSize(780, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelRoot = new JPanel(new GridLayout(1, 2));

        // =====================================================================
        // PANEL IZQUIERDO: SULLIVAN FEATURE SHOWCASE (GRADIENTE + IMAGEN ALTA DEF)
        // =====================================================================
        JPanel panelIzquierdo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();

                // 1. Fondo degradado corporativo
                GradientPaint gp = new GradientPaint(0, 0, COLOR_NAVY_DARK, w, h, COLOR_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // 2. Línea divisoria dorada al borde derecho
                g2.setColor(COLOR_ACCENT);
                g2.fillRect(w - 3, 0, 3, h);

                // 3. Dibujar a Sullivan nítido y centrado en la zona media
                Image sullivan = getImagenSullivan();
                if (sullivan != null) {
                    int maxH = 260;
                    int imgH = Math.min(maxH, sullivan.getHeight(null));
                    int imgW = (int) (sullivan.getWidth(null) * ((double) imgH / sullivan.getHeight(null)));
                    int x = (w - imgW) / 2;
                    int y = 140;
                    g2.drawImage(sullivan, x, y, imgW, imgH, null);
                }
                g2.dispose();
            }
        };
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(new EmptyBorder(32, 24, 24, 24));

        JLabel lblBadge = new JLabel("★ GRUPO 04 - MONSTER");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadge.setForeground(COLOR_ACCENT);
        lblBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("CelStore & BanQuito");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Facturación & Crédito Directo (16.5% Anual)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(203, 213, 225));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelIzquierdo.add(lblBadge);
        panelIzquierdo.add(Box.createVerticalStrut(6));
        panelIzquierdo.add(lblTitulo);
        panelIzquierdo.add(Box.createVerticalStrut(2));
        panelIzquierdo.add(lblSub);

        // Espacio para la imagen de Sullivan
        panelIzquierdo.add(Box.createVerticalGlue());

        // Footer con nombre de la mascota
        JPanel footerMascota = new JPanel();
        footerMascota.setLayout(new BoxLayout(footerMascota, BoxLayout.Y_AXIS));
        footerMascota.setOpaque(false);
        footerMascota.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMascota = new JLabel("James P. Sullivan");
        lblMascota.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMascota.setForeground(Color.WHITE);
        lblMascota.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRol = new JLabel("Mascota Oficial Grupo 04 • NRC: 36884");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRol.setForeground(new Color(148, 163, 184));
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerMascota.add(lblMascota);
        footerMascota.add(lblRol);
        panelIzquierdo.add(footerMascota);

        panelRoot.add(panelIzquierdo);

        // =====================================================================
        // PANEL DERECHO: FORMULARIO DE INICIO DE SESIÓN
        // =====================================================================
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(new EmptyBorder(38, 36, 38, 36));

        JLabel lblFormTitulo = new JLabel("Iniciar Sesión");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitulo.setForeground(COLOR_NAVY_DARK);
        lblFormTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFormSub = new JLabel("Ingrese sus credenciales para acceder al sistema.");
        lblFormSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFormSub.setForeground(COLOR_TEXT_MUTED);
        lblFormSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblError.setForeground(COLOR_DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelDerecho.add(lblFormTitulo);
        panelDerecho.add(Box.createVerticalStrut(4));
        panelDerecho.add(lblFormSub);
        panelDerecho.add(Box.createVerticalStrut(12));
        panelDerecho.add(lblError);
        panelDerecho.add(Box.createVerticalStrut(12));

        // Campo Usuario
        JLabel lblUser = new JLabel("Nombre de Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(51, 65, 85));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField("Monster");
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelDerecho.add(lblUser);
        panelDerecho.add(Box.createVerticalStrut(4));
        panelDerecho.add(txtUsuario);
        panelDerecho.add(Box.createVerticalStrut(14));

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña de Acceso:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(51, 65, 85));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtClave = new JPasswordField("");
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtClave.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelDerecho.add(lblPass);
        panelDerecho.add(Box.createVerticalStrut(4));
        panelDerecho.add(txtClave);
        panelDerecho.add(Box.createVerticalStrut(20));

        // Botón Iniciar Sesión
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> autenticar());

        panelDerecho.add(btnLogin);
        panelDerecho.add(Box.createVerticalStrut(20));

        // Hint de Credenciales Requeridas
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.Y_AXIS));
        hintPanel.setBackground(COLOR_BG_PANEL);
        hintPanel.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        hintPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        hintPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHint1 = new JLabel("Credenciales Oficiales Requeridas:");
        lblHint1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblHint1.setForeground(COLOR_TEXT_MUTED);
        lblHint1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHint2 = new JLabel("Usuario: Monster  |  Contraseña: Monster9");
        lblHint2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblHint2.setForeground(COLOR_NAVY_DARK);
        lblHint2.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintPanel.add(lblHint1);
        hintPanel.add(Box.createVerticalStrut(2));
        hintPanel.add(lblHint2);

        panelDerecho.add(hintPanel);

        // Botón Configuración de IP / Servidor Remoto
        JButton btnConfigIP = new JButton("⚙️ Conectar a otro equipo (IP: " + controlador.getConfiguracion().getHost() + ":" + controlador.getConfiguracion().getPort() + ")");
        btnConfigIP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnConfigIP.setForeground(COLOR_PRIMARY);
        btnConfigIP.setContentAreaFilled(false);
        btnConfigIP.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));
        btnConfigIP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfigIP.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfigIP.addActionListener(e -> {
            new DialogoConfigurarIP(this, controlador.getConfiguracion(), () -> {
                btnConfigIP.setText("⚙️ Conectar a otro equipo (IP: " + controlador.getConfiguracion().getHost() + ":" + controlador.getConfiguracion().getPort() + ")");
            }).setVisible(true);
        });
        panelDerecho.add(Box.createVerticalStrut(8));
        panelDerecho.add(btnConfigIP);

        panelRoot.add(panelDerecho);

        getRootPane().setDefaultButton(btnLogin);
        add(panelRoot);
    }

    private void autenticar() {
        String u = txtUsuario.getText().trim();
        String p = new String(txtClave.getPassword());

        if (controlador.autenticar(u, p)) {
            dispose();
            DesktopView view = new DesktopView(controlador);
            view.setVisible(true);
        } else {
            lblError.setText("❌ Credenciales incorrectas. Verifique Monster / Monster9");
            txtClave.setText("");
            txtClave.requestFocus();
        }
    }
}
