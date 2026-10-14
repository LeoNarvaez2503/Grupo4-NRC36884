
package ec.edu.monster.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for convertirTemperatura complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="convertirTemperatura"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="unidadInicial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unidadFinal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertirTemperatura", propOrder = {
    "valor",
    "unidadInicial",
    "unidadFinal"
})
public class ConvertirTemperatura {

    protected double valor;
    protected String unidadInicial;
    protected String unidadFinal;

    /**
     * Gets the value of the valor property.
     * 
     */
    public double getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     */
    public void setValor(double value) {
        this.valor = value;
    }

    /**
     * Gets the value of the unidadInicial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadInicial() {
        return unidadInicial;
    }

    /**
     * Sets the value of the unidadInicial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadInicial(String value) {
        this.unidadInicial = value;
    }

    /**
     * Gets the value of the unidadFinal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadFinal() {
        return unidadFinal;
    }

    /**
     * Sets the value of the unidadFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadFinal(String value) {
        this.unidadFinal = value;
    }

}
