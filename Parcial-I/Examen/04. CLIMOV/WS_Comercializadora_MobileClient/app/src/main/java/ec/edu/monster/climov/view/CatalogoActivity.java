package ec.edu.monster.climov.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.TelefonoModel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CatalogoActivity extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private LinearLayout layoutListaTelefonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        layoutListaTelefonos = findViewById(R.id.layoutListaTelefonos);
        Button btnRefrescar = findViewById(R.id.btnRefrescarCatalogo);
        btnRefrescar.setOnClickListener(v -> cargarLista());

        cargarLista();
    }

    private void cargarLista() {
        layoutListaTelefonos.removeAllViews();
        TextView cargando = new TextView(this);
        cargando.setText("Cargando catálogo desde el servicio REST...");
        layoutListaTelefonos.addView(cargando);

        executor.execute(() -> {
            List<TelefonoModel> lista = controlador.listarTelefonos();
            mainHandler.post(() -> {
                layoutListaTelefonos.removeAllViews();
                if (lista.isEmpty()) {
                    TextView vacio = new TextView(this);
                    vacio.setText("No hay celulares disponibles en el catálogo.");
                    layoutListaTelefonos.addView(vacio);
                    return;
                }

                for (TelefonoModel t : lista) {
                    LinearLayout card = new LinearLayout(this);
                    card.setOrientation(LinearLayout.VERTICAL);
                    card.setPadding(16, 16, 16, 16);
                    card.setBackgroundColor(Color.WHITE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 16);
                    card.setLayoutParams(lp);

                    TextView titulo = new TextView(this);
                    titulo.setText(t.getMarca() + " - " + t.getModelo());
                    titulo.setTypeface(null, Typeface.BOLD);
                    titulo.setTextSize(16f);
                    titulo.setTextColor(Color.parseColor("#182B49"));

                    TextView precio = new TextView(this);
                    precio.setText(String.format("Precio: $%.2f | Estado: %s", t.getPrecio(), t.getEstado()));
                    precio.setTextColor(Color.parseColor("#187846"));
                    precio.setTypeface(null, Typeface.BOLD);

                    TextView carac = new TextView(this);
                    carac.setText(t.getCaracteristicas());
                    carac.setTextSize(12f);
                    carac.setTextColor(Color.DKGRAY);

                    card.addView(titulo);
                    card.addView(precio);
                    card.addView(carac);
                    layoutListaTelefonos.addView(card);
                }
            });
        });
    }
}
