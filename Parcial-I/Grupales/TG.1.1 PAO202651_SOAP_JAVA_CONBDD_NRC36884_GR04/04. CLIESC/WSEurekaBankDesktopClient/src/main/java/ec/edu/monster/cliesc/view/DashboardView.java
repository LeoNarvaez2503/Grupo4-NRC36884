package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.CuentaModel;
import ec.edu.monster.cliesc.model.MovimientoModel;
import ec.edu.monster.cliesc.model.ResultadoOperacion;
import ec.edu.monster.cliesc.model.SucursalModel;
import ec.edu.monster.cliesc.model.UsuarioSesion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Capa Vista (PatrÃ³n MVC Tradicional): Formulario NetBeans GUI del Monitor MashUp y Operaciones Bancarias.
 */
public class DashboardView extends javax.swing.JFrame {

    private final AppControlador controlador;
    private DefaultTableModel modeloCuentas;
    private DefaultTableModel modeloMovimientos;
    private final List<SucursalModel> cacheSucursales = new ArrayList<>();
    private Timer timerMashup;
    private boolean refrescando = false;

    /**
     * Constructor por defecto para diseÃ±ador NetBeans (Matisse).
     */
    public DashboardView() {
        this(new AppControlador());
    }

    /**
     * Constructor con inyecciÃ³n del controlador MVC.
     * @param controlador Controlador de la aplicaciÃ³n
     */
    public DashboardView(AppControlador controlador) {
        this.controlador = controlador;
        initComponents();
        cargarImagenSulley();
        customInit();
    }

    private void cargarImagenSulley() {
        try {
            java.io.InputStream is = getClass().getResourceAsStream("/images/sullivan.png");
            if (is == null) {
                is = getClass().getResourceAsStream("/images/sullivan2.png");
            }
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("images/sullivan.png");
            }
            if (is != null) {
                java.awt.image.BufferedImage raw = javax.imageio.ImageIO.read(is);
                if (raw != null) {
                    java.awt.image.BufferedImage resized = new java.awt.image.BufferedImage(48, 48, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D g2 = resized.createGraphics();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(raw, 0, 0, 48, 48, null);
                    g2.dispose();
                    lblLogo.setIcon(new javax.swing.ImageIcon(resized));
                    lblLogo.setText("");
                    setIconImage(raw);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void customInit() {
        setLocationRelativeTo(null);

        // Estilo de botones de operaciones para alta legibilidad y contraste
        for (javax.swing.JButton b : new javax.swing.JButton[]{btnDeposito, btnRetiro, btnTransferir, btnSimular3, btnDesbloquear}) {
            b.setForeground(java.awt.Color.WHITE);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        UsuarioSesion s = controlador.getSesionActual();
        if (s != null && s.isAutenticado()) {
            lblUser.setText("Operador: " + s.getNombreCompleto() + " (" + s.getRol() + ") | Ventanilla: " + controlador.getVentanillaActiva());
            cmbVentanillaTop.setSelectedIndex(Math.max(0, controlador.getVentanillaActiva() - 1));
        }

        // ConfiguraciÃ³n de tabla de cuentas
        modeloCuentas = (DefaultTableModel) tablaCuentas.getModel();
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCuentas.setDefaultRenderer(Object.class, new MashupCellRenderer());
        tablaCuentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCuentas.getSelectedRow() >= 0) {
                String cta = String.valueOf(modeloCuentas.getValueAt(tablaCuentas.getSelectedRow(), 0));
                txtCuentaOp.setText(cta);
                cargarExtractoAsync(cta);
            }
        });

        // ConfiguraciÃ³n de tabla de extracto
        modeloMovimientos = (DefaultTableModel) tablaMovimientos.getModel();

        // Cargar sucursales en segundo plano
        cargarSucursalesAsync();

        // Timer de auto-refresco MashUp cada 2 segundos
        timerMashup = new Timer(2000, e -> {
            if (chkAutoMashup.isSelected() && !refrescando) {
                refrescarMashupAsync(false);
            }
        });
        timerMashup.start();

        // Cargar cuentas iniciales
        refrescarMashupAsync(true);
    }

    private void cargarSucursalesAsync() {
        SwingWorker<List<SucursalModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<SucursalModel> doInBackground() {
                return controlador.obtenerSucursales();
            }

            @Override
            protected void done() {
                try {
                    List<SucursalModel> lista = get();
                    cacheSucursales.clear();
                    cacheSucursales.addAll(lista);
                    cmbSucursal.removeAllItems();
                    cmbSucursal.addItem("0 - Todas las Sucursales");
                    for (SucursalModel suc : lista) {
                        cmbSucursal.addItem(suc.getIdSucursal() + " - " + suc.getNombre() + " (" + suc.getCiudad() + ")");
                    }
                } catch (Exception ignored) {
                }
            }
        };
        worker.execute();
    }

    private void refrescarMashupAsync(boolean limpiarExtracto) {
        refrescando = true;
        final int idSuc = obtenerIdSucursalSeleccionada();
        final String filtro = txtFiltro.getText().trim();

        SwingWorker<List<CuentaModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CuentaModel> doInBackground() {
                if (!filtro.isEmpty()) {
                    if (filtro.contains("-")) {
                        CuentaModel c = controlador.buscarCuentaPorNumero(filtro);
                        return c != null ? List.of(c) : List.of();
                    } else {
                        return controlador.buscarCuentasPorDni(filtro);
                    }
                }
                return controlador.obtenerCuentasPorSucursal(idSuc);
            }

            @Override
            protected void done() {
                try {
                    List<CuentaModel> lista = get();
                    actualizarTablaCuentas(lista);
                    if (limpiarExtracto && !lista.isEmpty()) {
                        tablaCuentas.setRowSelectionInterval(0, 0);
                    }
                } catch (Exception ignored) {
                } finally {
                    refrescando = false;
                }
            }
        };
        worker.execute();
    }

    private void actualizarTablaCuentas(List<CuentaModel> lista) {
        String seleccionPrevia = null;
        int row = tablaCuentas.getSelectedRow();
        if (row >= 0 && row < modeloCuentas.getRowCount()) {
            seleccionPrevia = String.valueOf(modeloCuentas.getValueAt(row, 0));
        }

        modeloCuentas.setRowCount(0);
        int rojas = 0;
        int restoredIndex = -1;

        for (int i = 0; i < lista.size(); i++) {
            CuentaModel c = lista.get(i);
            boolean esRojo = "ROJO".equalsIgnoreCase(c.getDisponibilidadMashup());
            if (esRojo) rojas++;

            String badge = esRojo ? "ROJO (BLOQUEADA)" : "VERDE (LIBRE)";
            String detalle = esRojo
                    ? ("Ventanilla " + c.getVentanillaBloqueo() + " (" + (c.getBloqueadoPor() != null ? c.getBloqueadoPor() : "Activa") + ")")
                    : "Disponible para operar";

            modeloCuentas.addRow(new Object[]{
                c.getNumeroCuenta(),
                c.getTitularCompleto(),
                c.getDniCliente(),
                c.getNombreSucursal(),
                String.format("$ %.2f", c.getSaldo()),
                badge,
                detalle
            });

            if (seleccionPrevia != null && seleccionPrevia.equals(c.getNumeroCuenta())) {
                restoredIndex = i;
            }
        }

        if (restoredIndex >= 0) {
            tablaCuentas.setRowSelectionInterval(restoredIndex, restoredIndex);
        }

        actualizarEstadoBarra(lista.size(), rojas);
    }

    private void actualizarEstadoBarra(int total, int rojas) {
        lblEstadoBarra.setText(String.format("Sincronizaci\u00f3n MashUp activa | Total cuentas: %d | Cuentas bloqueadas en ROJO: %d",
                total, rojas));
    }

    private void cargarExtractoAsync(String numeroCuenta) {
        SwingWorker<List<MovimientoModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MovimientoModel> doInBackground() {
                return controlador.obtenerExtracto(numeroCuenta);
            }

            @Override
            protected void done() {
                try {
                    List<MovimientoModel> movs = get();
                    modeloMovimientos.setRowCount(0);
                    for (MovimientoModel m : movs) {
                        modeloMovimientos.addRow(new Object[]{
                            m.getNumeroOperacion(),
                            m.getFechaHora(),
                            m.getTipo(),
                            String.format("$ %.2f", m.getMonto()),
                            String.format("$ %.2f", m.getSaldoResultante()),
                            "V" + m.getVentanilla() + " - " + m.getNombreEmpleado(),
                            m.getDescripcion()
                        });
                    }
                } catch (Exception ignored) {
                }
            }
        };
        worker.execute();
    }

    private int obtenerIdSucursalSeleccionada() {
        int idx = cmbSucursal.getSelectedIndex();
        if (idx <= 0 || (idx - 1) >= cacheSucursales.size()) {
            return 0;
        }
        return cacheSucursales.get(idx - 1).getIdSucursal();
    }

    private void registrarLog(String msg) {
        String hora = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        txtBitacora.append("[" + hora + "] " + msg + "\n");
        txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblBanner = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jLabelVentanilla = new javax.swing.JLabel();
        cmbVentanillaTop = new javax.swing.JComboBox<>();
        chkAutoMashup = new javax.swing.JCheckBox();
        btnCerrarSesion = new javax.swing.JButton();
        pnlFiltros = new javax.swing.JPanel();
        jLabelSucursal = new javax.swing.JLabel();
        cmbSucursal = new javax.swing.JComboBox<>();
        jLabelFiltro = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnVerTodas = new javax.swing.JButton();
        jScrollPaneCuentas = new javax.swing.JScrollPane();
        tablaCuentas = new javax.swing.JTable();
        jLabelExtracto = new javax.swing.JLabel();
        jScrollPaneMovimientos = new javax.swing.JScrollPane();
        tablaMovimientos = new javax.swing.JTable();
        pnlOperaciones = new javax.swing.JPanel();
        jLabelCtaOp = new javax.swing.JLabel();
        txtCuentaOp = new javax.swing.JTextField();
        jLabelCtaDestino = new javax.swing.JLabel();
        txtCuentaDestino = new javax.swing.JTextField();
        jLabelMonto = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        btnDeposito = new javax.swing.JButton();
        btnRetiro = new javax.swing.JButton();
        btnTransferir = new javax.swing.JButton();
        btnSimular3 = new javax.swing.JButton();
        btnDesbloquear = new javax.swing.JButton();
        jScrollPaneBitacora = new javax.swing.JScrollPane();
        txtBitacora = new javax.swing.JTextArea();
        lblEstadoBarra = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EurekaBank - Supervisi\u00f3n MashUp & Banca Concurrente");

        pnlHeader.setBackground(new java.awt.Color(15, 23, 42));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sullivan.png"))); // NOI18N
        lblLogo.setPreferredSize(new java.awt.Dimension(48, 48));

        lblBanner.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBanner.setForeground(new java.awt.Color(255, 255, 255));
        lblBanner.setText("EUREKABANK  |  SUPERVISI\u00d3N MASHUP & BANCA WEB");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblUser.setForeground(new java.awt.Color(148, 163, 180));
        lblUser.setText("Operador: James P. Sullivan (Monster Admin) | Superadmin Monster");

        jLabelVentanilla.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabelVentanilla.setForeground(new java.awt.Color(241, 232, 226));
        jLabelVentanilla.setText("Terminal:");

        cmbVentanillaTop.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cmbVentanillaTop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4" }));
        cmbVentanillaTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVentanillaTopActionPerformed(evt);
            }
        });

        chkAutoMashup.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        chkAutoMashup.setForeground(new java.awt.Color(74, 222, 128));
        chkAutoMashup.setSelected(true);
        chkAutoMashup.setText("Sincronizaci\u00f3n MashUp (2s)");
        chkAutoMashup.setOpaque(false);
        chkAutoMashup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAutoMashupActionPerformed(evt);
            }
        });

        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnCerrarSesion.setText("Cerrar Sesi\u00f3n");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBanner)
                    .addComponent(lblUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                .addComponent(jLabelVentanilla)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbVentanillaTop, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chkAutoMashup)
                .addGap(18, 18, 18)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlHeaderLayout.createSequentialGroup()
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBanner)
                            .addComponent(jLabelVentanilla)
                            .addComponent(cmbVentanillaTop, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkAutoMashup)
                            .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pnlFiltros.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        jLabelSucursal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelSucursal.setText("Sucursal:");

        cmbSucursal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cmbSucursal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0 - Todas las Sucursales" }));
        cmbSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSucursalActionPerformed(evt);
            }
        });

        jLabelFiltro.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelFiltro.setText("Buscar:");

        txtFiltro.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        btnBuscar.setText("Consultar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnVerTodas.setText("Ver Todas");
        btnVerTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTodasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltrosLayout = new javax.swing.GroupLayout(pnlFiltros);
        pnlFiltros.setLayout(pnlFiltrosLayout);
        pnlFiltrosLayout.setHorizontalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabelSucursal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelFiltro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVerTodas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFiltrosLayout.setVerticalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSucursal)
                    .addComponent(cmbSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFiltro)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnVerTodas))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jScrollPaneCuentas.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuentas Bancarias y Sem\u00e1foro de Concurrencia MashUp (VERDE = Libre | ROJO = Bloqueada)"));

        tablaCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cuenta", "Titular", "DNI", "Sucursal", "Saldo USD", "Sem\u00e1foro MashUp", "Detalle Concurrencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCuentas.setRowHeight(28);
        tablaCuentas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneCuentas.setViewportView(tablaCuentas);

        jLabelExtracto.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelExtracto.setText("Extracto Hist\u00f3rico de Movimientos (Orden Cronol\u00f3gico Descendente):");

        jScrollPaneMovimientos.setBorder(javax.swing.BorderFactory.createTitledBorder("Movimientos de la Cuenta Seleccionada"));

        tablaMovimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Operaci\u00f3n", "Fecha y Hora", "Tipo", "Monto USD", "Saldo Resultante", "Cajero / Vent.", "Descripci\u00f3n"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaMovimientos.setRowHeight(24);
        jScrollPaneMovimientos.setViewportView(tablaMovimientos);

        pnlOperaciones.setBackground(new java.awt.Color(255, 255, 255));
        pnlOperaciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones Transaccionales ACID"));

        jLabelCtaOp.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelCtaOp.setText("Cuenta Objetivo / Origen:");

        txtCuentaOp.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtCuentaOp.setText("001-0001");

        jLabelCtaDestino.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelCtaDestino.setText("Cuenta Destino (Transferencia):");

        txtCuentaDestino.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtCuentaDestino.setText("001-0002");

        jLabelMonto.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelMonto.setText("Monto USD ($):");

        txtMonto.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtMonto.setText("100.00");

        btnDeposito.setBackground(new java.awt.Color(22, 163, 74));
        btnDeposito.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnDeposito.setForeground(new java.awt.Color(22, 101, 52));
        btnDeposito.setText("Registrar Dep\u00f3sito (+)");
        btnDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositoActionPerformed(evt);
            }
        });

        btnRetiro.setBackground(new java.awt.Color(220, 38, 38));
        btnRetiro.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnRetiro.setForeground(new java.awt.Color(185, 28, 28));
        btnRetiro.setText("Registrar Retiro (-)");
        btnRetiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetiroActionPerformed(evt);
            }
        });

        btnTransferir.setBackground(new java.awt.Color(37, 99, 235));
        btnTransferir.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnTransferir.setForeground(new java.awt.Color(29, 78, 216));
        btnTransferir.setText("Transferir entre Cuentas");
        btnTransferir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferirActionPerformed(evt);
            }
        });

        btnSimular3.setBackground(new java.awt.Color(124, 58, 237));
        btnSimular3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimular3.setForeground(new java.awt.Color(109, 40, 217));
        btnSimular3.setText("Simular 3 Ventanillas Concurrentes");
        btnSimular3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimular3ActionPerformed(evt);
            }
        });

        btnDesbloquear.setBackground(new java.awt.Color(55, 65, 81));
        btnDesbloquear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDesbloquear.setForeground(new java.awt.Color(55, 65, 81));
        btnDesbloquear.setText("Desbloquear Cuenta (Admin Monster)");
        btnDesbloquear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesbloquearActionPerformed(evt);
            }
        });

        jScrollPaneBitacora.setBorder(javax.swing.BorderFactory.createTitledBorder("Bit\u00e1cora de Concurrencia y Auditor\u00eda"));

        txtBitacora.setEditable(false);
        txtBitacora.setBackground(new java.awt.Color(15, 20, 30));
        txtBitacora.setColumns(20);
        txtBitacora.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        txtBitacora.setForeground(new java.awt.Color(241, 232, 226));
        txtBitacora.setRows(5);
        jScrollPaneBitacora.setViewportView(txtBitacora);

        javax.swing.GroupLayout pnlOperacionesLayout = new javax.swing.GroupLayout(pnlOperaciones);
        pnlOperaciones.setLayout(pnlOperacionesLayout);
        pnlOperacionesLayout.setHorizontalGroup(
            pnlOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOperacionesLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCtaOp)
                    .addComponent(txtCuentaOp)
                    .addComponent(jLabelCtaDestino)
                    .addComponent(txtCuentaDestino)
                    .addComponent(jLabelMonto)
                    .addComponent(txtMonto)
                    .addComponent(btnDeposito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRetiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTransferir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSimular3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .addComponent(btnDesbloquear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneBitacora))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnlOperacionesLayout.setVerticalGroup(
            pnlOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOperacionesLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabelCtaOp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCuentaOp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCtaDestino)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCuentaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMonto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimular3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDesbloquear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneBitacora, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblEstadoBarra.setBackground(new java.awt.Color(241, 232, 226));
        lblEstadoBarra.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblEstadoBarra.setForeground(new java.awt.Color(30, 51, 71));
        lblEstadoBarra.setText("Sincronizaci\u00f3n MashUp activa | Total cuentas: 0 | Cuentas bloqueadas en ROJO: 0");
        lblEstadoBarra.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 12));
        lblEstadoBarra.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblEstadoBarra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneCuentas, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addComponent(jLabelExtracto)
                    .addComponent(jScrollPaneMovimientos))
                .addGap(12, 12, 12)
                .addComponent(pnlOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPaneCuentas, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabelExtracto)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPaneMovimientos, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                    .addComponent(pnlOperaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addComponent(lblEstadoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbVentanillaTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbVentanillaTopActionPerformed
        int v = cmbVentanillaTop.getSelectedIndex() + 1;
        controlador.setVentanillaActiva(v);
        UsuarioSesion s = controlador.getSesionActual();
        if (s != null) {
            lblUser.setText("Operador: " + s.getNombreCompleto() + " (" + s.getRol() + ") | Ventanilla: " + v);
        }
        registrarLog("Ventanilla local cambiada a: Ventanilla " + v);
    }//GEN-LAST:event_cmbVentanillaTopActionPerformed

    private void chkAutoMashupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAutoMashupActionPerformed
        if (chkAutoMashup.isSelected()) {
            registrarLog("Auto-sincronizaci\u00f3n MashUp activada (2.0s)");
            if (!timerMashup.isRunning()) timerMashup.start();
        } else {
            registrarLog("Auto-sincronizaci\u00f3n MashUp pausada");
            if (timerMashup.isRunning()) timerMashup.stop();
        }
    }//GEN-LAST:event_chkAutoMashupActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int resp = JOptionPane.showConfirmDialog(this,
                "\u00bfEst\u00e1 seguro que desea cerrar la sesi\u00f3n?",
                "Cerrar Sesi\u00f3n",
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            if (timerMashup != null) timerMashup.stop();
            controlador.cerrarSesion();
            new LoginView(controlador).setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void cmbSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSucursalActionPerformed
        refrescarMashupAsync(true);
    }//GEN-LAST:event_cmbSucursalActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        refrescarMashupAsync(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnVerTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTodasActionPerformed
        txtFiltro.setText("");
        cmbSucursal.setSelectedIndex(0);
        refrescarMashupAsync(true);
    }//GEN-LAST:event_btnVerTodasActionPerformed

    private void btnDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositoActionPerformed
        final String cta = txtCuentaOp.getText().trim();
        final double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto v\u00e1lido mayor a cero.", "Monto Inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnDeposito.setEnabled(false);
        registrarLog("-> Solicitando Dep\u00f3sito $" + monto + " en cuenta " + cta + " (Bloqueo ROJO)...");

        SwingWorker<ResultadoOperacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return controlador.depositar(cta, monto);
            }

            @Override
            protected void done() {
                btnDeposito.setEnabled(true);
                try {
                    ResultadoOperacion r = get();
                    if (r.isExito()) {
                        registrarLog("[APROBADO] " + r.getMensaje() + " | Saldo: $" + r.getSaldoActual());
                        refrescarMashupAsync(false);
                        cargarExtractoAsync(cta);
                    } else {
                        registrarLog("[RECHAZADO] " + r.getMensaje());
                        JOptionPane.showMessageDialog(DashboardView.this, r.getMensaje(), "Operaci\u00f3n Rechazada", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    registrarLog("[ERROR RED] " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }//GEN-LAST:event_btnDepositoActionPerformed

    private void btnRetiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetiroActionPerformed
        final String cta = txtCuentaOp.getText().trim();
        final double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto v\u00e1lido mayor a cero.", "Monto Inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnRetiro.setEnabled(false);
        registrarLog("-> Solicitando Retiro $" + monto + " en cuenta " + cta + " (Validando fondos + Bloqueo ROJO)...");

        SwingWorker<ResultadoOperacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return controlador.retirar(cta, monto);
            }

            @Override
            protected void done() {
                btnRetiro.setEnabled(true);
                try {
                    ResultadoOperacion r = get();
                    if (r.isExito()) {
                        registrarLog("[APROBADO] " + r.getMensaje() + " | Saldo: $" + r.getSaldoActual());
                        refrescarMashupAsync(false);
                        cargarExtractoAsync(cta);
                    } else {
                        registrarLog("[RECHAZADO] " + r.getMensaje());
                        JOptionPane.showMessageDialog(DashboardView.this, r.getMensaje(), "Operaci\u00f3n Rechazada", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    registrarLog("[ERROR RED] " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }//GEN-LAST:event_btnRetiroActionPerformed

    private void btnTransferirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferirActionPerformed
        final String orig = txtCuentaOp.getText().trim();
        final String dest = txtCuentaDestino.getText().trim();
        final double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto v\u00e1lido mayor a cero.", "Monto Inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (orig.equalsIgnoreCase(dest)) {
            JOptionPane.showMessageDialog(this, "La cuenta origen y destino deben ser distintas.", "Cuentas Iguales", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnTransferir.setEnabled(false);
        registrarLog("-> Solicitando Transferencia $" + monto + " de " + orig + " hacia " + dest + "...");

        SwingWorker<ResultadoOperacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return controlador.transferir(orig, dest, monto);
            }

            @Override
            protected void done() {
                btnTransferir.setEnabled(true);
                try {
                    ResultadoOperacion r = get();
                    if (r.isExito()) {
                        registrarLog("[APROBADO] " + r.getMensaje() + " | Saldo Origen: $" + r.getSaldoActual());
                        refrescarMashupAsync(false);
                        cargarExtractoAsync(orig);
                    } else {
                        registrarLog("[RECHAZADO] " + r.getMensaje());
                        JOptionPane.showMessageDialog(DashboardView.this, r.getMensaje(), "Transferencia Rechazada", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    registrarLog("[ERROR RED] " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }//GEN-LAST:event_btnTransferirActionPerformed

    private void btnSimular3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimular3ActionPerformed
        final String cta = txtCuentaOp.getText().trim();
        final double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto v\u00e1lido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnSimular3.setEnabled(false);
        registrarLog("=== INICIANDO SIMULACI\u00d3N CONCURRENTE (3 VENTANILLAS) SOBRE CUENTA " + cta + " ===");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                ExecutorService pool = Executors.newFixedThreadPool(4);
                try {
                    List<Future<ResultadoOperacion>> futures = new ArrayList<>();
                    for (int v = 1; v <= 3; v++) {
                        final int vent = v;
                        futures.add(pool.submit(() -> {
                            if (vent > 1) {
                                Thread.sleep(300L * vent);
                            }
                            return controlador.depositarDesdeVentanilla(cta, monto, vent);
                        }));
                    }

                    Thread.sleep(350);
                    SwingUtilities.invokeLater(() -> refrescarMashupAsync(false));

                    for (int i = 0; i < futures.size(); i++) {
                        int vent = i + 1;
                        try {
                            ResultadoOperacion r = futures.get(i).get();
                            String status = r.isExito() ? "[APROBADO]" : "[BLOQUEO/CONCURRENCIA]";
                            publish("-> Ventanilla " + vent + ": " + status + " " + r.getMensaje() + " | Nuevo saldo: $" + r.getSaldoActual());
                        } catch (Exception ex) {
                            publish("-> Ventanilla " + vent + ": [ERROR] " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    publish("[ERROR GENERAL SIMULACI\u00d3N] " + ex.getMessage());
                } finally {
                    pool.shutdown();
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String c : chunks) {
                    registrarLog(c);
                }
            }

            @Override
            protected void done() {
                btnSimular3.setEnabled(true);
                registrarLog("--- SIMULACI\u00d3N DE CONCURRENCIA COMPLETADA ---");
                refrescarMashupAsync(false);
                cargarExtractoAsync(cta);
            }
        };
        worker.execute();
    }//GEN-LAST:event_btnSimular3ActionPerformed

    private void btnDesbloquearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesbloquearActionPerformed
        final String cta = txtCuentaOp.getText().trim();
        registrarLog("-> Solicitando Desbloqueo Administrativo para cuenta " + cta + "...");

        SwingWorker<ResultadoOperacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return controlador.desbloquearCuenta(cta);
            }

            @Override
            protected void done() {
                try {
                    ResultadoOperacion r = get();
                    registrarLog("[" + (r.isExito() ? "DESBLOQUEADA" : "FALLO") + "] " + r.getMensaje());
                    refrescarMashupAsync(false);
                } catch (Exception ex) {
                    registrarLog("[ERROR DESBLOQUEO] " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }//GEN-LAST:event_btnDesbloquearActionPerformed

    /**
     * Renderer visual para la tabla de cuentas del monitor MashUp (SemÃ¡foro VERDE / ROJO).
     */
    private static class MashupCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String estado = String.valueOf(table.getModel().getValueAt(row, 5));
            boolean esRojo = estado.contains("ROJO");

            if (isSelected) {
                c.setBackground(new Color(2, 132, 199));
                c.setForeground(Color.WHITE);
                setFont(getFont().deriveFont(Font.BOLD));
            } else {
                if (column == 5) {
                    if (esRojo) {
                        c.setForeground(new Color(220, 38, 38));
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(new Color(22, 163, 74));
                        setFont(getFont().deriveFont(Font.BOLD));
                    }
                    c.setBackground(Color.WHITE);
                } else {
                    c.setForeground(new Color(15, 23, 42));
                    setFont(getFont().deriveFont(Font.PLAIN));
                    if (esRojo) {
                        c.setBackground(new Color(254, 242, 242));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
            }
            return c;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName()) || "Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        java.awt.EventQueue.invokeLater(() -> {
            new DashboardView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDeposito;
    private javax.swing.JButton btnDesbloquear;
    private javax.swing.JButton btnRetiro;
    private javax.swing.JButton btnSimular3;
    private javax.swing.JButton btnTransferir;
    private javax.swing.JButton btnVerTodas;
    private javax.swing.JCheckBox chkAutoMashup;
    private javax.swing.JComboBox<String> cmbSucursal;
    private javax.swing.JComboBox<String> cmbVentanillaTop;
    private javax.swing.JLabel jLabelCtaDestino;
    private javax.swing.JLabel jLabelCtaOp;
    private javax.swing.JLabel jLabelExtracto;
    private javax.swing.JLabel jLabelFiltro;
    private javax.swing.JLabel jLabelMonto;
    private javax.swing.JLabel jLabelSucursal;
    private javax.swing.JLabel jLabelVentanilla;
    private javax.swing.JScrollPane jScrollPaneBitacora;
    private javax.swing.JScrollPane jScrollPaneCuentas;
    private javax.swing.JScrollPane jScrollPaneMovimientos;
    private javax.swing.JLabel lblBanner;
    private javax.swing.JLabel lblEstadoBarra;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlOperaciones;
    private javax.swing.JTable tablaCuentas;
    private javax.swing.JTable tablaMovimientos;
    private javax.swing.JTextArea txtBitacora;
    private javax.swing.JTextField txtCuentaDestino;
    private javax.swing.JTextField txtCuentaOp;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtMonto;
    // End of variables declaration//GEN-END:variables
}