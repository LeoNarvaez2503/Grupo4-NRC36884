package ec.edu.monster.rest;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.MontoMaximoDTO;
import ec.edu.monster.modelo.OtorgarCreditoDTO;
import ec.edu.monster.modelo.SujetoCreditoDTO;
import ec.edu.monster.servicio.CreditoService;
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
 * Recurso RESTful de Crédito BanQuito - NRC 36884 Grupo 04.
 * Endpoints evaluados en la Rúbrica Oficial (Ítems 5, 6, 7, 10).
 */
@Path("/credito")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreditoResource {

    private final CreditoService service = new CreditoService();

    // =========================================================================
    // ÍTEM 5: Valida si una persona es sujeto de crédito en base a su cédula
    // =========================================================================
    @GET
    @Path("/es-sujeto/{cedula}")
    public Response validarSujetoCredito(@PathParam("cedula") String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El parámetro cédula es obligatorio.\"}")
                    .build();
        }
        SujetoCreditoDTO resultado = service.esSujetoCredito(cedula);
        return Response.ok(resultado).build();
    }

    // Alias compatible con proyecto de referencia
    @GET
    @Path("/verificar/{cedula}")
    public Response verificarSujetoRef(@PathParam("cedula") String cedula) {
        return validarSujetoCredito(cedula);
    }

    // =========================================================================
    // ÍTEM 6: Retorna el monto máximo de crédito para una persona por código
    // =========================================================================
    @GET
    @Path("/monto-maximo/{codCliente}")
    public Response obtenerMontoMaximo(@PathParam("codCliente") int codCliente) {
        if (codCliente <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El código de cliente debe ser mayor a 0.\"}")
                    .build();
        }
        MontoMaximoDTO resultado = service.calcularMontoMaximo(codCliente);
        return Response.ok(resultado).build();
    }

    // =========================================================================
    // ÍTEM 7: Otorga el crédito y crea la tabla de amortización
    // Parámetros: Código Cliente, Precio Celular, Número Cuotas
    // =========================================================================
    @POST
    @Path("/otorgar")
    public Response otorgarCredito(OtorgarCreditoDTO request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Cuerpo de petición JSON vacío.\"}")
                    .build();
        }
        OtorgarCreditoDTO respuesta = service.otorgarCredito(
                request.getCodCliente(),
                request.getPrecioTelefono(),
                request.getNumeroCuotas()
        );
        if (!respuesta.isExito()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
        }
        return Response.ok(respuesta).build();
    }

    // Alias compatible con proyecto de referencia
    @POST
    @Path("/crear-tabla-amortizacion")
    public Response crearTablaAmortizacionRef(OtorgarCreditoDTO request) {
        return otorgarCredito(request);
    }

    // =========================================================================
    // ÍTEM 10: Retorna la tabla de amortización para un crédito otorgado
    // =========================================================================
    @GET
    @Path("/tabla-amortizacion/{codCredito}")
    public Response obtenerTablaAmortizacion(@PathParam("codCredito") int codCredito) {
        if (codCredito <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El código de crédito debe ser mayor a 0.\"}")
                    .build();
        }
        List<CuotaAmortizacion> tabla = service.obtenerTablaAmortizacion(codCredito);
        if (tabla.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"No existe tabla de amortización para el crédito #" + codCredito + "\"}")
                    .build();
        }
        return Response.ok(tabla).build();
    }

    // Alias compatible con proyecto de referencia
    @GET
    @Path("/obtener-tabla-amortizacion/{codCredito}")
    public Response obtenerTablaRef(@PathParam("codCredito") int codCredito) {
        return obtenerTablaAmortizacion(codCredito);
    }

    @GET
    @Path("/tabla-amortizacion/cliente/{codCliente}")
    public Response obtenerTablaPorCliente(@PathParam("codCliente") int codCliente) {
        OtorgarCreditoDTO dto = service.obtenerCreditoActivoPorCliente(codCliente);
        if (!dto.isExito()) {
            return Response.status(Response.Status.NOT_FOUND).entity(dto).build();
        }
        return Response.ok(dto).build();
    }

    @GET
    @Path("/clientes")
    public Response listarClientes() {
        List<Cliente> clientes = service.listarClientes();
        return Response.ok(clientes).build();
    }

    @GET
    @Path("/simular")
    public Response simular(@QueryParam("monto") double monto, @QueryParam("cuotas") int cuotas) {
        if (monto <= 0 || cuotas < 3 || cuotas > 18) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Parámetros de simulación inválidos (monto > 0, cuotas entre 3 y 18).\"}")
                    .build();
        }
        List<CuotaAmortizacion> tabla = service.generarTablaAmortizacion(monto, cuotas, CreditoService.TASA_INTERES_ANUAL);
        return Response.ok(tabla).build();
    }
}
