package ec.edu.monster.climov.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private List<TelefonoModel> listaTelefonos = new ArrayList<>();

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
        }
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
        TelefonoModel sel = (TelefonoModel) spnTelefonos.getSelectedItem();
        if (sel == null) return;

        FacturaRequest req = new FacturaRequest();
        req.cedula = etCedula.getText().toString().trim();
        req.nombre = etNombre.getText().toString().trim();
        req.codTelefono = sel.getCodTelefono();
        req.formaPago = (String) spnFormaPago.getSelectedItem();
        req.cuotas = (Integer) spnCuotas.getSelectedItem();

        tvResultadoFactura.setText("Emitiendo factura al servicio RESTful...");
        layoutTablaAmortizacion.removeAllViews();

        executor.execute(() -> {
            FacturaResponse resp = controlador.facturar(req);
            mainHandler.post(() -> {
                if (resp.exito) {
                    tvResultadoFactura.setTextColor(Color.parseColor("#187846"));
                    tvResultadoFactura.setText("✅ FACTURA #" + resp.facturaId + " EMITIDA CON ÉXITO\n" + resp.mensaje);
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
