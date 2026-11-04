package ec.edu.monster.cliweb.controller;

import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.servicio.BanQuitoRestClient;
import ec.edu.monster.servicio.FacturacionService;
import java.io.Serializable;

/**
 * Capa Controlador (Patrón MVC): Coordina la vista JSF y la lógica de negocio.
 * NRC: 36884 - Grupo 04
 */
public class AppControlador implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final FacturacionService facturacionService = new FacturacionService();
    private final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();

    public CatalogoDAO getCatalogoDAO() {
        return catalogoDAO;
    }

    public FacturaDAO getFacturaDAO() {
        return facturaDAO;
    }

    public FacturacionService getFacturacionService() {
        return facturacionService;
    }

    public BanQuitoRestClient getBanquitoClient() {
        return banquitoClient;
    }
}
