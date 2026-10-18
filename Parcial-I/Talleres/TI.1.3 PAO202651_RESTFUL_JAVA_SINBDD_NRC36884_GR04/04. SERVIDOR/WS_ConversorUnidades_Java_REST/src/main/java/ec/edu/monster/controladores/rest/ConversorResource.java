package ec.edu.monster.controladores.rest;

import ec.edu.monster.modelos.dto.ConversionRequest;
import ec.edu.monster.modelos.dto.ConversionResponse;
import ec.edu.monster.modelos.dto.RespuestaGeneral;
import ec.edu.monster.modelos.utilidades.ConversorLongitud;
import ec.edu.monster.modelos.utilidades.ConversorMasa;
import ec.edu.monster.modelos.utilidades.ConversorTemperatura;
import ec.edu.monster.modelos.utilidades.enums.UnidadLongitud;
import ec.edu.monster.modelos.utilidades.enums.UnidadMasa;
import ec.edu.monster.modelos.utilidades.enums.UnidadTemperatura;
import ec.edu.monster.modelos.utilidades.mapeadores.UnidadMapper;
import ec.edu.monster.servicios.ServicioConversor;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Map;

/**
 * Endpoints RESTful para la conversión de unidades (Longitud, Masa y Temperatura).
 * Soporta solicitudes POST con cuerpo JSON y solicitudes GET con Query Parameters.
 * 
 * @author MeatPuppets
 */
@Path("/conversor")
@Produces(MediaType.APPLICATION_JSON)
public class ConversorResource {

    private final ServicioConversor<UnidadLongitud> servicioLongitud =
            new ServicioConversor<>(new ConversorLongitud());
    private final ServicioConversor<UnidadMasa> servicioMasa =
            new ServicioConversor<>(new ConversorMasa());
    private final ServicioConversor<UnidadTemperatura> servicioTemperatura =
            new ServicioConversor<>(new ConversorTemperatura());

    // =========================================================================
    // CONVERSIÓN DE LONGITUD
    // =========================================================================

    @POST
    @Path("/longitud")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response convertirLongitudPost(ConversionRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error("Cuerpo de solicitud requerido"))
                    .build();
        }
        return ejecutarConversionLongitud(request.getValor(), request.getUnidadInicial(), request.getUnidadFinal());
    }

    @GET
    @Path("/longitud")
    public Response convertirLongitudGet(@QueryParam("valor") double valor,
                                        @QueryParam("unidadInicial") String unidadInicial,
                                        @QueryParam("unidadFinal") String unidadFinal) {
        return ejecutarConversionLongitud(valor, unidadInicial, unidadFinal);
    }

    private Response ejecutarConversionLongitud(double valor, String inicial, String finalU) {
        try {
            UnidadLongitud origen = UnidadMapper.toLongitud(inicial);
            UnidadLongitud destino = UnidadMapper.toLongitud(finalU);
            double resultado = servicioLongitud.convertir(valor, origen, destino);

            ConversionResponse resp = new ConversionResponse(
                    true, "LONGITUD", valor, origen.name(), resultado, destino.name(), "Conversión exitosa"
            );
            return Response.ok(resp).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(RespuestaGeneral.error("Error interno en la conversión: " + e.getMessage()))
                    .build();
        }
    }

    // =========================================================================
    // CONVERSIÓN DE MASA
    // =========================================================================

    @POST
    @Path("/masa")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response convertirMasaPost(ConversionRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error("Cuerpo de solicitud requerido"))
                    .build();
        }
        return ejecutarConversionMasa(request.getValor(), request.getUnidadInicial(), request.getUnidadFinal());
    }

    @GET
    @Path("/masa")
    public Response convertirMasaGet(@QueryParam("valor") double valor,
                                    @QueryParam("unidadInicial") String unidadInicial,
                                    @QueryParam("unidadFinal") String unidadFinal) {
        return ejecutarConversionMasa(valor, unidadInicial, unidadFinal);
    }

    private Response ejecutarConversionMasa(double valor, String inicial, String finalU) {
        try {
            UnidadMasa origen = UnidadMapper.toMasa(inicial);
            UnidadMasa destino = UnidadMapper.toMasa(finalU);
            double resultado = servicioMasa.convertir(valor, origen, destino);

            ConversionResponse resp = new ConversionResponse(
                    true, "MASA", valor, origen.name(), resultado, destino.name(), "Conversión exitosa"
            );
            return Response.ok(resp).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(RespuestaGeneral.error("Error interno en la conversión: " + e.getMessage()))
                    .build();
        }
    }

    // =========================================================================
    // CONVERSIÓN DE TEMPERATURA
    // =========================================================================

    @POST
    @Path("/temperatura")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response convertirTemperaturaPost(ConversionRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error("Cuerpo de solicitud requerido"))
                    .build();
        }
        return ejecutarConversionTemperatura(request.getValor(), request.getUnidadInicial(), request.getUnidadFinal());
    }

    @GET
    @Path("/temperatura")
    public Response convertirTemperaturaGet(@QueryParam("valor") double valor,
                                           @QueryParam("unidadInicial") String unidadInicial,
                                           @QueryParam("unidadFinal") String unidadFinal) {
        return ejecutarConversionTemperatura(valor, unidadInicial, unidadFinal);
    }

    private Response ejecutarConversionTemperatura(double valor, String inicial, String finalU) {
        try {
            UnidadTemperatura origen = UnidadMapper.toTemperatura(inicial);
            UnidadTemperatura destino = UnidadMapper.toTemperatura(finalU);
            double resultado = servicioTemperatura.convertir(valor, origen, destino);

            ConversionResponse resp = new ConversionResponse(
                    true, "TEMPERATURA", valor, origen.name(), resultado, destino.name(), "Conversión exitosa"
            );
            return Response.ok(resp).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaGeneral.error(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(RespuestaGeneral.error("Error interno en la conversión: " + e.getMessage()))
                    .build();
        }
    }

    // =========================================================================
    // CATÁLOGO DE UNIDADES DISPONIBLES
    // =========================================================================

    @GET
    @Path("/unidades")
    public Response catalogoUnidades() {
        Map<String, Object> catalogo = Map.of(
                "longitud", Arrays.stream(UnidadLongitud.values()).map(Enum::name).toArray(),
                "masa", Arrays.stream(UnidadMasa.values()).map(Enum::name).toArray(),
                "temperatura", Arrays.stream(UnidadTemperatura.values()).map(Enum::name).toArray()
        );
        return Response.ok(RespuestaGeneral.ok("Catálogo de unidades disponibles", catalogo)).build();
    }
}
