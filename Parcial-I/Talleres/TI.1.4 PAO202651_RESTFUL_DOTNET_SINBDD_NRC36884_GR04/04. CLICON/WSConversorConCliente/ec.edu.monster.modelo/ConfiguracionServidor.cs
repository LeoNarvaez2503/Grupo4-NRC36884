using System;

namespace ec.edu.monster.modelo
{
    /// <summary>
    /// Punto unico de configuracion del servidor REST.
    /// Cambia solo Host cuando cambie la IP (o define la variable de entorno SERVIDOR_HOST).
    /// </summary>
    public static class ConfiguracionServidor
    {
        public const string Host = "localhost";
        public const string Puerto = "63281";
        public const string Ruta = "ServicioWebRest.svc";

        public static string HostActual =>
            Environment.GetEnvironmentVariable("SERVIDOR_HOST") is { Length: > 0 } h ? h : Host;

        /// <summary>Ej.: http://localhost:63281/ServicioWebRest.svc</summary>
        public static string UrlServicio => $"http://{HostActual}:{Puerto}/{Ruta}";
    }
}
