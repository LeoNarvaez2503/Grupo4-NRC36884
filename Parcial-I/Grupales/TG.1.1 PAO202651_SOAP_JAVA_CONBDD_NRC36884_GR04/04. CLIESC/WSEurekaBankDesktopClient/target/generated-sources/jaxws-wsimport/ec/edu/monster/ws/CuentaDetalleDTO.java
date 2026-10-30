
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para cuentaDetalleDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="cuentaDetalleDTO">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="apellidoCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="bloqueadoPor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="ciudadSucursal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="disponibilidadMashup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="dniCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="emailCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="estadoCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="idCliente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="idSucursal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombreCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombreSucursal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="numeroCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="saldo" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="ventanillaBloqueo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cuentaDetalleDTO", propOrder = {
    "apellidoCliente",
    "bloqueadoPor",
    "ciudadSucursal",
    "disponibilidadMashup",
    "dniCliente",
    "emailCliente",
    "estadoCuenta",
    "idCliente",
    "idSucursal",
    "moneda",
    "nombreCliente",
    "nombreSucursal",
    "numeroCuenta",
    "saldo",
    "ventanillaBloqueo"
})
public class CuentaDetalleDTO {

    protected String apellidoCliente;
    protected String bloqueadoPor;
    protected String ciudadSucursal;
    protected String disponibilidadMashup;
    protected String dniCliente;
    protected String emailCliente;
    protected String estadoCuenta;
    protected int idCliente;
    protected int idSucursal;
    protected String moneda;
    protected String nombreCliente;
    protected String nombreSucursal;
    protected String numeroCuenta;
    protected double saldo;
    protected int ventanillaBloqueo;

    /**
     * Obtiene el valor de la propiedad apellidoCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidoCliente() {
        return apellidoCliente;
    }

    /**
     * Define el valor de la propiedad apellidoCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidoCliente(String value) {
        this.apellidoCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad bloqueadoPor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBloqueadoPor() {
        return bloqueadoPor;
    }

    /**
     * Define el valor de la propiedad bloqueadoPor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBloqueadoPor(String value) {
        this.bloqueadoPor = value;
    }

    /**
     * Obtiene el valor de la propiedad ciudadSucursal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiudadSucursal() {
        return ciudadSucursal;
    }

    /**
     * Define el valor de la propiedad ciudadSucursal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiudadSucursal(String value) {
        this.ciudadSucursal = value;
    }

    /**
     * Obtiene el valor de la propiedad disponibilidadMashup.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisponibilidadMashup() {
        return disponibilidadMashup;
    }

    /**
     * Define el valor de la propiedad disponibilidadMashup.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisponibilidadMashup(String value) {
        this.disponibilidadMashup = value;
    }

    /**
     * Obtiene el valor de la propiedad dniCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDniCliente() {
        return dniCliente;
    }

    /**
     * Define el valor de la propiedad dniCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDniCliente(String value) {
        this.dniCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad emailCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailCliente() {
        return emailCliente;
    }

    /**
     * Define el valor de la propiedad emailCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailCliente(String value) {
        this.emailCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoCuenta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    /**
     * Define el valor de la propiedad estadoCuenta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoCuenta(String value) {
        this.estadoCuenta = value;
    }

    /**
     * Obtiene el valor de la propiedad idCliente.
     * 
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Define el valor de la propiedad idCliente.
     * 
     */
    public void setIdCliente(int value) {
        this.idCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad idSucursal.
     * 
     */
    public int getIdSucursal() {
        return idSucursal;
    }

    /**
     * Define el valor de la propiedad idSucursal.
     * 
     */
    public void setIdSucursal(int value) {
        this.idSucursal = value;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoneda(String value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Define el valor de la propiedad nombreCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCliente(String value) {
        this.nombreCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreSucursal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSucursal() {
        return nombreSucursal;
    }

    /**
     * Define el valor de la propiedad nombreSucursal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSucursal(String value) {
        this.nombreSucursal = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroCuenta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Define el valor de la propiedad numeroCuenta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCuenta(String value) {
        this.numeroCuenta = value;
    }

    /**
     * Obtiene el valor de la propiedad saldo.
     * 
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Define el valor de la propiedad saldo.
     * 
     */
    public void setSaldo(double value) {
        this.saldo = value;
    }

    /**
     * Obtiene el valor de la propiedad ventanillaBloqueo.
     * 
     */
    public int getVentanillaBloqueo() {
        return ventanillaBloqueo;
    }

    /**
     * Define el valor de la propiedad ventanillaBloqueo.
     * 
     */
    public void setVentanillaBloqueo(int value) {
        this.ventanillaBloqueo = value;
    }

}
