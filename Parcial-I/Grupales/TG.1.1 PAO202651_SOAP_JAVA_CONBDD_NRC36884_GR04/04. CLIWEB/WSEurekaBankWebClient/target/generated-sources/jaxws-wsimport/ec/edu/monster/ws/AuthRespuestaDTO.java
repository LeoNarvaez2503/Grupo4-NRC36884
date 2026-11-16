
package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for authRespuestaDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="authRespuestaDTO">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="autenticado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="idEmpleado" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="idSucursal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombreCompleto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombreSucursal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="rol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "authRespuestaDTO", propOrder = {
    "autenticado",
    "idEmpleado",
    "idSucursal",
    "mensaje",
    "nombreCompleto",
    "nombreSucursal",
    "rol",
    "token",
    "usuario",
    "ventanilla"
})
public class AuthRespuestaDTO {

    protected boolean autenticado;
    protected int idEmpleado;
    protected int idSucursal;
    protected String mensaje;
    protected String nombreCompleto;
    protected String nombreSucursal;
    protected String rol;
    protected String token;
    protected String usuario;
    protected int ventanilla;

    /**
     * Gets the value of the autenticado property.
     * 
     */
    public boolean isAutenticado() {
        return autenticado;
    }

    /**
     * Sets the value of the autenticado property.
     * 
     */
    public void setAutenticado(boolean value) {
        this.autenticado = value;
    }

    /**
     * Gets the value of the idEmpleado property.
     * 
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * Sets the value of the idEmpleado property.
     * 
     */
    public void setIdEmpleado(int value) {
        this.idEmpleado = value;
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
     * Gets the value of the nombreCompleto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Sets the value of the nombreCompleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCompleto(String value) {
        this.nombreCompleto = value;
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
     * Gets the value of the rol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRol() {
        return rol;
    }

    /**
     * Sets the value of the rol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRol(String value) {
        this.rol = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
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
