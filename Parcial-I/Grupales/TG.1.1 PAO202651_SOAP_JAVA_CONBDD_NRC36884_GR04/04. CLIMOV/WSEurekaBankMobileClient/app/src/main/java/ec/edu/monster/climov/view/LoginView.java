package ec.edu.monster.climov.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.ConfiguracionConexion;
import ec.edu.monster.climov.model.UsuarioSesion;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Capa Vista (Patrón MVC Tradicional): Actividad Android de Autenticación.
 */
public class LoginView extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText etUsuario;
    private EditText etClave;
    private EditText etHost;
    private EditText etPuerto;
    private Spinner spVentanilla;
    private Button btnLogin;
    private TextView tvEstadoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        etHost = findViewById(R.id.etHost);
        etPuerto = findViewById(R.id.etPuerto);
        spVentanilla = findViewById(R.id.spVentanilla);
        btnLogin = findViewById(R.id.btnLogin);
        tvEstadoLogin = findViewById(R.id.tvEstadoLogin);

        SharedPreferences sp = getSharedPreferences("EurekaBankPrefs", MODE_PRIVATE);
        String savedHost = sp.getString("server_host", ConfiguracionConexion.getHost());
        int savedPort = sp.getInt("server_port", ConfiguracionConexion.getPuerto());

        etHost.setText(savedHost);
        etPuerto.setText(String.valueOf(savedPort));
        ConfiguracionConexion.configurar(savedHost, savedPort);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4"});
        spVentanilla.setAdapter(adapter);

        btnLogin.setOnClickListener(v -> procesarLogin());
    }

    private void procesarLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String clave = etClave.getText().toString().trim();
        String host = etHost.getText().toString().trim();
        int puerto = 8080;
        try {
            puerto = Integer.parseInt(etPuerto.getText().toString().trim());
        } catch (NumberFormatException ignored) {
        }
        int ventanilla = spVentanilla.getSelectedItemPosition() + 1;

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        controlador.configurarServidor(host, puerto);
        controlador.setVentanillaActiva(ventanilla);

        SharedPreferences sp = getSharedPreferences("EurekaBankPrefs", MODE_PRIVATE);
        sp.edit().putString("server_host", host).putInt("server_port", puerto).apply();
        btnLogin.setEnabled(false);
        tvEstadoLogin.setText("Conectando con servidor EurekaBank...");

        executor.execute(() -> {
            try {
                UsuarioSesion sesion = controlador.iniciarSesion(usuario, clave);
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    if (sesion != null && sesion.isAutenticado()) {
                        Toast.makeText(this, sesion.getMensaje(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, DashboardView.class));
                        finish();
                    } else {
                        String msg = sesion != null ? sesion.getMensaje() : "Credenciales inválidas.";
                        tvEstadoLogin.setText(msg);
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    tvEstadoLogin.setText("Error de red: " + e.getMessage());
                    Toast.makeText(this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
