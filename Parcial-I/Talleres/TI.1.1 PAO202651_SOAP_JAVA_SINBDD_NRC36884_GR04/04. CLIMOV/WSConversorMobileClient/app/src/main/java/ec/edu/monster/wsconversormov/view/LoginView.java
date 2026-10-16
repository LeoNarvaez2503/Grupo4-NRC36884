package ec.edu.monster.wsconversormov.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.wsconversormov.R;
import ec.edu.monster.wsconversormov.controller.AppControlador;
import ec.edu.monster.wsconversormov.model.ConfiguracionConexion;
import ec.edu.monster.wsconversormov.model.UsuarioSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Vista de Inicio de Sesión para Cliente Móvil (Patrón MVC).
 */
public class LoginView extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtContrasenia;
    private CheckBox chkMostrarContrasenia;
    private Button btnIngresar;
    private Button btnLimpiar;
    private TextView btnConfigIp;

    private final AppControlador controlador = new AppControlador();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarVistas();
        configurarEventos();
    }

    private void inicializarVistas() {
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        chkMostrarContrasenia = findViewById(R.id.chkMostrarContrasenia);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnConfigIp = findViewById(R.id.btnConfigIp);
    }

    private void configurarEventos() {
        chkMostrarContrasenia.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtContrasenia.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                txtContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            txtContrasenia.setSelection(txtContrasenia.getText().length());
        });

        btnLimpiar.setOnClickListener(v -> {
            txtUsuario.setText("");
            txtContrasenia.setText("");
            txtUsuario.requestFocus();
        });

        btnIngresar.setOnClickListener(v -> procesarLogin());
        btnConfigIp.setOnClickListener(v -> mostrarDialogoConfigIp());
    }

    private void procesarLogin() {
        String user = txtUsuario.getText() != null ? txtUsuario.getText().toString().trim() : "";
        String pass = txtContrasenia.getText() != null ? txtContrasenia.getText().toString() : "";

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el usuario y la contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnIngresar.setEnabled(false);
        btnIngresar.setText("Conectando...");

        executor.execute(() -> {
            try {
                String token = controlador.login(user, pass);

                mainHandler.post(() -> {
                    btnIngresar.setEnabled(true);
                    btnIngresar.setText(R.string.btn_ingresar);

                    if (token != null && !token.isBlank()) {
                        UsuarioSession session = new UsuarioSession(user.toUpperCase(), token);
                        Toast.makeText(LoginView.this, "¡Bienvenido " + user + "!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginView.this, ConversorView.class);
                        intent.putExtra("usuario_session", session);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginView.this, "Usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    btnIngresar.setEnabled(true);
                    btnIngresar.setText(R.string.btn_ingresar);

                    String msg = e.getMessage();
                    if (msg == null || msg.contains("Fault") || msg.isBlank()) {
                        msg = "Usuario o contraseña incorrectos o error de red.";
                    }
                    Toast.makeText(LoginView.this, msg, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarDialogoConfigIp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configurar Endpoint SOAP");

        final EditText input = new EditText(this);
        input.setText(ConfiguracionConexion.getEndpointUrl());
        input.setPadding(30, 20, 30, 20);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevaUrl = input.getText().toString();
            ConfiguracionConexion.setEndpointUrl(nuevaUrl);
            Toast.makeText(LoginView.this, "URL actualizada.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
