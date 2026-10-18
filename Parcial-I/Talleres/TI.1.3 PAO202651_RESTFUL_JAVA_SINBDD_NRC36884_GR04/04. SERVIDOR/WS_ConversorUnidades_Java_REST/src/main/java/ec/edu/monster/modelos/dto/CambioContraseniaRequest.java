package ec.edu.monster.modelos.dto;

import java.io.Serializable;

public class CambioContraseniaRequest implements Serializable {

    private String contraseniaActual;
    private String contraseniaNueva;

    public CambioContraseniaRequest() {
    }

    public CambioContraseniaRequest(String contraseniaActual, String contraseniaNueva) {
        this.contraseniaActual = contraseniaActual;
        this.contraseniaNueva = contraseniaNueva;
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }

    public String getContraseniaNueva() {
        return contraseniaNueva;
    }

    public void setContraseniaNueva(String contraseniaNueva) {
        this.contraseniaNueva = contraseniaNueva;
    }
}
