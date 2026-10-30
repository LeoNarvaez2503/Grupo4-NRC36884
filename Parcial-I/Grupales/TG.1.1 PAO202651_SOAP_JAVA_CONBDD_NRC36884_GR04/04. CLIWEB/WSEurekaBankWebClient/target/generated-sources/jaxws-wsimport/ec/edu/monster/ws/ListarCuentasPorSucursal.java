
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para listarCuentasPorSucursal complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
     * Obtiene el valor de la propiedad sucursalId.
     * 
     */
    public int getSucursalId() {
        return sucursalId;
    }

    /**
     * Define el valor de la propiedad sucursalId.
     * 
     */
    public void setSucursalId(int value) {
        this.sucursalId = value;
    }

}
