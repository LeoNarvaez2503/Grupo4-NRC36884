package ec.edu.monster.climov.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.CuentaModel;
import ec.edu.monster.climov.model.MovimientoModel;
import ec.edu.monster.climov.model.ResultadoOperacion;
import ec.edu.monster.climov.model.UsuarioSesion;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Capa Vista (Patrón MVC Tradicional): Actividad Android de Monitoreo MashUp y Operaciones.
 * Sincroniza dinámicamente cada 2.5s el estado VERDE (Libre) / ROJO (En Proceso).
 */
public class DashboardView extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mashupHandler = new Handler(Looper.getMainLooper());

    private TextView tvUsuarioHeader;
    private TextView tvRolHeader;
    private TextView tvKpiVerde;
    private TextView tvKpiRojo;
    private TextView tvResultadoOp;
    private EditText etCuentaOrigen;
    private EditText etCuentaDestino;
    private EditText etMonto;
    private LinearLayout contenedorCuentas;

    private boolean activo = false;

    private final Runnable tareaPolling = new Runnable() {
        @Override
        public void run() {
            if (!activo) return;
            sincronizarCuentasMashup();
            mashupHandler.postDelayed(this, 2500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        UsuarioSesion sesion = controlador.getSesionActual();
        if (sesion == null || !sesion.isAutenticado()) {
            startActivity(new Intent(this, LoginView.class));
            finish();
            return;
        }

        tvUsuarioHeader = findViewById(R.id.tvUsuarioHeader);
        tvRolHeader = findViewById(R.id.tvRolHeader);
        tvKpiVerde = findViewById(R.id.tvKpiVerde);
        tvKpiRojo = findViewById(R.id.tvKpiRojo);
        tvResultadoOp = findViewById(R.id.tvResultadoOp);
        etCuentaOrigen = findViewById(R.id.etCuentaOrigen);
        etCuentaDestino = findViewById(R.id.etCuentaDestino);
        etMonto = findViewById(R.id.etMonto);
        contenedorCuentas = findViewById(R.id.contenedorCuentas);

        tvUsuarioHeader.setText(sesion.getNombreCompleto());
        tvRolHeader.setText(sesion.getRol() + " | Ventanilla " + controlador.getVentanillaActiva() + " | MashUp en Vivo");

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnDeposito = findViewById(R.id.btnDeposito);
        Button btnRetiro = findViewById(R.id.btnRetiro);
        Button btnTransferir = findViewById(R.id.btnTransferir);
        Button btnExtracto = findViewById(R.id.btnExtracto);
        Button btnDesbloquear = findViewById(R.id.btnDesbloquear);

        btnLogout.setOnClickListener(v -> {
            controlador.cerrarSesion();
            startActivity(new Intent(this, LoginView.class));
            finish();
        });

        btnDeposito.setOnClickListener(v -> ejecutarOperacion("DEPOSITO"));
        btnRetiro.setOnClickListener(v -> ejecutarOperacion("RETIRO"));
        btnTransferir.setOnClickListener(v -> ejecutarOperacion("TRANSFERENCIA"));
        btnExtracto.setOnClickListener(v -> mostrarDialogoExtracto(etCuentaOrigen.getText().toString().trim()));
        btnDesbloquear.setOnClickListener(v -> ejecutarDesbloqueo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        activo = true;
        mashupHandler.post(tareaPolling);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activo = false;
        mashupHandler.removeCallbacks(tareaPolling);
    }

    private void sincronizarCuentasMashup() {
        executor.execute(() -> {
            try {
                List<CuentaModel> cuentas = controlador.obtenerTodasLasCuentas();
                runOnUiThread(() -> renderizarTarjetasCuentas(cuentas));
            } catch (Exception ignored) {
            }
        });
    }

    private void renderizarTarjetasCuentas(List<CuentaModel> cuentas) {
        contenedorCuentas.removeAllViews();
        int verdes = 0;
        int rojas = 0;

        for (CuentaModel c : cuentas) {
            boolean esRojo = "ROJO".equalsIgnoreCase(c.getDisponibilidadMashup());
            if (esRojo) rojas++; else verdes++;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(28, 22, 28, 22);
            card.setBackgroundColor(esRojo ? Color.parseColor("#FEE2E2") : Color.parseColor("#FFFFFF"));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 18);
            card.setLayoutParams(params);

            TextView tvTitulo = new TextView(this);
            tvTitulo.setText(String.format(Locale.US, "%s  —  $ %.2f %s",
                    c.getNumeroCuenta(), c.getSaldo(), c.getMoneda()));
            tvTitulo.setTextSize(16f);
            tvTitulo.setTypeface(null, Typeface.BOLD);
            tvTitulo.setTextColor(Color.parseColor("#0F172A"));
            card.addView(tvTitulo);

            TextView tvSub = new TextView(this);
            tvSub.setText("Titular: " + c.getTitularCompleto() + " | " + c.getNombreSucursal());
            tvSub.setTextSize(13f);
            tvSub.setTextColor(Color.parseColor("#475569"));
            card.addView(tvSub);

            TextView tvSemaforo = new TextView(this);
            if (esRojo) {
                tvSemaforo.setText("● ROJO (EN PROCESO - Ventanilla " + c.getVentanillaBloqueo() + ")");
                tvSemaforo.setTextColor(Color.parseColor("#DC2626"));
            } else {
                tvSemaforo.setText("● VERDE (CUENTA LIBRE DISPONIBLE)");
                tvSemaforo.setTextColor(Color.parseColor("#16A34A"));
            }
            tvSemaforo.setTextSize(13f);
            tvSemaforo.setTypeface(null, Typeface.BOLD);
            card.addView(tvSemaforo);

            card.setOnClickListener(v -> {
                etCuentaOrigen.setText(c.getNumeroCuenta());
                Toast.makeText(this, "Cuenta seleccionada: " + c.getNumeroCuenta(), Toast.LENGTH_SHORT).show();
            });

            contenedorCuentas.addView(card);
        }

        tvKpiVerde.setText("● VERDE Libres: " + verdes);
        tvKpiRojo.setText("● ROJO Ocupadas: " + rojas);
    }

    private void ejecutarOperacion(String tipo) {
        String origen = etCuentaOrigen.getText().toString().trim();
        String destino = etCuentaDestino.getText().toString().trim();
        double monto;
        try {
            monto = Double.parseDouble(etMonto.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Monto inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        tvResultadoOp.setText("Procesando en servidor (Semáforo MashUp ROJO)...");
        mashupHandler.postDelayed(this::sincronizarCuentasMashup, 400);

        executor.execute(() -> {
            try {
                ResultadoOperacion res;
                if ("DEPOSITO".equals(tipo)) {
                    res = controlador.depositar(origen, monto);
                } else if ("RETIRO".equals(tipo)) {
                    res = controlador.retirar(origen, monto);
                } else {
                    res = controlador.transferir(origen, destino, monto);
                }
                runOnUiThread(() -> {
                    tvResultadoOp.setText((res.isExito() ? "✔ " : "✖ ") + res.getMensaje());
                    tvResultadoOp.setTextColor(res.isExito() ? Color.parseColor("#166534") : Color.parseColor("#991B1B"));
                    sincronizarCuentasMashup();
                });
            } catch (Exception e) {
                runOnUiThread(() -> tvResultadoOp.setText("Error: " + e.getMessage()));
            }
        });
    }

    private void ejecutarDesbloqueo() {
        String cuenta = etCuentaOrigen.getText().toString().trim();
        executor.execute(() -> {
            try {
                ResultadoOperacion res = controlador.desbloquearCuenta(cuenta);
                runOnUiThread(() -> {
                    tvResultadoOp.setText(res.getMensaje());
                    sincronizarCuentasMashup();
                });
            } catch (Exception e) {
                runOnUiThread(() -> tvResultadoOp.setText("Error desbloqueo: " + e.getMessage()));
            }
        });
    }

    private void mostrarDialogoExtracto(String cuenta) {
        executor.execute(() -> {
            try {
                List<MovimientoModel> movs = controlador.obtenerExtracto(cuenta);
                StringBuilder sb = new StringBuilder();
                if (movs.isEmpty()) {
                    sb.append("Sin movimientos registrados.");
                } else {
                    for (int i = 0; i < Math.min(movs.size(), 15); i++) {
                        MovimientoModel m = movs.get(i);
                        sb.append(String.format(Locale.US, "• %s | %s\n  Monto: $%.2f | Saldo: $%.2f\n  V%d (%s) - %s\n\n",
                                m.getNumeroOperacion(), m.getTipo(), m.getMonto(), m.getSaldoResultante(),
                                m.getVentanilla(), m.getNombreEmpleado(), m.getFechaHora()));
                    }
                }
                runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("Extracto Cuenta " + cuenta)
                        .setMessage(sb.toString())
                        .setPositiveButton("Cerrar", null)
                        .show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error extracto: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
