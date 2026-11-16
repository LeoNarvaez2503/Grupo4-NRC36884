package vista;

import controlador.PaisController;
import controlador.PaisController.ReporteEstadistico;
import controlador.PaisController.ResultadoImportacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.Pais;

/**
 * Vista principal Swing (MVC) para la gestión y consulta de países.
 */
public class FrmPais extends JFrame {

    private final PaisController controller;
    private final String usuarioActual;
    private final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-EC"));

    // Barra superior (API y estado)
    private JButton btnImportarTodo;
    private JTextField txtBuscarApiNombre;
    private JButton btnConsultarApiNombre;
    private JLabel lblEstadoBD;
    private JLabel lblEstadoOperacion;

    // Formulario CRUD (Actividad 2)
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCapital;
    private JComboBox<String> cmbRegionForm;
    private JTextField txtPoblacion;
    private JTextField txtMoneda;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Filtros y Tabla principal (Actividades 3 y 4)
    private JTextField txtFiltroNombreBd;
    private JButton btnBuscarBd;
    private JComboBox<String> cmbFiltroRegion;
    private JButton btnImportarRegion;
    private JButton btnRefrescar;
    private JTable tblPaises;
    private DefaultTableModel modeloTablaPaises;
    private JLabel lblTotalTabla;

    // Reportes Estadísticos (Actividad 5)
    private JButton btnGenerarReporte;
    private JLabel lblStatTotalPaises;
    private JLabel lblStatPaisMasPoblado;
    private JLabel lblStatRegionTop;
    private JTable tblReporteRegiones;
    private DefaultTableModel modeloTablaReporte;

    public FrmPais() {
        this("Invitado");
    }

    public FrmPais(String usuarioActual) {
        this.usuarioActual = (usuarioActual == null || usuarioActual.isBlank()) ? "Invitado" : usuarioActual;
        this.controller = new PaisController();
        initComponents();
        verificarEstadoInicial();
        cargarTablaPaises(controller.listarPaises());
        actualizarReportes();
    }

    private void initComponents() {
        setTitle("Taller 1 - Consumo de API Pública (REST Countries) | Java + MySQL + MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setPreferredSize(new Dimension(1220, 780));
        setLayout(new BorderLayout(10, 10));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelSuperior() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(new EmptyBorder(10, 12, 4, 12));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(26, 54, 93));
        header.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblTitulo = new JLabel("Gestión de Países — Consumo de API REST con MySQL y MVC");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblEstadoBD = new JLabel("MySQL: verificando...");
        lblEstadoBD.setForeground(new Color(226, 232, 240));
        lblEstadoBD.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblUsuario = new JLabel("Usuario: " + usuarioActual + "   |   ");
        lblUsuario.setForeground(new Color(186, 230, 253));
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton btnCerrarSesion = crearBoton("Cerrar Sesión", new Color(220, 38, 38), Color.WHITE);
        btnCerrarSesion.addActionListener(e -> {
            new FrmLogin().setVisible(true);
            dispose();
        });

        JPanel headerDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerDerecha.setOpaque(false);
        headerDerecha.add(lblUsuario);
        headerDerecha.add(lblEstadoBD);
        headerDerecha.add(Box.createHorizontalStrut(6));
        headerDerecha.add(btnCerrarSesion);

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(headerDerecha, BorderLayout.EAST);

        // Barra de acciones API (Importar todos + Actividad 1: Consultar por nombre)
        JPanel apiBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        apiBar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                "Consumo de API Pública",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(30, 41, 59)
        ));

        btnImportarTodo = crearBoton("IMPORTAR PAÍSES (API)", new Color(37, 99, 235), Color.WHITE);
        btnImportarTodo.addActionListener(e -> importarTodosLosPaises());

        apiBar.add(btnImportarTodo);
        apiBar.add(Box.createHorizontalStrut(12));
        apiBar.add(new JLabel("Consultar país en API por nombre:"));

        txtBuscarApiNombre = new JTextField(16);
        txtBuscarApiNombre.setToolTipText("Ejemplo: ecuador, japan, spain");
        txtBuscarApiNombre.addActionListener(e -> consultarPaisEnApiPorNombre());
        apiBar.add(txtBuscarApiNombre);

        btnConsultarApiNombre = crearBoton("Consultar API por Nombre", new Color(15, 118, 110), Color.WHITE);
        btnConsultarApiNombre.addActionListener(e -> consultarPaisEnApiPorNombre());
        apiBar.add(btnConsultarApiNombre);

        contenedor.add(header, BorderLayout.NORTH);
        contenedor.add(apiBar, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearPanelCentral() {
        JPanel centro = new JPanel(new BorderLayout(12, 10));
        centro.setBorder(new EmptyBorder(0, 12, 0, 12));

        centro.add(crearPanelFormularioCrud(), BorderLayout.WEST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Listado de Países (MySQL)", crearPanelTablaPaises());
        tabs.addTab("Reportes Estadísticos", crearPanelReportes());

        centro.add(tabs, BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearPanelFormularioCrud() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                "Formulario País (CRUD)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), new Color(30, 41, 59)
        ));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(8, 10, 8, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));

        txtNombre = new JTextField();
        txtCapital = new JTextField();
        cmbRegionForm = new JComboBox<>(new String[]{
            "Americas", "Europe", "Asia", "Africa", "Oceania", "Antarctic", "Polar"
        });
        cmbRegionForm.setEditable(true);
        txtPoblacion = new JTextField();
        txtMoneda = new JTextField();

        agregarCampo(form, gbc, 0, "ID:", txtId);
        agregarCampo(form, gbc, 1, "Nombre:", txtNombre);
        agregarCampo(form, gbc, 2, "Capital:", txtCapital);
        agregarCampo(form, gbc, 3, "Región:", cmbRegionForm);
        agregarCampo(form, gbc, 4, "Población:", txtPoblacion);
        agregarCampo(form, gbc, 5, "Moneda:", txtMoneda);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        form.add(Box.createVerticalGlue(), gbc);

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setBorder(new EmptyBorder(6, 10, 10, 10));

        btnCrear = crearBoton("Crear / Guardar", new Color(22, 163, 74), Color.WHITE);
        btnActualizar = crearBoton("Actualizar", new Color(217, 119, 6), Color.WHITE);
        btnEliminar = crearBoton("Eliminar", new Color(220, 38, 38), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 116, 139), Color.WHITE);

        btnCrear.addActionListener(e -> crearPais());
        btnActualizar.addActionListener(e -> actualizarPais());
        btnEliminar.addActionListener(e -> eliminarPais());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        panel.add(form, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTablaPaises() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Barra de búsqueda y filtro por región (Actividad 4)
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filtros.add(new JLabel("Buscar en BD:"));
        txtFiltroNombreBd = new JTextField(14);
        txtFiltroNombreBd.addActionListener(e -> filtrarEnBdPorNombre());
        filtros.add(txtFiltroNombreBd);

        btnBuscarBd = crearBoton("Buscar", new Color(51, 65, 85), Color.WHITE);
        btnBuscarBd.addActionListener(e -> filtrarEnBdPorNombre());
        filtros.add(btnBuscarBd);

        filtros.add(Box.createHorizontalStrut(8));
        filtros.add(new JLabel("Región:"));
        cmbFiltroRegion = new JComboBox<>(new String[]{
            "Todas", "América", "Europa", "Asia", "África", "Oceanía"
        });
        cmbFiltroRegion.addActionListener(e -> filtrarEnBdPorRegion());
        filtros.add(cmbFiltroRegion);

        btnImportarRegion = crearBoton("Importar Región (API)", new Color(14, 116, 144), Color.WHITE);
        btnImportarRegion.addActionListener(e -> importarRegionDesdeApi());
        filtros.add(btnImportarRegion);

        btnRefrescar = crearBoton("Mostrar Todos", new Color(71, 85, 105), Color.WHITE);
        btnRefrescar.addActionListener(e -> {
            txtFiltroNombreBd.setText("");
            cmbFiltroRegion.setSelectedIndex(0);
            cargarTablaPaises(controller.listarPaises());
        });
        filtros.add(btnRefrescar);

        // Tabla JTable (Actividad 3)
        modeloTablaPaises = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Capital", "Región", "Población", "Moneda"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 4 -> Long.class;
                    default -> String.class;
                };
            }
        };

        tblPaises = new JTable(modeloTablaPaises);
        tblPaises.setRowHeight(25);
        tblPaises.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPaises.setAutoCreateRowSorter(true);
        tblPaises.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPaises.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Anchos de columna
        tblPaises.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblPaises.getColumnModel().getColumn(0).setMaxWidth(70);
        tblPaises.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblPaises.getColumnModel().getColumn(2).setPreferredWidth(130);
        tblPaises.getColumnModel().getColumn(3).setPreferredWidth(95);
        tblPaises.getColumnModel().getColumn(4).setPreferredWidth(115);
        tblPaises.getColumnModel().getColumn(5).setPreferredWidth(180);

        // Formatear población con separador de miles manteniendo orden numérico
        DefaultTableCellRenderer poblacionRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (value instanceof Number num) {
                    setText(numberFormat.format(num.longValue()));
                }
                return this;
            }
        };
        tblPaises.getColumnModel().getColumn(4).setCellRenderer(poblacionRenderer);

        tblPaises.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPaisSeleccionadoEnFormulario();
            }
        });

        lblTotalTabla = new JLabel("Registros mostrados: 0");
        lblTotalTabla.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblTotalTabla.setBorder(new EmptyBorder(4, 2, 0, 2));

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblPaises), BorderLayout.CENTER);
        panel.add(lblTotalTabla, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 12, 0));
        lblStatTotalPaises = crearTarjetaEstadistica(tarjetas, "Total de Países", "0", new Color(37, 99, 235));
        lblStatPaisMasPoblado = crearTarjetaEstadistica(tarjetas, "País Más Poblado", "N/A", new Color(22, 163, 74));
        lblStatRegionTop = crearTarjetaEstadistica(tarjetas, "Región con Más Países", "N/A", new Color(124, 58, 237));

        btnGenerarReporte = crearBoton("Actualizar Reportes Estadísticos", new Color(30, 41, 59), Color.WHITE);
        btnGenerarReporte.addActionListener(e -> actualizarReportes());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnGenerarReporte);

        top.add(tarjetas, BorderLayout.CENTER);
        top.add(btnPanel, BorderLayout.SOUTH);

        modeloTablaReporte = new DefaultTableModel(
                new Object[]{"Región", "Número de Países", "Porcentaje (%)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReporteRegiones = new JTable(modeloTablaReporte);
        tblReporteRegiones.setRowHeight(26);
        tblReporteRegiones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblReporteRegiones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblReporteRegiones.setRowSorter(new TableRowSorter<>(modeloTablaReporte));

        JPanel tablaContainer = new JPanel(new BorderLayout(4, 4));
        tablaContainer.setBorder(BorderFactory.createTitledBorder("Número de Países por Región"));
        tablaContainer.add(new JScrollPane(tblReporteRegiones), BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(tablaContainer, BorderLayout.CENTER);
        return panel;
    }

    private JLabel crearTarjetaEstadistica(JPanel parent, String titulo, String valorInicial, Color acento) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 1, 1, 1, acento),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(new Color(100, 116, 139));

        JLabel lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValor.setForeground(new Color(15, 23, 42));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblValor);
        parent.add(card);
        return lblValor;
    }

    private JPanel crearPanelInferior() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(4, 12, 8, 12));
        lblEstadoOperacion = new JLabel("Listo.");
        lblEstadoOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstadoOperacion.setForeground(new Color(51, 65, 85));
        footer.add(lblEstadoOperacion, BorderLayout.WEST);
        return footer;
    }

    private void agregarCampo(JPanel form, GridBagConstraints gbc, int fila, String etiqueta, Component campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.25;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        form.add(campo, gbc);
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void verificarEstadoInicial() {
        boolean ok = controller.verificarConexionBD();
        if (ok) {
            lblEstadoBD.setText("MySQL: Conectado (api_paises)");
            lblEstadoBD.setForeground(new Color(134, 239, 172));
        } else {
            lblEstadoBD.setText("MySQL: Sin conexión");
            lblEstadoBD.setForeground(new Color(252, 165, 165));
        }
    }

    // --- Acciones de API y Controlador ---

    private void importarTodosLosPaises() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnImportarTodo.setEnabled(false);
        lblEstadoOperacion.setText("Consultando API pública e importando países en MySQL...");

        new SwingWorker<ResultadoImportacion, Void>() {
            @Override
            protected ResultadoImportacion doInBackground() {
                return controller.importarPaises();
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnImportarTodo.setEnabled(true);
                try {
                    ResultadoImportacion res = get();
                    lblEstadoOperacion.setText(res.mensaje());
                    verificarEstadoInicial();
                    cargarTablaPaises(controller.listarPaises());
                    actualizarReportes();
                    JOptionPane.showMessageDialog(
                            FrmPais.this,
                            res.mensaje(),
                            res.exito() ? "Importación Finalizada" : "Aviso de Importación",
                            res.exito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FrmPais.this,
                            "Error al importar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void consultarPaisEnApiPorNombre() {
        String nombre = txtBuscarApiNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del país a consultar (ej: ecuador).");
            txtBuscarApiNombre.requestFocus();
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnConsultarApiNombre.setEnabled(false);
        lblEstadoOperacion.setText("Consultando API por nombre: " + nombre + "...");

        new SwingWorker<List<Pais>, Void>() {
            @Override
            protected List<Pais> doInBackground() {
                return controller.consultarApiPorNombre(nombre);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnConsultarApiNombre.setEnabled(true);
                try {
                    List<Pais> encontrados = get();
                    if (encontrados.isEmpty()) {
                        lblEstadoOperacion.setText("No se encontró '" + nombre + "' en la API.");
                        JOptionPane.showMessageDialog(FrmPais.this,
                                "No se encontraron resultados en la API para: " + nombre,
                                "Sin resultados", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Pais primero = encontrados.get(0);
                    llenarFormularioDesdePais(primero, false);
                    lblEstadoOperacion.setText("País encontrado en API: " + primero.getNombre());

                    int opcion = JOptionPane.showConfirmDialog(
                            FrmPais.this,
                            String.format("Se encontraron %d resultado(s) en la API.\n"
                                    + "País: %s\nCapital: %s\nRegión: %s\nPoblación: %s\nMoneda: %s\n\n"
                                    + "¿Desea guardar/actualizar los resultados en MySQL?",
                                    encontrados.size(),
                                    primero.getNombre(),
                                    primero.getCapital(),
                                    primero.getRegion(),
                                    numberFormat.format(primero.getPoblacion()),
                                    primero.getMoneda()),
                            "Consulta API por Nombre",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (opcion == JOptionPane.YES_OPTION) {
                        ResultadoImportacion res = controller.importarPorNombreDesdeApi(nombre);
                        lblEstadoOperacion.setText(res.mensaje());
                        cargarTablaPaises(controller.listarPaises());
                        actualizarReportes();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FrmPais.this,
                            "Error consultando API: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void importarRegionDesdeApi() {
        String regionUi = (String) cmbFiltroRegion.getSelectedItem();
        if (regionUi == null || regionUi.equalsIgnoreCase("Todas")) {
            importarTodosLosPaises();
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnImportarRegion.setEnabled(false);
        lblEstadoOperacion.setText("Importando región " + regionUi + " desde API...");

        new SwingWorker<ResultadoImportacion, Void>() {
            @Override
            protected ResultadoImportacion doInBackground() {
                return controller.importarPorRegionDesdeApi(regionUi);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnImportarRegion.setEnabled(true);
                try {
                    ResultadoImportacion res = get();
                    lblEstadoOperacion.setText(res.mensaje());
                    cargarTablaPaises(controller.buscarEnBdPorRegion(regionUi));
                    actualizarReportes();
                    JOptionPane.showMessageDialog(FrmPais.this, res.mensaje(),
                            "Importación por Región", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FrmPais.this,
                            "Error importando región: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // --- Acciones CRUD ---

    private void crearPais() {
        Pais pais = leerFormulario(false);
        if (pais == null) {
            return;
        }
        String error = controller.crearPais(pais);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validación / Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        lblEstadoOperacion.setText("País guardado correctamente (ID " + pais.getId() + "): " + pais.getNombre());
        limpiarFormulario();
        cargarTablaPaises(controller.listarPaises());
        actualizarReportes();
    }

    private void actualizarPais() {
        Pais pais = leerFormulario(true);
        if (pais == null) {
            return;
        }
        String error = controller.actualizarPais(pais);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validación / Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        lblEstadoOperacion.setText("País actualizado correctamente (ID " + pais.getId() + "): " + pais.getNombre());
        limpiarFormulario();
        cargarTablaPaises(controller.listarPaises());
        actualizarReportes();
    }

    private void eliminarPais() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un país de la tabla para eliminar.");
            return;
        }
        int id = Integer.parseInt(idTexto);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el país '" + txtNombre.getText() + "' (ID " + id + ")?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        String error = controller.eliminarPais(id);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblEstadoOperacion.setText("País eliminado (ID " + id + ").");
        limpiarFormulario();
        cargarTablaPaises(controller.listarPaises());
        actualizarReportes();
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtCapital.setText("");
        cmbRegionForm.setSelectedIndex(0);
        txtPoblacion.setText("");
        txtMoneda.setText("");
        tblPaises.clearSelection();
        txtNombre.requestFocus();
    }

    private Pais leerFormulario(boolean requiereId) {
        int id = 0;
        if (requiereId) {
            String idStr = txtId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione primero un país de la tabla.");
                return null;
            }
            id = Integer.parseInt(idStr);
        }
        String nombre = txtNombre.getText().trim();
        String capital = txtCapital.getText().trim();
        Object regObj = cmbRegionForm.getSelectedItem();
        String region = regObj != null ? PaisController.normalizarRegionApi(regObj.toString()) : "";
        String pobTexto = txtPoblacion.getText().trim().replace(",", "").replace(".", "");
        String moneda = txtMoneda.getText().trim();

        long poblacion = 0;
        if (!pobTexto.isEmpty()) {
            try {
                poblacion = Long.parseLong(pobTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La población debe ser un número entero válido.",
                        "Dato inválido", JOptionPane.WARNING_MESSAGE);
                txtPoblacion.requestFocus();
                return null;
            }
        }
        return new Pais(id, nombre, capital.isEmpty() ? "N/A" : capital,
                region, poblacion, moneda.isEmpty() ? "N/A" : moneda);
    }

    private void cargarPaisSeleccionadoEnFormulario() {
        int viewRow = tblPaises.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        int modelRow = tblPaises.convertRowIndexToModel(viewRow);
        Pais p = new Pais(
                (Integer) modeloTablaPaises.getValueAt(modelRow, 0),
                String.valueOf(modeloTablaPaises.getValueAt(modelRow, 1)),
                String.valueOf(modeloTablaPaises.getValueAt(modelRow, 2)),
                String.valueOf(modeloTablaPaises.getValueAt(modelRow, 3)),
                ((Number) modeloTablaPaises.getValueAt(modelRow, 4)).longValue(),
                String.valueOf(modeloTablaPaises.getValueAt(modelRow, 5))
        );
        llenarFormularioDesdePais(p, true);
    }

    private void llenarFormularioDesdePais(Pais p, boolean incluirId) {
        txtId.setText(incluirId && p.getId() > 0 ? String.valueOf(p.getId()) : "");
        txtNombre.setText(p.getNombre() != null ? p.getNombre() : "");
        txtCapital.setText(p.getCapital() != null ? p.getCapital() : "");
        cmbRegionForm.setSelectedItem(p.getRegion() != null ? p.getRegion() : "Americas");
        txtPoblacion.setText(String.valueOf(p.getPoblacion()));
        txtMoneda.setText(p.getMoneda() != null ? p.getMoneda() : "");
    }

    // --- Filtros, Tabla y Reportes ---

    private void filtrarEnBdPorNombre() {
        String texto = txtFiltroNombreBd.getText().trim();
        List<Pais> lista = controller.buscarEnBdPorNombre(texto);
        cargarTablaPaises(lista);
        lblEstadoOperacion.setText("Búsqueda local por '" + texto + "': " + lista.size() + " país(es).");
    }

    private void filtrarEnBdPorRegion() {
        String region = (String) cmbFiltroRegion.getSelectedItem();
        List<Pais> lista = controller.buscarEnBdPorRegion(region);
        cargarTablaPaises(lista);
        lblEstadoOperacion.setText("Filtro por región '" + region + "': " + lista.size() + " país(es).");
    }

    private void cargarTablaPaises(List<Pais> lista) {
        modeloTablaPaises.setRowCount(0);
        for (Pais p : lista) {
            modeloTablaPaises.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCapital(),
                p.getRegion(),
                p.getPoblacion(),
                p.getMoneda()
            });
        }
        lblTotalTabla.setText("Registros mostrados: " + lista.size());
    }

    private void actualizarReportes() {
        ReporteEstadistico rep = controller.generarReporteEstadistico();
        lblStatTotalPaises.setText(numberFormat.format(rep.totalPaises()));

        if (rep.paisMasPoblado() != null) {
            Pais top = rep.paisMasPoblado();
            lblStatPaisMasPoblado.setText("<html>" + top.getNombre() + "<br><small>"
                    + numberFormat.format(top.getPoblacion()) + " hab. (" + top.getRegion() + ")</small></html>");
        } else {
            lblStatPaisMasPoblado.setText("N/A");
        }

        if (rep.regionConMasPaises() != null) {
            lblStatRegionTop.setText("<html>" + rep.regionConMasPaises().getKey() + "<br><small>"
                    + rep.regionConMasPaises().getValue() + " países</small></html>");
        } else {
            lblStatRegionTop.setText("N/A");
        }

        modeloTablaReporte.setRowCount(0);
        int total = rep.totalPaises();
        for (Map.Entry<String, Integer> entry : rep.paisesPorRegion().entrySet()) {
            double pct = total > 0 ? (entry.getValue() * 100.0) / total : 0.0;
            modeloTablaReporte.addRow(new Object[]{
                entry.getKey(),
                entry.getValue(),
                String.format(Locale.US, "%.1f%%", pct)
            });
        }
    }
}
