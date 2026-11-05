package ec.edu.monster.climov.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.FacturaModel.FacturaItem;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pantalla de Historial de Facturas Emitidas (Android).
 * Sincronización reactiva automática en tiempo real cada 2.5s (Patrón EurekaBank).
 * Cualquier factura emitida desde Web, Escritorio o Móvil aparece aquí en vivo.
 * NRC: 36884 | Grupo 04 (Monster)
 */
public class HistorialActivity extends AppCompatActivity {

    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView tvTotalFacturadoMobile;
    private TextView tvCountCreditoMobile;
    private TextView tvCountEfectivoMobile;
    private LinearLayout layoutListaFacturas;

    private boolean activo = false;
    private int ultimaCantidadFacturas = -1;
    private int ultimoIdFactura = -1;

    private final Runnable tareaPolling = new Runnable() {
        @Override
        public void run() {
            if (!activo) return;
            sincronizarFacturas();
            mainHandler.postDelayed(this, 2500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvTotalFacturadoMobile = findViewById(R.id.tvTotalFacturadoMobile);
        tvCountCreditoMobile = findViewById(R.id.tvCountCreditoMobile);
        tvCountEfectivoMobile = findViewById(R.id.tvCountEfectivoMobile);
        layoutListaFacturas = findViewById(R.id.layoutListaFacturas);
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

    private void sincronizarFacturas() {
        executor.execute(() -> {
            List<FacturaItem> facturas = controlador.listarFacturas();
            mainHandler.post(() -> {
                if (!activo || facturas == null) return;
                int ultimoId = facturas.isEmpty() ? 0 : facturas.get(facturas.size() - 1).numFactura;
                if (facturas.size() != ultimaCantidadFacturas || ultimoId != ultimoIdFactura) {
                    ultimaCantidadFacturas = facturas.size();
                    ultimoIdFactura = ultimoId;
                    actualizarUIFacturas(facturas);
                }
            });
        });
    }

    private void actualizarUIFacturas(List<FacturaItem> facturas) {
        // Calcular KPIs
        double totalMonto = 0.0;
        int countCredito = 0;
        int countEfectivo = 0;

        for (FacturaItem f : facturas) {
            totalMonto += f.totalPagar;
            if ("EFECTIVO".equalsIgnoreCase(f.formaPago)) {
                countEfectivo++;
            } else {
                countCredito++;
            }
        }

        tvTotalFacturadoMobile.setText(String.format(Locale.US, "$ %.2f", totalMonto));
        tvCountCreditoMobile.setText(countCredito + " facturas");
        tvCountEfectivoMobile.setText(countEfectivo + " facturas");

        // Construir listado de tarjetas
        layoutListaFacturas.removeAllViews();

        if (facturas.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No hay facturas registradas en la base comercializadora_db.");
            empty.setPadding(16, 32, 16, 16);
            empty.setTextColor(getResources().getColor(R.color.brand_navy));
            layoutListaFacturas.addView(empty);
            return;
        }

        // Renderizar en orden inverso (más reciente primero)
        for (int i = facturas.size() - 1; i >= 0; i--) {
            layoutListaFacturas.addView(crearTarjetaFactura(facturas.get(i)));
        }
    }

    private View crearTarjetaFactura(FacturaItem f) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundResource(R.drawable.card_rounded_white);
        card.setElevation(3f);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 12);
        card.setLayoutParams(lp);

        // Fila 1: Factura # y Forma de Pago Badge
        LinearLayout fila1 = new LinearLayout(this);
        fila1.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvNum = new TextView(this);
        tvNum.setText("Factura #" + f.numFactura);
        tvNum.setTextSize(14f);
        tvNum.setTypeface(null, Typeface.BOLD);
        tvNum.setTextColor(getResources().getColor(R.color.brand_navy));

        View space1 = new View(this);
        LinearLayout.LayoutParams spLp = new LinearLayout.LayoutParams(0, 1, 1f);
        space1.setLayoutParams(spLp);

        TextView tvBadgePago = new TextView(this);
        boolean esEfectivo = "EFECTIVO".equalsIgnoreCase(f.formaPago);
        tvBadgePago.setText(esEfectivo ? "EFECTIVO (-42%)" : "CRÉDITO DIRECTO");
        tvBadgePago.setTextSize(9f);
        tvBadgePago.setTypeface(null, Typeface.BOLD);
        tvBadgePago.setPadding(10, 3, 10, 3);
        tvBadgePago.setBackgroundResource(esEfectivo ? R.drawable.badge_green : R.drawable.badge_gold);
        tvBadgePago.setTextColor(esEfectivo ? Color.parseColor("#15803D") : getResources().getColor(R.color.accent));

        fila1.addView(tvNum);
        fila1.addView(space1);
        fila1.addView(tvBadgePago);
        card.addView(fila1);

        // Comprobante & Fecha
        TextView tvComprobante = new TextView(this);
        tvComprobante.setText("Comprobante: " + f.numeroComprobante + " | Fecha: " + (f.fecha != null ? f.fecha : "Hoy"));
        tvComprobante.setTextSize(10f);
        tvComprobante.setTextColor(getResources().getColor(R.color.gray));
        tvComprobante.setPadding(0, 2, 0, 8);
        card.addView(tvComprobante);

        // Cliente
        TextView tvCliente = new TextView(this);
        tvCliente.setText("👤 " + f.nombreCliente + "  (C.I. " + f.cedulaCliente + ")");
        tvCliente.setTextSize(13f);
        tvCliente.setTypeface(null, Typeface.BOLD);
        tvCliente.setTextColor(Color.parseColor("#1E293B"));
        card.addView(tvCliente);

        // Dispositivo
        TextView tvDispositivo = new TextView(this);
        StringBuilder articulos = new StringBuilder();
        if (f.items != null && !f.items.isEmpty()) {
            for (ec.edu.monster.climov.model.FacturaModel.FacturaDetalle d : f.items) {
                if (articulos.length() > 0) articulos.append("\n");
                articulos.append("📱 ").append(d.cantidad).append(" × ").append(d.modeloTelefono)
                        .append(String.format(Locale.US, "  ($%.2f)", d.subtotal));
            }
        } else {
            articulos.append("📱 ").append(f.modeloTelefono);
        }
        tvDispositivo.setText(articulos.toString());
        tvDispositivo.setTextSize(12f);
        tvDispositivo.setTextColor(getResources().getColor(R.color.primary));
        tvDispositivo.setPadding(0, 2, 0, 6);
        card.addView(tvDispositivo);

        // Fila Totales
        LinearLayout filaTotales = new LinearLayout(this);
        filaTotales.setOrientation(LinearLayout.HORIZONTAL);
        filaTotales.setPadding(0, 6, 0, 0);

        TextView tvPrecios = new TextView(this);
        tvPrecios.setText(String.format(Locale.US, "Subtotal: $%.2f | Desc: -$%.2f", f.subtotal > 0 ? f.subtotal : f.precioUnitario, f.valorDescuento));
        tvPrecios.setTextSize(11f);
        tvPrecios.setTextColor(getResources().getColor(R.color.gray));

        View space2 = new View(this);
        space2.setLayoutParams(spLp);

        TextView tvTotal = new TextView(this);
        tvTotal.setText(String.format(Locale.US, "Total: $%.2f", f.totalPagar));
        tvTotal.setTextSize(14f);
        tvTotal.setTypeface(null, Typeface.BOLD);
        tvTotal.setTextColor(getResources().getColor(R.color.success));

        filaTotales.addView(tvPrecios);
        filaTotales.addView(space2);
        filaTotales.addView(tvTotal);
        card.addView(filaTotales);

        // Si es crédito BanQuito, mostrar número de crédito
        if (f.codCreditoBanco != null && f.codCreditoBanco > 0) {
            TextView tvCred = new TextView(this);
            tvCred.setText("🏦 Crédito BanQuito Core: #" + f.codCreditoBanco);
            tvCred.setTextSize(10f);
            tvCred.setTypeface(null, Typeface.BOLD);
            tvCred.setTextColor(Color.parseColor("#B45309"));
            tvCred.setPadding(0, 4, 0, 0);
            card.addView(tvCred);
        }

        return card;
    }
}
