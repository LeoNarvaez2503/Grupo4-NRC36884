package ec.edu.monster.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;

@WebServiceClient(name = "WSConversorUnidades", targetNamespace = "http://ws.controladores.monster.edu.ec/", wsdlLocation = "http://localhost:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl")
public class WSConversorUnidades_Service extends Service {

    private static final URL WSCONVERSORUNIDADES_WSDL_LOCATION;
    private static final WebServiceException WSCONVERSORUNIDADES_EXCEPTION;
    private static final QName WSCONVERSORUNIDADES_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "WSConversorUnidades");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WSCONVERSORUNIDADES_WSDL_LOCATION = url;
        WSCONVERSORUNIDADES_EXCEPTION = e;
    }

    public WSConversorUnidades_Service() {
        super(__getWsdlLocation(), WSCONVERSORUNIDADES_QNAME);
    }

    public WSConversorUnidades_Service(URL wsdlLocation) {
        super(wsdlLocation, WSCONVERSORUNIDADES_QNAME);
    }

    public WSConversorUnidades_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @WebEndpoint(name = "WSConversorUnidadesPort")
    public WSConversorUnidades getWSConversorUnidadesPort() {
        return super.getPort(new QName("http://ws.controladores.monster.edu.ec/", "WSConversorUnidadesPort"), WSConversorUnidades.class);
    }

    private static URL __getWsdlLocation() {
        if (WSCONVERSORUNIDADES_EXCEPTION != null) {
            throw WSCONVERSORUNIDADES_EXCEPTION;
        }
        return WSCONVERSORUNIDADES_WSDL_LOCATION;
    }
}
