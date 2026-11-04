package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.model.ConfiguracionServidor;
import ec.edu.monster.cliesc.model.ConfiguracionServidor.ResultadoPrueba;
import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Diálogo modal para configurar la dirección IP y puerto del servidor remoto.
 * Permite conectarse desde cualquier equipo o laptop en la red local.
 */
public class DialogoConfigurarIP extends JDialog {

    private final ConfiguracionServidor config;
    private final Runnable alGuardarCallback;

    private JTextField txtHost;
    private JTextField txtPuerto;
    private JLabel lblResultado;
    private JButton btnProbar;
    private JButton btnGuardar;

    private static final Color COLOR_NAVY_DARK = new Color(9, 37, 64);
    private static final Color COLOR_PRIMARY = new Color(15, 76, 129);
    private static final Color COLOR_ACCENT = new Color(245, 158, 11);
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129);
    private static final Color COLOR_DANGER = new Color(239, 68, 68);
    private static final Color COLOR_BORDER = new Color(226, 232, 240);

    public DialogoConfigurarIP(Window owner, ConfiguracionServidor config, Runnable alGuardarCallback) {
        super(owner, "Configurar IP del Servidor Remoto (Payara / BanQuito)", ModalityType.APPLICATION_MODAL);
        this.config = config;
        this.alGuardarCallback = alGuardarCallback;
        initUI();
    }

    private void initUI() {
        setSize(520, 420);
        setResizable(false);
        setLocationRelativeTo(getOwner());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(COLOR_NAVY_DARK);
        header.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_ACCENT),
                new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblTit = new JLabel("⚙️ Configuración de Conexión Remota");
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Ingrese la dirección IP del servidor donde corre Payara Server (:8080).");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(203, 213, 225));

        header.add(lblTit);
        header.add(Box.createVerticalStrut(2));
        header.add(lblSub);
        root.add(header, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 24, 14, 24));

        // Host / IP
        JLabel lblHost = new JLabel("Dirección IP o Host del Servidor:");
        lblHost.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHost.setForeground(COLOR_NAVY_DARK);
        txtHost = new JTextField(config.getHost());
        txtHost.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtHost.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // Puerto
        JLabel lblPort = new JLabel("Puerto de Servicio HTTP (Payara):");
        lblPort.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPort.setForeground(COLOR_NAVY_DARK);
        txtPuerto = new JTextField(String.valueOf(config.getPort()));
        txtPuerto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPuerto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // Botonera de presets rápidos
        JPanel presets = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        presets.setOpaque(false);

        JButton btnLocal = new JButton("📍 Localhost (127.0.0.1)");
        btnLocal.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLocal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLocal.addActionListener(e -> {
            txtHost.setText("127.0.0.1");
            txtPuerto.setText("8080");
        });

        JButton btnDetectar = new JButton("🌐 Detectar Mi IP LAN");
        btnDetectar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnDetectar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetectar.addActionListener(e -> {
            String ipDetectada = detectarIpLocal();
            if (ipDetectada != null) {
                txtHost.setText(ipDetectada);
                txtPuerto.setText("8080");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo auto-detectar la IP local. Ingrésela manualmente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        presets.add(btnLocal);
        presets.add(btnDetectar);

        // Mensaje de Resultado de Prueba
        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblResultado.setForeground(new Color(100, 116, 139));
        lblResultado.setBorder(new EmptyBorder(8, 0, 4, 0));

        form.add(lblHost);
        form.add(Box.createVerticalStrut(4));
        form.add(txtHost);
        form.add(Box.createVerticalStrut(10));
        form.add(lblPort);
        form.add(Box.createVerticalStrut(4));
        form.add(txtPuerto);
        form.add(Box.createVerticalStrut(6));
        form.add(presets);
        form.add(Box.createVerticalStrut(4));
        form.add(lblResultado);

        root.add(form, BorderLayout.CENTER);

        // Botones Inferiores (Probar, Guardar, Cancelar)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248, 250, 252));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        btnProbar = new JButton("🔍 Probar Conexión");
        btnProbar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnProbar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProbar.addActionListener(e -> ejecutarPrueba());

        btnGuardar = new JButton("💾 Guardar y Conectar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setBackground(COLOR_PRIMARY);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.addActionListener(e -> dispose());

        footer.add(btnProbar);
        footer.add(btnGuardar);
        footer.add(btnCancelar);

        root.add(footer, BorderLayout.SOUTH);

        add(root);
    }

    private String detectarIpLocal() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (Exception ignored) {}
        }
        return null;
    }

    private void ejecutarPrueba() {
        String host = txtHost.getText().trim();
        int puerto = 8080;
        try {
            puerto = Integer.parseInt(txtPuerto.getText().trim());
        } catch (Exception ignored) {}

        btnProbar.setEnabled(false);
        lblResultado.setForeground(COLOR_NAVY_DARK);
        lblResultado.setText("⏳ Probando conexión a " + host + ":" + puerto + "...");

        final int pFinal = puerto;
        SwingWorker<ResultadoPrueba, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoPrueba doInBackground() {
                return ConfiguracionServidor.probarConexion(host, pFinal);
            }

            @Override
            protected void done() {
                btnProbar.setEnabled(true);
                try {
                    ResultadoPrueba res = get();
                    if (res.exitoso) {
                        lblResultado.setForeground(COLOR_SUCCESS);
                        lblResultado.setText("✔ Conectado exitosamente con BanQuito y Comercializadora.");
                    } else {
                        lblResultado.setForeground(COLOR_DANGER);
                        lblResultado.setText(res.mensaje);
                    }
                } catch (Exception ex) {
                    lblResultado.setForeground(COLOR_DANGER);
                    lblResultado.setText("❌ Error de prueba: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void guardar() {
        String host = txtHost.getText().trim();
        int puerto = 8080;
        try {
            puerto = Integer.parseInt(txtPuerto.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un número entero válido (ej. 8080).", "Puerto Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        config.configurar(host, puerto);
        if (alGuardarCallback != null) {
            try {
                alGuardarCallback.run();
            } catch (Exception ignored) {}
        }
        JOptionPane.showMessageDialog(this, "Configuración guardada exitosamente:\n" + config.getUrlBase(), "Servidor Configurado", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
