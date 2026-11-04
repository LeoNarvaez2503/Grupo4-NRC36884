package ec.edu.monster.climov.view;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.TelefonoModel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pantalla de Mantenimiento CRUD Completo para el Catálogo de Celulares (Android).
 * Funcionalidades:
 * - Create: Registrar nuevo smartphone
 * - Read: Listar catálogo con estado y precio
 * - Update: Modificar datos y precio
 * - Delete: Eliminar o desactivar smartphone
 */
public class CatalogoActivity extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private LinearLayout layoutListaTelefonos;

    private boolean activo = false;
    private int ultimaCantidadTelefonos = -1;
    private int ultimoChecksumCatalogo = -1;

    private final Runnable tareaPolling = new Runnable() {
        @Override
        public void run() {
            if (!activo) return;
            sincronizarCatalogoSilencioso();
            mainHandler.postDelayed(this, 2500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        layoutListaTelefonos = findViewById(R.id.layoutListaTelefonos);
        Button btnRefrescar = findViewById(R.id.btnRefrescarCatalogo);
        Button btnNuevo = findViewById(R.id.btnNuevoCelular);

        btnRefrescar.setOnClickListener(v -> cargarLista());
        btnNuevo.setOnClickListener(v -> mostrarDialogoFormulario(null));

        cargarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activo = true;
        mainHandler.post(tareaPolling);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activo = false;
        mainHandler.removeCallbacks(tareaPolling);
    }

    private void cargarLista() {
        layoutListaTelefonos.removeAllViews();
        TextView cargando = new TextView(this);
        cargando.setText("⏳ Cargando catálogo desde la Base de Datos...");
        cargando.setPadding(16, 24, 16, 16);
        cargando.setTextColor(getResources().getColor(R.color.brand_navy));
        layoutListaTelefonos.addView(cargando);

        ultimaCantidadTelefonos = -1;
        ultimoChecksumCatalogo = -1;
        sincronizarCatalogoSilencioso();
    }

    private void sincronizarCatalogoSilencioso() {
        executor.execute(() -> {
            List<TelefonoModel> lista = controlador.listarTelefonos();
            int checksum = calcularChecksum(lista);
            mainHandler.post(() -> {
                if (!activo || lista == null) return;
                if (lista.size() != ultimaCantidadTelefonos || checksum != ultimoChecksumCatalogo) {
                    ultimaCantidadTelefonos = lista.size();
                    ultimoChecksumCatalogo = checksum;
                    layoutListaTelefonos.removeAllViews();
                    if (lista.isEmpty()) {
                        TextView vacio = new TextView(this);
                        vacio.setText("No hay celulares registrados o no se pudo conectar al servidor.");
                        vacio.setPadding(16, 24, 16, 16);
                        layoutListaTelefonos.addView(vacio);
                        return;
                    }

                    for (TelefonoModel t : lista) {
                        layoutListaTelefonos.addView(crearTarjetaTelefono(t));
                    }
                }
            });
        });
    }

    private int calcularChecksum(List<TelefonoModel> tels) {
        if (tels == null) return 0;
        int sum = 0;
        for (TelefonoModel t : tels) {
            sum = 31 * sum + t.getCodTelefono() + (int)(t.getPrecio() * 100) + (t.getEstado() != null ? t.getEstado().hashCode() : 0);
        }
        return sum;
    }

    private View crearTarjetaTelefono(TelefonoModel t) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 18, 20, 18);
        card.setBackgroundResource(R.drawable.card_rounded_white);
        card.setElevation(4f);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 14);
        card.setLayoutParams(lp);

        // Fila 1: Marca & Estado (Badges)
        LinearLayout filaBadges = new LinearLayout(this);
        filaBadges.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvMarcaBadge = new TextView(this);
        tvMarcaBadge.setText(t.getMarca().toUpperCase());
        tvMarcaBadge.setBackgroundResource(R.drawable.badge_gold);
        tvMarcaBadge.setTextColor(getResources().getColor(R.color.accent));
        tvMarcaBadge.setTextSize(10f);
        tvMarcaBadge.setTypeface(null, Typeface.BOLD);
        tvMarcaBadge.setPadding(12, 4, 12, 4);

        View spacer = new View(this);
        LinearLayout.LayoutParams spacerLp = new LinearLayout.LayoutParams(0, 1, 1f);
        spacer.setLayoutParams(spacerLp);

        TextView tvEstadoBadge = new TextView(this);
        boolean disponible = "DISPONIBLE".equalsIgnoreCase(t.getEstado()) || "ACTIVO".equalsIgnoreCase(t.getEstado());
        tvEstadoBadge.setText(disponible ? "● DISPONIBLE" : "○ INACTIVO");
        tvEstadoBadge.setBackgroundResource(disponible ? R.drawable.badge_green : R.drawable.badge_red);
        tvEstadoBadge.setTextColor(disponible ? Color.parseColor("#166534") : Color.parseColor("#991B1B"));
        tvEstadoBadge.setTextSize(10f);
        tvEstadoBadge.setTypeface(null, Typeface.BOLD);
        tvEstadoBadge.setPadding(12, 4, 12, 4);

        filaBadges.addView(tvMarcaBadge);
        filaBadges.addView(spacer);
        filaBadges.addView(tvEstadoBadge);
        card.addView(filaBadges);

        // Modelo
        TextView tvModelo = new TextView(this);
        tvModelo.setText(t.getModelo());
        tvModelo.setTypeface(null, Typeface.BOLD);
        tvModelo.setTextSize(17f);
        tvModelo.setTextColor(getResources().getColor(R.color.brand_navy));
        tvModelo.setPadding(0, 8, 0, 2);
        card.addView(tvModelo);

        // Precio
        TextView tvPrecio = new TextView(this);
        tvPrecio.setText(String.format("$%.2f USD", t.getPrecio()));
        tvPrecio.setTextSize(19f);
        tvPrecio.setTypeface(null, Typeface.BOLD);
        tvPrecio.setTextColor(getResources().getColor(R.color.primary));
        card.addView(tvPrecio);

        // Características
        if (t.getCaracteristicas() != null && !t.getCaracteristicas().isBlank()) {
            TextView tvCarac = new TextView(this);
            tvCarac.setText(t.getCaracteristicas());
            tvCarac.setTextSize(12f);
            tvCarac.setTextColor(Color.parseColor("#64748B"));
            tvCarac.setPadding(0, 6, 0, 12);
            card.addView(tvCarac);
        }

        // Fila Botones de Acción (Editar y Eliminar)
        LinearLayout filaAcciones = new LinearLayout(this);
        filaAcciones.setOrientation(LinearLayout.HORIZONTAL);
        filaAcciones.setPadding(0, 6, 0, 0);

        Button btnEditar = new Button(this, null, android.R.attr.borderlessButtonStyle);
        btnEditar.setText("✏️ Editar");
        btnEditar.setTextSize(12f);
        btnEditar.setTextColor(getResources().getColor(R.color.primary));
        btnEditar.setBackgroundResource(R.drawable.card_rounded_white);
        btnEditar.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(0, 90, 1f);
        lpBtn.setMarginEnd(8);
        btnEditar.setLayoutParams(lpBtn);
        btnEditar.setOnClickListener(v -> mostrarDialogoFormulario(t));

        Button btnEliminar = new Button(this, null, android.R.attr.borderlessButtonStyle);
        btnEliminar.setText("🗑️ Eliminar");
        btnEliminar.setTextSize(12f);
        btnEliminar.setTextColor(getResources().getColor(R.color.danger));
        btnEliminar.setBackgroundResource(R.drawable.badge_red);
        btnEliminar.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams lpDel = new LinearLayout.LayoutParams(0, 90, 1f);
        btnEliminar.setLayoutParams(lpDel);
        btnEliminar.setOnClickListener(v -> confirmarEliminar(t));

        filaAcciones.addView(btnEditar);
        filaAcciones.addView(btnEliminar);
        card.addView(filaAcciones);

        return card;
    }

    private void mostrarDialogoFormulario(TelefonoModel tExistente) {
        boolean esEdicion = (tExistente != null);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formulario_celular, null);

        TextView tvTitulo = dialogView.findViewById(R.id.tvTituloDialogoCelular);
        EditText etMarca = dialogView.findViewById(R.id.etMarcaCelular);
        EditText etModelo = dialogView.findViewById(R.id.etModeloCelular);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecioCelular);
        EditText etCarac = dialogView.findViewById(R.id.etCaracteristicasCelular);
        Spinner spnEstado = dialogView.findViewById(R.id.spnEstadoCelular);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarCelular);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarCelular);

        // Configurar opciones de estado
        String[] estados = {"DISPONIBLE", "INACTIVO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spnEstado.setAdapter(adapter);

        if (esEdicion) {
            tvTitulo.setText("Editar Celular #" + tExistente.getCodTelefono());
            etMarca.setText(tExistente.getMarca());
            etModelo.setText(tExistente.getModelo());
            etPrecio.setText(String.valueOf(tExistente.getPrecio()));
            etCarac.setText(tExistente.getCaracteristicas() != null ? tExistente.getCaracteristicas() : "");
            if ("INACTIVO".equalsIgnoreCase(tExistente.getEstado())) {
                spnEstado.setSelection(1);
            } else {
                spnEstado.setSelection(0);
            }
        } else {
            tvTitulo.setText("Registrar Nuevo Celular");
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnGuardar.setOnClickListener(v -> {
            String marca = etMarca.getText().toString().trim();
            String modelo = etModelo.getText().toString().trim();
            String precioStr = etPrecio.getText().toString().trim();
            String carac = etCarac.getText().toString().trim();
            String estado = (String) spnEstado.getSelectedItem();

            if (marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete Marca, Modelo y Precio", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
                if (precio <= 0) {
                    Toast.makeText(this, "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception ex) {
                Toast.makeText(this, "Precio numérico inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            btnGuardar.setEnabled(false);

            TelefonoModel tel = esEdicion ? tExistente : new TelefonoModel();
            tel.setMarca(marca);
            tel.setModelo(modelo);
            tel.setPrecio(precio);
            tel.setCaracteristicas(carac);
            tel.setEstado(estado);

            executor.execute(() -> {
                boolean ok;
                if (esEdicion) {
                    ok = controlador.actualizarTelefono(tel);
                } else {
                    ok = controlador.crearTelefono(tel);
                }

                mainHandler.post(() -> {
                    btnGuardar.setEnabled(true);
                    if (ok) {
                        Toast.makeText(this, esEdicion ? "✔ Celular actualizado exitosamente" : "✔ Celular creado en el catálogo", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        cargarLista();
                    } else {
                        Toast.makeText(this, "❌ Error al guardar en el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }

    private void confirmarEliminar(TelefonoModel t) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Desea eliminar o dar de baja el celular " + t.getMarca() + " " + t.getModelo() + " (ID: " + t.getCodTelefono() + ")?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    executor.execute(() -> {
                        boolean ok = controlador.eliminarTelefono(t.getCodTelefono());
                        mainHandler.post(() -> {
                            if (ok) {
                                Toast.makeText(this, "✔ Celular eliminado/desactivado del catálogo", Toast.LENGTH_SHORT).show();
                                cargarLista();
                            } else {
                                Toast.makeText(this, "❌ No se pudo eliminar el celular", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
