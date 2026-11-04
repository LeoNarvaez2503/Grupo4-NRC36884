package ec.edu.monster.cliesc.controller;

import ec.edu.monster.cliesc.model.ComercializadoraService;
import ec.edu.monster.cliesc.model.ConfiguracionServidor;
import ec.edu.monster.cliesc.model.FacturaModel.*;
import ec.edu.monster.cliesc.model.TelefonoModel;
import java.util.List;

public class AppControlador {

    private final ComercializadoraService service;

    public AppControlador() {
        this.service = new ComercializadoraService();
    }

    public ConfiguracionServidor getConfiguracion() {
        return service.getConfig();
    }

    public boolean autenticar(String usuario, String clave) {
        return service.autenticar(usuario, clave);
    }

    public List<TelefonoModel> listarTelefonos() {
        return service.listarTelefonos();
    }

    public boolean agregarTelefono(TelefonoModel tel) {
        return service.crearTelefono(tel);
    }

    public boolean actualizarTelefono(TelefonoModel tel) {
        return service.actualizarTelefono(tel);
    }

    public boolean eliminarTelefono(int id) {
        return service.eliminarTelefono(id);
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

    public List<FacturaItem> listarFacturas() {
        return service.listarFacturas();
    }
}
