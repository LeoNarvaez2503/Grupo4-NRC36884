package ec.edu.monster.controladores.rest;

import ec.edu.monster.modelos.dto.CambioContraseniaRequest;
import ec.edu.monster.modelos.dto.LoginRequest;
import ec.edu.monster.modelos.dto.LoginResponse;
import ec.edu.monster.modelos.dto.RespuestaGeneral;
import ec.edu.monster.seguridad.AdministradorCredenciales;
import ec.edu.monster.seguridad.AdministradorToken;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoints REST para autenticación, gestión de tokens y cambio de contraseñas.
 * 
 * @author MeatPuppets
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacionResource {

    /**
     * Inicio de sesión mediante cuerpo JSON.
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginJson(LoginRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LoginResponse(false, null, null, "Datos de inicio de sesión no proporcionados"))
                    .build();
        }
        return procesarLogin(request.getUsuario(), request.getContrasenia());
    }

    /**
     * Inicio de sesión mediante Formulario o Query Params.
     * POST /api/auth/login-form
     */
    @POST
    @Path("/login-form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginForm(@FormParam("usuario") String usuario,
                              @FormParam("contrasenia") String contrasenia) {
        return procesarLogin(usuario, contrasenia);
    }

    private Response procesarLogin(String usuario, String contrasenia) {
        if (AdministradorCredenciales.validarUsuario(usuario, contrasenia)) {
            String token = AdministradorToken.generarToken();
            LoginResponse response = new LoginResponse(
                    true, token, usuario.trim().toUpperCase(), "Autenticación exitosa"
            );
            return Response.ok(response).build();
        }

        LoginResponse response = new LoginResponse(
                false, null, usuario, "Usuario o contraseña incorrectos"
        );
        return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
    }

    /**
     * Cambio de contraseña para el usuario autenticado.
     * POST /api/auth/cambiar-contrasenia
     */
    @POST
    @Path("/cambiar-contrasenia")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarContrasenia(CambioContraseniaRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error("Datos requeridos no proporcionados"))
                    .build();
        }

        try {
            AdministradorCredenciales.cambiarContrasenia(
                    request.getContraseniaActual(), request.getContraseniaNueva()
            );
            return Response.ok(RespuestaGeneral.ok("Contraseña actualizada correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error(e.getMessage()))
                    .build();
        }
    }

    /**
     * Valida si un token sigue vigente.
     * GET /api/auth/validar-token
     */
    @GET
    @Path("/validar-token")
    public Response validarToken(@HeaderParam("Authorization") String authHeader,
                                 @QueryParam("token") String tokenParam) {
        String token = extraerToken(authHeader, tokenParam);
        if (token != null && AdministradorToken.validarToken(token)) {
            return Response.ok(RespuestaGeneral.ok("Token vigente y válido")).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(RespuestaGeneral.error("Token inválido o expirado"))
                .build();
    }

    /**
     * Cierra sesión invalidando el token.
     * POST /api/auth/logout
     */
    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader,
                           @QueryParam("token") String tokenParam) {
        String token = extraerToken(authHeader, tokenParam);
        if (token != null) {
            AdministradorToken.invalidarToken(token);
        }
        return Response.ok(RespuestaGeneral.ok("Sesión cerrada correctamente")).build();
    }

    private String extraerToken(String authHeader, String tokenParam) {
        if (tokenParam != null && !tokenParam.trim().isEmpty()) {
            return tokenParam.trim();
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        return authHeader;
    }
}
