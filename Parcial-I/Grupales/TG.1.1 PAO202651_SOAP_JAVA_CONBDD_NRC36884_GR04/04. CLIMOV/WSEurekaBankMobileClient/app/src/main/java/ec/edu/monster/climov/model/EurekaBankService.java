package ec.edu.monster.climov.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Capa Modelo (Patrón MVC Tradicional): Consumidor nativo Android del Servicio Web SOAP EurekaBankWS.
 * Construye sobres XML SOAP 1.1 tipados bajo http://ws.monster.edu.ec/ sin lógica de negocio local.
 */
public class EurekaBankService {

    private static final String NS = "http://ws.monster.edu.ec/";

    public String getEndpointActual() {
        return ConfiguracionConexion.getSoapEndpoint();
    }

    public UsuarioSesion login(String usuario, String clave) {
        String body = "<ws:autenticarUsuario>"
                + "<usuario>" + esc(usuario) + "</usuario>"
                + "<clave>" + esc(clave) + "</clave>"
                + "</ws:autenticarUsuario>";
        Document doc = invocarSoap(body);
        Element ret = primerElemento(doc, "return");
        UsuarioSesion s = new UsuarioSesion();
        if (ret != null) {
            s.setAutenticado(Boolean.parseBoolean(texto(ret, "autenticado")));
            s.setMensaje(texto(ret, "mensaje"));
            s.setToken(texto(ret, "token"));
            s.setIdEmpleado(entero(ret, "idEmpleado"));
            s.setUsuario(texto(ret, "usuario"));
            s.setNombreCompleto(texto(ret, "nombreCompleto"));
            s.setRol(texto(ret, "rol"));
            s.setIdSucursal(entero(ret, "idSucursal"));
            s.setNombreSucursal(texto(ret, "nombreSucursal"));
            s.setVentanilla(entero(ret, "ventanilla"));
        }
        return s;
    }

    public List<SucursalModel> listarSucursales() {
        Document doc = invocarSoap("<ws:listarSucursales/>");
        List<SucursalModel> lista = new ArrayList<>();
        for (Element el : elementos(doc, "return")) {
            SucursalModel s = new SucursalModel();
            s.setIdSucursal(entero(el, "idSucursal"));
            s.setCodigo(texto(el, "codigo"));
            s.setNombre(texto(el, "nombre"));
            s.setCiudad(texto(el, "ciudad"));
            s.setDireccion(texto(el, "direccion"));
            s.setTotalCuentas(entero(el, "totalCuentas"));
            lista.add(s);
        }
        return lista;
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        Document doc = invocarSoap("<ws:listarCuentasPorSucursal><sucursalId>" + sucursalId + "</sucursalId></ws:listarCuentasPorSucursal>");
        List<CuentaModel> lista = new ArrayList<>();
        for (Element el : elementos(doc, "return")) {
            lista.add(mapearCuenta(el));
        }
        return lista;
    }

    public CuentaModel obtenerCuentaPorNumero(String numeroCuenta) {
        Document doc = invocarSoap("<ws:obtenerCuentaPorNumero><numeroCuenta>" + esc(numeroCuenta) + "</numeroCuenta></ws:obtenerCuentaPorNumero>");
        Element ret = primerElemento(doc, "return");
        return ret != null ? mapearCuenta(ret) : null;
    }

    public List<CuentaModel> consultarCuentasPorCliente(String dni) {
        Document doc = invocarSoap("<ws:consultarCuentasPorCliente><dni>" + esc(dni) + "</dni></ws:consultarCuentasPorCliente>");
        List<CuentaModel> lista = new ArrayList<>();
        for (Element el : elementos(doc, "return")) {
            lista.add(mapearCuenta(el));
        }
        return lista;
    }

    public List<MovimientoModel> consultarExtracto(String numeroCuenta) {
        Document doc = invocarSoap("<ws:consultarExtracto><numeroCuenta>" + esc(numeroCuenta) + "</numeroCuenta></ws:consultarExtracto>");
        List<MovimientoModel> lista = new ArrayList<>();
        for (Element el : elementos(doc, "return")) {
            MovimientoModel m = new MovimientoModel();
            m.setIdMovimiento(entero(el, "idMovimiento"));
            m.setNumeroOperacion(texto(el, "numeroOperacion"));
            m.setNumeroCuenta(texto(el, "numeroCuenta"));
            m.setIdEmpleado(entero(el, "idEmpleado"));
            m.setNombreEmpleado(texto(el, "nombreEmpleado"));
            m.setVentanilla(entero(el, "ventanilla"));
            m.setTipo(texto(el, "tipo"));
            m.setMonto(decimal(el, "monto"));
            m.setSaldoResultante(decimal(el, "saldoResultante"));
            m.setCuentaReferencia(texto(el, "cuentaReferencia"));
            m.setDescripcion(texto(el, "descripcion"));
            m.setFechaHora(texto(el, "fechaHora"));
            lista.add(m);
        }
        return lista;
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        String body = "<ws:depositar>"
                + "<numeroCuenta>" + esc(numeroCuenta) + "</numeroCuenta>"
                + "<monto>" + monto + "</monto>"
                + "<empleadoId>" + empleadoId + "</empleadoId>"
                + "<ventanilla>" + ventanilla + "</ventanilla>"
                + "</ws:depositar>";
        return mapearOperacion(primerElemento(invocarSoap(body), "return"));
    }

    public ResultadoOperacion retirar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        String body = "<ws:retirar>"
                + "<numeroCuenta>" + esc(numeroCuenta) + "</numeroCuenta>"
                + "<monto>" + monto + "</monto>"
                + "<empleadoId>" + empleadoId + "</empleadoId>"
                + "<ventanilla>" + ventanilla + "</ventanilla>"
                + "</ws:retirar>";
        return mapearOperacion(primerElemento(invocarSoap(body), "return"));
    }

    public ResultadoOperacion transferir(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId, int ventanilla) {
        String body = "<ws:transferir>"
                + "<cuentaOrigen>" + esc(cuentaOrigen) + "</cuentaOrigen>"
                + "<cuentaDestino>" + esc(cuentaDestino) + "</cuentaDestino>"
                + "<monto>" + monto + "</monto>"
                + "<empleadoId>" + empleadoId + "</empleadoId>"
                + "<ventanilla>" + ventanilla + "</ventanilla>"
                + "</ws:transferir>";
        return mapearOperacion(primerElemento(invocarSoap(body), "return"));
    }

    public ResultadoOperacion desbloquearCuenta(String numeroCuenta, String usuarioAdmin) {
        String body = "<ws:desbloquearCuenta>"
                + "<numeroCuenta>" + esc(numeroCuenta) + "</numeroCuenta>"
                + "<usuarioAdmin>" + esc(usuarioAdmin) + "</usuarioAdmin>"
                + "</ws:desbloquearCuenta>";
        return mapearOperacion(primerElemento(invocarSoap(body), "return"));
    }

    private Document invocarSoap(String cuerpoOperacion) {
        String envelope = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"" + NS + "\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>" + cuerpoOperacion + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(ConfiguracionConexion.getSoapEndpoint());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            byte[] bytes = envelope.getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(bytes);
            }

            InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String xml;
            try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
                xml = sc.hasNext() ? sc.next() : "";
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Error SOAP Android (" + ConfiguracionConexion.getSoapEndpoint() + "): " + e.getMessage(), e);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private CuentaModel mapearCuenta(Element el) {
        CuentaModel c = new CuentaModel();
        c.setNumeroCuenta(texto(el, "numeroCuenta"));
        c.setMoneda(texto(el, "moneda"));
        c.setSaldo(decimal(el, "saldo"));
        c.setEstadoCuenta(texto(el, "estadoCuenta"));
        c.setDisponibilidadMashup(texto(el, "disponibilidadMashup"));
        c.setVentanillaBloqueo(entero(el, "ventanillaBloqueo"));
        c.setBloqueadoPor(texto(el, "bloqueadoPor"));
        c.setIdCliente(entero(el, "idCliente"));
        c.setDniCliente(texto(el, "dniCliente"));
        c.setNombreCliente(texto(el, "nombreCliente"));
        c.setApellidoCliente(texto(el, "apellidoCliente"));
        c.setEmailCliente(texto(el, "emailCliente"));
        c.setIdSucursal(entero(el, "idSucursal"));
        c.setNombreSucursal(texto(el, "nombreSucursal"));
        c.setCiudadSucursal(texto(el, "ciudadSucursal"));
        return c;
    }

    private ResultadoOperacion mapearOperacion(Element el) {
        ResultadoOperacion r = new ResultadoOperacion();
        if (el == null) {
            r.setExito(false);
            r.setMensaje("Sin respuesta del servidor SOAP.");
            return r;
        }
        r.setExito(Boolean.parseBoolean(texto(el, "exito")));
        r.setCodigo(texto(el, "codigo"));
        r.setMensaje(texto(el, "mensaje"));
        r.setNumeroOperacion(texto(el, "numeroOperacion"));
        r.setNumeroCuenta(texto(el, "numeroCuenta"));
        r.setSaldoAnterior(decimal(el, "saldoAnterior"));
        r.setMonto(decimal(el, "monto"));
        r.setSaldoActual(decimal(el, "saldoActual"));
        r.setFechaHora(texto(el, "fechaHora"));
        return r;
    }

    private List<Element> elementos(Document doc, String localName) {
        List<Element> res = new ArrayList<>();
        NodeList nl = doc.getElementsByTagNameNS("*", localName);
        if (nl.getLength() == 0) {
            nl = doc.getElementsByTagName(localName);
        }
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                res.add((Element) n);
            }
        }
        return res;
    }

    private Element primerElemento(Document doc, String localName) {
        List<Element> l = elementos(doc, localName);
        return l.isEmpty() ? null : l.get(0);
    }

    private String texto(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagNameNS("*", tag);
        if (nl.getLength() == 0) {
            nl = parent.getElementsByTagName(tag);
        }
        return nl.getLength() > 0 && nl.item(0) != null ? nl.item(0).getTextContent() : "";
    }

    private int entero(Element parent, String tag) {
        try {
            return Integer.parseInt(texto(parent, tag).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double decimal(Element parent, String tag) {
        try {
            return Double.parseDouble(texto(parent, tag).trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
