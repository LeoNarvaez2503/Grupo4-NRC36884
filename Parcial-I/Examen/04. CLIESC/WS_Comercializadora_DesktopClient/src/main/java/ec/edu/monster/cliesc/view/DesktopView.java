package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.FacturaModel.*;
import ec.edu.monster.cliesc.model.TelefonoModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Cliente de Escritorio (Java Swing) para CelStore & BanQuito Core.
 * Diseñado con la misma identidad visual corporativa (Monster / Sullivan)
 * que el Cliente Web JSF.
 * NRC: 36884 | Grupo 04
 */
public class DesktopView extends JFrame {

    private final AppControlador controlador;

    // Colores de la paleta oficial
    private static final Color COLOR_NAVY_DARK = new Color(9, 37, 64);
    private static final Color COLOR_PRIMARY = new Color(15, 76, 129);
    private static final Color COLOR_ACCENT = new Color(245, 158, 11);
    private static final Color COLOR_BG = new Color(248, 250, 252);
    private static final Color COLOR_BORDER = new Color(226, 232, 240);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129);
    private static final Color COLOR_DANGER = new Color(239, 68, 68);

    // Modelos de tabla
    private DefaultTableModel modeloCatalogo;
    private DefaultTableModel modeloAmortizacionFactura;
    private DefaultTableModel modeloAmortizacionConsulta;
    private DefaultTableModel modeloHistorialFacturas;

    // Componentes de Facturación
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JComboBox<TelefonoModel> cmbTelefonos;
    private JComboBox<String> cmbFormaPago;
    private JComboBox<Integer> cmbCuotas;
    private JLabel lblPrecioOriginal;
    private JLabel lblDescuentoOCupo;
    private JLabel lblCuotaEstimada;
    private JLabel lblTotalPagar;
    private JLabel lblAlertaEstado;

    // Componentes de Consulta BanQuito
    private JTextField txtCedulaConsulta;
    private JTextField txtCodCreditoConsulta;
    private JLabel lblDetalleConsulta;

    // Sincronización reactiva automática cada 2.5s (Patrón EurekaBank)
    private Timer timerSincronizacion;
    private boolean sincronizando = false;
    private int ultimaCantidadFacturas = -1;
    private int ultimoIdFactura = -1;
    private int ultimaCantidadTelefonos = -1;
    private int ultimoChecksumCatalogo = -1;

    public DesktopView(AppControlador controlador) {
        this.controlador = controlador;
        initUI();
    }

    private void initUI() {
        setTitle("CelStore & BanQuito Core — Cliente Escritorio (Grupo 04 - Monster)");
        setSize(1240, 780);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelRoot = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image sullivan = getImagenSullivanFondo();
                if (sullivan != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                    int imgH = getHeight() - 100;
                    int imgW = (int) (sullivan.getWidth(null) * ((double) imgH / sullivan.getHeight(null)));
                    int x = (getWidth() - imgW) / 2;
                    int y = 70;
                    g2.drawImage(sullivan, x, y, imgW, imgH, null);
                    g2.dispose();
                }
            }
        };
        panelRoot.setBackground(COLOR_BG);

        // 1. HEADER HERO CON MASCOTA SULLIVAN
        panelRoot.add(crearHeroHeader(), BorderLayout.NORTH);

        // 2. TABS PRINCIPALES (MISMO ORDEN QUE EL CLIENTE WEB)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.addTab("🛒 1. Facturación & Crédito Directo", crearPanelFacturacion());
        tabbedPane.addTab("📱 2. Catálogo de Celulares (CRUD)", crearPanelCatalogo());
        tabbedPane.addTab("🏦 3. Consulta de Amortización", crearPanelConsultasBanquito());
        tabbedPane.addTab("📋 4. Historial de Facturas", crearPanelHistorial());

        panelRoot.add(tabbedPane, BorderLayout.CENTER);

        // 3. BARRA DE ESTADO
        panelRoot.add(crearStatusBar(), BorderLayout.SOUTH);

        add(panelRoot);
        cargarTelefonosEnCombo();
        actualizarCalculosEnVivo();
        iniciarSincronizacionAutomatica();
    }

    private Image imgSullivanFondo = null;

    private Image getImagenSullivanFondo() {
        if (imgSullivanFondo != null) {
            return imgSullivanFondo;
        }
        try {
            java.net.URL resUrl = getClass().getResource("/images/sullivan.png");
            if (resUrl != null) {
                imgSullivanFondo = ImageIO.read(resUrl);
                return imgSullivanFondo;
            }
        } catch (Exception ignored) {}

        String[] posiblesRutas = {
            "images/sullivan.png",
            "src/main/resources/images/sullivan.png",
            "target/classes/images/sullivan.png",
            "../04. CLIWEB/WS_Comercializadora_WebClient/src/main/webapp/images/sullivan.png",
            "/home/meatpuppets/.gemini/antigravity/brain/dcea8f20-a6d3-4d8d-9be2-c46d706c8b21/.user_uploaded/media_1793809565490_9ea731fa.png"
        };
        for (String r : posiblesRutas) {
            try {
                File f = new File(r);
                if (f.exists()) {
                    imgSullivanFondo = ImageIO.read(f);
                    return imgSullivanFondo;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private JPanel crearHeroHeader() {
        JPanel hero = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();

                // 1. Fondo base azul marino
                g2.setColor(COLOR_NAVY_DARK);
                g2.fillRect(0, 0, w, h);

                // 2. Pintar imagen de Sullivan de fondo a la derecha
                Image sullivan = getImagenSullivanFondo();
                if (sullivan != null) {
                    int imgH = h + 20;
                    int imgW = (int) (sullivan.getWidth(null) * ((double) imgH / sullivan.getHeight(null)));
                    int imgX = w - imgW + 20;
                    g2.drawImage(sullivan, imgX, -10, imgW, imgH, null);
                }

                // 3. Gradiente superpuesto para garantizar lectura y contraste de textos
                Paint gpIzq = new GradientPaint(
                        0, 0, new Color(9, 37, 64, 255),
                        (float) (w * 0.70), 0, new Color(9, 37, 64, 185)
                );
                g2.setPaint(gpIzq);
                g2.fillRect(0, 0, (int) (w * 0.70), h);

                // Suave tinte azul marino transparente sobre la imagen
                Paint gpDer = new GradientPaint(
                        (float) (w * 0.65), 0, new Color(9, 37, 64, 180),
                        (float) w, 0, new Color(15, 76, 129, 90)
                );
                g2.setPaint(gpDer);
                g2.fillRect((int) (w * 0.65), 0, (int) (w * 0.35), h);
            }
        };
        hero.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_ACCENT),
                new EmptyBorder(18, 26, 18, 26)
        ));

        // Lado Izquierdo: Textos y Metadatos
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblBadge = new JLabel("★ ALIANZA ESTRATÉGICA AUTORIZADA — MONSTER CORE");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadge.setForeground(COLOR_ACCENT);

        JLabel lblTitulo = new JLabel("CelStore & BanQuito Core");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Facturación de Smartphones con Crédito Directo y Amortización Francesa (16.5% Anual)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(203, 213, 225));

        JLabel lblTags = new JLabel("NRC: 36884 | Grupo 04 (Monster) | Java 21 RESTful Client | Conexión Core :8080");
        lblTags.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTags.setForeground(new Color(148, 163, 184));

        infoPanel.add(lblBadge);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(lblSub);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblTags);

        // Lado Derecho: Insignia Corporativa sobre el fondo de Sullivan
        JPanel mascotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        mascotPanel.setOpaque(false);

        JPanel mascotBadgeCard = new JPanel();
        mascotBadgeCard.setOpaque(false);
        mascotBadgeCard.setLayout(new BoxLayout(mascotBadgeCard, BoxLayout.Y_AXIS));
        mascotBadgeCard.setBorder(new CompoundBorder(
                new LineBorder(COLOR_ACCENT, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));

        JLabel lblSulleyBadge = new JLabel("★ GRUPO 04 - MONSTER");
        lblSulleyBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSulleyBadge.setForeground(COLOR_ACCENT);

        JLabel lblSulleyName = new JLabel("James P. Sullivan");
        lblSulleyName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSulleyName.setForeground(Color.WHITE);

        JLabel lblSulleyRole = new JLabel("Mascota Oficial Grupo 04");
        lblSulleyRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSulleyRole.setForeground(new Color(203, 213, 225));

        JLabel lblSulleyPill = new JLabel("● ESTADO: CONECTADO (:8080)");
        lblSulleyPill.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSulleyPill.setForeground(COLOR_SUCCESS);

        mascotBadgeCard.add(lblSulleyBadge);
        mascotBadgeCard.add(Box.createVerticalStrut(3));
        mascotBadgeCard.add(lblSulleyName);
        mascotBadgeCard.add(lblSulleyRole);
        mascotBadgeCard.add(Box.createVerticalStrut(4));
        mascotBadgeCard.add(lblSulleyPill);
        mascotBadgeCard.add(Box.createVerticalStrut(6));

        JButton btnCerrarSesion = new JButton("🚪 Cerrar Sesión (Monster)");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(220, 38, 38));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginView(controlador).setVisible(true);
        });
        mascotBadgeCard.add(btnCerrarSesion);

        mascotPanel.add(mascotBadgeCard);

        hero.add(infoPanel, BorderLayout.CENTER);
        hero.add(mascotPanel, BorderLayout.EAST);
        return hero;
    }

    private JPanel crearStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(6, 16, 6, 16)
        ));
        JLabel lbl = new JLabel("🟢 Servicios Distribuidos: BanQuito REST Core (:8080) | ● Reactivo en Vivo (2.5s)");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(16, 185, 129));
        bar.add(lbl, BorderLayout.WEST);

        JButton btnCambiarIP = new JButton("⚙️ Servidor IP: " + controlador.getConfiguracion().getHost() + ":" + controlador.getConfiguracion().getPort());
        btnCambiarIP.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCambiarIP.setForeground(COLOR_PRIMARY);
        btnCambiarIP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarIP.addActionListener(e -> {
            new DialogoConfigurarIP(this, controlador.getConfiguracion(), () -> {
                btnCambiarIP.setText("⚙️ Servidor IP: " + controlador.getConfiguracion().getHost() + ":" + controlador.getConfiguracion().getPort());
                cargarTelefonosEnCombo();
            }).setVisible(true);
        });
        bar.add(btnCambiarIP, BorderLayout.EAST);

        return bar;
    }

    // =========================================================================
    // PESTAÑA 1: FACTURACIÓN & CRÉDITO DIRECTO
    // =========================================================================
    private JPanel crearPanelFacturacion() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));

        // 1. Botonera de 4 Clientes Oficiales de la Rúbrica
        panel.add(crearBotoneraRubrica(), BorderLayout.NORTH);

        // 2. División Central: Formulario a la izquierda, Resumen + Amortización a la derecha
        JPanel split = new JPanel(new GridLayout(1, 2, 16, 16));
        split.setBackground(COLOR_BG);

        // Izquierda: Formulario de Venta
        split.add(crearCardFormularioVenta());

        // Derecha: KPIs + Tabla de Amortización Francesa
        split.add(crearCardResumenYAmortizacion());

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearBotoneraRubrica() {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel lbl = new JLabel("⚡ Casos Oficiales de Prueba ante el Tribunal (Rúbrica - 1 Clic):");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_NAVY_DARK);
        card.add(lbl, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 4, 10, 0));
        grid.setBackground(Color.WHITE);

        JButton btn1 = crearBotonRubrica("1. Carlos Andrade", "C.I. 1710000001 | Xiaomi Redmi ($450)", "Aprobado (Cuota $78.65)", new Color(16, 185, 129));
        btn1.addActionListener(e -> seleccionarClienteDemo(1));

        JButton btn2 = crearBotonRubrica("2. Andrea Morales", "C.I. 1710000002 | Xiaomi 14 Pro ($850)", "Rechazo: Capacidad Insuf.", new Color(239, 68, 68));
        btn2.addActionListener(e -> seleccionarClienteDemo(2));

        JButton btn3 = crearBotonRubrica("3. Mateo Salazar", "C.I. 1710000003 | Varón 22 años", "Rechazo: Edad < 25 años", new Color(245, 158, 11));
        btn3.addActionListener(e -> seleccionarClienteDemo(3));

        JButton btn4 = crearBotonRubrica("4. Elena Castro", "C.I. 1710000004 | Xiaomi ($450)", "Efectivo: 42% Descuento", COLOR_PRIMARY);
        btn4.addActionListener(e -> seleccionarClienteDemo(4));

        grid.add(btn1);
        grid.add(btn2);
        grid.add(btn3);
        grid.add(btn4);

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JButton crearBotonRubrica(String titulo, String subtitulo, String estado, Color accentColor) {
        String html = String.format(
                "<html><div style='padding:4px; text-align:left;'>"
                + "<b>%s</b><br>"
                + "<span style='font-size:10px; color:#64748b;'>%s</span><br>"
                + "<b style='color:%s; font-size:11px;'>● %s</b>"
                + "</div></html>",
                titulo, subtitulo, toHex(accentColor), estado
        );
        JButton btn = new JButton(html);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createLineBorder(COLOR_BORDER, 1)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private JPanel crearCardFormularioVenta() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel title = new JLabel("🛒 Datos de la Venta / Factura");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(COLOR_NAVY_DARK);
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 12));
        form.setBackground(Color.WHITE);

        txtCedula = new JTextField("1710000001");
        txtNombre = new JTextField("Carlos Andrade");
        cmbTelefonos = new JComboBox<>();
        cmbFormaPago = new JComboBox<>(new String[]{"CREDITO_DIRECTO", "EFECTIVO"});
        cmbCuotas = new JComboBox<>(new Integer[]{3, 6, 9, 12, 18});
        cmbCuotas.setSelectedItem(6);

        cmbTelefonos.addActionListener(e -> actualizarCalculosEnVivo());
        cmbFormaPago.addActionListener(e -> actualizarCalculosEnVivo());
        cmbCuotas.addActionListener(e -> actualizarCalculosEnVivo());

        form.add(new JLabel("Cédula Solicitante:"));
        form.add(txtCedula);
        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);
        form.add(new JLabel("Teléfono Celular:"));
        form.add(cmbTelefonos);
        form.add(new JLabel("Modalidad de Pago:"));
        form.add(cmbFormaPago);
        form.add(new JLabel("Plazo Cuotas (Crédito):"));
        form.add(cmbCuotas);

        card.add(form, BorderLayout.CENTER);

        // Botonera Inferior del Formulario
        JPanel actions = new JPanel(new BorderLayout(8, 8));
        actions.setBackground(Color.WHITE);

        lblAlertaEstado = new JLabel("Seleccione un cliente de prueba o ingrese los datos para facturar.");
        lblAlertaEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAlertaEstado.setForeground(COLOR_TEXT_MUTED);

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.WHITE);

        JButton btnVerificar = new JButton("🔍 Verificar en BanQuito");
        btnVerificar.setBackground(COLOR_PRIMARY);
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerificar.setFocusPainted(false);
        btnVerificar.addActionListener(e -> verificarCreditoEnVivo());

        JButton btnEmitir = new JButton("💳 Emitir Factura Oficial");
        btnEmitir.setBackground(COLOR_SUCCESS);
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEmitir.setFocusPainted(false);
        btnEmitir.addActionListener(e -> procesarFacturacion());

        btnRow.add(btnVerificar);
        btnRow.add(btnEmitir);

        actions.add(lblAlertaEstado, BorderLayout.NORTH);
        actions.add(btnRow, BorderLayout.SOUTH);

        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JPanel crearCardResumenYAmortizacion() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));

        // KPIs Financieros (4 cajas en fila)
        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 8, 0));
        kpiRow.setBackground(Color.WHITE);

        lblPrecioOriginal = new JLabel("$450.00", SwingConstants.CENTER);
        lblPrecioOriginal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrecioOriginal.setForeground(COLOR_PRIMARY);

        lblDescuentoOCupo = new JLabel("$0.00", SwingConstants.CENTER);
        lblDescuentoOCupo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDescuentoOCupo.setForeground(COLOR_SUCCESS);

        lblCuotaEstimada = new JLabel("$0.00", SwingConstants.CENTER);
        lblCuotaEstimada.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCuotaEstimada.setForeground(COLOR_PRIMARY);

        lblTotalPagar = new JLabel("$450.00", SwingConstants.CENTER);
        lblTotalPagar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalPagar.setForeground(COLOR_SUCCESS);

        kpiRow.add(crearKpiBox("PRECIO ORIGINAL", lblPrecioOriginal));
        kpiRow.add(crearKpiBox("DESC / CUPO", lblDescuentoOCupo));
        kpiRow.add(crearKpiBox("CUOTA MENSUAL", lblCuotaEstimada));
        kpiRow.add(crearKpiBox("TOTAL OPERACIÓN", lblTotalPagar));

        card.add(kpiRow, BorderLayout.NORTH);

        // Tabla de Amortización Francesa
        JPanel tableWrap = new JPanel(new BorderLayout(0, 6));
        tableWrap.setBackground(Color.WHITE);

        JLabel lblTblTitle = new JLabel("📊 Tabla de Amortización Francesa (BanQuito Core - 16.5% Anual):");
        lblTblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTblTitle.setForeground(COLOR_NAVY_DARK);

        modeloAmortizacionFactura = new DefaultTableModel(
                new Object[]{"# Cuota", "Valor Cuota ($)", "Interés ($)", "Capital ($)", "Saldo ($)"}, 0);
        JTable tablaAmort = new JTable(modeloAmortizacionFactura);
        estilizarTablaModerna(tablaAmort);

        tableWrap.add(lblTblTitle, BorderLayout.NORTH);
        tableWrap.add(new JScrollPane(tablaAmort), BorderLayout.CENTER);

        card.add(tableWrap, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearKpiBox(String titulo, JLabel valorLabel) {
        JPanel box = new JPanel(new BorderLayout(0, 4));
        box.setBackground(new Color(248, 250, 252));
        box.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        JLabel lblT = new JLabel(titulo, SwingConstants.CENTER);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblT.setForeground(COLOR_TEXT_MUTED);
        box.add(lblT, BorderLayout.NORTH);
        box.add(valorLabel, BorderLayout.CENTER);
        return box;
    }

    private void estilizarTablaModerna(JTable tabla) {
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setSelectionBackground(new Color(224, 242, 254));
        tabla.setSelectionForeground(COLOR_NAVY_DARK);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(COLOR_BORDER);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(COLOR_PRIMARY);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(center);
    }

    // =========================================================================
    // PESTAÑA 2: CATÁLOGO DE CELULARES (CRUD)
    // =========================================================================
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topBar.setBackground(COLOR_BG);

        JButton btnRefrescar = new JButton("🔄 Refrescar Catálogo");
        btnRefrescar.setBackground(COLOR_PRIMARY);
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.addActionListener(e -> refrescarCatalogo());

        JButton btnNuevo = new JButton("➕ Registrar Celular");
        btnNuevo.setBackground(COLOR_SUCCESS);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevo.addActionListener(e -> agregarTelefonoDialog());

        modeloCatalogo = new DefaultTableModel(
                new Object[]{"ID", "Marca", "Modelo", "Precio ($ USD)", "Estado", "Especificaciones Técnicas"}, 0);
        JTable tabla = new JTable(modeloCatalogo);
        estilizarTablaModerna(tabla);

        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(COLOR_PRIMARY);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.addActionListener(e -> editarTelefonoDialog(tabla));

        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(239, 68, 68));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEliminar.addActionListener(e -> eliminarTelefonoDialog(tabla));

        topBar.add(btnRefrescar);
        topBar.add(btnNuevo);
        topBar.add(btnEditar);
        topBar.add(btnEliminar);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // PESTAÑA 3: CONSULTAS BANQUITO Y AMORTIZACIÓN
    // =========================================================================
    private JPanel crearPanelConsultasBanquito() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel topSearch = new JPanel(new GridLayout(1, 2, 16, 0));
        topSearch.setBackground(COLOR_BG);

        // Búsqueda por Código de Crédito
        JPanel card1 = new JPanel(new BorderLayout(8, 8));
        card1.setBackground(Color.WHITE);
        card1.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(12, 14, 12, 14)));
        txtCodCreditoConsulta = new JTextField("1");
        JButton btnBuscarCred = new JButton("Buscar Tabla por # Crédito");
        btnBuscarCred.setBackground(COLOR_PRIMARY);
        btnBuscarCred.setForeground(Color.WHITE);
        btnBuscarCred.addActionListener(e -> consultarAmortizacionClick());
        card1.add(new JLabel("Código de Crédito BanQuito (#):"), BorderLayout.NORTH);
        card1.add(txtCodCreditoConsulta, BorderLayout.CENTER);
        card1.add(btnBuscarCred, BorderLayout.SOUTH);

        // Búsqueda por Cédula
        JPanel card2 = new JPanel(new BorderLayout(8, 8));
        card2.setBackground(Color.WHITE);
        card2.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(12, 14, 12, 14)));
        txtCedulaConsulta = new JTextField("1710000001");
        JButton btnBuscarCed = new JButton("Verificar Sujeto y Capacidad por C.I.");
        btnBuscarCed.setBackground(COLOR_PRIMARY);
        btnBuscarCed.setForeground(Color.WHITE);
        btnBuscarCed.addActionListener(e -> validarSujetoClick());
        card2.add(new JLabel("Cédula de Identidad:"), BorderLayout.NORTH);
        card2.add(txtCedulaConsulta, BorderLayout.CENTER);
        card2.add(btnBuscarCed, BorderLayout.SOUTH);

        topSearch.add(card1);
        topSearch.add(card2);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(12, 14, 12, 14)));

        lblDetalleConsulta = new JLabel("Ingrese un código de crédito o cédula para consultar el Core Bancario.");
        lblDetalleConsulta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDetalleConsulta.setForeground(COLOR_NAVY_DARK);

        modeloAmortizacionConsulta = new DefaultTableModel(
                new Object[]{"# Cuota", "Valor Cuota ($)", "Interés Pagado ($)", "Capital Pagado ($)", "Saldo Restante ($)"}, 0);
        JTable tabla = new JTable(modeloAmortizacionConsulta);
        estilizarTablaModerna(tabla);

        centerPanel.add(lblDetalleConsulta, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        panel.add(topSearch, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // PESTAÑA 4: HISTORIAL DE FACTURAS EMITIDAS
    // =========================================================================
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(COLOR_BG);
        JButton btnRefrescar = new JButton("🔄 Actualizar Historial de Facturas");
        btnRefrescar.setBackground(COLOR_PRIMARY);
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.addActionListener(e -> cargarHistorialFacturas());
        top.add(btnRefrescar);

        modeloHistorialFacturas = new DefaultTableModel(
                new Object[]{"Factura #", "Comprobante", "Fecha", "Cliente", "Cédula", "Celular", "Forma Pago", "Precio ($)", "Desc ($)", "Total ($)", "Crédito #"}, 0);
        JTable tabla = new JTable(modeloHistorialFacturas);
        estilizarTablaModerna(tabla);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // LÓGICA DE NEGOCIO Y BOTONERA RÚBRICA
    // =========================================================================
    private void seleccionarClienteDemo(int caso) {
        switch (caso) {
            case 1 -> {
                txtCedula.setText("1710000001");
                txtNombre.setText("Carlos Andrade");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(6);
                seleccionarTelefonoPorModelo("Redmi");
                verificarCreditoEnVivo();
            }
            case 2 -> {
                txtCedula.setText("1710000002");
                txtNombre.setText("Andrea Morales");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(6);
                seleccionarTelefonoPorModelo("Xiaomi 14");
                verificarCreditoEnVivo();
            }
            case 3 -> {
                txtCedula.setText("1710000003");
                txtNombre.setText("Mateo Salazar");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(6);
                seleccionarTelefonoPorModelo("Motorola");
                verificarCreditoEnVivo();
            }
            case 4 -> {
                txtCedula.setText("1710000004");
                txtNombre.setText("Elena Castro");
                cmbFormaPago.setSelectedItem("EFECTIVO");
                seleccionarTelefonoPorModelo("Redmi");
                actualizarCalculosEnVivo();
                lblAlertaEstado.setText("<html><b style='color:#10b981;'>✔ Cliente 4 (Elena Castro) en Efectivo: Aplica 42% de descuento directo.</b></html>");
            }
        }
    }

    private void seleccionarTelefonoPorModelo(String keyword) {
        for (int i = 0; i < cmbTelefonos.getItemCount(); i++) {
            TelefonoModel t = cmbTelefonos.getItemAt(i);
            if (t.getModelo().toLowerCase().contains(keyword.toLowerCase())
                    || t.getMarca().toLowerCase().contains(keyword.toLowerCase())) {
                cmbTelefonos.setSelectedIndex(i);
                break;
            }
        }
        actualizarCalculosEnVivo();
    }

    private void actualizarCalculosEnVivo() {
        TelefonoModel sel = (TelefonoModel) cmbTelefonos.getSelectedItem();
        if (sel == null) return;

        double precio = sel.getPrecio();
        lblPrecioOriginal.setText(String.format("$%.2f", precio));

        String fp = (String) cmbFormaPago.getSelectedItem();
        if ("EFECTIVO".equals(fp)) {
            double desc = Math.round(precio * 0.42 * 100.0) / 100.0;
            double tot = precio - desc;
            lblDescuentoOCupo.setText(String.format("-$%.2f (42%%)", desc));
            lblDescuentoOCupo.setForeground(COLOR_DANGER);
            lblCuotaEstimada.setText("N/A");
            lblTotalPagar.setText(String.format("$%.2f", tot));
            cmbCuotas.setEnabled(false);
        } else {
            cmbCuotas.setEnabled(true);
            lblDescuentoOCupo.setText("Consultando...");
            lblDescuentoOCupo.setForeground(COLOR_SUCCESS);
            lblTotalPagar.setText(String.format("$%.2f", precio));
        }
    }

    private void verificarCreditoEnVivo() {
        String cedula = txtCedula.getText().trim();
        TelefonoModel tel = (TelefonoModel) cmbTelefonos.getSelectedItem();
        int cuotas = (Integer) cmbCuotas.getSelectedItem();
        if (tel == null || cedula.isBlank()) return;

        lblAlertaEstado.setText("Consultando Core Bancario BanQuito...");

        new SwingWorker<Void, Void>() {
            private SujetoCreditoResponse calif;
            private MontoMaximoResponse capacidad;
            private List<CuotaAmortizacion> amort;

            @Override
            protected Void doInBackground() {
                calif = controlador.verificarSujeto(cedula);
                if (calif != null && calif.codCliente != null && calif.codCliente > 0) {
                    capacidad = controlador.consultarMontoMaximo(calif.codCliente);
                    if (calif.esSujeto && capacidad != null && tel.getPrecio() <= capacidad.montoMaximo) {
                        amort = controlador.consultarTablaAmortizacion(1); // Muestra simulación
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                if (calif == null || !calif.esSujeto) {
                    String m = calif != null ? calif.motivo : "Cliente no sujeto a crédito.";
                    lblAlertaEstado.setText("<html><b style='color:#ef4444;'>✖ BanQuito Denegó: " + m + "</b></html>");
                    lblDescuentoOCupo.setText("$0.00");
                    lblCuotaEstimada.setText("$0.00");
                    modeloAmortizacionFactura.setRowCount(0);
                    return;
                }

                double cupo = capacidad != null ? capacidad.montoMaximo : 0.0;
                lblDescuentoOCupo.setText(String.format("Cupo: $%.2f", cupo));

                if (tel.getPrecio() > cupo) {
                    lblAlertaEstado.setText(String.format("<html><b style='color:#ef4444;'>✖ Capacidad Insuficiente: Precio ($%.2f) excede cupo aprobado ($%.2f).</b></html>", tel.getPrecio(), cupo));
                    lblCuotaEstimada.setText("$0.00");
                    modeloAmortizacionFactura.setRowCount(0);
                    return;
                }

                double cuotaAprox = Math.round((tel.getPrecio() * (0.01375 / (1 - Math.pow(1 + 0.01375, -cuotas)))) * 100.0) / 100.0;
                lblCuotaEstimada.setText(String.format("$%.2f/m", cuotaAprox));
                lblAlertaEstado.setText(String.format("<html><b style='color:#10b981;'>✔ Crédito Aprobado por BanQuito: Cupo $%.2f. Cuota fija estimada: $%.2f a %d meses.</b></html>", cupo, cuotaAprox, cuotas));

                cargarTablaAmortizacion(modeloAmortizacionFactura, amort);
            }
        }.execute();
    }

    private void procesarFacturacion() {
        FacturaRequest req = new FacturaRequest();
        req.cedula = txtCedula.getText().trim();
        req.nombre = txtNombre.getText().trim();
        TelefonoModel sel = (TelefonoModel) cmbTelefonos.getSelectedItem();
        if (sel == null) return;
        req.codTelefono = sel.getCodTelefono();
        req.formaPago = (String) cmbFormaPago.getSelectedItem();
        req.cuotas = (Integer) cmbCuotas.getSelectedItem();

        lblAlertaEstado.setText("Procesando factura...");

        new SwingWorker<FacturaResponse, Void>() {
            @Override
            protected FacturaResponse doInBackground() {
                return controlador.facturar(req);
            }

            @Override
            protected void done() {
                try {
                    FacturaResponse resp = get();
                    if (resp.exito) {
                        lblAlertaEstado.setText("<html><b style='color:#10b981;'>✔ Factura #" + resp.facturaId + " emitida exitosamente.</b></html>");
                        JOptionPane.showMessageDialog(DesktopView.this, resp.mensaje, "Facturación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                        if (resp.tabla != null && !resp.tabla.isEmpty()) {
                            cargarTablaAmortizacion(modeloAmortizacionFactura, resp.tabla);
                        }
                        cargarHistorialFacturas();
                    } else {
                        lblAlertaEstado.setText("<html><b style='color:#ef4444;'>✖ " + resp.mensaje + "</b></html>");
                        JOptionPane.showMessageDialog(DesktopView.this, resp.mensaje, "Rechazo de Facturación", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    lblAlertaEstado.setText("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void cargarTablaAmortizacion(DefaultTableModel mod, List<CuotaAmortizacion> cuotas) {
        mod.setRowCount(0);
        if (cuotas == null) return;
        for (CuotaAmortizacion c : cuotas) {
            mod.addRow(new Object[]{
                c.numCuota,
                String.format("%.2f", c.valorCuota),
                String.format("%.2f", c.interesPagado),
                String.format("%.2f", c.capitalPagado),
                String.format("%.2f", c.saldo)
            });
        }
    }

    private void cargarTelefonosEnCombo() {
        cmbTelefonos.removeAllItems();
        List<TelefonoModel> tels = controlador.listarTelefonos();
        for (TelefonoModel t : tels) {
            cmbTelefonos.addItem(t);
        }
        refrescarCatalogo();
    }

    private void refrescarCatalogo() {
        modeloCatalogo.setRowCount(0);
        List<TelefonoModel> tels = controlador.listarTelefonos();
        ultimaCantidadTelefonos = tels.size();
        ultimoChecksumCatalogo = calcularChecksumCatalogo(tels);
        for (TelefonoModel t : tels) {
            modeloCatalogo.addRow(new Object[]{
                t.getCodTelefono(),
                t.getMarca(),
                t.getModelo(),
                String.format("%.2f", t.getPrecio()),
                t.getEstado(),
                t.getCaracteristicas()
            });
        }
    }

    private void cargarHistorialFacturas() {
        modeloHistorialFacturas.setRowCount(0);
        List<FacturaItem> facturas = controlador.listarFacturas();
        ultimaCantidadFacturas = facturas.size();
        ultimoIdFactura = facturas.isEmpty() ? 0 : facturas.get(facturas.size() - 1).numFactura;
        for (FacturaItem f : facturas) {
            modeloHistorialFacturas.addRow(new Object[]{
                f.numFactura,
                f.numeroComprobante,
                f.fecha,
                f.nombreCliente,
                f.cedulaCliente,
                f.modeloTelefono,
                f.formaPago,
                String.format("%.2f", f.precioUnitario),
                String.format("%.2f", f.valorDescuento),
                String.format("%.2f", f.totalPagar),
                f.codCreditoBanco != null ? "#" + f.codCreditoBanco : "-"
            });
        }
    }

    private void iniciarSincronizacionAutomatica() {
        if (timerSincronizacion != null && timerSincronizacion.isRunning()) {
            timerSincronizacion.stop();
        }
        timerSincronizacion = new Timer(2500, e -> {
            if (!sincronizando) {
                sincronizarEnSegundoPlano();
            }
        });
        timerSincronizacion.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (timerSincronizacion != null) {
                    timerSincronizacion.stop();
                }
            }
        });
    }

    private void sincronizarEnSegundoPlano() {
        sincronizando = true;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private List<FacturaItem> facturas;
            private List<TelefonoModel> tels;

            @Override
            protected Void doInBackground() {
                try {
                    facturas = controlador.listarFacturas();
                    tels = controlador.listarTelefonos();
                } catch (Exception ignored) {}
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (facturas != null) {
                        actualizarHistorialSilencioso(facturas);
                    }
                    if (tels != null) {
                        actualizarCatalogoSilencioso(tels);
                    }
                } finally {
                    sincronizando = false;
                }
            }
        };
        worker.execute();
    }

    private void actualizarHistorialSilencioso(List<FacturaItem> facturas) {
        int ultimoId = facturas.isEmpty() ? 0 : facturas.get(facturas.size() - 1).numFactura;
        if (facturas.size() != ultimaCantidadFacturas || ultimoId != ultimoIdFactura) {
            ultimaCantidadFacturas = facturas.size();
            ultimoIdFactura = ultimoId;
            modeloHistorialFacturas.setRowCount(0);
            for (FacturaItem f : facturas) {
                modeloHistorialFacturas.addRow(new Object[]{
                    f.numFactura,
                    f.numeroComprobante,
                    f.fecha,
                    f.nombreCliente,
                    f.cedulaCliente,
                    f.modeloTelefono,
                    f.formaPago,
                    String.format("%.2f", f.precioUnitario),
                    String.format("%.2f", f.valorDescuento),
                    String.format("%.2f", f.totalPagar),
                    f.codCreditoBanco != null ? "#" + f.codCreditoBanco : "-"
                });
            }
        }
    }

    private int calcularChecksumCatalogo(List<TelefonoModel> tels) {
        int sum = 0;
        for (TelefonoModel t : tels) {
            sum = 31 * sum + t.getCodTelefono() + (int)(t.getPrecio() * 100) + (t.getEstado() != null ? t.getEstado().hashCode() : 0);
        }
        return sum;
    }

    private void actualizarCatalogoSilencioso(List<TelefonoModel> tels) {
        int checksum = calcularChecksumCatalogo(tels);
        if (tels.size() != ultimaCantidadTelefonos || checksum != ultimoChecksumCatalogo) {
            ultimaCantidadTelefonos = tels.size();
            ultimoChecksumCatalogo = checksum;
            modeloCatalogo.setRowCount(0);
            for (TelefonoModel t : tels) {
                modeloCatalogo.addRow(new Object[]{
                    t.getCodTelefono(),
                    t.getMarca(),
                    t.getModelo(),
                    String.format("%.2f", t.getPrecio()),
                    t.getEstado(),
                    t.getCaracteristicas()
                });
            }

            TelefonoModel selActual = (TelefonoModel) cmbTelefonos.getSelectedItem();
            int selCod = selActual != null ? selActual.getCodTelefono() : -1;
            cmbTelefonos.removeAllItems();
            TelefonoModel nuevoSel = null;
            for (TelefonoModel t : tels) {
                cmbTelefonos.addItem(t);
                if (t.getCodTelefono() == selCod) {
                    nuevoSel = t;
                }
            }
            if (nuevoSel != null) {
                cmbTelefonos.setSelectedItem(nuevoSel);
            }
        }
    }

    private void agregarTelefonoDialog() {
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtPrecio = new JTextField("350.00");
        JTextField txtCaract = new JTextField("128GB, 8GB RAM, Pantalla OLED");

        Object[] fields = {
            "Marca:", txtMarca,
            "Modelo:", txtModelo,
            "Precio ($ USD):", txtPrecio,
            "Características:", txtCaract
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Registrar Nuevo Teléfono en Catálogo", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                TelefonoModel t = new TelefonoModel(0, txtMarca.getText().trim(), txtModelo.getText().trim(),
                        Double.parseDouble(txtPrecio.getText().trim()), txtCaract.getText().trim(), "", "DISPONIBLE");
                if (controlador.agregarTelefono(t)) {
                    JOptionPane.showMessageDialog(this, "Teléfono registrado en el catálogo exitosamente.");
                    refrescarCatalogo();
                    cargarTelefonosEnCombo();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarTelefonoDialog(JTable tabla) {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un celular de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tabla.getValueAt(row, 0).toString());
        String marca = tabla.getValueAt(row, 1).toString();
        String modelo = tabla.getValueAt(row, 2).toString();
        String precio = tabla.getValueAt(row, 3).toString();
        String estado = tabla.getValueAt(row, 4).toString();
        String carac = tabla.getValueAt(row, 5) != null ? tabla.getValueAt(row, 5).toString() : "";

        JTextField txtMarca = new JTextField(marca);
        JTextField txtModelo = new JTextField(modelo);
        JTextField txtPrecio = new JTextField(precio);
        JTextField txtCaract = new JTextField(carac);
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"DISPONIBLE", "INACTIVO"});
        cmbEstado.setSelectedItem(estado);

        Object[] fields = {
            "ID Celular: #" + id,
            "Marca:", txtMarca,
            "Modelo:", txtModelo,
            "Precio ($ USD):", txtPrecio,
            "Características:", txtCaract,
            "Estado:", cmbEstado
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Editar Celular #" + id, JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                TelefonoModel t = new TelefonoModel(id, txtMarca.getText().trim(), txtModelo.getText().trim(),
                        Double.parseDouble(txtPrecio.getText().trim()), txtCaract.getText().trim(), "", (String) cmbEstado.getSelectedItem());
                if (controlador.actualizarTelefono(t)) {
                    JOptionPane.showMessageDialog(this, "Celular actualizado exitosamente.");
                    refrescarCatalogo();
                    cargarTelefonosEnCombo();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar celular.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarTelefonoDialog(JTable tabla) {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un celular de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tabla.getValueAt(row, 0).toString());
        String modelo = tabla.getValueAt(row, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar o desactivar el celular #" + id + " (" + modelo + ")?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controlador.eliminarTelefono(id)) {
                JOptionPane.showMessageDialog(this, "Celular eliminado o desactivado del catálogo.");
                refrescarCatalogo();
                cargarTelefonosEnCombo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el celular.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validarSujetoClick() {
        String cedula = txtCedulaConsulta.getText().trim();
        SujetoCreditoResponse r = controlador.verificarSujeto(cedula);
        if (r != null) {
            lblDetalleConsulta.setText(String.format("<html><b>Cédula:</b> %s | <b>Cliente:</b> %s | <b>Calificado:</b> %s | <b>Motivo:</b> %s</html>",
                    cedula, r.nombre, r.esSujeto ? "SÍ" : "NO", r.motivo));
        } else {
            lblDetalleConsulta.setText("Error al consultar BanQuito.");
        }
    }

    private void consultarAmortizacionClick() {
        try {
            int credId = Integer.parseInt(txtCodCreditoConsulta.getText().trim());
            List<CuotaAmortizacion> amort = controlador.consultarTablaAmortizacion(credId);
            cargarTablaAmortizacion(modeloAmortizacionConsulta, amort);
            lblDetalleConsulta.setText("Tabla de Amortización para Crédito #" + credId + " cargada exitosamente.");
        } catch (Exception ex) {
            lblDetalleConsulta.setText("Código de crédito inválido: " + ex.getMessage());
        }
    }
}
