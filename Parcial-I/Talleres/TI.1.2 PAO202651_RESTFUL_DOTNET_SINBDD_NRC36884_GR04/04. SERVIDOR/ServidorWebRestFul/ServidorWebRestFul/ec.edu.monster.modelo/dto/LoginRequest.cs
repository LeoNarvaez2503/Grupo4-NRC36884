using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class LoginRequest
    {
        [DataMember(Name = "usuario")]
        public string Usuario { get; set; }

        [DataMember(Name = "contrasenia")]
        public string Contrasenia { get; set; }

        public LoginRequest()
        {
        }

        public LoginRequest(string usuario, string contrasenia)
        {
            this.Usuario = usuario;
            this.Contrasenia = contrasenia;
        }
    }
}
