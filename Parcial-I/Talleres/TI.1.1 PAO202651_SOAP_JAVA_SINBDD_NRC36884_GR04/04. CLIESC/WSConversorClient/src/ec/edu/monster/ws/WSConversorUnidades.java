package ec.edu.monster.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService(name = "WSConversorUnidades", targetNamespace = "http://ws.controladores.monster.edu.ec/")
public interface WSConversorUnidades {

    @WebMethod(operationName = "login")
    @WebResult(targetNamespace = "")
    String login(
        @WebParam(name = "usuario", targetNamespace = "") String usuario,
        @WebParam(name = "contrasenia", targetNamespace = "") String contrasenia);

    @WebMethod(operationName = "convertirLongitud")
    @WebResult(targetNamespace = "")
    double convertirLongitud(
        @WebParam(name = "valor", targetNamespace = "") double valor,
        @WebParam(name = "unidadInicial", targetNamespace = "") String unidadInicial,
        @WebParam(name = "unidadFinal", targetNamespace = "") String unidadFinal);

    @WebMethod(operationName = "convertirMasa")
    @WebResult(targetNamespace = "")
    double convertirMasa(
        @WebParam(name = "valor", targetNamespace = "") double valor,
        @WebParam(name = "unidadInicial", targetNamespace = "") String unidadInicial,
        @WebParam(name = "unidadFinal", targetNamespace = "") String unidadFinal);

    @WebMethod(operationName = "convertirTemperatura")
    @WebResult(targetNamespace = "")
    double convertirTemperatura(
        @WebParam(name = "valor", targetNamespace = "") double valor,
        @WebParam(name = "unidadInicial", targetNamespace = "") String unidadInicial,
        @WebParam(name = "unidadFinal", targetNamespace = "") String unidadFinal);

    @WebMethod(operationName = "cambiarContrasenia")
    @WebResult(targetNamespace = "")
    String cambiarContrasenia(
        @WebParam(name = "contraseniaActual", targetNamespace = "") String contraseniaActual,
        @WebParam(name = "contraseniaNueva", targetNamespace = "") String contraseniaNueva);
}
