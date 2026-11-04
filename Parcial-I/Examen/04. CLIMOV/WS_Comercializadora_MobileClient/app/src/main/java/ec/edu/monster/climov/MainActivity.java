package ec.edu.monster.climov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.model.ConfiguracionConexion;
import ec.edu.monster.climov.view.AmortizacionActivity;
import ec.edu.monster.climov.view.CatalogoActivity;
import ec.edu.monster.climov.view.DialogoConfiguracionIP;
import ec.edu.monster.climov.view.FacturacionActivity;

/**
 * Pantalla Principal Mobile Client (Android)
 * Identidad visual homologada con Web JSF y Escritorio Swing (Grupo 04 - Monster / Sullivan)
 * NRC: 36884 | Arquitectura de Software
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvIpActualMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfiguracionConexion.init(this);
        setContentView(R.layout.activity_main);

        // Ocultar barra superior por defecto si existe para diseño limpio
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvIpActualMobile = findViewById(R.id.tvIpActualMobile);
        actualizarTextoIp();

        View btnConfigIp = findViewById(R.id.btnConfigurarIpMain);
        if (btnConfigIp != null) {
            btnConfigIp.setOnClickListener(v -> DialogoConfiguracionIP.mostrar(this, this::actualizarTextoIp));
        }
        if (tvIpActualMobile != null) {
            tvIpActualMobile.setOnClickListener(v -> DialogoConfiguracionIP.mostrar(this, this::actualizarTextoIp));
        }

        // 1. Acceso a los 4 casos rápidos de evaluación (Rúbrica)
        findViewById(R.id.btnQuickC1).setOnClickListener(v -> abrirFacturacionConCaso(1));
        findViewById(R.id.btnQuickC2).setOnClickListener(v -> abrirFacturacionConCaso(2));
        findViewById(R.id.btnQuickC3).setOnClickListener(v -> abrirFacturacionConCaso(3));
        findViewById(R.id.btnQuickC4).setOnClickListener(v -> abrirFacturacionConCaso(4));

        // 2. Módulos Principales
        findViewById(R.id.btnIrFacturacionCard).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FacturacionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnIrCatalogoCard).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnIrAmortizacionCard).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AmortizacionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnIrHistorialCard).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ec.edu.monster.climov.view.HistorialActivity.class);
            startActivity(intent);
        });

        // 3. Cerrar Sesión
        View btnSalir = findViewById(R.id.btnCerrarSesionMobile);
        if (btnSalir != null) {
            btnSalir.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ec.edu.monster.climov.view.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void actualizarTextoIp() {
        if (tvIpActualMobile != null) {
            tvIpActualMobile.setText("● Servidor: " + ConfiguracionConexion.getUrlBase() + " (Toca para cambiar IP)");
        }
    }

    private void abrirFacturacionConCaso(int caso) {
        Intent intent = new Intent(MainActivity.this, FacturacionActivity.class);
        intent.putExtra("caso", caso);
        startActivity(intent);
    }
}
