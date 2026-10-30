
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferir complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="transferir">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="cuentaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="cuentaDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="empleadoId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="ventanilla" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transferir", propOrder = {
    "cuentaOrigen",
    "cuentaDestino",
    "monto",
    "empleadoId",
    "ventanilla"
})
public class Transferir {

    protected String cuentaOrigen;
    protected String cuentaDestino;
    protected double monto;
    protected int empleadoId;
    protected int ventanilla;

    /**
     * Gets the value of the cuentaOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    /**
     * Sets the value of the cuentaOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaOrigen(String value) {
        this.cuentaOrigen = value;
    }

    /**
     * Gets the value of the cuentaDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaDestino() {
        return cuentaDestino;
    }

    /**
     * Sets the value of the cuentaDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaDestino(String value) {
        this.cuentaDestino = value;
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
     * Gets the value of the empleadoId property.
     * 
     */
    public int getEmpleadoId() {
        return empleadoId;
    }

    /**
     * Sets the value of the empleadoId property.
     * 
     */
    public void setEmpleadoId(int value) {
        this.empleadoId = value;
    }

    /**
     * Gets the value of the ventanilla property.
     * 
     */
    public int getVentanilla() {
        return ventanilla;
    }

    /**
     * Sets the value of the ventanilla property.
     * 
     */
    public void setVentanilla(int value) {
        this.ventanilla = value;
    }

}
