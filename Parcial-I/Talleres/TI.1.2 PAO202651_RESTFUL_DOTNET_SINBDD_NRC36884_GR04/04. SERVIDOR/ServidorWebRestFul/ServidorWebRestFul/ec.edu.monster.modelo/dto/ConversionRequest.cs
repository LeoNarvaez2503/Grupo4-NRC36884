using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class ConversionRequest
    {
        [DataMember(Name = "valor")]
        public double Valor { get; set; }

        [DataMember(Name = "unidadInicial")]
        public string UnidadInicial { get; set; }

        [DataMember(Name = "unidadFinal")]
        public string UnidadFinal { get; set; }

        public ConversionRequest(double valor, string unidadInicial, string unidadFinal)
        {
            this.Valor = valor;
            this.UnidadInicial = unidadInicial;
            this.UnidadFinal = unidadFinal;
        }
    }
}
