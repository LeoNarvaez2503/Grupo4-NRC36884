
package ec.edu.monster.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cambiarContrasenia complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cambiarContrasenia"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contraseniaActual" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contraseniaNueva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cambiarContrasenia", propOrder = {
    "contraseniaActual",
    "contraseniaNueva"
})
public class CambiarContrasenia {

    protected String contraseniaActual;
    protected String contraseniaNueva;

    /**
     * Gets the value of the contraseniaActual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContraseniaActual() {
        return contraseniaActual;
    }

    /**
     * Sets the value of the contraseniaActual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContraseniaActual(String value) {
        this.contraseniaActual = value;
    }

    /**
     * Gets the value of the contraseniaNueva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContraseniaNueva() {
        return contraseniaNueva;
    }

    /**
     * Sets the value of the contraseniaNueva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContraseniaNueva(String value) {
        this.contraseniaNueva = value;
    }

}
