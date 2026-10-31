package ec.edu.monster.climov.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.FacturaModel.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AmortizacionActivity extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private EditText etCedulaConsulta;
    private EditText etCodCreditoConsulta;
    private TextView tvResultadoConsulta;
    private LinearLayout layoutTablaConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortizacion);

        etCedulaConsulta = findViewById(R.id.etCedulaConsulta);
        etCodCreditoConsulta = findViewById(R.id.etCodCreditoConsulta);
        tvResultadoConsulta = findViewById(R.id.tvResultadoConsulta);
        layoutTablaConsulta = findViewById(R.id.layoutTablaConsulta);

        Button btnVerificar = findViewById(R.id.btnVerificarSujeto);
        Button btnTabla = findViewById(R.id.btnConsultarTabla);

        btnVerificar.setOnClickListener(v -> verificarSujeto());
        btnTabla.setOnClickListener(v -> consultarTabla());
    }

    private void verificarSujeto() {
        String ci = etCedulaConsulta.getText().toString().trim();
        tvResultadoConsulta.setText("Consultando BanQuito REST API...");
        layoutTablaConsulta.removeAllViews();

        executor.execute(() -> {
            SujetoCreditoResponse r = controlador.verificarSujeto(ci);
            mainHandler.post(() -> {
                if (r.esSujeto) {
                    tvResultadoConsulta.setTextColor(Color.parseColor("#187846"));
                    tvResultadoConsulta.setText(String.format("✅ SUJETO APROBADO: %s\nCliente: %s (Cód: %d) | Edad: %d | Género: %s",
                            r.motivo, r.nombre, r.codCliente, r.edad, r.genero));
                } else {
                    tvResultadoConsulta.setTextColor(Color.RED);
                    tvResultadoConsulta.setText("❌ RECHAZADO: " + r.motivo);
                }
            });
        });
    }

    private void consultarTabla() {
        String codStr = etCodCreditoConsulta.getText().toString().trim();
        if (codStr.isEmpty()) return;

        int codCredito = Integer.parseInt(codStr);
        tvResultadoConsulta.setText("Cargando tabla de amortización...");
        layoutTablaConsulta.removeAllViews();

        executor.execute(() -> {
            List<CuotaAmortizacion> cuotas = controlador.consultarTablaAmortizacion(codCredito);
            mainHandler.post(() -> {
                if (cuotas.isEmpty()) {
                    tvResultadoConsulta.setTextColor(Color.RED);
                    tvResultadoConsulta.setText("No se encontraron cuotas para el crédito #" + codCredito);
                } else {
                    tvResultadoConsulta.setTextColor(Color.parseColor("#182B49"));
                    tvResultadoConsulta.setText("✅ Crédito #" + codCredito + " - " + cuotas.size() + " cuotas generadas:");

                    for (CuotaAmortizacion c : cuotas) {
                        TextView fila = new TextView(this);
                        fila.setText(String.format("Cuota %d: Cuota $%.2f | Int: $%.2f | Cap: $%.2f | Saldo: $%.2f",
                                c.numCuota, c.valorCuota, c.interesPagado, c.capitalPagado, c.saldo));
                        fila.setTextSize(12f);
                        fila.setPadding(0, 4, 0, 4);
                        layoutTablaConsulta.addView(fila);
                    }
                }
            });
        });
    }
}
