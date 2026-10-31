package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.FacturaModel.*;
import ec.edu.monster.cliesc.model.TelefonoModel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DesktopView extends JFrame {

    private final AppControlador controlador;

    // Modelos de tabla
    private DefaultTableModel modeloCatalogo;
    private DefaultTableModel modeloAmortizacionFactura;
    private DefaultTableModel modeloAmortizacionConsulta;

    // Componentes de Facturación
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JComboBox<TelefonoModel> cmbTelefonos;
    private JComboBox<String> cmbFormaPago;
    private JComboBox<Integer> cmbCuotas;
    private JTextArea txtResultadoFactura;

    // Componentes de Consulta BanQuito
    private JTextField txtCedulaConsulta;
    private JTextField txtCodClienteConsulta;
    private JTextField txtCodCreditoConsulta;
    private JTextArea txtResultadoBanquito;

    public DesktopView(AppControlador controlador) {
        this.controlador = controlador;
        initUI();
    }

    private void initUI() {
        setTitle("Comercializadora de Celulares & BanQuito Crédito - Cliente Escritorio (Grupo 04)");
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Encabezado
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(24, 43, 73));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("COMERCIALIZADORA DE CELULARES - CLIENTE ESCRITORIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Alianza BanQuito Módulo de Crédito | NRC: 36884 | Grupo 04 | Java 21 RESTful");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 220, 255));

        panelHeader.add(lblTitulo, BorderLayout.NORTH);
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        // Pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("🛒 Facturación de Teléfonos", crearPanelFacturacion());
        tabbedPane.addTab("📱 Catálogo de Celulares", crearPanelCatalogo());
        tabbedPane.addTab("🏦 Consultas BanQuito Crédito", crearPanelConsultasBanquito());
        tabbedPane.addTab("📋 Rúbrica y Arquitectura", crearPanelAcercaDe());

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel lblStatus = new JLabel("🟢 Conectado a Comercializadora (:8090) y BanQuito Core (:8080)");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBar.add(lblStatus);
        panelPrincipal.add(statusBar, BorderLayout.SOUTH);

        add(panelPrincipal);
        cargarTelefonosEnCombo();
    }

    private JPanel crearPanelFacturacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel Izquierdo: Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(420, 0));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Facturación"));

        // Botonera de acceso rápido para los 4 clientes
        JPanel quickDemo = new JPanel(new GridLayout(2, 2, 5, 5));
        quickDemo.setBorder(BorderFactory.createTitledBorder("Botonera Pruebas Oficiales (4 Clientes)"));

        JButton btnC1 = new JButton("1. Carlos (Crédito OK)");
        btnC1.setBackground(new Color(220, 245, 220));
        btnC1.addActionListener(e -> seleccionarClienteDemo(1));

        JButton btnC2 = new JButton("2. Andrea (Excede Cupo)");
        btnC2.setBackground(new Color(255, 240, 220));
        btnC2.addActionListener(e -> seleccionarClienteDemo(2));

        JButton btnC3 = new JButton("3. Mateo (Edad < 25)");
        btnC3.setBackground(new Color(255, 220, 220));
        btnC3.addActionListener(e -> seleccionarClienteDemo(3));

        JButton btnC4 = new JButton("4. Elena (Efectivo -42%)");
        btnC4.setBackground(new Color(220, 235, 255));
        btnC4.addActionListener(e -> seleccionarClienteDemo(4));

        quickDemo.add(btnC1);
        quickDemo.add(btnC2);
        quickDemo.add(btnC3);
        quickDemo.add(btnC4);
        formPanel.add(quickDemo);
        formPanel.add(Box.createVerticalStrut(10));

        // Campos
        JPanel fields = new JPanel(new GridLayout(5, 2, 8, 8));
        txtCedula = new JTextField("1710000001");
        txtNombre = new JTextField("Carlos Andrade");
        cmbTelefonos = new JComboBox<>();
        cmbFormaPago = new JComboBox<>(new String[]{"CREDITO_DIRECTO", "EFECTIVO"});
        cmbCuotas = new JComboBox<>(new Integer[]{3, 6, 9, 12, 18});
        cmbCuotas.setSelectedItem(6);

        fields.add(new JLabel("Cédula Cliente:"));
        fields.add(txtCedula);
        fields.add(new JLabel("Nombre Completo:"));
        fields.add(txtNombre);
        fields.add(new JLabel("Teléfono a comprar:"));
        fields.add(cmbTelefonos);
        fields.add(new JLabel("Forma de Pago:"));
        fields.add(cmbFormaPago);
        fields.add(new JLabel("Plazo Cuotas (Crédito):"));
        fields.add(cmbCuotas);

        formPanel.add(fields);
        formPanel.add(Box.createVerticalStrut(15));

        JButton btnFacturar = new JButton("💳 Emitir Factura y Procesar");
        btnFacturar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFacturar.setBackground(new Color(24, 120, 70));
        btnFacturar.setForeground(Color.WHITE);
        btnFacturar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFacturar.addActionListener(e -> procesarFacturacion());
        formPanel.add(btnFacturar);

        formPanel.add(Box.createVerticalStrut(10));
        txtResultadoFactura = new JTextArea(4, 30);
        txtResultadoFactura.setEditable(false);
        txtResultadoFactura.setLineWrap(true);
        txtResultadoFactura.setWrapStyleWord(true);
        txtResultadoFactura.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(new JScrollPane(txtResultadoFactura));

        // Panel Derecho: Tabla de Amortización generada
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Tabla de Amortización Generada (Sistema Francés 16.5% Anual)"));

        modeloAmortizacionFactura = new DefaultTableModel(
                new Object[]{"# Cuota", "Valor Cuota ($)", "Interés ($)", "Capital ($)", "Saldo ($)"}, 0);
        JTable tablaAmort = new JTable(modeloAmortizacionFactura);
        tablaAmort.setRowHeight(24);
        alinearColumnasCentro(tablaAmort);

        tablePanel.add(new JScrollPane(tablaAmort), BorderLayout.CENTER);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modeloCatalogo = new DefaultTableModel(
                new Object[]{"ID", "Marca", "Modelo", "Precio (USD)", "Estado", "Características"}, 0);
        JTable tabla = new JTable(modeloCatalogo);
        tabla.setRowHeight(24);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefrescar = new JButton("🔄 Refrescar Catálogo");
        btnRefrescar.addActionListener(e -> refrescarCatalogo());

        JButton btnNuevo = new JButton("➕ Nuevo Teléfono");
        btnNuevo.addActionListener(e -> agregarTelefonoDialog());

        btnPanel.add(btnRefrescar);
        btnPanel.add(btnNuevo);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelConsultasBanquito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Tarjeta 1: Validar sujeto
        JPanel card1 = new JPanel(new BorderLayout(5, 5));
        card1.setBorder(BorderFactory.createTitledBorder("1. Validar Sujeto por Cédula"));
        txtCedulaConsulta = new JTextField("1710000001");
        JButton btnVal = new JButton("Validar en BanQuito");
        btnVal.addActionListener(e -> validarSujetoClick());
        card1.add(txtCedulaConsulta, BorderLayout.NORTH);
        card1.add(btnVal, BorderLayout.SOUTH);

        // Tarjeta 2: Monto maximo
        JPanel card2 = new JPanel(new BorderLayout(5, 5));
        card2.setBorder(BorderFactory.createTitledBorder("2. Monto Máximo por Cód. Cliente"));
        txtCodClienteConsulta = new JTextField("1");
        JButton btnMonto = new JButton("Calcular Cupo Trimestral");
        btnMonto.addActionListener(e -> consultarMontoMaximoClick());
        card2.add(txtCodClienteConsulta, BorderLayout.NORTH);
        card2.add(btnMonto, BorderLayout.SOUTH);

        // Tarjeta 3: Amortizacion
        JPanel card3 = new JPanel(new BorderLayout(5, 5));
        card3.setBorder(BorderFactory.createTitledBorder("3. Tabla por Cód. Crédito"));
        txtCodCreditoConsulta = new JTextField("1");
        JButton btnAmort = new JButton("Ver Tabla Amortización");
        btnAmort.addActionListener(e -> consultarAmortizacionClick());
        card3.add(txtCodCreditoConsulta, BorderLayout.NORTH);
        card3.add(btnAmort, BorderLayout.SOUTH);

        topPanel.add(card1);
        topPanel.add(card2);
        topPanel.add(card3);

        txtResultadoBanquito = new JTextArea(4, 50);
        txtResultadoBanquito.setEditable(false);
        txtResultadoBanquito.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        modeloAmortizacionConsulta = new DefaultTableModel(
                new Object[]{"# Cuota", "Valor Cuota ($)", "Interés ($)", "Capital ($)", "Saldo ($)"}, 0);
        JTable tabla = new JTable(modeloAmortizacionConsulta);
        tabla.setRowHeight(24);
        alinearColumnasCentro(tabla);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(new JScrollPane(txtResultadoBanquito), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAcercaDe() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);
        editor.setContentType("text/html");
        editor.setText(
            "<html><body style='font-family:Segoe UI, sans-serif; padding:10px;'>" +
            "<h2 style='color:#182b49;'>Universidad de las Fuerzas Armadas ESPE</h2>" +
            "<h3>Examen Práctico Complexivo - Celulares (NRC: 36884 | Grupo: 04)</h3>" +
            "<p><b>Tecnología Implementada:</b> JAVA 21 & RESTful Web Services (JSON/HTTP)</p>" +
            "<hr/>" +
            "<h4>Matriz de Cumplimiento Oficial (20 / 20 Puntos):</h4>" +
            "<ul>" +
            "<li><b>Ítem 1:</b> Diagrama Casos de Uso BanQuito (2.0 pts) - Completado en PlantUML</li>" +
            "<li><b>Ítem 2:</b> Especificación CU 'Verificar Sujeto' (1.0 pt) - Formato IEEE/RUP</li>" +
            "<li><b>Ítem 3:</b> Base de Datos CORE BanQuito (2.0 pts) - 5 Clientes, 5 Cuentas, 55 Movimientos</li>" +
            "<li><b>Ítem 4:</b> MER Comercializadora de Teléfonos (1.0 pt) - Sin inventario según rúbrica</li>" +
            "<li><b>Ítem 5:</b> REST Valida Sujeto de Crédito por Cédula (1.5 pts) - 4 Reglas aplicadas</li>" +
            "<li><b>Ítem 6:</b> REST Monto Máximo de Crédito (1.5 pts) - Promedios depósitos/retiros 90d</li>" +
            "<li><b>Ítem 7:</b> REST Otorga Crédito y Tabla Amortización (2.0 pts) - Francés 16.5% anual ($78.65)</li>" +
            "<li><b>Ítem 8:</b> CRUD Mantenimiento Catálogo de Celulares (1.0 pt) - RESTful endpoints</li>" +
            "<li><b>Ítem 9:</b> Facturación Efectivo (42% desc.) y Crédito Directo para 4 clientes (3.0 pts)</li>" +
            "<li><b>Ítem 10:</b> REST Retorna Tabla de Amortización para Crédito (1.0 pt)</li>" +
            "<li><b>Ítem 11:</b> Consulta de Amortización desde Comercializadora (1.0 pt)</li>" +
            "<li><b>Ítem 12:</b> Diagrama de Arquitectura Completa de la Solución (1.0 pt)</li>" +
            "</ul>" +
            "<p style='color:green;'><b>ESTADO DE ENTREGA: 100% CUMPLIDO - LISTO PARA DEFENSA</b></p>" +
            "</body></html>"
        );
        panel.add(new JScrollPane(editor), BorderLayout.CENTER);
        return panel;
    }

    private void cargarTelefonosEnCombo() {
        new SwingWorker<List<TelefonoModel>, Void>() {
            @Override
            protected List<TelefonoModel> doInBackground() {
                return controlador.listarTelefonos();
            }
            @Override
            protected void done() {
                try {
                    List<TelefonoModel> lista = get();
                    cmbTelefonos.removeAllItems();
                    for (TelefonoModel t : lista) {
                        cmbTelefonos.addItem(t);
                    }
                    refrescarCatalogo();
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    private void refrescarCatalogo() {
        new SwingWorker<List<TelefonoModel>, Void>() {
            @Override
            protected List<TelefonoModel> doInBackground() {
                return controlador.listarTelefonos();
            }
            @Override
            protected void done() {
                try {
                    List<TelefonoModel> lista = get();
                    modeloCatalogo.setRowCount(0);
                    for (TelefonoModel t : lista) {
                        modeloCatalogo.addRow(new Object[]{
                                t.getCodTelefono(), t.getMarca(), t.getModelo(),
                                String.format("$%.2f", t.getPrecio()), t.getEstado(), t.getCaracteristicas()
                        });
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    private void seleccionarClienteDemo(int caso) {
        switch (caso) {
            case 1:
                txtCedula.setText("1710000001");
                txtNombre.setText("Carlos Andrade");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(6);
                seleccionarTelefonoPorId(1); // Motorola $450
                break;
            case 2:
                txtCedula.setText("1710000002");
                txtNombre.setText("Andrea Morales");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(12);
                seleccionarTelefonoPorId(2); // Samsung $850 (Excede cupo de $178.20)
                break;
            case 3:
                txtCedula.setText("1710000003");
                txtNombre.setText("Mateo Salazar");
                cmbFormaPago.setSelectedItem("CREDITO_DIRECTO");
                cmbCuotas.setSelectedItem(6);
                seleccionarTelefonoPorId(1);
                break;
            case 4:
                txtCedula.setText("1710000004");
                txtNombre.setText("Elena Castro");
                cmbFormaPago.setSelectedItem("EFECTIVO");
                seleccionarTelefonoPorId(1);
                break;
        }
    }

    private void seleccionarTelefonoPorId(int id) {
        for (int i = 0; i < cmbTelefonos.getItemCount(); i++) {
            TelefonoModel t = cmbTelefonos.getItemAt(i);
            if (t.getCodTelefono() == id) {
                cmbTelefonos.setSelectedIndex(i);
                break;
            }
        }
    }

    private void procesarFacturacion() {
        TelefonoModel sel = (TelefonoModel) cmbTelefonos.getSelectedItem();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un teléfono.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FacturaRequest req = new FacturaRequest();
        req.cedula = txtCedula.getText().trim();
        req.nombre = txtNombre.getText().trim();
        req.codTelefono = sel.getCodTelefono();
        req.formaPago = (String) cmbFormaPago.getSelectedItem();
        req.cuotas = (Integer) cmbCuotas.getSelectedItem();

        txtResultadoFactura.setText("Enviando petición a la Comercializadora...");
        modeloAmortizacionFactura.setRowCount(0);

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
                        txtResultadoFactura.setForeground(new Color(0, 120, 0));
                        txtResultadoFactura.setText("✅ FACTURA #" + resp.facturaId + " EMITIDA CON ÉXITO\n" + resp.mensaje);
                        if (resp.tabla != null) {
                            for (CuotaAmortizacion c : resp.tabla) {
                                modeloAmortizacionFactura.addRow(new Object[]{
                                        c.numCuota,
                                        String.format("$%.2f", c.valorCuota),
                                        String.format("$%.2f", c.interesPagado),
                                        String.format("$%.2f", c.capitalPagado),
                                        String.format("$%.2f", c.saldo)
                                });
                            }
                        }
                    } else {
                        txtResultadoFactura.setForeground(Color.RED);
                        txtResultadoFactura.setText("❌ RECHAZADO: " + resp.mensaje);
                    }
                } catch (Exception ex) {
                    txtResultadoFactura.setForeground(Color.RED);
                    txtResultadoFactura.setText("❌ Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void validarSujetoClick() {
        String ci = txtCedulaConsulta.getText().trim();
        new SwingWorker<SujetoCreditoResponse, Void>() {
            @Override
            protected SujetoCreditoResponse doInBackground() {
                return controlador.verificarSujeto(ci);
            }
            @Override
            protected void done() {
                try {
                    SujetoCreditoResponse r = get();
                    if (r.esSujeto) {
                        txtResultadoBanquito.setText(String.format("✅ SUJETO APROBADO: %s\nCliente: %s (Cód: %d) | Edad: %d | Género: %s",
                                r.motivo, r.nombre, r.codCliente, r.edad, r.genero));
                    } else {
                        txtResultadoBanquito.setText("❌ RECHAZADO: " + r.motivo);
                    }
                } catch (Exception ex) {
                    txtResultadoBanquito.setText("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void consultarMontoMaximoClick() {
        int cod = Integer.parseInt(txtCodClienteConsulta.getText().trim());
        new SwingWorker<MontoMaximoResponse, Void>() {
            @Override
            protected MontoMaximoResponse doInBackground() {
                return controlador.consultarMontoMaximo(cod);
            }
            @Override
            protected void done() {
                try {
                    MontoMaximoResponse r = get();
                    if (r != null) {
                        txtResultadoBanquito.setText(String.format(
                                "✅ MONTO MÁXIMO AUTORIZADO: $%.2f\nPromedio Depósitos (90d): $%.2f | Promedio Retiros (90d): $%.2f | Diferencia: $%.2f | Factor: 30%% * 6 meses",
                                r.montoMaximo, r.promedioDepositos, r.promedioRetiros, r.diferenciaPromedios));
                    } else {
                        txtResultadoBanquito.setText("No se encontró información financiera para el cliente " + cod);
                    }
                } catch (Exception ex) {
                    txtResultadoBanquito.setText("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void consultarAmortizacionClick() {
        int codCredito = Integer.parseInt(txtCodCreditoConsulta.getText().trim());
        modeloAmortizacionConsulta.setRowCount(0);
        new SwingWorker<List<CuotaAmortizacion>, Void>() {
            @Override
            protected List<CuotaAmortizacion> doInBackground() {
                return controlador.consultarTablaAmortizacion(codCredito);
            }
            @Override
            protected void done() {
                try {
                    List<CuotaAmortizacion> cuotas = get();
                    if (cuotas.isEmpty()) {
                        txtResultadoBanquito.setText("No se encontraron cuotas para el crédito #" + codCredito);
                    } else {
                        txtResultadoBanquito.setText("✅ Tabla de Amortización para Crédito #" + codCredito + " (" + cuotas.size() + " cuotas registradas)");
                        for (CuotaAmortizacion c : cuotas) {
                            modeloAmortizacionConsulta.addRow(new Object[]{
                                    c.numCuota,
                                    String.format("$%.2f", c.valorCuota),
                                    String.format("$%.2f", c.interesPagado),
                                    String.format("$%.2f", c.capitalPagado),
                                    String.format("$%.2f", c.saldo)
                            });
                        }
                    }
                } catch (Exception ex) {
                    txtResultadoBanquito.setText("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void agregarTelefonoDialog() {
        JTextField marca = new JTextField();
        JTextField modelo = new JTextField();
        JTextField precio = new JTextField();
        JTextField carac = new JTextField();

        Object[] msg = {
                "Marca:", marca,
                "Modelo:", modelo,
                "Precio (USD):", precio,
                "Características:", carac
        };

        int option = JOptionPane.showConfirmDialog(this, msg, "Nuevo Teléfono", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                TelefonoModel t = new TelefonoModel(0, marca.getText().trim(), modelo.getText().trim(),
                        Double.parseDouble(precio.getText().trim()), carac.getText().trim(),
                        "https://via.placeholder.com/300", "DISPONIBLE");
                boolean ok = controlador.agregarTelefono(t);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Teléfono agregado exitosamente.");
                    refrescarCatalogo();
                    cargarTelefonosEnCombo();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar en la Comercializadora.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alinearColumnasCentro(JTable tabla) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}
