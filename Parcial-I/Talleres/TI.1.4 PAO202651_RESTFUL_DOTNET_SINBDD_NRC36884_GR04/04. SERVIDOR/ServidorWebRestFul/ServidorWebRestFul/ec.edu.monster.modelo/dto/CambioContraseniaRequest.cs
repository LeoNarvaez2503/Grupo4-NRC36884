using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class CambioContraseniaRequest
    {
        [DataMember(Name = "usuario", EmitDefaultValue = false)]
        public string Usuario { get; set; }

        [DataMember(Name = "contraseniaActual")]
        public string ContraseniaActual { get; set; }

        [DataMember(Name = "contraseniaNueva")]
        public string ContraseniaNueva { get; set; }

        public CambioContraseniaRequest()
        {
        }

        public CambioContraseniaRequest(string contraseniaActual, string contraseniaNueva)
        {
            this.ContraseniaActual = contraseniaActual;
            this.ContraseniaNueva = contraseniaNueva;
        }

        public CambioContraseniaRequest(string usuario, string contraseniaActual, string contraseniaNueva)
        {
            this.Usuario = usuario;
            this.ContraseniaActual = contraseniaActual;
            this.ContraseniaNueva = contraseniaNueva;
        }
    }
}
