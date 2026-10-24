using System;
using System.Collections.Generic;
using System.Globalization;
using ec.edu.monster.modelo.dto;
using ec.edu.monster.modelo.enums;
using ec.edu.monster.modelo.mapeadores;
using ec.edu.monster.modelo.utilidades;
using ec.edu.monster.servicio;

namespace ec.edu.monster.controlador
{
    /// <summary>
    /// Controlador MVC para la lógica de conversión de magnitudes físicas (Longitud, Masa, Temperatura).
    /// Recibe las solicitudes de los clientes y coordina con los servicios del modelo.
    /// </summary>
    public class ConversorControlador
    {
        private readonly ServicioConversor<UnidadLongitud> servicioLongitud;
        private readonly ServicioConversor<UnidadMasa> servicioMasa;
        private readonly ServicioConversor<UnidadTemperatura> servicioTemperatura;

        public ConversorControlador()
        {
            this.servicioLongitud = new ServicioConversor<UnidadLongitud>(new ConversorLongitud());
            this.servicioMasa = new ServicioConversor<UnidadMasa>(new ConversorMasa());
            this.servicioTemperatura = new ServicioConversor<UnidadTemperatura>(new ConversorTemperatura());
        }

        public ConversionResponse ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadLongitud origen = UnidadMapper.ToLongitud(unidadInicial);
                UnidadLongitud destino = UnidadMapper.ToLongitud(unidadFinal);
                double resultado = servicioLongitud.Convertir(valor, origen, destino);

                return new ConversionResponse(
                    true,
                    "LONGITUD",
                    valor,
                    origen.ToString(),
                    resultado,
                    destino.ToString(),
                    "Conversión de longitud realizada exitosamente"
                );
            }
            catch (Exception ex)
            {
                return new ConversionResponse(
                    false,
                    "LONGITUD",
                    valor,
                    unidadInicial,
                    0,
                    unidadFinal,
                    ex.Message
                );
            }
        }

        public ConversionResponse ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadMasa origen = UnidadMapper.ToMasa(unidadInicial);
                UnidadMasa destino = UnidadMapper.ToMasa(unidadFinal);
                double resultado = servicioMasa.Convertir(valor, origen, destino);

                return new ConversionResponse(
                    true,
                    "MASA",
                    valor,
                    origen.ToString(),
                    resultado,
                    destino.ToString(),
                    "Conversión de masa realizada exitosamente"
                );
            }
            catch (Exception ex)
            {
                return new ConversionResponse(
                    false,
                    "MASA",
                    valor,
                    unidadInicial,
                    0,
                    unidadFinal,
                    ex.Message
                );
            }
        }

        public ConversionResponse ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadTemperatura origen = UnidadMapper.ToTemperatura(unidadInicial);
                UnidadTemperatura destino = UnidadMapper.ToTemperatura(unidadFinal);
                double resultado = servicioTemperatura.Convertir(valor, origen, destino);

                return new ConversionResponse(
                    true,
                    "TEMPERATURA",
                    valor,
                    origen.ToString(),
                    resultado,
                    destino.ToString(),
                    "Conversión de temperatura realizada exitosamente"
                );
            }
            catch (Exception ex)
            {
                return new ConversionResponse(
                    false,
                    "TEMPERATURA",
                    valor,
                    unidadInicial,
                    0,
                    unidadFinal,
                    ex.Message
                );
            }
        }

        public CatalogoUnidadesResponse ObtenerCatalogoUnidades()
        {
            var respuesta = new CatalogoUnidadesResponse
            {
                Exito = true,
                Mensaje = "Catálogo de unidades de medida disponibles"
            };

            foreach (var item in Enum.GetNames(typeof(UnidadLongitud)))
            {
                respuesta.Longitud.Add(item);
            }

            foreach (var item in Enum.GetNames(typeof(UnidadMasa)))
            {
                respuesta.Masa.Add(item);
            }

            foreach (var item in Enum.GetNames(typeof(UnidadTemperatura)))
            {
                respuesta.Temperatura.Add(item);
            }

            return respuesta;
        }

        public static double ParsearNumero(string valorStr)
        {
            if (string.IsNullOrWhiteSpace(valorStr))
            {
                return 0.0;
            }

            string normalizado = valorStr.Trim().Replace(',', '.');
            if (double.TryParse(normalizado, NumberStyles.Any, CultureInfo.InvariantCulture, out double res))
            {
                return res;
            }

            throw new ArgumentException("El valor numérico '" + valorStr + "' no tiene un formato válido.");
        }
    }
}
