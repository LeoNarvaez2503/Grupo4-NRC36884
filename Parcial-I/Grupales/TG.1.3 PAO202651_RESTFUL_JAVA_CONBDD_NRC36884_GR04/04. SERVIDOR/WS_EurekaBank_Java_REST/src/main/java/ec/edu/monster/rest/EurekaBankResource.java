package ec.edu.monster.rest;

import ec.edu.monster.controlador.OperacionesController;
import ec.edu.monster.modelo.AuthRespuestaDTO;
import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.modelo.MovimientoDTO;
import ec.edu.monster.modelo.OperacionRespuestaDTO;
import ec.edu.monster.modelo.SucursalDTO;
import ec.edu.monster.modelo.peticion.LoginRequest;
import ec.edu.monster.modelo.peticion.OperacionRequest;
import ec.edu.monster.modelo.peticion.TransferenciaRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso RESTful Principal de EurekaBank - Grupo 04 (GR04).
 * Dominio: ec.edu.monster | Base URI: /WS_EurekaBank_Java_REST/api
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EurekaBankResource {

    private final OperacionesController controlador = new OperacionesController();

    @POST
    @Path("/auth/login")
    public Response autenticarUsuario(LoginRequest req) {
        if (req == null) {
            AuthRespuestaDTO err = new AuthRespuestaDTO();
            err.setAutenticado(false);
            err.setMensaje("Solicitud de login vacía.");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }
        AuthRespuestaDTO resp = controlador.autenticarUsuario(req.getUsuario(), req.getClave());
        if (resp.isAutenticado()) {
            return Response.ok(resp).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
    }

    @GET
    @Path("/sucursales")
    public List<SucursalDTO> listarSucursales() {
        return controlador.listarSucursales();
    }

    @GET
    @Path("/cuentas")
    public List<CuentaDetalleDTO> listarCuentasPorSucursal(@QueryParam("sucursal") @DefaultValue("0") int sucursalId) {
        return controlador.listarCuentasPorSucursal(sucursalId);
    }

    @GET
    @Path("/cuentas/{numeroCuenta}")
    public Response obtenerCuentaPorNumero(@PathParam("numeroCuenta") String numeroCuenta) {
        CuentaDetalleDTO cuenta = controlador.obtenerCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "La cuenta " + numeroCuenta + " no existe."))
                    .build();
        }
        return Response.ok(cuenta).build();
    }

    @GET
    @Path("/clientes/{dni}/cuentas")
    public List<CuentaDetalleDTO> consultarCuentasPorCliente(@PathParam("dni") String dni) {
        return controlador.consultarCuentasPorCliente(dni);
    }

    @GET
    @Path("/cuentas/{numeroCuenta}/movimientos")
    public List<MovimientoDTO> consultarExtracto(@PathParam("numeroCuenta") String numeroCuenta) {
        return controlador.consultarExtracto(numeroCuenta);
    }

    @GET
    @Path("/mashup")
    public List<CuentaDetalleDTO> consultarEstadoMashUp() {
        return controlador.listarCuentasPorSucursal(0);
    }

    @POST
    @Path("/movimientos/deposito")
    public Response depositar(OperacionRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(OperacionRespuestaDTO.error("ERROR_VALIDACION", "Cuerpo JSON vacío.")).build();
        }
        OperacionRespuestaDTO res = controlador.depositar(
                req.getNumeroCuenta(), req.getMonto(), req.getEmpleadoId(), req.getVentanilla());
        return construirRespuestaOperacion(res);
    }

    @POST
    @Path("/movimientos/retiro")
    public Response retirar(OperacionRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(OperacionRespuestaDTO.error("ERROR_VALIDACION", "Cuerpo JSON vacío.")).build();
        }
        OperacionRespuestaDTO res = controlador.retirar(
                req.getNumeroCuenta(), req.getMonto(), req.getEmpleadoId(), req.getVentanilla());
        return construirRespuestaOperacion(res);
    }

    @POST
    @Path("/movimientos/transferencia")
    public Response transferir(TransferenciaRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(OperacionRespuestaDTO.error("ERROR_VALIDACION", "Cuerpo JSON vacío.")).build();
        }
        OperacionRespuestaDTO res = controlador.transferir(
                req.getCuentaOrigen(), req.getCuentaDestino(), req.getMonto(), req.getEmpleadoId(), req.getVentanilla());
        return construirRespuestaOperacion(res);
    }

    @POST
    @Path("/cuentas/{numeroCuenta}/desbloquear")
    public Response desbloquearCuenta(@PathParam("numeroCuenta") String numeroCuenta, OperacionRequest req) {
        String admin = (req != null && req.getUsuarioAdmin() != null) ? req.getUsuarioAdmin() : "Monster";
        OperacionRespuestaDTO res = controlador.desbloquearCuenta(numeroCuenta, admin);
        return construirRespuestaOperacion(res);
    }

    @POST
    @Path("/cuentas/{numeroCuenta}/bloquear")
    public Response bloquearCuentaManual(@PathParam("numeroCuenta") String numeroCuenta, OperacionRequest req) {
        int vent = req != null ? req.getVentanilla() : 1;
        String usr = (req != null && req.getUsuarioAdmin() != null) ? req.getUsuarioAdmin() : "Monster";
        OperacionRespuestaDTO res = controlador.bloquearCuentaManual(numeroCuenta, vent, usr);
        return construirRespuestaOperacion(res);
    }

    private Response construirRespuestaOperacion(OperacionRespuestaDTO res) {
        if (res.isExito()) {
            return Response.ok(res).build();
        }
        if ("CONCURRENCIA_BLOQUEADA".equals(res.getCodigo())) {
            return Response.status(Response.Status.CONFLICT).entity(res).build();
        }
        if ("CUENTA_NO_EXISTE".equals(res.getCodigo())) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}
