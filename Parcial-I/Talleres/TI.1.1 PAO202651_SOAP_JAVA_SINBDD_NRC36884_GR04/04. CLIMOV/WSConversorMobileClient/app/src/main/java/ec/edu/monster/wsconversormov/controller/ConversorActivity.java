package ec.edu.monster.wsconversormov.controller;

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
import ec.edu.monster.wsconversormov.model.UnidadLongitud;
import ec.edu.monster.wsconversormov.model.UnidadMasa;
import ec.edu.monster.wsconversormov.model.UnidadTemperatura;
import ec.edu.monster.wsconversormov.model.UsuarioSession;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConversorActivity extends AppCompatActivity {

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
            txtUsuarioSesion.setText("Usuario: " + session.getUsuario() + " (" + session.getHoraIngreso() + ")");
        } else {
            txtUsuarioSesion.setText("Usuario: MONSTER");
        }
    }

    private void configurarSpinnerMagnitud() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, magnitudes);
        spnMagnitud.setAdapter(adapter);

        spnMagnitud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarUnidades();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Actualiza las unidades de los Spinners según la magnitud seleccionada (idéntico a Swing).
     */
    private void actualizarUnidades() {
        String seleccion = (String) spnMagnitud.getSelectedItem();
        if (seleccion == null) {
            seleccion = "LONGITUD";
        }

        String[] unidades;
        switch (seleccion) {
            case "MASA":
                unidades = Arrays.stream(UnidadMasa.values())
                        .map(Enum::name)
                        .toArray(String[]::new);
                txtTitulo.setText(R.string.title_masa);
                break;
            case "TEMPERATURA":
                unidades = Arrays.stream(UnidadTemperatura.values())
                        .map(Enum::name)
                        .toArray(String[]::new);
                txtTitulo.setText(R.string.title_temperatura);
                break;
            case "LONGITUD":
            default:
                unidades = Arrays.stream(UnidadLongitud.values())
                        .map(Enum::name)
                        .toArray(String[]::new);
                txtTitulo.setText(R.string.title_longitud);
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unidades);
        spnUndInit.setAdapter(adapter);
        spnUndFinal.setAdapter(adapter);

        if (unidades.length > 1) {
            spnUndInit.setSelection(0);
            spnUndFinal.setSelection(1);
        }

        txtResultado.setText("");
    }

    private void configurarEventos() {
        btnConvertir.setOnClickListener(v -> procesarConversion());

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this, "Sesión finalizada.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ConversorActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void procesarConversion() {
        String txtVal = txtValor.getText() != null ? txtValor.getText().toString().trim() : "";
        if (txtVal.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un valor a convertir.", Toast.LENGTH_SHORT).show();
            return;
        }

        final double num;
        try {
            num = Double.parseDouble(txtVal);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Ingrese un número válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String unidadInicial = (String) spnUndInit.getSelectedItem();
        final String unidadFinal = (String) spnUndFinal.getSelectedItem();
        final String tipo = (String) spnMagnitud.getSelectedItem();

        btnConvertir.setEnabled(false);
        btnConvertir.setText("Convirtiendo...");

        executor.execute(() -> {
            try {
                final double conversion;

                if ("MASA".equalsIgnoreCase(tipo)) {
                    conversion = controlador.convertirMasa(num, unidadInicial, unidadFinal);
                } else if ("TEMPERATURA".equalsIgnoreCase(tipo)) {
                    conversion = controlador.convertirTemperatura(num, unidadInicial, unidadFinal);
                } else {
                    conversion = controlador.convertirLongitud(num, unidadInicial, unidadFinal);
                }

                mainHandler.post(() -> {
                    btnConvertir.setEnabled(true);
                    btnConvertir.setText(R.string.btn_convertir);
                    txtResultado.setText(String.format("%.4f", conversion));
                });

            } catch (Exception ex) {
                mainHandler.post(() -> {
                    btnConvertir.setEnabled(true);
                    btnConvertir.setText(R.string.btn_convertir);

                    String msg = ex.getMessage();
                    if (msg == null || msg.isBlank()) {
                        msg = "Error al conectar con el servidor SOAP.";
                    }
                    Toast.makeText(ConversorActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
