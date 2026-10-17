using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text.Json;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.modelo;

namespace ec.edu.monster.controlador
{
    public class ConversorController : Controller
    {
        private readonly AppControlador controlador = new AppControlador();
        private const string SessionHistorialKey = "HistorialConversiones";

        [HttpGet]
        [Route("Conversor")]
        [Route("Conversor/Index")]
        public IActionResult Index(string? magnitud)
        {
            string? token = HttpContext.Session.GetString("Token");
            string? usuario = HttpContext.Session.GetString("Usuario");

            if (string.IsNullOrEmpty(token))
            {
                return RedirectToAction("Index", "Login");
            }

            var model = new ConversorViewModel
            {
                NombreUsuario = usuario ?? "Usuario",
                TipoMagnitud = string.IsNullOrWhiteSpace(magnitud) ? "LONGITUD" : magnitud.ToUpperInvariant(),
                Historial = ObtenerHistorial()
            };

            ConfigurarUnidades(model);
            return View("~/Views/Conversor/Index.cshtml", model);
        }

        [HttpPost]
        [Route("Conversor/Convertir")]
        [ValidateAntiForgeryToken]
        public IActionResult Convertir(ConversorViewModel model)
        {
            string? token = HttpContext.Session.GetString("Token");
            string? usuario = HttpContext.Session.GetString("Usuario");

            if (string.IsNullOrEmpty(token))
            {
                return RedirectToAction("Index", "Login");
            }

            model.NombreUsuario = usuario ?? "Usuario";
            model.Historial = ObtenerHistorial();
            ConfigurarUnidades(model);

            string txtVal = model.Valor?.Trim() ?? "";
            if (string.IsNullOrEmpty(txtVal))
            {
                model.MensajeError = "Por favor ingresa un número para realizar la conversión.";
                return View("~/Views/Conversor/Index.cshtml", model);
            }

            string normalizado = txtVal.Replace(',', '.');
            if (!double.TryParse(normalizado, NumberStyles.Float, CultureInfo.InvariantCulture, out double num))
            {
                model.MensajeError = "El valor ingresado no es un número válido. Por favor escribe un número (ejemplo: 25 o 12.5).";
                return View("~/Views/Conversor/Index.cshtml", model);
            }

            try
            {
                double resultado;
                string tipo = model.TipoMagnitud.ToUpperInvariant();

                if (tipo == "MASA")
                {
                    resultado = controlador.ConvertirMasa(num, model.UnidadOrigen, model.UnidadDestino);
                }
                else if (tipo == "TEMPERATURA")
                {
                    resultado = controlador.ConvertirTemperatura(num, model.UnidadOrigen, model.UnidadDestino);
                }
                else
                {
                    resultado = controlador.ConvertirLogintud(num, model.UnidadOrigen, model.UnidadDestino);
                }

                model.Resultado = resultado.ToString(CultureInfo.InvariantCulture);

                // Agregar al historial de conversiones
                var itemHistorial = new HistorialItem
                {
                    Magnitud = tipo,
                    ValorOrigen = num,
                    UnidadOrigen = model.UnidadOrigen,
                    ValorDestino = resultado,
                    UnidadDestino = model.UnidadDestino
                };
                AgregarHistorial(itemHistorial);
                model.Historial = ObtenerHistorial();
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                model.MensajeError = "Se perdió la conexión con el servidor web SOAP. Verifica que el servicio siga activo.";
            }
            catch (Exception ex)
            {
                if (ex.ToString().Contains("denegó expresamente") ||
                    ex.ToString().Contains("SocketException") ||
                    ex.ToString().Contains("ConnectFailure") ||
                    ex.ToString().Contains("No connection could be made"))
                {
                    model.MensajeError = "No se pudo conectar con el servidor web SOAP. Verifica que el servicio esté activo.";
                }
                else
                {
                    model.MensajeError = "No se pudo realizar la conversión: " + ex.Message;
                }
            }

            return View("~/Views/Conversor/Index.cshtml", model);
        }

        [HttpPost]
        [Route("Conversor/CambiarMagnitud")]
        public IActionResult CambiarMagnitud([FromForm] string tipoMagnitud)
        {
            return RedirectToAction("Index", new { magnitud = tipoMagnitud });
        }

        [HttpPost]
        [Route("Conversor/Limpiar")]
        public IActionResult Limpiar([FromForm] string tipoMagnitud)
        {
            return RedirectToAction("Index", new { magnitud = tipoMagnitud });
        }

        private void ConfigurarUnidades(ConversorViewModel model)
        {
            switch (model.TipoMagnitud.ToUpperInvariant())
            {
                case "MASA":
                    model.UnidadesDisponibles = Enum.GetNames(typeof(UnidadMasa)).ToList();
                    model.Titulo = "Conversor de Masa";
                    break;
                case "TEMPERATURA":
                    model.UnidadesDisponibles = Enum.GetNames(typeof(UnidadTemperatura)).ToList();
                    model.Titulo = "Conversor de Temperatura";
                    break;
                case "LONGITUD":
                default:
                    model.TipoMagnitud = "LONGITUD";
                    model.UnidadesDisponibles = Enum.GetNames(typeof(UnidadLongitud)).ToList();
                    model.Titulo = "Conversor de Longitudes";
                    break;
            }

            if (string.IsNullOrEmpty(model.UnidadOrigen) || !model.UnidadesDisponibles.Contains(model.UnidadOrigen))
            {
                model.UnidadOrigen = model.UnidadesDisponibles.FirstOrDefault() ?? "";
            }
            if (string.IsNullOrEmpty(model.UnidadDestino) || !model.UnidadesDisponibles.Contains(model.UnidadDestino))
            {
                model.UnidadDestino = model.UnidadesDisponibles.Count > 1 ? model.UnidadesDisponibles[1] : model.UnidadesDisponibles.FirstOrDefault() ?? "";
            }
        }

        private List<HistorialItem> ObtenerHistorial()
        {
            string? json = HttpContext.Session.GetString(SessionHistorialKey);
            if (string.IsNullOrEmpty(json))
            {
                return new List<HistorialItem>();
            }
            try
            {
                return JsonSerializer.Deserialize<List<HistorialItem>>(json) ?? new List<HistorialItem>();
            }
            catch
            {
                return new List<HistorialItem>();
            }
        }

        private void AgregarHistorial(HistorialItem item)
        {
            var list = ObtenerHistorial();
            list.Insert(0, item);
            if (list.Count > 10)
            {
                list = list.Take(10).ToList();
            }
            HttpContext.Session.SetString(SessionHistorialKey, JsonSerializer.Serialize(list));
        }
    }
}
