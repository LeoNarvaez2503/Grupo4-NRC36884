using System;
using System.ServiceModel;
using ServidorDotNet_Grupo4.modelos.enums;
using ServidorDotNet_Grupo4.modelos.mapeadores;
using ServidorDotNet_Grupo4.modelos.seguridad;
using ServidorDotNet_Grupo4.modelos.utilidades;
using ServidorDotNet_Grupo4.servicios;

namespace ServidorDotNet_Grupo4.controladores
{
    public class ConversorControlador
    {
        private readonly ServicioConversor<UnidadLongitud> servicioLongitud;
        private readonly ServicioConversor<UnidadMasa> servicioMasa;
        private readonly ServicioConversor<UnidadTemperatura> servicioTemperatura;

        public ConversorControlador()
        {
            servicioLongitud = new ServicioConversor<UnidadLongitud>(new ConversorLongitud());
            servicioMasa = new ServicioConversor<UnidadMasa>(new ConversorMasa());
            servicioTemperatura = new ServicioConversor<UnidadTemperatura>(new ConversorTemperatura());
        }

        public string Login(string usuario, string password)
        {
            try
            {
                if (AdministradorCredenciales.ValidarUsuario(usuario, password))
                {
                    return AdministradorToken.GenerarToken();
                }
                
                return null;
            }
            catch (Exception ex)
            {
                throw new FaultException("Error en el inicio de sesión: " + ex.Message);
            }
        }

        public string CambiarPassword(string usuario, string passwordActual, string passwordNueva)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(usuario))
                {
                    AdministradorCredenciales.CambiarContrasenia(passwordActual, passwordNueva);
                }
                else
                {
                    AdministradorCredenciales.CambiarContrasenia(usuario, passwordActual, passwordNueva);
                }
                return "Contraseña cambiada exitosamente";
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadLongitud origen = UnidadMapper.ToLongitud(unidadInicial);
                UnidadLongitud destino = UnidadMapper.ToLongitud(unidadFinal);
                return servicioLongitud.Convertir(valor, origen, destino);
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadMasa origen = UnidadMapper.ToMasa(unidadInicial);
                UnidadMasa destino = UnidadMapper.ToMasa(unidadFinal);
                return servicioMasa.Convertir(valor, origen, destino);
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                UnidadTemperatura origen = UnidadMapper.ToTemperatura(unidadInicial);
                UnidadTemperatura destino = UnidadMapper.ToTemperatura(unidadFinal);
                return servicioTemperatura.Convertir(valor, origen, destino);
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }
    }
}
