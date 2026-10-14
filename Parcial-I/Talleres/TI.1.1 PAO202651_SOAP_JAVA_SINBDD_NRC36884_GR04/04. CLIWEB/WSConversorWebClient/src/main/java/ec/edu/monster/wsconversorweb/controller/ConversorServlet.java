package ec.edu.monster.wsconversorweb.controller;

import ec.edu.monster.wsconversorweb.model.HistorialItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ConversorServlet", urlPatterns = {"/conversor"})
public class ConversorServlet extends HttpServlet {

    private final AppControlador controlador = new AppControlador();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Si la petición solicita limpiar historial por GET
        String accion = request.getParameter("accion");
        if ("limpiar".equalsIgnoreCase(accion)) {
            session.removeAttribute("historial");
            response.sendRedirect(request.getContextPath() + "/conversor");
            return;
        }

        request.getRequestDispatcher("/conversor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if ("limpiar".equalsIgnoreCase(accion)) {
            session.removeAttribute("historial");
            response.sendRedirect(request.getContextPath() + "/conversor");
            return;
        }

        String magnitud = request.getParameter("magnitud");
        if (magnitud == null || magnitud.isBlank()) magnitud = "LONGITUD";

        String valorStr = request.getParameter("valor");
        String unidadInicial = request.getParameter("unidadInicial");
        String unidadFinal = request.getParameter("unidadFinal");

        if (valorStr == null || valorStr.isBlank() || unidadInicial == null || unidadFinal == null) {
            request.setAttribute("mensajeError", "Por favor ingrese todos los datos para la conversión.");
            request.setAttribute("magnitud", magnitud);
            request.getRequestDispatcher("/conversor.jsp").forward(request, response);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr.trim());
            double resultado;

            switch (magnitud.toUpperCase()) {
                case "MASA":
                    resultado = controlador.convertirMasa(valor, unidadInicial, unidadFinal);
                    break;
                case "TEMPERATURA":
                    resultado = controlador.convertirTemperatura(valor, unidadInicial, unidadFinal);
                    break;
                case "LONGITUD":
                default:
                    resultado = controlador.convertirLongitud(valor, unidadInicial, unidadFinal);
                    break;
            }

            String resultadoFormateado = String.format("%.4f", resultado);
            String formula = valor + " " + unidadInicial + " = " + resultadoFormateado + " " + unidadFinal;

            // Guardar en el historial de sesión
            @SuppressWarnings("unchecked")
            List<HistorialItem> historial = (List<HistorialItem>) session.getAttribute("historial");
            if (historial == null) {
                historial = new ArrayList<>();
            }
            historial.add(0, new HistorialItem(magnitud.toUpperCase(), valor, unidadInicial, resultado, unidadFinal));
            if (historial.size() > 20) {
                historial.remove(historial.size() - 1);
            }
            session.setAttribute("historial", historial);

            // Parámetros para la vista JSP
            request.setAttribute("magnitud", magnitud.toUpperCase());
            request.setAttribute("valor", valor);
            request.setAttribute("unidadInicial", unidadInicial);
            request.setAttribute("unidadFinal", unidadFinal);
            request.setAttribute("resultado", resultado);
            request.setAttribute("resultadoFormateado", resultadoFormateado);
            request.setAttribute("formula", formula);

        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "El valor numérico ingresado no es válido.");
            request.setAttribute("magnitud", magnitud);
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al conectar con el servidor SOAP: " + e.getMessage());
            request.setAttribute("magnitud", magnitud);
        }

        request.getRequestDispatcher("/conversor.jsp").forward(request, response);
    }
}
