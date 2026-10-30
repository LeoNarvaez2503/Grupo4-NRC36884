
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para retirar complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="retirar">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="numeroCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "retirar", propOrder = {
    "numeroCuenta",
    "monto",
    "empleadoId",
    "ventanilla"
})
public class Retirar {

    protected String numeroCuenta;
    protected double monto;
    protected int empleadoId;
    protected int ventanilla;

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
     * Obtiene el valor de la propiedad monto.
     * 
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     */
    public void setMonto(double value) {
        this.monto = value;
    }

    /**
     * Obtiene el valor de la propiedad empleadoId.
     * 
     */
    public int getEmpleadoId() {
        return empleadoId;
    }

    /**
     * Define el valor de la propiedad empleadoId.
     * 
     */
    public void setEmpleadoId(int value) {
        this.empleadoId = value;
    }

    /**
     * Obtiene el valor de la propiedad ventanilla.
     * 
     */
    public int getVentanilla() {
        return ventanilla;
    }

    /**
     * Define el valor de la propiedad ventanilla.
     * 
     */
    public void setVentanilla(int value) {
        this.ventanilla = value;
    }

}
