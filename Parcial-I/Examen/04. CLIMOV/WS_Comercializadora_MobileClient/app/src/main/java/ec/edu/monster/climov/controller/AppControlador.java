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

    public boolean autenticar(String usuario, String clave) {
        return service.autenticar(usuario, clave);
    }

    public List<TelefonoModel> listarTelefonos() {
        return service.listarTelefonos();
    }

    public boolean crearTelefono(TelefonoModel tel) {
        return service.crearTelefono(tel);
    }

    public boolean actualizarTelefono(TelefonoModel tel) {
        return service.actualizarTelefono(tel);
    }

    public boolean eliminarTelefono(int codTelefono) {
        return service.eliminarTelefono(codTelefono);
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
