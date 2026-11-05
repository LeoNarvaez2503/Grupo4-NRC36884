package ec.edu.monster.cliweb.servlet;

import com.google.gson.Gson;
import ec.edu.monster.dao.CatalogoDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.dao.UsuarioDAO;
import ec.edu.monster.dao.UsuarioDAO.UsuarioDTO;
import ec.edu.monster.modelo.BanQuitoDTOs.*;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.TelefonoCelular;
import ec.edu.monster.servicio.BanQuitoRestClient;
import ec.edu.monster.servicio.CarritoParser;
import ec.edu.monster.servicio.FacturacionService;
import ec.edu.monster.servicio.FacturacionService.CarritoResuelto;
import ec.edu.monster.servicio.FacturacionService.ItemCarrito;
import ec.edu.monster.servicio.FacturacionService.ResultadoFactura;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet API RESTful para la Comercializadora en Payara Server.
 * Permite que los Clientes de Consola, Escritorio y Móvil consuman la API en:
 * http://localhost:8080/WS_Comercializadora_WebClient/api/*
 */
@WebServlet(name = "ComercializadoraServlet", urlPatterns = {"/api/*"})
public class ComercializadoraServlet extends HttpServlet {

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final FacturacionService facturacionService = new FacturacionService();
    private final BanQuitoRestClient banquitoClient = new BanQuitoRestClient();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarCORS(resp);
        String path = req.getPathInfo();
        if (path == null) path = "";

        resp.setContentType("application/json; charset=UTF-8");

        try {
            if (path.equals("/telefonos")) {
                List<TelefonoCelular> lista = catalogoDAO.listar(false);
                resp.getWriter().write(gson.toJson(lista));
                return;
            }

            if (path.equals("/facturas")) {
                List<Factura> list = facturacionService.listarFacturas();
                resp.getWriter().write(gson.toJson(list));
                return;
            }

            if (path.equals("/auth/login") || path.equals("/login")) {
                String u = req.getParameter("usuario");
                String c = req.getParameter("clave");
                UsuarioDTO res = usuarioDAO.autenticar(u, c);
                resp.setStatus(res.isAutenticado() ? 200 : 401);
                resp.getWriter().write(gson.toJson(res));
                return;
            }

            if (path.equals("/verificar-credito")) {
                String cedula = req.getParameter("cedula");
                int codTelefono = parseInt(req.getParameter("codTelefono"), 0);
                int cuotas = parseInt(req.getParameter("cuotas"), 6);

                // Carrito: ?items=1:2,5:1 (codTelefono:cantidad). Sin items se usa codTelefono (1 unidad).
                List<ItemCarrito> carrito = CarritoParser.desdeQuery(req.getParameter("items"), codTelefono);
                CarritoResuelto cr = facturacionService.resolverCarrito(carrito);
                double precio = cr.ok() ? cr.subtotal() : 0.0;

                SujetoCreditoResponse sujeto = banquitoClient.validarSujetoCredito(cedula != null ? cedula.trim() : "");
                MontoMaximoResponse capacidad = null;
                boolean capacidadSuficiente = false;
                List<CuotaAmortizacion> amort = List.of();

                if (sujeto != null && sujeto.codCliente > 0) {
                    capacidad = banquitoClient.obtenerMontoMaximo(sujeto.codCliente);
                    if (capacidad != null) {
                        capacidadSuficiente = (precio <= capacidad.montoMaximo);
                        if (sujeto.esSujeto && capacidadSuficiente && cuotas >= 3 && cuotas <= 18) {
                            amort = banquitoClient.simularAmortizacion(precio, cuotas);
                        }
                    }
                }

                Map<String, Object> r = new HashMap<>();
                r.put("exito", true);
                r.put("sujeto", sujeto);
                r.put("capacidad", capacidad);
                r.put("precioTelefono", precio);
                r.put("totalCarrito", precio);
                r.put("totalArticulos", cr.totalArticulos());
                if (!cr.ok()) r.put("mensajeCarrito", cr.error());
                r.put("capacidadSuficiente", capacidadSuficiente);
                r.put("tablaAmortizacion", amort);

                resp.getWriter().write(gson.toJson(r));
                return;
            }

            if (path.equals("/amortizacion")) {
                int codCredito = parseInt(req.getParameter("codCredito"), 0);
                String cedula = req.getParameter("cedula");

                if (codCredito > 0) {
                    List<CuotaAmortizacion> tabla = banquitoClient.obtenerTablaAmortizacion(codCredito);
                    resp.getWriter().write(gson.toJson(tabla));
                    return;
                } else if (cedula != null && !cedula.isBlank()) {
                    SujetoCreditoResponse sujeto = banquitoClient.validarSujetoCredito(cedula.trim());
                    if (sujeto != null && sujeto.codCliente > 0) {
                        Factura fac = null;
                        for (Factura f : facturaDAO.listar()) {
                            if (cedula.trim().equals(f.getCedulaCliente()) && f.getCodCreditoBanco() != null) {
                                fac = f;
                                break;
                            }
                        }
                        int credId = fac != null && fac.getCodCreditoBanco() != null ? fac.getCodCreditoBanco() : 1;
                        List<CuotaAmortizacion> tabla = banquitoClient.obtenerTablaAmortizacion(credId);
                        resp.getWriter().write(gson.toJson(Map.of("cliente", sujeto, "creditoId", credId, "tabla", tabla)));
                        return;
                    }
                }
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta no encontrada: " + path + "\"}");

        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarCORS(resp);
        String path = req.getPathInfo();
        if (path == null) path = "";

        resp.setContentType("application/json; charset=UTF-8");

        try {
            if (path.equals("/auth/login") || path.equals("/login")) {
                InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                Map<String, String> creds = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, String>>(){}.getType());
                String u = creds != null && creds.get("usuario") != null ? creds.get("usuario") : "";
                String c = creds != null && creds.get("clave") != null ? creds.get("clave") : "";
                UsuarioDTO res = usuarioDAO.autenticar(u, c);
                resp.setStatus(res.isAutenticado() ? 200 : 401);
                resp.getWriter().write(gson.toJson(res));
                return;
            }

            if (path.equals("/facturar")) {
                InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                Map<String, Object> body = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, Object>>(){}.getType());
                String cedula = body.get("cedula") != null ? body.get("cedula").toString() : "";
                String nombre = body.get("nombre") != null ? body.get("nombre").toString() : "";
                List<ItemCarrito> carrito = CarritoParser.desdeBody(body);
                String formaPago = body.get("formaPago") != null ? body.get("formaPago").toString() : "EFECTIVO";
                int cuotas = body.get("cuotas") instanceof Number n ? n.intValue() : 6;

                ResultadoFactura res;
                if ("EFECTIVO".equalsIgnoreCase(formaPago)) {
                    res = facturacionService.facturarEfectivo(cedula, nombre, carrito);
                } else {
                    res = facturacionService.facturarCreditoDirecto(cedula, nombre, carrito, cuotas);
                }

                resp.setStatus(res.exito() ? 200 : 400);
                resp.getWriter().write(gson.toJson(res));
                return;
            }

            if (path.equals("/telefonos")) {
                InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                TelefonoCelular tel = gson.fromJson(reader, TelefonoCelular.class);
                int id = catalogoDAO.insertar(tel);
                tel.setCodTelefono(id);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(tel));
                return;
            }

            if (path.equals("/telefonos/actualizar") || path.startsWith("/telefonos/actualizar")) {
                InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                TelefonoCelular tel = gson.fromJson(reader, TelefonoCelular.class);
                boolean ok = catalogoDAO.actualizar(tel);
                resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(Map.of("exito", ok, "mensaje", ok ? "Teléfono actualizado exitosamente" : "No se pudo actualizar")));
                return;
            }

            if (path.equals("/telefonos/eliminar") || path.startsWith("/telefonos/eliminar")) {
                int id = 0;
                String idParam = req.getParameter("id");
                if (idParam != null && !idParam.isBlank()) {
                    id = parseInt(idParam, 0);
                } else {
                    InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                    Map<String, Object> body = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, Object>>(){}.getType());
                    if (body != null && body.get("codTelefono") instanceof Number n) {
                        id = n.intValue();
                    }
                }
                boolean ok = catalogoDAO.eliminar(id);
                resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(Map.of("exito", ok, "mensaje", ok ? "Teléfono eliminado/desactivado" : "No se pudo eliminar")));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta no encontrada: " + path + "\"}");

        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarCORS(resp);
        resp.setContentType("application/json; charset=UTF-8");
        try {
            InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
            TelefonoCelular tel = gson.fromJson(reader, TelefonoCelular.class);
            boolean ok = catalogoDAO.actualizar(tel);
            resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("exito", ok, "mensaje", ok ? "Teléfono actualizado exitosamente" : "No se pudo actualizar")));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarCORS(resp);
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String path = req.getPathInfo();
            int id = 0;
            if (path != null && path.length() > 1) {
                String sub = path.substring(path.lastIndexOf('/') + 1);
                id = parseInt(sub, 0);
            }
            if (id == 0) id = parseInt(req.getParameter("id"), 0);

            boolean ok = catalogoDAO.eliminar(id);
            resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("exito", ok, "mensaje", ok ? "Teléfono eliminado" : "No se pudo eliminar")));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private int parseInt(String val, int def) {
        if (val == null || val.isBlank()) return def;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
