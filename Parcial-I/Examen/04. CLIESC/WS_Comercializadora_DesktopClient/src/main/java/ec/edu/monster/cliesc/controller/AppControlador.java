package ec.edu.monster.cliesc.controller;

import ec.edu.monster.cliesc.model.ComercializadoraService;
import ec.edu.monster.cliesc.model.FacturaModel.*;
import ec.edu.monster.cliesc.model.TelefonoModel;
import java.util.List;

public class AppControlador {

    private final ComercializadoraService service;

    public AppControlador() {
        this.service = new ComercializadoraService();
    }

    public List<TelefonoModel> listarTelefonos() {
        return service.listarTelefonos();
    }

    public boolean agregarTelefono(TelefonoModel tel) {
        return service.crearTelefono(tel);
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
