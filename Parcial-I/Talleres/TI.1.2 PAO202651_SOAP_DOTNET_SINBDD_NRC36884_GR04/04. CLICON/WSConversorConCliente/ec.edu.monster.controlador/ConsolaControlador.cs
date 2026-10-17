using System;
using ec.edu.monster.vista;

namespace ec.edu.monster.controlador
{
    public class ConsolaControlador
    {
        private readonly ConsolaView vista = new ConsolaView();
        private readonly AppControlador appControlador = new AppControlador();

        public void Iniciar()
        {
            vista.MostrarBanner();

            if (!RealizarLogin())
            {
                vista.MostrarMensaje("Acceso cancelado. Finalizando programa.");
                return;
            }

            BucleMenuPrincipal();
        }

        private bool RealizarLogin()
        {
            Console.WriteLine("\n--- INICIO DE SESIÓN ---");
            Console.WriteLine("(Credenciales oficiales: MONSTER / MONSTER9)");

            while (true)
            {
                string usuario = vista.LeerTexto("Usuario");
                string contrasenia = vista.LeerContrasenia("Contraseña");

                if (string.IsNullOrEmpty(usuario) || string.IsNullOrEmpty(contrasenia))
                {
                    vista.MostrarMensaje("Por favor complete todos los campos.");
                    continue;
                }

                try
                {
                    string token = appControlador.Login(usuario, contrasenia);
                    if (!string.IsNullOrWhiteSpace(token))
                    {
                        vista.MostrarMensaje($"¡Bienvenido {usuario}! Sesión iniciada correctamente.");
                        return true;
                    }
                    else
                    {
                        vista.MostrarError("Usuario o contraseña incorrectos. Verifica tus credenciales e intenta nuevamente.");
                    }
                }
                catch (System.ServiceModel.FaultException)
                {
                    vista.MostrarError("Usuario o contraseña incorrectos. Verifica tus credenciales e intenta nuevamente.");
                }
                catch (System.ServiceModel.EndpointNotFoundException)
                {
                    vista.MostrarError("No se pudo conectar con el servidor web SOAP. Por favor, asegúrate de que el servidor esté iniciado.");
                }
                catch (TimeoutException)
                {
                    vista.MostrarError("El servidor tardó demasiado tiempo en responder. Verifica tu conexión e intenta de nuevo.");
                }
                catch (Exception ex)
                {
                    if (ex.ToString().Contains("denegó expresamente") ||
                        ex.ToString().Contains("SocketException") ||
                        ex.ToString().Contains("ConnectFailure") ||
                        ex.ToString().Contains("No connection could be made"))
                    {
                        vista.MostrarError("No se pudo conectar con el servidor web SOAP. Por favor, asegúrate de que el servidor esté iniciado.");
                    }
                    else
                    {
                        vista.MostrarError("Ocurrió un error al intentar iniciar sesión: " + ex.Message);
                    }
                }

                string reintentar = vista.LeerTexto("¿Desea reintentar? (s/n)");
                if (!reintentar.Equals("s", StringComparison.OrdinalIgnoreCase))
                {
                    return false;
                }
            }
        }

        private void BucleMenuPrincipal()
        {
            bool continuar = true;

            while (continuar)
            {
                vista.MostrarMenuPrincipal();
                int opcion = vista.LeerEntero("Seleccione una opción", 1, 4);

                switch (opcion)
                {
                    case 1:
                        ProcesarLongitud();
                        break;
                    case 2:
                        ProcesarMasa();
                        break;
                    case 3:
                        ProcesarTemperatura();
                        break;
                    case 4:
                        continuar = false;
                        vista.MostrarMensaje("¡Gracias por utilizar el WS Conversor! Hasta luego.");
                        break;
                }
            }
        }

        private void ProcesarLongitud()
        {
            try
            {
                Console.WriteLine("\n>>> CONVERSOR DE LONGITUDES <<<");
                string unidadInicial = vista.SeleccionarUnidadLongitud("Inicial");
                double valor = vista.LeerDouble("Ingrese el valor a convertir");
                string unidadFinal = vista.SeleccionarUnidadLongitud("Final");

                double resultado = appControlador.ConvertirLongitud(valor, unidadInicial, unidadFinal);
                vista.MostrarResultado(resultado);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                vista.MostrarError("Se perdió la conexión con el servidor web SOAP. Verifica que el servicio siga activo.");
            }
            catch (Exception ex)
            {
                vista.MostrarError("Error en la conversión: " + ex.Message);
            }
        }

        private void ProcesarMasa()
        {
            try
            {
                Console.WriteLine("\n>>> CONVERSOR DE MASA <<<");
                string unidadInicial = vista.SeleccionarUnidadMasa("Inicial");
                double valor = vista.LeerDouble("Ingrese el valor a convertir");
                string unidadFinal = vista.SeleccionarUnidadMasa("Final");

                double resultado = appControlador.ConvertirMasa(valor, unidadInicial, unidadFinal);
                vista.MostrarResultado(resultado);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                vista.MostrarError("Se perdió la conexión con el servidor web SOAP. Verifica que el servicio siga activo.");
            }
            catch (Exception ex)
            {
                vista.MostrarError("Error en la conversión: " + ex.Message);
            }
        }

        private void ProcesarTemperatura()
        {
            try
            {
                Console.WriteLine("\n>>> CONVERSOR DE TEMPERATURA <<<");
                string unidadInicial = vista.SeleccionarUnidadTemperatura("Inicial");
                double valor = vista.LeerDouble("Ingrese el valor a convertir");
                string unidadFinal = vista.SeleccionarUnidadTemperatura("Final");

                double resultado = appControlador.ConvertirTemperatura(valor, unidadInicial, unidadFinal);
                vista.MostrarResultado(resultado);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                vista.MostrarError("Se perdió la conexión con el servidor web SOAP. Verifica que el servicio siga activo.");
            }
            catch (Exception ex)
            {
                vista.MostrarError("Error en la conversión: " + ex.Message);
            }
        }
    }
}
