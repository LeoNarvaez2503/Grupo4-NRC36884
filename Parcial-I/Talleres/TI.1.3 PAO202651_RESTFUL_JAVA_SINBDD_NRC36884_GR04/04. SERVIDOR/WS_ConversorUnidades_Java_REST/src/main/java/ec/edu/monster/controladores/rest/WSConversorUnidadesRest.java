package ec.edu.monster.controladores.rest;

import ec.edu.monster.modelos.dto.ConversionResponse;
import ec.edu.monster.modelos.dto.LoginResponse;
import ec.edu.monster.modelos.dto.RespuestaGeneral;
import ec.edu.monster.modelos.utilidades.ConversorLongitud;
import ec.edu.monster.modelos.utilidades.ConversorMasa;
import ec.edu.monster.modelos.utilidades.ConversorTemperatura;
import ec.edu.monster.modelos.utilidades.enums.UnidadLongitud;
import ec.edu.monster.modelos.utilidades.enums.UnidadMasa;
import ec.edu.monster.modelos.utilidades.enums.UnidadTemperatura;
import ec.edu.monster.modelos.utilidades.mapeadores.UnidadMapper;
import ec.edu.monster.seguridad.AdministradorCredenciales;
import ec.edu.monster.seguridad.AdministradorToken;
import ec.edu.monster.servicios.ServicioConversor;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint de compatibilidad directa con los métodos del Web Service SOAP original.
 * Permite invocar las 5 operaciones principales con la misma nomenclatura:
 * - login
 * - cambiarContrasenia
 * - convertirLongitud
 * - convertirMasa
 * - convertirTemperatura
 * 
 * @author MeatPuppets
 */
@Path("/WSConversorUnidades")
@Produces(MediaType.APPLICATION_JSON)
public class WSConversorUnidadesRest {

    private final ServicioConversor<UnidadLongitud> servicioLongitud =
            new ServicioConversor<>(new ConversorLongitud());
    private final ServicioConversor<UnidadMasa> servicioMasa =
            new ServicioConversor<>(new ConversorMasa());
    private final ServicioConversor<UnidadTemperatura> servicioTemperatura =
            new ServicioConversor<>(new ConversorTemperatura());

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response login(@QueryParam("usuario") String qUser,
                          @QueryParam("contrasenia") String qPass,
                          @FormParam("usuario") String fUser,
                          @FormParam("contrasenia") String fPass) {

        String u = (qUser != null) ? qUser : fUser;
        String p = (qPass != null) ? qPass : fPass;

        if (AdministradorCredenciales.validarUsuario(u, p)) {
            String token = AdministradorToken.generarToken();
            return Response.ok(new LoginResponse(true, token, u.toUpperCase(), "Login correcto")).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new LoginResponse(false, null, u, "Credenciales incorrectas")).build();
    }

    @POST
    @Path("/cambiarContrasenia")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response cambiarContrasenia(@QueryParam("contraseniaActual") String qActual,
                                       @QueryParam("contraseniaNueva") String qNueva,
                                       @FormParam("contraseniaActual") String fActual,
                                       @FormParam("contraseniaNueva") String fNueva) {

        String actual = (qActual != null) ? qActual : fActual;
        String nueva = (qNueva != null) ? qNueva : fNueva;

        try {
            AdministradorCredenciales.cambiarContrasenia(actual, nueva);
            return Response.ok(RespuestaGeneral.ok("Contrasenia actualizada correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error(e.getMessage())).build();
        }
    }

    @GET
    @Path("/convertirLongitud")
    public Response convertirLongitudGet(@QueryParam("valor") double valor,
                                        @QueryParam("unidadInicial") String unidadInicial,
                                        @QueryParam("unidadFinal") String unidadFinal) {
        return convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    @POST
    @Path("/convertirLongitud")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response convertirLongitudPost(@FormParam("valor") double valor,
                                         @FormParam("unidadInicial") String unidadInicial,
                                         @FormParam("unidadFinal") String unidadFinal) {
        return convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    private Response convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        try {
            UnidadLongitud origen = UnidadMapper.toLongitud(unidadInicial);
            UnidadLongitud destino = UnidadMapper.toLongitud(unidadFinal);
            double resultado = servicioLongitud.convertir(valor, origen, destino);
            return Response.ok(new ConversionResponse(true, "LONGITUD", valor, origen.name(), resultado, destino.name(), "OK")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(RespuestaGeneral.error(e.getMessage())).build();
        }
    }

    @GET
    @Path("/convertirMasa")
    public Response convertirMasaGet(@QueryParam("valor") double valor,
                                     @QueryParam("unidadInicial") String unidadInicial,
                                     @QueryParam("unidadFinal") String unidadFinal) {
        return convertirMasa(valor, unidadInicial, unidadFinal);
    }

    @POST
    @Path("/convertirMasa")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response convertirMasaPost(@FormParam("valor") double valor,
                                      @FormParam("unidadInicial") String unidadInicial,
                                      @FormParam("unidadFinal") String unidadFinal) {
        return convertirMasa(valor, unidadInicial, unidadFinal);
    }

    private Response convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        try {
            UnidadMasa origen = UnidadMapper.toMasa(unidadInicial);
            UnidadMasa destino = UnidadMapper.toMasa(unidadFinal);
            double resultado = servicioMasa.convertir(valor, origen, destino);
            return Response.ok(new ConversionResponse(true, "MASA", valor, origen.name(), resultado, destino.name(), "OK")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(RespuestaGeneral.error(e.getMessage())).build();
        }
    }

    @GET
    @Path("/convertirTemperatura")
    public Response convertirTemperaturaGet(@QueryParam("valor") double valor,
                                            @QueryParam("unidadInicial") String unidadInicial,
                                            @QueryParam("unidadFinal") String unidadFinal) {
        return convertirTemperatura(valor, unidadInicial, unidadFinal);
    }

    @POST
    @Path("/convertirTemperatura")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response convertirTemperaturaPost(@FormParam("valor") double valor,
                                             @FormParam("unidadInicial") String unidadInicial,
                                             @FormParam("unidadFinal") String unidadFinal) {
        return convertirTemperatura(valor, unidadInicial, unidadFinal);
    }

    private Response convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        try {
            UnidadTemperatura origen = UnidadMapper.toTemperatura(unidadInicial);
            UnidadTemperatura destino = UnidadMapper.toTemperatura(unidadFinal);
            double resultado = servicioTemperatura.convertir(valor, origen, destino);
            return Response.ok(new ConversionResponse(true, "TEMPERATURA", valor, origen.name(), resultado, destino.name(), "OK")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(RespuestaGeneral.error(e.getMessage())).build();
        }
    }
}
