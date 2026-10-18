package ec.edu.monster.modelos.dto;

import java.io.Serializable;

public class RespuestaGeneral implements Serializable {

    private boolean exito;
    private String mensaje;
    private Object data;

    public RespuestaGeneral() {
    }

    public RespuestaGeneral(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public RespuestaGeneral(boolean exito, String mensaje, Object data) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.data = data;
    }

    public static RespuestaGeneral ok(String mensaje) {
        return new RespuestaGeneral(true, mensaje);
    }

    public static RespuestaGeneral ok(String mensaje, Object data) {
        return new RespuestaGeneral(true, mensaje, data);
    }

    public static RespuestaGeneral error(String mensaje) {
        return new RespuestaGeneral(false, mensaje);
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
