
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operacionRespuestaDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="operacionRespuestaDTO">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="exito" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="fechaHora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="numeroCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="numeroOperacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="saldoActual" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="saldoAnterior" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operacionRespuestaDTO", propOrder = {
    "codigo",
    "exito",
    "fechaHora",
    "mensaje",
    "monto",
    "numeroCuenta",
    "numeroOperacion",
    "saldoActual",
    "saldoAnterior"
})
public class OperacionRespuestaDTO {

    protected String codigo;
    protected boolean exito;
    protected String fechaHora;
    protected String mensaje;
    protected double monto;
    protected String numeroCuenta;
    protected String numeroOperacion;
    protected double saldoActual;
    protected double saldoAnterior;

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the exito property.
     * 
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * Sets the value of the exito property.
     * 
     */
    public void setExito(boolean value) {
        this.exito = value;
    }

    /**
     * Gets the value of the fechaHora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaHora() {
        return fechaHora;
    }

    /**
     * Sets the value of the fechaHora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaHora(String value) {
        this.fechaHora = value;
    }

    /**
     * Gets the value of the mensaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the value of the mensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
    }

    /**
     * Gets the value of the monto property.
     * 
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     * 
     */
    public void setMonto(double value) {
        this.monto = value;
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
     * Gets the value of the numeroOperacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroOperacion() {
        return numeroOperacion;
    }

    /**
     * Sets the value of the numeroOperacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroOperacion(String value) {
        this.numeroOperacion = value;
    }

    /**
     * Gets the value of the saldoActual property.
     * 
     */
    public double getSaldoActual() {
        return saldoActual;
    }

    /**
     * Sets the value of the saldoActual property.
     * 
     */
    public void setSaldoActual(double value) {
        this.saldoActual = value;
    }

    /**
     * Gets the value of the saldoAnterior property.
     * 
     */
    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    /**
     * Sets the value of the saldoAnterior property.
     * 
     */
    public void setSaldoAnterior(double value) {
        this.saldoAnterior = value;
    }

}
