package ec.edu.monster.climov.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.MainActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.ConfiguracionConexion;

/**
 * Pantalla de Login Móvil (Android).
 * Credenciales oficiales requeridas:
 * Usuario: Monster
 * Contraseña: Monster9
 * NRC: 36884 | Grupo 04
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etClave;
    private TextView tvErrorLogin;
    private Button btnIngresar;
    private Button btnConfigurarIpMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfiguracionConexion.init(this);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        tvErrorLogin = findViewById(R.id.tvErrorLogin);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnConfigurarIpMobile = findViewById(R.id.btnConfigurarIpMobile);

        actualizarBotonIp();

        btnIngresar.setOnClickListener(v -> autenticar());
        if (btnConfigurarIpMobile != null) {
            btnConfigurarIpMobile.setOnClickListener(v -> DialogoConfiguracionIP.mostrar(this, this::actualizarBotonIp));
        }
    }

    private void actualizarBotonIp() {
        if (btnConfigurarIpMobile != null) {
            btnConfigurarIpMobile.setText("⚙️ Servidor: " + ConfiguracionConexion.getHost() + ":" + ConfiguracionConexion.getPort() + " (Cambiar IP)");
        }
    }

    private void autenticar() {
        String usuario = etUsuario.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        btnIngresar.setEnabled(false);
        tvErrorLogin.setVisibility(View.GONE);

        java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
            boolean ok = AppControlador.getInstancia().autenticar(usuario, clave);
            runOnUiThread(() -> {
                btnIngresar.setEnabled(true);
                if (ok) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvErrorLogin.setText("❌ Credenciales incorrectas. Verifique en BDD (Monster / Monster9)");
                    tvErrorLogin.setVisibility(View.VISIBLE);
                    etClave.setText("");
                    etClave.requestFocus();
                }
            });
        });
    }
}
