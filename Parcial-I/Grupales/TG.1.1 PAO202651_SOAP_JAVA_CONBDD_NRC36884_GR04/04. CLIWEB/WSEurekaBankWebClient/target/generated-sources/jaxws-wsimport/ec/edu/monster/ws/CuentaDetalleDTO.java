
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cuentaDetalleDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the apellidoCliente property.
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
     * Sets the value of the apellidoCliente property.
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
     * Gets the value of the bloqueadoPor property.
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
     * Sets the value of the bloqueadoPor property.
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
     * Gets the value of the ciudadSucursal property.
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
     * Sets the value of the ciudadSucursal property.
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
     * Gets the value of the disponibilidadMashup property.
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
     * Sets the value of the disponibilidadMashup property.
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
     * Gets the value of the dniCliente property.
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
     * Sets the value of the dniCliente property.
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
     * Gets the value of the emailCliente property.
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
     * Sets the value of the emailCliente property.
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
     * Gets the value of the estadoCuenta property.
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
     * Sets the value of the estadoCuenta property.
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
     * Gets the value of the idCliente property.
     * 
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Sets the value of the idCliente property.
     * 
     */
    public void setIdCliente(int value) {
        this.idCliente = value;
    }

    /**
     * Gets the value of the idSucursal property.
     * 
     */
    public int getIdSucursal() {
        return idSucursal;
    }

    /**
     * Sets the value of the idSucursal property.
     * 
     */
    public void setIdSucursal(int value) {
        this.idSucursal = value;
    }

    /**
     * Gets the value of the moneda property.
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
     * Sets the value of the moneda property.
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
     * Gets the value of the nombreCliente property.
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
     * Sets the value of the nombreCliente property.
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
     * Gets the value of the nombreSucursal property.
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
     * Sets the value of the nombreSucursal property.
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
     * Gets the value of the numeroCuenta property.
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
     * Sets the value of the numeroCuenta property.
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
     * Gets the value of the saldo property.
     * 
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Sets the value of the saldo property.
     * 
     */
    public void setSaldo(double value) {
        this.saldo = value;
    }

    /**
     * Gets the value of the ventanillaBloqueo property.
     * 
     */
    public int getVentanillaBloqueo() {
        return ventanillaBloqueo;
    }

    /**
     * Sets the value of the ventanillaBloqueo property.
     * 
     */
    public void setVentanillaBloqueo(int value) {
        this.ventanillaBloqueo = value;
    }

}
