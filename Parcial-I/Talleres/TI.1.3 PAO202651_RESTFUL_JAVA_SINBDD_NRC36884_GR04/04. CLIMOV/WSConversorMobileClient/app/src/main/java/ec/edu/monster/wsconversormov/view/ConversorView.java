package ec.edu.monster.wsconversormov.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.wsconversormov.R;
import ec.edu.monster.wsconversormov.controller.AppControlador;
import ec.edu.monster.wsconversormov.model.UnidadLongitud;
import ec.edu.monster.wsconversormov.model.UnidadMasa;
import ec.edu.monster.wsconversormov.model.UnidadTemperatura;
import ec.edu.monster.wsconversormov.model.UsuarioSession;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Vista del Conversor de Unidades para Cliente Móvil (Patrón MVC).
 */
public class ConversorView extends AppCompatActivity {

    private TextView txtUsuarioSesion;
    private TextView btnCerrarSesion;
    private TextView txtTitulo;
    private Spinner spnMagnitud;
    private EditText txtValor;
    private Spinner spnUndInit;
    private Spinner spnUndFinal;
    private Button btnConvertir;
    private TextView txtResultado;

    private final AppControlador controlador = new AppControlador();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final String[] magnitudes = new String[]{"LONGITUD", "MASA", "TEMPERATURA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        inicializarVistas();
        configurarDatosUsuario();
        configurarSpinnerMagnitud();
        configurarEventos();
    }

    private void inicializarVistas() {
        txtUsuarioSesion = findViewById(R.id.txtUsuarioSesion);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        txtTitulo = findViewById(R.id.txtTitulo);
        spnMagnitud = findViewById(R.id.spnMagnitud);
        txtValor = findViewById(R.id.txtValor);
        spnUndInit = findViewById(R.id.spnUndInit);
        spnUndFinal = findViewById(R.id.spnUndFinal);
        btnConvertir = findViewById(R.id.btnConvertir);
        txtResultado = findViewById(R.id.txtResultado);
    }

    private void configurarDatosUsuario() {
        UsuarioSession session = (UsuarioSession) getIntent().getSerializableExtra("usuario_session");
        if (session != null) {
            txtUsuarioSesion.setText("Usuario: " + session.getUsuario());
        }
    }

    private void configurarSpinnerMagnitud() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                magnitudes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMagnitud.setAdapter(adapter);

        spnMagnitud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarSpinnersSegunMagnitud(magnitudes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void actualizarSpinnersSegunMagnitud(String magnitud) {
        String[] unidades;
        switch (magnitud) {
            case "MASA":
                txtTitulo.setText(R.string.title_masa);
                unidades = Arrays.stream(UnidadMasa.values()).map(Enum::name).toArray(String[]::new);
                break;
            case "TEMPERATURA":
                txtTitulo.setText(R.string.title_temperatura);
                unidades = Arrays.stream(UnidadTemperatura.values()).map(Enum::name).toArray(String[]::new);
                break;
            case "LONGITUD":
            default:
                txtTitulo.setText(R.string.title_longitud);
                unidades = Arrays.stream(UnidadLongitud.values()).map(Enum::name).toArray(String[]::new);
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                unidades
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnUndInit.setAdapter(adapter);
        spnUndFinal.setAdapter(adapter);

        if (unidades.length > 1) {
            spnUndInit.setSelection(0);
            spnUndFinal.setSelection(1);
        }
    }

    private void configurarEventos() {
        btnConvertir.setOnClickListener(v -> realizarConversion());

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(ConversorView.this, LoginView.class);
            startActivity(intent);
            finish();
        });
    }

    private void realizarConversion() {
        String valorStr = txtValor.getText() != null ? txtValor.getText().toString().trim() : "";
        if (valorStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un valor a convertir.", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        String magnitud = (String) spnMagnitud.getSelectedItem();
        String undInit = (String) spnUndInit.getSelectedItem();
        String undFinal = (String) spnUndFinal.getSelectedItem();

        btnConvertir.setEnabled(false);
        btnConvertir.setText("Convirtiendo...");

        executor.execute(() -> {
            try {
                double resultado;
                switch (magnitud) {
                    case "MASA":
                        resultado = controlador.convertirMasa(valor, undInit, undFinal);
                        break;
                    case "TEMPERATURA":
                        resultado = controlador.convertirTemperatura(valor, undInit, undFinal);
                        break;
                    case "LONGITUD":
                    default:
                        resultado = controlador.convertirLongitud(valor, undInit, undFinal);
                        break;
                }

                mainHandler.post(() -> {
                    btnConvertir.setEnabled(true);
                    btnConvertir.setText(R.string.btn_convertir);
                    txtResultado.setText(String.format("%.4f %s", resultado, undFinal));
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    btnConvertir.setEnabled(true);
                    btnConvertir.setText(R.string.btn_convertir);
                    Toast.makeText(ConversorView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
