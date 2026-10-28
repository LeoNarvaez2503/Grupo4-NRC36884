using System;

namespace ec.edu.monster.modelo
{
    /// <summary>
    /// Punto unico de configuracion del servidor SOAP.
    /// Cambia solo Host cuando cambie la IP (o define la variable de entorno SERVIDOR_HOST).
    /// </summary>
    public static class ConfiguracionServidor
    {
        public const string Host = "10.234.96.150";
        public const string Puerto = "55981";
        public const string Ruta = "ServicioWeb.svc";

        public static string HostActual =>
            Environment.GetEnvironmentVariable("SERVIDOR_HOST") is { Length: > 0 } h ? h : Host;

        /// <summary>Ej.: http://10.234.96.150:55981/ServicioWeb.svc</summary>
        public static string UrlServicio => $"http://{HostActual}:{Puerto}/{Ruta}";
    }
}
