using System;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo.dto
{
    [DataContract]
    public class ConversionResponse
    {
        [DataMember(Name = "exito")]
        public bool Exito { get; set; }

        [DataMember(Name = "tipo")]
        public string Tipo { get; set; }

        [DataMember(Name = "valorInicial")]
        public double ValorInicial { get; set; }

        [DataMember(Name = "unidadInicial")]
        public string UnidadInicial { get; set; }

        [DataMember(Name = "valorFinal")]
        public double ValorFinal { get; set; }

        [DataMember(Name = "unidadFinal")]
        public string UnidadFinal { get; set; }

        [DataMember(Name = "mensaje")]
        public string Mensaje { get; set; }

        [DataMember(Name = "timestamp")]
        public string Timestamp { get; set; }

        public ConversionResponse()
        {
            this.Timestamp = DateTime.UtcNow.ToString("o");
        }

        public ConversionResponse(bool exito, string tipo, double valorInicial, string unidadInicial,
                                  double valorFinal, string unidadFinal, string mensaje)
        {
            this.Exito = exito;
            this.Tipo = tipo;
            this.ValorInicial = valorInicial;
            this.UnidadInicial = unidadInicial;
            this.ValorFinal = valorFinal;
            this.UnidadFinal = unidadFinal;
            this.Mensaje = mensaje;
            this.Timestamp = DateTime.UtcNow.ToString("o");
        }
    }
}
