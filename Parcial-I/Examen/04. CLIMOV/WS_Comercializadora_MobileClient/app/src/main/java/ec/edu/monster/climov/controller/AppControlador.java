package ec.edu.monster.climov.controller;

import ec.edu.monster.climov.model.ComercializadoraService;
import ec.edu.monster.climov.model.FacturaModel.*;
import ec.edu.monster.climov.model.TelefonoModel;
import java.util.List;

public class AppControlador {

    private static AppControlador instancia;
    private final ComercializadoraService service;

    private AppControlador() {
        this.service = new ComercializadoraService();
    }

    public static synchronized AppControlador getInstancia() {
        if (instancia == null) {
            instancia = new AppControlador();
        }
        return instancia;
    }

    public List<TelefonoModel> listarTelefonos() {
        return service.listarTelefonos();
    }

    public SujetoCreditoResponse verificarSujeto(String cedula) {
        return service.verificarSujetoCredito(cedula);
    }

    public MontoMaximoResponse consultarMontoMaximo(int codCliente) {
        return service.obtenerMontoMaximo(codCliente);
    }

    public FacturaResponse facturar(FacturaRequest req) {
        return service.emitirFactura(req);
    }

    public List<CuotaAmortizacion> consultarTablaAmortizacion(int codCredito) {
        return service.consultarTablaAmortizacion(codCredito);
    }
}
