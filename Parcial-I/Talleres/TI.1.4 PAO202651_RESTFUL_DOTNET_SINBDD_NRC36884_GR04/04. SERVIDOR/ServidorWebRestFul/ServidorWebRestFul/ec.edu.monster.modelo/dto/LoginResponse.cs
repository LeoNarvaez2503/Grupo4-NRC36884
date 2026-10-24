using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class LoginResponse
    {
        [DataMember(Name = "exito")]
        public bool Exito { get; set; }

        [DataMember(Name = "token", EmitDefaultValue = false)]
        public string Token { get; set; }

        [DataMember(Name = "usuario")]
        public string Usuario { get; set; }

        [DataMember(Name = "mensaje")]
        public string Mensaje { get; set; }

        public LoginResponse()
        {
        }

        public LoginResponse(bool exito, string token, string usuario, string mensaje)
        {
            this.Exito = exito;
            this.Token = token;
            this.Usuario = usuario;
            this.Mensaje = mensaje;
        }
    }
}
