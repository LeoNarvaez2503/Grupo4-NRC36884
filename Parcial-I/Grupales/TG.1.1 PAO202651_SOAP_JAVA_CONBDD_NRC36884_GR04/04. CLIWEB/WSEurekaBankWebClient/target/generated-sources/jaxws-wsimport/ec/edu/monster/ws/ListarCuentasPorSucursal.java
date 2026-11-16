
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listarCuentasPorSucursal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="listarCuentasPorSucursal">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="sucursalId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listarCuentasPorSucursal", propOrder = {
    "sucursalId"
})
public class ListarCuentasPorSucursal {

    protected int sucursalId;

    /**
     * Gets the value of the sucursalId property.
     * 
     */
    public int getSucursalId() {
        return sucursalId;
    }

    /**
     * Sets the value of the sucursalId property.
     * 
     */
    public void setSucursalId(int value) {
        this.sucursalId = value;
    }

}
