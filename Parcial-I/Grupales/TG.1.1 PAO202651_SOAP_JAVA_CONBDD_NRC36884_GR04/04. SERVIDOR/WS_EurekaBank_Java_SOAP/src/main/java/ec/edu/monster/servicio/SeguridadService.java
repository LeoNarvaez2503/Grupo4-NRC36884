package ec.edu.monster.servicio;

import ec.edu.monster.dao.EmpleadoDAO;
import ec.edu.monster.modelo.AuthRespuestaDTO;

/**
 * Servicio de Seguridad y Autenticación Centralizada (RQF-001).
 */
public class SeguridadService {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public AuthRespuestaDTO autenticar(String usuario, String clave) {
        return empleadoDAO.autenticar(usuario, clave);
    }

    public String obtenerUsuarioPorId(int idEmpleado) {
        return empleadoDAO.obtenerNombreUsuarioPorId(idEmpleado);
    }
}
