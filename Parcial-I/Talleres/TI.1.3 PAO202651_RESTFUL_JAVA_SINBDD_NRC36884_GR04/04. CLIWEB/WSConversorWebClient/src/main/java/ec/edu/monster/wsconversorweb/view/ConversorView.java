package ec.edu.monster.wsconversorweb.view;

import ec.edu.monster.wsconversorweb.controller.AppControlador;
import ec.edu.monster.wsconversorweb.model.HistorialItem;
import ec.edu.monster.wsconversorweb.model.UnidadLongitud;
import ec.edu.monster.wsconversorweb.model.UnidadMasa;
import ec.edu.monster.wsconversorweb.model.UnidadTemperatura;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vista / Bean del Conversor de Unidades para Cliente Web (Patrón MVC).
 */
@Named("conversorView")
@SessionScoped
public class ConversorView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginView loginView;

    private String tipoMagnitud = "LONGITUD";
    private Double valor;
    private String unidadOrigen;
    private String unidadDestino;
    private Double resultado;
    private final List<HistorialItem> historial = new ArrayList<>();
    private final AppControlador controlador = new AppControlador();

    @PostConstruct
    public void init() {
        actualizarUnidades();
    }

    public void verificarSesion() {
        if (loginView == null || !loginView.isAutenticado()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (Exception ignored) {
            }
        }
    }

    public void cambiarMagnitud() {
        actualizarUnidades();
        resultado = null;
    }

    private void actualizarUnidades() {
        List<String> disponibles = getUnidadesDisponibles();
        if (!disponibles.isEmpty()) {
            unidadOrigen = disponibles.get(0);
            unidadDestino = disponibles.size() > 1 ? disponibles.get(1) : disponibles.get(0);
        }
    }

    public List<String> getUnidadesDisponibles() {
        switch (tipoMagnitud) {
            case "MASA":
                return Arrays.stream(UnidadMasa.values()).map(Enum::name).toList();
            case "TEMPERATURA":
                return Arrays.stream(UnidadTemperatura.values()).map(Enum::name).toList();
            case "LONGITUD":
            default:
                return Arrays.stream(UnidadLongitud.values()).map(Enum::name).toList();
        }
    }

    public String getTitulo() {
        switch (tipoMagnitud) {
            case "MASA":
                return "Conversor de Masa";
            case "TEMPERATURA":
                return "Conversor de Temperatura";
            case "LONGITUD":
            default:
                return "Conversor de Longitudes";
        }
    }

    public void convertir() {
        if (loginView == null || !loginView.isAutenticado()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe iniciar sesión primero.", null));
            return;
        }

        if (valor == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor ingrese un valor numérico.", null));
            return;
        }

        try {
            double res;
            switch (tipoMagnitud) {
                case "MASA":
                    res = controlador.convertirMasa(valor, unidadOrigen, unidadDestino);
                    break;
                case "TEMPERATURA":
                    res = controlador.convertirTemperatura(valor, unidadOrigen, unidadDestino);
                    break;
                case "LONGITUD":
                default:
                    res = controlador.convertirLongitud(valor, unidadOrigen, unidadDestino);
                    break;
            }
            resultado = res;
            historial.add(0, new HistorialItem(tipoMagnitud, valor, unidadOrigen, res, unidadDestino));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en conversión: " + e.getMessage(), null));
        }
    }

    public void limpiar() {
        valor = null;
        resultado = null;
        actualizarUnidades();
    }

    public String logout() {
        return loginView.logout();
    }

    public String getTipoMagnitud() {
        return tipoMagnitud;
    }

    public void setTipoMagnitud(String tipoMagnitud) {
        this.tipoMagnitud = tipoMagnitud;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidadOrigen() {
        return unidadOrigen;
    }

    public void setUnidadOrigen(String unidadOrigen) {
        this.unidadOrigen = unidadOrigen;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public void setUnidadDestino(String unidadDestino) {
        this.unidadDestino = unidadDestino;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    public List<HistorialItem> getHistorial() {
        return historial;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
}
