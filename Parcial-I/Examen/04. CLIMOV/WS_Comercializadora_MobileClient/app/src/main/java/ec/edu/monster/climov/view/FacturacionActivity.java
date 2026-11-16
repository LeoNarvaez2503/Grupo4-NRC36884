package ec.edu.monster.climov.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.FacturaModel.*;
import ec.edu.monster.climov.model.TelefonoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FacturacionActivity extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private EditText etCedula;
    private EditText etNombre;
    private Spinner spnTelefonos;
    private Spinner spnFormaPago;
    private Spinner spnCuotas;
    private TextView tvResultadoFactura;
    private LinearLayout layoutTablaAmortizacion;

    // Carrito de compras (varios articulos en una sola factura)
    private static final int MAX_POR_ITEM = 10;
    private EditText etCantidad;
    private LinearLayout layoutCarrito;
    private TextView tvTituloCarrito;
    private TextView tvTotalCarrito;
    private final List<LineaCarrito> carrito = new ArrayList<>();

    /** Renglon del carrito en el cliente: telefono + cantidad. */
    private static class LineaCarrito {
        final TelefonoModel tel;
        int cantidad;

        LineaCarrito(TelefonoModel tel, int cantidad) {
            this.tel = tel;
            this.cantidad = cantidad;
        }

        double subtotal() {
            return Math.round(tel.getPrecio() * cantidad * 100.0) / 100.0;
        }
    }

    private List<TelefonoModel> listaTelefonos = new ArrayList<>();
    private boolean activo = false;
    private int ultimoChecksumTelefonos = -1;

    private final Runnable tareaPollingTelefonos = new Runnable() {
        @Override
        public void run() {
            if (!activo) return;
            sincronizarTelefonosSilencioso();
            mainHandler.postDelayed(this, 2500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        spnTelefonos = findViewById(R.id.spnTelefonos);
        spnFormaPago = findViewById(R.id.spnFormaPago);
        spnCuotas = findViewById(R.id.spnCuotas);
        tvResultadoFactura = findViewById(R.id.tvResultadoFactura);
        layoutTablaAmortizacion = findViewById(R.id.layoutTablaAmortizacion);
        etCantidad = findViewById(R.id.etCantidad);
        layoutCarrito = findViewById(R.id.layoutCarrito);
        tvTituloCarrito = findViewById(R.id.tvTituloCarrito);
        tvTotalCarrito = findViewById(R.id.tvTotalCarrito);
        findViewById(R.id.btnAgregarCarrito).setOnClickListener(v -> agregarAlCarrito());
        findViewById(R.id.btnVaciarCarrito).setOnClickListener(v -> {
            carrito.clear();
            renderCarrito();
        });

        // Spinners
        ArrayAdapter<String> fpAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"CREDITO_DIRECTO", "EFECTIVO"});
        spnFormaPago.setAdapter(fpAdapter);

        ArrayAdapter<Integer> cuotasAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new Integer[]{3, 6, 9, 12, 18});
        spnCuotas.setAdapter(cuotasAdapter);
        spnCuotas.setSelection(1); // 6 cuotas por defecto

        // Botonera 4 Clientes
        findViewById(R.id.btnDemoC1).setOnClickListener(v -> demoCliente(1));
        findViewById(R.id.btnDemoC2).setOnClickListener(v -> demoCliente(2));
        findViewById(R.id.btnDemoC3).setOnClickListener(v -> demoCliente(3));
        findViewById(R.id.btnDemoC4).setOnClickListener(v -> demoCliente(4));

        Button btnEmitir = findViewById(R.id.btnEmitirFactura);
        btnEmitir.setOnClickListener(v -> procesarFacturacion());

        cargarTelefonos();
        renderCarrito();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activo = true;
        mainHandler.post(tareaPollingTelefonos);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activo = false;
        mainHandler.removeCallbacks(tareaPollingTelefonos);
    }

    private void sincronizarTelefonosSilencioso() {
        executor.execute(() -> {
            List<TelefonoModel> nuevos = controlador.listarTelefonos();
            int checksum = calcularChecksum(nuevos);
            mainHandler.post(() -> {
                if (!activo || nuevos == null) return;
                if (nuevos.size() != listaTelefonos.size() || checksum != ultimoChecksumTelefonos) {
                    ultimoChecksumTelefonos = checksum;
                    TelefonoModel selActual = (TelefonoModel) spnTelefonos.getSelectedItem();
                    int selCod = selActual != null ? selActual.getCodTelefono() : -1;

                    listaTelefonos = nuevos;
                    ArrayAdapter<TelefonoModel> adapter = new ArrayAdapter<>(FacturacionActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, listaTelefonos);
                    spnTelefonos.setAdapter(adapter);

                    if (selCod > 0) {
                        for (int i = 0; i < listaTelefonos.size(); i++) {
                            if (listaTelefonos.get(i).getCodTelefono() == selCod) {
                                spnTelefonos.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            });
        });
    }

    private int calcularChecksum(List<TelefonoModel> tels) {
        if (tels == null) return 0;
        int sum = 0;
        for (TelefonoModel t : tels) {
            sum = 31 * sum + t.getCodTelefono() + (int)(t.getPrecio() * 100);
        }
        return sum;
    }

    private void demoCliente(int caso) {
        switch (caso) {
            case 1:
                etCedula.setText("1710000001");
                etNombre.setText("Carlos Andrade");
                spnFormaPago.setSelection(0); // CREDITO_DIRECTO
                spnCuotas.setSelection(1); // 6
                seleccionarTelefonoIndex(0); // Motorola $450
                break;
            case 2:
                etCedula.setText("1710000002");
                etNombre.setText("Andrea Morales");
                spnFormaPago.setSelection(0);
                spnCuotas.setSelection(3); // 12
                seleccionarTelefonoIndex(1); // Samsung $850
                break;
            case 3:
                etCedula.setText("1710000003");
                etNombre.setText("Mateo Salazar");
                spnFormaPago.setSelection(0);
                spnCuotas.setSelection(1);
                seleccionarTelefonoIndex(0);
                break;
            case 4:
                etCedula.setText("1710000004");
                etNombre.setText("Elena Castro");
                spnFormaPago.setSelection(1); // EFECTIVO
                seleccionarTelefonoIndex(0);
                break;
        }
    }

    private void seleccionarTelefonoIndex(int idx) {
        if (spnTelefonos.getAdapter() != null && idx < spnTelefonos.getAdapter().getCount()) {
            spnTelefonos.setSelection(idx);
            // Caso de rubrica: el carrito queda con 1 unidad de ese telefono
            carrito.clear();
            carrito.add(new LineaCarrito((TelefonoModel) spnTelefonos.getAdapter().getItem(idx), 1));
            renderCarrito();
        }
    }

    // =====================================================================
    // CARRITO DE COMPRAS
    // =====================================================================

    private LineaCarrito buscarLinea(int codTelefono) {
        for (LineaCarrito l : carrito) {
            if (l.tel.getCodTelefono() == codTelefono) return l;
        }
        return null;
    }

    private double subtotalCarrito() {
        double sum = 0;
        for (LineaCarrito l : carrito) sum += l.subtotal();
        return Math.round(sum * 100.0) / 100.0;
    }

    private int totalArticulos() {
        int n = 0;
        for (LineaCarrito l : carrito) n += l.cantidad;
        return n;
    }

    private void agregarAlCarrito() {
        TelefonoModel sel = (TelefonoModel) spnTelefonos.getSelectedItem();
        if (sel == null) {
            tvResultadoFactura.setTextColor(Color.RED);
            tvResultadoFactura.setText("Seleccione un teléfono del catálogo.");
            return;
        }
        int cant;
        try {
            cant = Integer.parseInt(etCantidad.getText().toString().trim());
        } catch (NumberFormatException e) {
            cant = 0;
        }
        if (cant < 1) {
            tvResultadoFactura.setTextColor(Color.RED);
            tvResultadoFactura.setText("La cantidad debe ser al menos 1.");
            return;
        }
        LineaCarrito existente = buscarLinea(sel.getCodTelefono());
        int nueva = (existente != null ? existente.cantidad : 0) + cant;
        if (nueva > MAX_POR_ITEM) {
            tvResultadoFactura.setTextColor(Color.RED);
            tvResultadoFactura.setText("Máximo " + MAX_POR_ITEM + " unidades por artículo.");
            return;
        }
        if (existente != null) existente.cantidad = nueva; else carrito.add(new LineaCarrito(sel, cant));
        etCantidad.setText("1");
        tvResultadoFactura.setTextColor(Color.parseColor("#187846"));
        tvResultadoFactura.setText("✔ " + cant + " × " + sel.getMarca() + " " + sel.getModelo() + " agregado al carrito.");
        renderCarrito();
    }

    private void renderCarrito() {
        layoutCarrito.removeAllViews();
        tvTituloCarrito.setText("🛒 CARRITO DE COMPRAS (" + totalArticulos() + (totalArticulos() == 1 ? " artículo)" : " artículos)"));
        tvTotalCarrito.setText(String.format(Locale.US, "Total carrito: $%.2f", subtotalCarrito()));

        if (carrito.isEmpty()) {
            TextView vacio = new TextView(this);
            vacio.setText("El carrito está vacío. Elija un teléfono y pulse «Agregar al Carrito».");
            vacio.setTextSize(11f);
            vacio.setTextColor(Color.parseColor("#64748B"));
            vacio.setPadding(0, 4, 0, 4);
            layoutCarrito.addView(vacio);
            return;
        }

        for (LineaCarrito l : new ArrayList<>(carrito)) {
            LinearLayout fila = new LinearLayout(this);
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setGravity(Gravity.CENTER_VERTICAL);
            fila.setPadding(0, 2, 0, 2);

            TextView tv = new TextView(this);
            tv.setText(String.format(Locale.US, "%d × %s %s\n$%.2f c/u  →  $%.2f",
                    l.cantidad, l.tel.getMarca(), l.tel.getModelo(), l.tel.getPrecio(), l.subtotal()));
            tv.setTextSize(11f);
            tv.setTextColor(Color.parseColor("#1E293B"));
            LinearLayout.LayoutParams lpTxt = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv.setLayoutParams(lpTxt);

            Button menos = crearBotonMini("−");
            menos.setOnClickListener(v -> {
                if (l.cantidad <= 1) carrito.remove(l); else l.cantidad--;
                renderCarrito();
            });
            Button mas = crearBotonMini("+");
            mas.setOnClickListener(v -> {
                if (l.cantidad < MAX_POR_ITEM) l.cantidad++;
                renderCarrito();
            });
            Button quitar = crearBotonMini("✕");
            quitar.setOnClickListener(v -> {
                carrito.remove(l);
                renderCarrito();
            });

            fila.addView(tv);
            fila.addView(menos);
            fila.addView(mas);
            fila.addView(quitar);
            layoutCarrito.addView(fila);
        }
    }

    private Button crearBotonMini(String texto) {
        Button b = new Button(this);
        b.setText(texto);
        b.setTextSize(12f);
        b.setAllCaps(false);
        b.setMinWidth(0);
        b.setMinimumWidth(0);
        b.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(88, 80);
        lp.setMargins(4, 0, 0, 0);
        b.setLayoutParams(lp);
        return b;
    }

    private void cargarTelefonos() {
        executor.execute(() -> {
            listaTelefonos = controlador.listarTelefonos();
            mainHandler.post(() -> {
                ArrayAdapter<TelefonoModel> adapter = new ArrayAdapter<>(FacturacionActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, listaTelefonos);
                spnTelefonos.setAdapter(adapter);
            });
        });
    }

    private void procesarFacturacion() {
        if (carrito.isEmpty()) {
            tvResultadoFactura.setTextColor(Color.RED);
            tvResultadoFactura.setText("❌ El carrito está vacío. Agregue al menos un teléfono para facturar.");
            return;
        }

        FacturaRequest req = new FacturaRequest();
        req.cedula = etCedula.getText().toString().trim();
        req.nombre = etNombre.getText().toString().trim();
        for (LineaCarrito l : carrito) {
            req.items.add(new ItemCarrito(l.tel.getCodTelefono(), l.cantidad));
        }
        req.formaPago = (String) spnFormaPago.getSelectedItem();
        req.cuotas = (Integer) spnCuotas.getSelectedItem();

        tvResultadoFactura.setText("Emitiendo factura al servicio RESTful...");
        layoutTablaAmortizacion.removeAllViews();

        executor.execute(() -> {
            FacturaResponse resp = controlador.facturar(req);
            mainHandler.post(() -> {
                if (resp.exito) {
                    tvResultadoFactura.setTextColor(Color.parseColor("#187846"));
                    tvResultadoFactura.setText("✅ FACTURA #" + resp.facturaId + " EMITIDA CON ÉXITO\n" + resp.mensaje + detalleFactura(resp.factura));
                    carrito.clear();       // compra finalizada: el carrito se vacia
                    renderCarrito();
                    if (resp.tabla != null && !resp.tabla.isEmpty()) {
                        mostrarTablaAmortizacion(resp.tabla);
                    }
                } else {
                    tvResultadoFactura.setTextColor(Color.RED);
                    tvResultadoFactura.setText("❌ TRANSACCIÓN RECHAZADA:\n" + resp.mensaje);
                }
            });
        });
    }

    /** Texto del detalle de la factura: todos los articulos + totales. */
    private String detalleFactura(FacturaInfo fi) {
        if (fi == null || fi.items == null || fi.items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("\n\n— DETALLE DE LA FACTURA —");
        for (FacturaDetalle d : fi.items) {
            sb.append(String.format(Locale.US, "\n%d × %s  ($%.2f c/u)  = $%.2f", d.cantidad, d.modeloTelefono, d.precioUnitario, d.subtotal));
        }
        sb.append(String.format(Locale.US, "\nSubtotal: $%.2f", fi.subtotal));
        if (fi.valorDescuento > 0) {
            sb.append(String.format(Locale.US, "\nDescuento (%.0f%%): -$%.2f", fi.porcentajeDescuento, fi.valorDescuento));
        }
        sb.append(String.format(Locale.US, "\nTOTAL A PAGAR: $%.2f", fi.totalPagar));
        if (fi.codCreditoBanco != null && fi.numCuotas != null && fi.valorCuota != null) {
            sb.append(String.format(Locale.US, "\nCrédito BanQuito #%d: %d cuotas de $%.2f", fi.codCreditoBanco, fi.numCuotas, fi.valorCuota));
        }
        return sb.toString();
    }

    private void mostrarTablaAmortizacion(List<CuotaAmortizacion> cuotas) {
        TextView header = new TextView(this);
        header.setText("--- TABLA DE AMORTIZACIÓN BANQUITO (FRANCÉS 16.5%) ---");
        header.setTypeface(null, Typeface.BOLD);
        header.setTextColor(Color.parseColor("#182B49"));
        header.setPadding(0, 8, 0, 8);
        layoutTablaAmortizacion.addView(header);

        for (CuotaAmortizacion c : cuotas) {
            TextView fila = new TextView(this);
            fila.setText(String.format("Cuota %d: Valor $%.2f | Int: $%.2f | Cap: $%.2f | Saldo: $%.2f",
                    c.numCuota, c.valorCuota, c.interesPagado, c.capitalPagado, c.saldo));
            fila.setTextSize(11f);
            fila.setPadding(0, 2, 0, 2);
            layoutTablaAmortizacion.addView(fila);
        }
    }
}
