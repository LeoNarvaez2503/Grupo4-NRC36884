package ec.edu.monster.wsconversorweb.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/conversor", "/conversor.jsp"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        boolean logueado = (session != null && session.getAttribute("usuario") != null);

        if (!logueado) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}
