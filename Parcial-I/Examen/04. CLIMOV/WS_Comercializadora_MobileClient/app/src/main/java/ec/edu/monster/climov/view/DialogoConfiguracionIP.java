package ec.edu.monster.climov.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.model.ConfiguracionConexion;
import ec.edu.monster.climov.model.ConfiguracionConexion.ResultadoPrueba;
import java.util.concurrent.Executors;

/**
 * Diálogo interactivo para configurar la dirección IP y puerto del servidor Payara / BanQuito en Android.
 */
public class DialogoConfiguracionIP {

    public static void mostrar(Activity activity, Runnable alGuardarCallback) {
        if (activity == null || activity.isFinishing()) return;

        ConfiguracionConexion.init(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_configurar_ip, null);

        EditText etHost = dialogView.findViewById(R.id.etDlgHost);
        EditText etPuerto = dialogView.findViewById(R.id.etDlgPuerto);
        TextView tvEstado = dialogView.findViewById(R.id.tvDlgEstadoPrueba);
        Button btnPresetEmulador = dialogView.findViewById(R.id.btnPresetEmulador);
        Button btnPresetLan = dialogView.findViewById(R.id.btnPresetLan);
        Button btnProbar = dialogView.findViewById(R.id.btnDlgProbar);
        Button btnGuardar = dialogView.findViewById(R.id.btnDlgGuardar);

        etHost.setText(ConfiguracionConexion.getHost());
        etPuerto.setText(String.valueOf(ConfiguracionConexion.getPort()));

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        btnPresetEmulador.setOnClickListener(v -> {
            etHost.setText("10.0.2.2");
            etPuerto.setText("8080");
        });

        btnPresetLan.setOnClickListener(v -> {
            etHost.setText("192.168.18.249");
            etPuerto.setText("8080");
        });

        btnProbar.setOnClickListener(v -> {
            String testHost = etHost.getText().toString().trim();
            int testPort = 8080;
            try {
                testPort = Integer.parseInt(etPuerto.getText().toString().trim());
            } catch (Exception ignored) {}

            btnProbar.setEnabled(false);
            tvEstado.setVisibility(View.VISIBLE);
            tvEstado.setBackgroundResource(R.drawable.badge_amber);
            tvEstado.setTextColor(activity.getResources().getColor(R.color.brand_navy));
            tvEstado.setText("⏳ Probando conexión a " + testHost + ":" + testPort + "...");

            final int pFinal = testPort;
            Executors.newSingleThreadExecutor().execute(() -> {
                ResultadoPrueba res = ConfiguracionConexion.probarConexion(testHost, pFinal);
                new Handler(Looper.getMainLooper()).post(() -> {
                    btnProbar.setEnabled(true);
                    if (res.exitoso) {
                        tvEstado.setBackgroundResource(R.drawable.badge_green);
                        tvEstado.setTextColor(activity.getResources().getColor(R.color.success));
                        tvEstado.setText(res.mensaje);
                    } else {
                        tvEstado.setBackgroundResource(R.drawable.badge_red);
                        tvEstado.setTextColor(activity.getResources().getColor(R.color.danger));
                        tvEstado.setText(res.mensaje);
                    }
                });
            });
        });

        btnGuardar.setOnClickListener(v -> {
            String nuevoHost = etHost.getText().toString().trim();
            int nuevoPuerto = 8080;
            try {
                nuevoPuerto = Integer.parseInt(etPuerto.getText().toString().trim());
            } catch (Exception e) {
                Toast.makeText(activity, "Puerto inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            ConfiguracionConexion.guardar(activity, nuevoHost, nuevoPuerto);
            Toast.makeText(activity, "✔ Servidor guardado: " + ConfiguracionConexion.getUrlBase(), Toast.LENGTH_LONG).show();

            if (alGuardarCallback != null) {
                alGuardarCallback.run();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
