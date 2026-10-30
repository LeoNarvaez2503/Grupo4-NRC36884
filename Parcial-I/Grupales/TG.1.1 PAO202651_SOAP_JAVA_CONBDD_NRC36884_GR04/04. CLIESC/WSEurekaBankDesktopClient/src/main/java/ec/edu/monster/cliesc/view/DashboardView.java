package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.CuentaModel;
import ec.edu.monster.cliesc.model.MovimientoModel;
import ec.edu.monster.cliesc.model.ResultadoOperacion;
import ec.edu.monster.cliesc.model.SucursalModel;
import ec.edu.monster.cliesc.model.UsuarioSesion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Capa Vista (Patrón MVC Tradicional): Consola Gráfica de Ventanillas y Monitor MashUp.
 * Sincroniza en tiempo real el semáforo cromático VERDE (Libre) / ROJO (En Proceso).
 */
public class DashboardView extends JFrame {

    private final AppControlador controlador;
    private final DefaultTableModel modeloCuentas;
    private final DefaultTableModel modeloMovimientos;
    private final JTable tablaCuentas;
    private final JComboBox<String> cmbSucursal = new JComboBox<>();
    private final JComboBox<String> cmbVentanilla = new JComboBox<>(new String[]{
        "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4"
    });
    private final JCheckBox chkAutoMashup = new JCheckBox("Sincronización MashUp en Vivo (2s)", true);
    private final JTextField txtFiltro = new JTextField(14);
    private final JTextField txtCuentaOp = new JTextField("001-0001", 14);
    private final JTextField txtCuentaDestino = new JTextField("001-0002", 14);
    private final JTextField txtMonto = new JTextField("100.00", 14);
    private final JTextArea txtBitacora = new JTextArea(6, 26);
    private final JLabel lblEstadoBarra = new JLabel("Conectado al servidor EurekaBank");

    private final List<SucursalModel> cacheSucursales = new ArrayList<>();
    private Timer timerMashup;
    private boolean refrescando = false;

    public DashboardView(AppControlador controlador) {
        this.controlador = controlador;
        this.modeloCuentas = new DefaultTableModel(
                new Object[]{"Cuenta", "Titular", "DNI", "Sucursal", "Saldo (USD)", "Semáforo MashUp", "Detalle Bloqueo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        this.modeloMovimientos = new DefaultTableModel(
                new Object[]{"Nro. Operación", "Tipo", "Monto (USD)", "Saldo Resultante", "Ventanilla / Cajero", "Referencia", "Fecha / Hora"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        this.tablaCuentas = new JTable(modeloCuentas);
        configurarInterfaz();
        cargarSucursalesYCuentas();
        iniciarPollingMashup();
    }

    private void configurarInterfaz() {
        UsuarioSesion s = controlador.getSesionActual();
        setTitle("EurekaBank - Estación Transaccional & Monitor MashUp | " + s.getNombreCompleto() + " [" + s.getRol() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 760);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(241, 245, 249));
        root.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // Barra Superior
        JPanel topBar = new JPanel(new BorderLayout(12, 6));
        topBar.setBackground(new Color(15, 23, 42));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTop.setOpaque(false);

        JLabel lblLogoTop = new JLabel();
        URL imgTopUrl = getClass().getClassLoader().getResource("images/sullivan2.png");
        if (imgTopUrl != null) {
            Image img = new ImageIcon(imgTopUrl).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblLogoTop.setIcon(new ImageIcon(img));
        }
        leftTop.add(lblLogoTop);

        JLabel lblUser = new JLabel("EUREKABANK GR04  |  Operador: " + s.getNombreCompleto() + " (" + s.getRol() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUser.setForeground(Color.WHITE);
        leftTop.add(lblUser);

        topBar.add(leftTop, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);

        JLabel lblVent = new JLabel("Terminal:");
        lblVent.setForeground(new Color(148, 163, 184));
        rightTop.add(lblVent);

        cmbVentanilla.setSelectedIndex(Math.max(0, controlador.getVentanillaActiva() - 1));
        cmbVentanilla.addActionListener(e -> {
            controlador.setVentanillaActiva(cmbVentanilla.getSelectedIndex() + 1);
            registrarLog("Terminal cambiada a Ventanilla " + controlador.getVentanillaActiva());
        });
        rightTop.add(cmbVentanilla);

        chkAutoMashup.setOpaque(false);
        chkAutoMashup.setForeground(new Color(74, 222, 128));
        rightTop.add(chkAutoMashup);

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.addActionListener(e -> cerrarSesion());
        rightTop.add(btnSalir);

        topBar.add(rightTop, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // Panel Central Izquierdo: Tabla MashUp + Extracto
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setOpaque(false);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        filterBar.setBackground(Color.WHITE);
        filterBar.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        filterBar.add(new JLabel("Sucursal:"));
        cmbSucursal.addActionListener(e -> refrescarCuentasAsync());
        filterBar.add(cmbSucursal);

        filterBar.add(new JLabel("Buscar Cuenta/DNI:"));
        filterBar.add(txtFiltro);
        JButton btnBuscar = new JButton("Consultar");
        btnBuscar.addActionListener(e -> buscarCuentaEspecifica());
        filterBar.add(btnBuscar);

        JButton btnVerTodas = new JButton("Actualizar MashUp");
        btnVerTodas.addActionListener(e -> {
            txtFiltro.setText("");
            refrescarCuentasAsync();
        });
        filterBar.add(btnVerTodas);

        leftPanel.add(filterBar, BorderLayout.NORTH);

        tablaCuentas.setRowHeight(30);
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCuentas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCuentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCuentas.setDefaultRenderer(Object.class, new MashupCellRenderer());
        tablaCuentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCuentas.getSelectedRow() >= 0) {
                String cta = String.valueOf(modeloCuentas.getValueAt(tablaCuentas.getSelectedRow(), 0));
                txtCuentaOp.setText(cta);
                cargarExtractoAsync(cta);
            }
        });

        JScrollPane scrollCuentas = new JScrollPane(tablaCuentas);
        scrollCuentas.setBorder(BorderFactory.createTitledBorder("Cuentas Bancarias y Semáforo de Concurrencia MashUp (VERDE = Libre | ROJO = Bloqueada en Ventanilla)"));

        JTable tablaMovs = new JTable(modeloMovimientos);
        tablaMovs.setRowHeight(26);
        tablaMovs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaMovs.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollMovs = new JScrollPane(tablaMovs);
        scrollMovs.setBorder(BorderFactory.createTitledBorder("Extracto Histórico de Movimientos (Orden Cronológico Descendente)"));

        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCuentas, scrollMovs);
        splitVertical.setDividerLocation(330);
        splitVertical.setResizeWeight(0.55);
        leftPanel.add(splitVertical, BorderLayout.CENTER);

        // Panel Derecho: Operaciones Transaccionales
        JPanel rightPanel = crearPanelOperaciones();
        rightPanel.setPreferredSize(new Dimension(370, 600));

        root.add(leftPanel, BorderLayout.CENTER);
        root.add(rightPanel, BorderLayout.EAST);

        lblEstadoBarra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstadoBarra.setForeground(new Color(51, 65, 85));
        root.add(lblEstadoBarra, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel crearPanelOperaciones() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Operaciones de Ventanilla
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Cuenta Objetivo / Origen:"), gbc);
        gbc.gridy = 1;
        txtCuentaOp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        form.add(txtCuentaOp, gbc);

        gbc.gridy = 2;
        form.add(new JLabel("Cuenta Destino (solo Transferencia):"), gbc);
        gbc.gridy = 3;
        txtCuentaDestino.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(txtCuentaDestino, gbc);

        gbc.gridy = 4;
        form.add(new JLabel("Monto de Operación (USD):"), gbc);
        gbc.gridy = 5;
        txtMonto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        form.add(txtMonto, gbc);

        JButton btnDeposito = crearBoton("Registrar Depósito (+)", new Color(22, 163, 74));
        btnDeposito.addActionListener(e -> ejecutarTransaccion("DEPOSITO"));
        gbc.gridy = 6;
        form.add(btnDeposito, gbc);

        JButton btnRetiro = crearBoton("Registrar Retiro (-)", new Color(220, 38, 38));
        btnRetiro.addActionListener(e -> ejecutarTransaccion("RETIRO"));
        gbc.gridy = 7;
        form.add(btnRetiro, gbc);

        JButton btnTransferir = crearBoton("Transferir entre Cuentas", new Color(37, 99, 235));
        btnTransferir.addActionListener(e -> ejecutarTransaccion("TRANSFERENCIA"));
        gbc.gridy = 8;
        form.add(btnTransferir, gbc);

        JButton btnSimular3 = crearBoton("Simular 3 Ventanillas Simultáneas", new Color(124, 58, 237));
        btnSimular3.addActionListener(e -> simularConcurrenciaTresVentanillas());
        gbc.gridy = 9;
        form.add(btnSimular3, gbc);

        JButton btnDesbloquear = crearBoton("Desbloquear Cuenta (Admin Monster)", new Color(71, 85, 105));
        btnDesbloquear.addActionListener(e -> ejecutarDesbloqueo());
        gbc.gridy = 10;
        form.add(btnDesbloquear, gbc);

        tabs.addTab("Operaciones ACID", form);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 6, 4));

        JLabel lblMascota = new JLabel();
        URL imgOpUrl = getClass().getClassLoader().getResource("images/sullivan.png");
        if (imgOpUrl != null) {
            Image imgOp = new ImageIcon(imgOpUrl).getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            lblMascota.setIcon(new ImageIcon(imgOp));
        }
        JLabel lblHeaderOp = new JLabel(" EurekaBank Terminal Operativa");
        lblHeaderOp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHeaderOp.setForeground(new Color(30, 41, 59));

        JPanel innerHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        innerHeader.setOpaque(false);
        innerHeader.add(lblHeaderOp);
        innerHeader.add(lblMascota);
        headerPanel.add(innerHeader, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(tabs, BorderLayout.CENTER);

        panel.add(northPanel, BorderLayout.NORTH);

        txtBitacora.setEditable(false);
        txtBitacora.setLineWrap(true);
        txtBitacora.setWrapStyleWord(true);
        txtBitacora.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtBitacora.setBackground(new Color(15, 23, 42));
        txtBitacora.setForeground(new Color(226, 232, 240));
        JScrollPane scrollLog = new JScrollPane(txtBitacora);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Bitácora de Concurrencia y Auditoría"));
        panel.add(scrollLog, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(280, 35));
        return b;
    }

    private void cargarSucursalesYCuentas() {
        new SwingWorker<List<SucursalModel>, Void>() {
            @Override
            protected List<SucursalModel> doInBackground() {
                return controlador.obtenerSucursales();
            }

            @Override
            protected void done() {
                try {
                    cacheSucursales.clear();
                    cacheSucursales.addAll(get());
                    cmbSucursal.removeAllItems();
                    cmbSucursal.addItem("0 - Todas las Sucursales");
                    for (SucursalModel s : cacheSucursales) {
                        cmbSucursal.addItem(s.getIdSucursal() + " - " + s.getNombre() + " (" + s.getCiudad() + ")");
                    }
                    refrescarCuentasAsync();
                } catch (Exception e) {
                    registrarLog("Error cargando sucursales: " + e.getMessage());
                }
            }
        }.execute();
    }

    private int obtenerSucursalSeleccionada() {
        int idx = cmbSucursal.getSelectedIndex();
        if (idx <= 0 || idx > cacheSucursales.size()) {
            return 0;
        }
        return cacheSucursales.get(idx - 1).getIdSucursal();
    }

    private void refrescarCuentasAsync() {
        if (refrescando || !txtFiltro.getText().trim().isEmpty()) {
            return;
        }
        refrescando = true;
        final int sucId = obtenerSucursalSeleccionada();
        final String cuentaSeleccionada = tablaCuentas.getSelectedRow() >= 0
                ? String.valueOf(modeloCuentas.getValueAt(tablaCuentas.getSelectedRow(), 0))
                : txtCuentaOp.getText().trim();

        new SwingWorker<List<CuentaModel>, Void>() {
            @Override
            protected List<CuentaModel> doInBackground() {
                return controlador.obtenerCuentasPorSucursal(sucId);
            }

            @Override
            protected void done() {
                refrescando = false;
                try {
                    poblarTablaCuentas(get(), cuentaSeleccionada);
                } catch (Exception e) {
                    lblEstadoBarra.setText("Error sincronizando MashUp: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void poblarTablaCuentas(List<CuentaModel> cuentas, String cuentaSeleccionada) {
        modeloCuentas.setRowCount(0);
        int filaSel = -1;
        int rojas = 0;
        for (int i = 0; i < cuentas.size(); i++) {
            CuentaModel c = cuentas.get(i);
            boolean esRojo = "ROJO".equalsIgnoreCase(c.getDisponibilidadMashup());
            if (esRojo) rojas++;
            String estadoTxt = esRojo ? "ROJO (EN PROCESO)" : "VERDE (LIBRE)";
            String detalle = esRojo
                    ? "Ventanilla " + c.getVentanillaBloqueo() + " - " + (c.getBloqueadoPor() != null ? c.getBloqueadoPor() : "Ocupada")
                    : "Disponible para operar";
            modeloCuentas.addRow(new Object[]{
                c.getNumeroCuenta(),
                c.getTitularCompleto(),
                c.getDniCliente(),
                c.getNombreSucursal(),
                String.format("$ %.2f", c.getSaldo()),
                estadoTxt,
                detalle
            });
            if (c.getNumeroCuenta().equalsIgnoreCase(cuentaSeleccionada)) {
                filaSel = i;
            }
        }
        if (filaSel >= 0 && filaSel < modeloCuentas.getRowCount()) {
            tablaCuentas.setRowSelectionInterval(filaSel, filaSel);
        }
        lblEstadoBarra.setText("Sincronización MashUp activa | Total cuentas: " + cuentas.size() + " | Cuentas bloqueadas en ROJO: " + rojas);
    }

    private void buscarCuentaEspecifica() {
        String filtro = txtFiltro.getText().trim();
        if (filtro.isEmpty()) {
            refrescarCuentasAsync();
            return;
        }
        new SwingWorker<List<CuentaModel>, Void>() {
            @Override
            protected List<CuentaModel> doInBackground() {
                if (filtro.contains("-")) {
                    CuentaModel c = controlador.buscarCuentaPorNumero(filtro);
                    return c != null ? List.of(c) : List.of();
                }
                return controlador.buscarCuentasPorDni(filtro);
            }

            @Override
            protected void done() {
                try {
                    List<CuentaModel> lista = get();
                    poblarTablaCuentas(lista, filtro);
                    if (!lista.isEmpty()) {
                        cargarExtractoAsync(lista.get(0).getNumeroCuenta());
                    }
                } catch (Exception e) {
                    registrarLog("Error en búsqueda: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void cargarExtractoAsync(String numeroCuenta) {
        new SwingWorker<List<MovimientoModel>, Void>() {
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
                            m.getTipo(),
                            String.format("$ %.2f", m.getMonto()),
                            String.format("$ %.2f", m.getSaldoResultante()),
                            "V" + m.getVentanilla() + " - " + m.getNombreEmpleado(),
                            m.getCuentaReferencia() != null ? m.getCuentaReferencia() : "-",
                            m.getFechaHora()
                        });
                    }
                } catch (Exception e) {
                    registrarLog("Error cargando extracto: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void ejecutarTransaccion(String tipo) {
        final String cta = txtCuentaOp.getText().trim();
        final String dest = txtCuentaDestino.getText().trim();
        final double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        registrarLog("[V" + controlador.getVentanillaActiva() + "] Enviando " + tipo + " sobre " + cta + " ($" + monto + ")...");
        Timer quickPoll = new Timer(450, ev -> refrescarCuentasAsync());
        quickPoll.setRepeats(false);
        quickPoll.start();

        new SwingWorker<ResultadoOperacion, Void>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return switch (tipo) {
                    case "DEPOSITO" -> controlador.depositar(cta, monto);
                    case "RETIRO" -> controlador.retirar(cta, monto);
                    default -> controlador.transferir(cta, dest, monto);
                };
            }

            @Override
            protected void done() {
                try {
                    ResultadoOperacion r = get();
                    registrarLog((r.isExito() ? "✔ " : "✖ ") + r.getMensaje());
                    refrescarCuentasAsync();
                    cargarExtractoAsync(cta);
                    JOptionPane.showMessageDialog(DashboardView.this, r.getMensaje(),
                            r.isExito() ? "Transacción Exitosa" : "Transacción Rechazada (" + r.getCodigo() + ")",
                            r.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    registrarLog("✖ Error de comunicación: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void simularConcurrenciaTresVentanillas() {
        final String cta = txtCuentaOp.getText().trim();
        registrarLog("=== INICIANDO PRUEBA CONCURRENTE: Ventanillas 1, 2 y 3 sobre " + cta + " ===");

        new Thread(() -> {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            try {
                List<Future<String>> futuros = new ArrayList<>();
                for (int v = 1; v <= 3; v++) {
                    final int vent = v;
                    futuros.add(pool.submit(() -> {
                        if (vent > 1) Thread.sleep(220L * vent);
                        ResultadoOperacion r = controlador.depositarDesdeVentanilla(cta, 20.00, vent);
                        return "[Ventanilla " + vent + "] " + (r.isExito() ? "ÉXITO: " : "BLOQUEADA: ") + r.getMensaje();
                    }));
                }
                Thread.sleep(500);
                SwingUtilities.invokeLater(this::refrescarCuentasAsync);

                StringBuilder resumen = new StringBuilder();
                for (Future<String> f : futuros) {
                    String msg = f.get();
                    resumen.append(msg).append("\n\n");
                    SwingUtilities.invokeLater(() -> registrarLog(msg));
                }
                SwingUtilities.invokeLater(() -> {
                    refrescarCuentasAsync();
                    cargarExtractoAsync(cta);
                    JOptionPane.showMessageDialog(this, resumen.toString(),
                            "Resultado Escenario Crítico 3 Ventanillas", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> registrarLog("Error simulación: " + ex.getMessage()));
            } finally {
                pool.shutdownNow();
            }
        }).start();
    }

    private void ejecutarDesbloqueo() {
        final String cta = txtCuentaOp.getText().trim();
        new SwingWorker<ResultadoOperacion, Void>() {
            @Override
            protected ResultadoOperacion doInBackground() {
                return controlador.desbloquearCuenta(cta);
            }

            @Override
            protected void done() {
                try {
                    ResultadoOperacion r = get();
                    registrarLog("[ADMIN] " + r.getMensaje());
                    refrescarCuentasAsync();
                    JOptionPane.showMessageDialog(DashboardView.this, r.getMensaje(), "Desbloqueo Monster", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    registrarLog("Error en desbloqueo: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void iniciarPollingMashup() {
        timerMashup = new Timer(2000, e -> {
            if (chkAutoMashup.isSelected()) {
                refrescarCuentasAsync();
            }
        });
        timerMashup.start();
    }

    private void cerrarSesion() {
        if (timerMashup != null) {
            timerMashup.stop();
        }
        controlador.cerrarSesion();
        new LoginView(controlador).setVisible(true);
        dispose();
    }

    private void registrarLog(String linea) {
        txtBitacora.append(linea + "\n");
        txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
    }

    private static class MashupCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String estado = String.valueOf(table.getModel().getValueAt(row, 5));
            boolean rojo = estado.startsWith("ROJO");
            if (!isSelected) {
                if (rojo) {
                    c.setBackground(new Color(254, 226, 226));
                    c.setForeground(new Color(153, 27, 27));
                } else if (column == 5) {
                    c.setBackground(new Color(220, 252, 231));
                    c.setForeground(new Color(22, 101, 52));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(new Color(15, 23, 42));
                }
            }
            if (column == 5) {
                setFont(getFont().deriveFont(Font.BOLD));
            }
            return c;
        }
    }
}
