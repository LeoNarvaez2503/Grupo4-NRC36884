package ec.edu.monster.wsconversorweb.controller;

import ec.edu.monster.wsconversorweb.model.UsuarioSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AppControlador controlador = new AppControlador();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/conversor");
            return;
        }

        String logout = request.getParameter("logout");
        if (logout != null) {
            request.setAttribute("mensajeInfo", "Sesión cerrada correctamente.");
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("contrasenia");

        String user = (usuario != null) ? usuario.trim() : "";
        String pass = (contrasenia != null) ? contrasenia : "";

        if (user.isEmpty() || pass.isEmpty()) {
            request.setAttribute("mensajeError", "Por favor complete todos los campos.");
            request.setAttribute("usuarioIngresado", user);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            String token = controlador.login(user, pass);
            if (token != null && !token.isBlank()) {
                HttpSession session = request.getSession(true);
                UsuarioSession sesionUsuario = new UsuarioSession(user.toUpperCase(), token);
                session.setAttribute("usuario", sesionUsuario);
                response.sendRedirect(request.getContextPath() + "/conversor");
            } else {
                request.setAttribute("mensajeError", "Credenciales incorrectas.");
                request.setAttribute("usuarioIngresado", user);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg == null || errorMsg.isBlank() || errorMsg.contains("Fault")) {
                errorMsg = "Credenciales incorrectas o error en el servicio SOAP.";
            }
            request.setAttribute("mensajeError", errorMsg);
            request.setAttribute("usuarioIngresado", user);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
