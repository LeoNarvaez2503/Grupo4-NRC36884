using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class RespuestaGeneral
    {
        [DataMember(Name = "exito")]
        public bool Exito { get; set; }

        [DataMember(Name = "mensaje")]
        public string Mensaje { get; set; }

        [DataMember(Name = "data", EmitDefaultValue = false)]
        public string Data { get; set; }

        public RespuestaGeneral()
        {
        }

        public RespuestaGeneral(bool exito, string mensaje)
        {
            this.Exito = exito;
            this.Mensaje = mensaje;
        }

        public RespuestaGeneral(bool exito, string mensaje, string data)
        {
            this.Exito = exito;
            this.Mensaje = mensaje;
            this.Data = data;
        }

        public static RespuestaGeneral Ok(string mensaje)
        {
            return new RespuestaGeneral(true, mensaje);
        }

        public static RespuestaGeneral Ok(string mensaje, string data)
        {
            return new RespuestaGeneral(true, mensaje, data);
        }

        public static RespuestaGeneral Error(string mensaje)
        {
            return new RespuestaGeneral(false, mensaje);
        }
    }
}
