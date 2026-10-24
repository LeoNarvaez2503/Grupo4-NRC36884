using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class CatalogoUnidadesResponse
    {
        [DataMember(Name = "exito")]
        public bool Exito { get; set; }

        [DataMember(Name = "mensaje")]
        public string Mensaje { get; set; }

        [DataMember(Name = "longitud")]
        public List<string> Longitud { get; set; }

        [DataMember(Name = "masa")]
        public List<string> Masa { get; set; }

        [DataMember(Name = "temperatura")]
        public List<string> Temperatura { get; set; }

        public CatalogoUnidadesResponse()
        {
            this.Longitud = new List<string>();
            this.Masa = new List<string>();
            this.Temperatura = new List<string>();
        }
    }
}
