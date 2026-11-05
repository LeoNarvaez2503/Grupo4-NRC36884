
package ec.edu.monster.ws;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


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

    private static final QName _AutenticarUsuario_QNAME = new QName("http://ws.monster.edu.ec/", "autenticarUsuario");
    private static final QName _AutenticarUsuarioResponse_QNAME = new QName("http://ws.monster.edu.ec/", "autenticarUsuarioResponse");
    private static final QName _BloquearCuentaManual_QNAME = new QName("http://ws.monster.edu.ec/", "bloquearCuentaManual");
    private static final QName _BloquearCuentaManualResponse_QNAME = new QName("http://ws.monster.edu.ec/", "bloquearCuentaManualResponse");
    private static final QName _ConsultarCuentasPorCliente_QNAME = new QName("http://ws.monster.edu.ec/", "consultarCuentasPorCliente");
    private static final QName _ConsultarCuentasPorClienteResponse_QNAME = new QName("http://ws.monster.edu.ec/", "consultarCuentasPorClienteResponse");
    private static final QName _ConsultarEstadoMashUp_QNAME = new QName("http://ws.monster.edu.ec/", "consultarEstadoMashUp");
    private static final QName _ConsultarEstadoMashUpResponse_QNAME = new QName("http://ws.monster.edu.ec/", "consultarEstadoMashUpResponse");
    private static final QName _ConsultarExtracto_QNAME = new QName("http://ws.monster.edu.ec/", "consultarExtracto");
    private static final QName _ConsultarExtractoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "consultarExtractoResponse");
    private static final QName _Depositar_QNAME = new QName("http://ws.monster.edu.ec/", "depositar");
    private static final QName _DepositarResponse_QNAME = new QName("http://ws.monster.edu.ec/", "depositarResponse");
    private static final QName _DesbloquearCuenta_QNAME = new QName("http://ws.monster.edu.ec/", "desbloquearCuenta");
    private static final QName _DesbloquearCuentaResponse_QNAME = new QName("http://ws.monster.edu.ec/", "desbloquearCuentaResponse");
    private static final QName _ListarCuentasPorSucursal_QNAME = new QName("http://ws.monster.edu.ec/", "listarCuentasPorSucursal");
    private static final QName _ListarCuentasPorSucursalResponse_QNAME = new QName("http://ws.monster.edu.ec/", "listarCuentasPorSucursalResponse");
    private static final QName _ListarSucursales_QNAME = new QName("http://ws.monster.edu.ec/", "listarSucursales");
    private static final QName _ListarSucursalesResponse_QNAME = new QName("http://ws.monster.edu.ec/", "listarSucursalesResponse");
    private static final QName _ObtenerCuentaPorNumero_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerCuentaPorNumero");
    private static final QName _ObtenerCuentaPorNumeroResponse_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerCuentaPorNumeroResponse");
    private static final QName _Retirar_QNAME = new QName("http://ws.monster.edu.ec/", "retirar");
    private static final QName _RetirarResponse_QNAME = new QName("http://ws.monster.edu.ec/", "retirarResponse");
    private static final QName _Transferir_QNAME = new QName("http://ws.monster.edu.ec/", "transferir");
    private static final QName _TransferirResponse_QNAME = new QName("http://ws.monster.edu.ec/", "transferirResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.edu.monster.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AutenticarUsuario }
     * 
     * @return
     *     the new instance of {@link AutenticarUsuario }
     */
    public AutenticarUsuario createAutenticarUsuario() {
        return new AutenticarUsuario();
    }

    /**
     * Create an instance of {@link AutenticarUsuarioResponse }
     * 
     * @return
     *     the new instance of {@link AutenticarUsuarioResponse }
     */
    public AutenticarUsuarioResponse createAutenticarUsuarioResponse() {
        return new AutenticarUsuarioResponse();
    }

    /**
     * Create an instance of {@link BloquearCuentaManual }
     * 
     * @return
     *     the new instance of {@link BloquearCuentaManual }
     */
    public BloquearCuentaManual createBloquearCuentaManual() {
        return new BloquearCuentaManual();
    }

    /**
     * Create an instance of {@link BloquearCuentaManualResponse }
     * 
     * @return
     *     the new instance of {@link BloquearCuentaManualResponse }
     */
    public BloquearCuentaManualResponse createBloquearCuentaManualResponse() {
        return new BloquearCuentaManualResponse();
    }

    /**
     * Create an instance of {@link ConsultarCuentasPorCliente }
     * 
     * @return
     *     the new instance of {@link ConsultarCuentasPorCliente }
     */
    public ConsultarCuentasPorCliente createConsultarCuentasPorCliente() {
        return new ConsultarCuentasPorCliente();
    }

    /**
     * Create an instance of {@link ConsultarCuentasPorClienteResponse }
     * 
     * @return
     *     the new instance of {@link ConsultarCuentasPorClienteResponse }
     */
    public ConsultarCuentasPorClienteResponse createConsultarCuentasPorClienteResponse() {
        return new ConsultarCuentasPorClienteResponse();
    }

    /**
     * Create an instance of {@link ConsultarEstadoMashUp }
     * 
     * @return
     *     the new instance of {@link ConsultarEstadoMashUp }
     */
    public ConsultarEstadoMashUp createConsultarEstadoMashUp() {
        return new ConsultarEstadoMashUp();
    }

    /**
     * Create an instance of {@link ConsultarEstadoMashUpResponse }
     * 
     * @return
     *     the new instance of {@link ConsultarEstadoMashUpResponse }
     */
    public ConsultarEstadoMashUpResponse createConsultarEstadoMashUpResponse() {
        return new ConsultarEstadoMashUpResponse();
    }

    /**
     * Create an instance of {@link ConsultarExtracto }
     * 
     * @return
     *     the new instance of {@link ConsultarExtracto }
     */
    public ConsultarExtracto createConsultarExtracto() {
        return new ConsultarExtracto();
    }

    /**
     * Create an instance of {@link ConsultarExtractoResponse }
     * 
     * @return
     *     the new instance of {@link ConsultarExtractoResponse }
     */
    public ConsultarExtractoResponse createConsultarExtractoResponse() {
        return new ConsultarExtractoResponse();
    }

    /**
     * Create an instance of {@link Depositar }
     * 
     * @return
     *     the new instance of {@link Depositar }
     */
    public Depositar createDepositar() {
        return new Depositar();
    }

    /**
     * Create an instance of {@link DepositarResponse }
     * 
     * @return
     *     the new instance of {@link DepositarResponse }
     */
    public DepositarResponse createDepositarResponse() {
        return new DepositarResponse();
    }

    /**
     * Create an instance of {@link DesbloquearCuenta }
     * 
     * @return
     *     the new instance of {@link DesbloquearCuenta }
     */
    public DesbloquearCuenta createDesbloquearCuenta() {
        return new DesbloquearCuenta();
    }

    /**
     * Create an instance of {@link DesbloquearCuentaResponse }
     * 
     * @return
     *     the new instance of {@link DesbloquearCuentaResponse }
     */
    public DesbloquearCuentaResponse createDesbloquearCuentaResponse() {
        return new DesbloquearCuentaResponse();
    }

    /**
     * Create an instance of {@link ListarCuentasPorSucursal }
     * 
     * @return
     *     the new instance of {@link ListarCuentasPorSucursal }
     */
    public ListarCuentasPorSucursal createListarCuentasPorSucursal() {
        return new ListarCuentasPorSucursal();
    }

    /**
     * Create an instance of {@link ListarCuentasPorSucursalResponse }
     * 
     * @return
     *     the new instance of {@link ListarCuentasPorSucursalResponse }
     */
    public ListarCuentasPorSucursalResponse createListarCuentasPorSucursalResponse() {
        return new ListarCuentasPorSucursalResponse();
    }

    /**
     * Create an instance of {@link ListarSucursales }
     * 
     * @return
     *     the new instance of {@link ListarSucursales }
     */
    public ListarSucursales createListarSucursales() {
        return new ListarSucursales();
    }

    /**
     * Create an instance of {@link ListarSucursalesResponse }
     * 
     * @return
     *     the new instance of {@link ListarSucursalesResponse }
     */
    public ListarSucursalesResponse createListarSucursalesResponse() {
        return new ListarSucursalesResponse();
    }

    /**
     * Create an instance of {@link ObtenerCuentaPorNumero }
     * 
     * @return
     *     the new instance of {@link ObtenerCuentaPorNumero }
     */
    public ObtenerCuentaPorNumero createObtenerCuentaPorNumero() {
        return new ObtenerCuentaPorNumero();
    }

    /**
     * Create an instance of {@link ObtenerCuentaPorNumeroResponse }
     * 
     * @return
     *     the new instance of {@link ObtenerCuentaPorNumeroResponse }
     */
    public ObtenerCuentaPorNumeroResponse createObtenerCuentaPorNumeroResponse() {
        return new ObtenerCuentaPorNumeroResponse();
    }

    /**
     * Create an instance of {@link Retirar }
     * 
     * @return
     *     the new instance of {@link Retirar }
     */
    public Retirar createRetirar() {
        return new Retirar();
    }

    /**
     * Create an instance of {@link RetirarResponse }
     * 
     * @return
     *     the new instance of {@link RetirarResponse }
     */
    public RetirarResponse createRetirarResponse() {
        return new RetirarResponse();
    }

    /**
     * Create an instance of {@link Transferir }
     * 
     * @return
     *     the new instance of {@link Transferir }
     */
    public Transferir createTransferir() {
        return new Transferir();
    }

    /**
     * Create an instance of {@link TransferirResponse }
     * 
     * @return
     *     the new instance of {@link TransferirResponse }
     */
    public TransferirResponse createTransferirResponse() {
        return new TransferirResponse();
    }

    /**
     * Create an instance of {@link OperacionRespuestaDTO }
     * 
     * @return
     *     the new instance of {@link OperacionRespuestaDTO }
     */
    public OperacionRespuestaDTO createOperacionRespuestaDTO() {
        return new OperacionRespuestaDTO();
    }

    /**
     * Create an instance of {@link CuentaDetalleDTO }
     * 
     * @return
     *     the new instance of {@link CuentaDetalleDTO }
     */
    public CuentaDetalleDTO createCuentaDetalleDTO() {
        return new CuentaDetalleDTO();
    }

    /**
     * Create an instance of {@link AuthRespuestaDTO }
     * 
     * @return
     *     the new instance of {@link AuthRespuestaDTO }
     */
    public AuthRespuestaDTO createAuthRespuestaDTO() {
        return new AuthRespuestaDTO();
    }

    /**
     * Create an instance of {@link SucursalDTO }
     * 
     * @return
     *     the new instance of {@link SucursalDTO }
     */
    public SucursalDTO createSucursalDTO() {
        return new SucursalDTO();
    }

    /**
     * Create an instance of {@link MovimientoDTO }
     * 
     * @return
     *     the new instance of {@link MovimientoDTO }
     */
    public MovimientoDTO createMovimientoDTO() {
        return new MovimientoDTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticarUsuario }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AutenticarUsuario }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "autenticarUsuario")
    public JAXBElement<AutenticarUsuario> createAutenticarUsuario(AutenticarUsuario value) {
        return new JAXBElement<>(_AutenticarUsuario_QNAME, AutenticarUsuario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticarUsuarioResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AutenticarUsuarioResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "autenticarUsuarioResponse")
    public JAXBElement<AutenticarUsuarioResponse> createAutenticarUsuarioResponse(AutenticarUsuarioResponse value) {
        return new JAXBElement<>(_AutenticarUsuarioResponse_QNAME, AutenticarUsuarioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BloquearCuentaManual }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BloquearCuentaManual }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "bloquearCuentaManual")
    public JAXBElement<BloquearCuentaManual> createBloquearCuentaManual(BloquearCuentaManual value) {
        return new JAXBElement<>(_BloquearCuentaManual_QNAME, BloquearCuentaManual.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BloquearCuentaManualResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BloquearCuentaManualResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "bloquearCuentaManualResponse")
    public JAXBElement<BloquearCuentaManualResponse> createBloquearCuentaManualResponse(BloquearCuentaManualResponse value) {
        return new JAXBElement<>(_BloquearCuentaManualResponse_QNAME, BloquearCuentaManualResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarCuentasPorCliente }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarCuentasPorCliente }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarCuentasPorCliente")
    public JAXBElement<ConsultarCuentasPorCliente> createConsultarCuentasPorCliente(ConsultarCuentasPorCliente value) {
        return new JAXBElement<>(_ConsultarCuentasPorCliente_QNAME, ConsultarCuentasPorCliente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarCuentasPorClienteResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarCuentasPorClienteResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarCuentasPorClienteResponse")
    public JAXBElement<ConsultarCuentasPorClienteResponse> createConsultarCuentasPorClienteResponse(ConsultarCuentasPorClienteResponse value) {
        return new JAXBElement<>(_ConsultarCuentasPorClienteResponse_QNAME, ConsultarCuentasPorClienteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstadoMashUp }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarEstadoMashUp }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarEstadoMashUp")
    public JAXBElement<ConsultarEstadoMashUp> createConsultarEstadoMashUp(ConsultarEstadoMashUp value) {
        return new JAXBElement<>(_ConsultarEstadoMashUp_QNAME, ConsultarEstadoMashUp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstadoMashUpResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarEstadoMashUpResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarEstadoMashUpResponse")
    public JAXBElement<ConsultarEstadoMashUpResponse> createConsultarEstadoMashUpResponse(ConsultarEstadoMashUpResponse value) {
        return new JAXBElement<>(_ConsultarEstadoMashUpResponse_QNAME, ConsultarEstadoMashUpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarExtracto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarExtracto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarExtracto")
    public JAXBElement<ConsultarExtracto> createConsultarExtracto(ConsultarExtracto value) {
        return new JAXBElement<>(_ConsultarExtracto_QNAME, ConsultarExtracto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarExtractoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarExtractoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarExtractoResponse")
    public JAXBElement<ConsultarExtractoResponse> createConsultarExtractoResponse(ConsultarExtractoResponse value) {
        return new JAXBElement<>(_ConsultarExtractoResponse_QNAME, ConsultarExtractoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Depositar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Depositar }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "depositar")
    public JAXBElement<Depositar> createDepositar(Depositar value) {
        return new JAXBElement<>(_Depositar_QNAME, Depositar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DepositarResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DepositarResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "depositarResponse")
    public JAXBElement<DepositarResponse> createDepositarResponse(DepositarResponse value) {
        return new JAXBElement<>(_DepositarResponse_QNAME, DepositarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DesbloquearCuenta }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DesbloquearCuenta }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "desbloquearCuenta")
    public JAXBElement<DesbloquearCuenta> createDesbloquearCuenta(DesbloquearCuenta value) {
        return new JAXBElement<>(_DesbloquearCuenta_QNAME, DesbloquearCuenta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DesbloquearCuentaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DesbloquearCuentaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "desbloquearCuentaResponse")
    public JAXBElement<DesbloquearCuentaResponse> createDesbloquearCuentaResponse(DesbloquearCuentaResponse value) {
        return new JAXBElement<>(_DesbloquearCuentaResponse_QNAME, DesbloquearCuentaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarCuentasPorSucursal }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarCuentasPorSucursal }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarCuentasPorSucursal")
    public JAXBElement<ListarCuentasPorSucursal> createListarCuentasPorSucursal(ListarCuentasPorSucursal value) {
        return new JAXBElement<>(_ListarCuentasPorSucursal_QNAME, ListarCuentasPorSucursal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarCuentasPorSucursalResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarCuentasPorSucursalResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarCuentasPorSucursalResponse")
    public JAXBElement<ListarCuentasPorSucursalResponse> createListarCuentasPorSucursalResponse(ListarCuentasPorSucursalResponse value) {
        return new JAXBElement<>(_ListarCuentasPorSucursalResponse_QNAME, ListarCuentasPorSucursalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarSucursales }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarSucursales }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarSucursales")
    public JAXBElement<ListarSucursales> createListarSucursales(ListarSucursales value) {
        return new JAXBElement<>(_ListarSucursales_QNAME, ListarSucursales.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarSucursalesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarSucursalesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarSucursalesResponse")
    public JAXBElement<ListarSucursalesResponse> createListarSucursalesResponse(ListarSucursalesResponse value) {
        return new JAXBElement<>(_ListarSucursalesResponse_QNAME, ListarSucursalesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerCuentaPorNumero }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerCuentaPorNumero }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerCuentaPorNumero")
    public JAXBElement<ObtenerCuentaPorNumero> createObtenerCuentaPorNumero(ObtenerCuentaPorNumero value) {
        return new JAXBElement<>(_ObtenerCuentaPorNumero_QNAME, ObtenerCuentaPorNumero.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerCuentaPorNumeroResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerCuentaPorNumeroResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerCuentaPorNumeroResponse")
    public JAXBElement<ObtenerCuentaPorNumeroResponse> createObtenerCuentaPorNumeroResponse(ObtenerCuentaPorNumeroResponse value) {
        return new JAXBElement<>(_ObtenerCuentaPorNumeroResponse_QNAME, ObtenerCuentaPorNumeroResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Retirar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Retirar }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "retirar")
    public JAXBElement<Retirar> createRetirar(Retirar value) {
        return new JAXBElement<>(_Retirar_QNAME, Retirar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetirarResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RetirarResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "retirarResponse")
    public JAXBElement<RetirarResponse> createRetirarResponse(RetirarResponse value) {
        return new JAXBElement<>(_RetirarResponse_QNAME, RetirarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transferir }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Transferir }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "transferir")
    public JAXBElement<Transferir> createTransferir(Transferir value) {
        return new JAXBElement<>(_Transferir_QNAME, Transferir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferirResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransferirResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "transferirResponse")
    public JAXBElement<TransferirResponse> createTransferirResponse(TransferirResponse value) {
        return new JAXBElement<>(_TransferirResponse_QNAME, TransferirResponse.class, null, value);
    }

}
