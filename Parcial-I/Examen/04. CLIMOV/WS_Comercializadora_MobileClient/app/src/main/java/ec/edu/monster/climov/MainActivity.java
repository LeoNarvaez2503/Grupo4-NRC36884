package ec.edu.monster.climov;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.view.AmortizacionActivity;
import ec.edu.monster.climov.view.CatalogoActivity;
import ec.edu.monster.climov.view.FacturacionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFacturacion = findViewById(R.id.btnIrFacturacion);
        Button btnCatalogo = findViewById(R.id.btnIrCatalogo);
        Button btnAmortizacion = findViewById(R.id.btnIrAmortizacion);

        btnFacturacion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FacturacionActivity.class);
            startActivity(intent);
        });

        btnCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        btnAmortizacion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AmortizacionActivity.class);
            startActivity(intent);
        });
    }
}
