
package ec.edu.monster.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ec.edu.monster.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CambiarContrasenia_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "cambiarContrasenia");
    private final static QName _CambiarContraseniaResponse_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "cambiarContraseniaResponse");
    private final static QName _ConvertirLongitud_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirLongitud");
    private final static QName _ConvertirLongitudResponse_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirLongitudResponse");
    private final static QName _ConvertirMasa_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirMasa");
    private final static QName _ConvertirMasaResponse_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirMasaResponse");
    private final static QName _ConvertirTemperatura_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirTemperatura");
    private final static QName _ConvertirTemperaturaResponse_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "convertirTemperaturaResponse");
    private final static QName _Login_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "login");
    private final static QName _LoginResponse_QNAME = new QName("http://ws.controladores.monster.edu.ec/", "loginResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.edu.monster.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CambiarContrasenia }
     * 
     */
    public CambiarContrasenia createCambiarContrasenia() {
        return new CambiarContrasenia();
    }

    /**
     * Create an instance of {@link CambiarContraseniaResponse }
     * 
     */
    public CambiarContraseniaResponse createCambiarContraseniaResponse() {
        return new CambiarContraseniaResponse();
    }

    /**
     * Create an instance of {@link ConvertirLongitud }
     * 
     */
    public ConvertirLongitud createConvertirLongitud() {
        return new ConvertirLongitud();
    }

    /**
     * Create an instance of {@link ConvertirLongitudResponse }
     * 
     */
    public ConvertirLongitudResponse createConvertirLongitudResponse() {
        return new ConvertirLongitudResponse();
    }

    /**
     * Create an instance of {@link ConvertirMasa }
     * 
     */
    public ConvertirMasa createConvertirMasa() {
        return new ConvertirMasa();
    }

    /**
     * Create an instance of {@link ConvertirMasaResponse }
     * 
     */
    public ConvertirMasaResponse createConvertirMasaResponse() {
        return new ConvertirMasaResponse();
    }

    /**
     * Create an instance of {@link ConvertirTemperatura }
     * 
     */
    public ConvertirTemperatura createConvertirTemperatura() {
        return new ConvertirTemperatura();
    }

    /**
     * Create an instance of {@link ConvertirTemperaturaResponse }
     * 
     */
    public ConvertirTemperaturaResponse createConvertirTemperaturaResponse() {
        return new ConvertirTemperaturaResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CambiarContrasenia }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CambiarContrasenia }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "cambiarContrasenia")
    public JAXBElement<CambiarContrasenia> createCambiarContrasenia(CambiarContrasenia value) {
        return new JAXBElement<CambiarContrasenia>(_CambiarContrasenia_QNAME, CambiarContrasenia.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CambiarContraseniaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CambiarContraseniaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "cambiarContraseniaResponse")
    public JAXBElement<CambiarContraseniaResponse> createCambiarContraseniaResponse(CambiarContraseniaResponse value) {
        return new JAXBElement<CambiarContraseniaResponse>(_CambiarContraseniaResponse_QNAME, CambiarContraseniaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirLongitud }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirLongitud }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirLongitud")
    public JAXBElement<ConvertirLongitud> createConvertirLongitud(ConvertirLongitud value) {
        return new JAXBElement<ConvertirLongitud>(_ConvertirLongitud_QNAME, ConvertirLongitud.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirLongitudResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirLongitudResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirLongitudResponse")
    public JAXBElement<ConvertirLongitudResponse> createConvertirLongitudResponse(ConvertirLongitudResponse value) {
        return new JAXBElement<ConvertirLongitudResponse>(_ConvertirLongitudResponse_QNAME, ConvertirLongitudResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirMasa }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirMasa }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirMasa")
    public JAXBElement<ConvertirMasa> createConvertirMasa(ConvertirMasa value) {
        return new JAXBElement<ConvertirMasa>(_ConvertirMasa_QNAME, ConvertirMasa.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirMasaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirMasaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirMasaResponse")
    public JAXBElement<ConvertirMasaResponse> createConvertirMasaResponse(ConvertirMasaResponse value) {
        return new JAXBElement<ConvertirMasaResponse>(_ConvertirMasaResponse_QNAME, ConvertirMasaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirTemperatura }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirTemperatura }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirTemperatura")
    public JAXBElement<ConvertirTemperatura> createConvertirTemperatura(ConvertirTemperatura value) {
        return new JAXBElement<ConvertirTemperatura>(_ConvertirTemperatura_QNAME, ConvertirTemperatura.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirTemperaturaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConvertirTemperaturaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "convertirTemperaturaResponse")
    public JAXBElement<ConvertirTemperaturaResponse> createConvertirTemperaturaResponse(ConvertirTemperaturaResponse value) {
        return new JAXBElement<ConvertirTemperaturaResponse>(_ConvertirTemperaturaResponse_QNAME, ConvertirTemperaturaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Login }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.controladores.monster.edu.ec/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

}
