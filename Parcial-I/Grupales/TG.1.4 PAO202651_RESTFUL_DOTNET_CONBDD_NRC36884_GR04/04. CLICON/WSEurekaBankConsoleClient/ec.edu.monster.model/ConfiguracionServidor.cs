namespace ec.edu.monster.model
{
    /// <summary>
    /// Configuración de red hacia el Servidor EurekaBank.
    /// El host se lee de la configuración central del proyecto
    /// ("04. CONFIG/servidor.properties"), copiada junto al ejecutable al compilar.
    /// Puerto y ruta del servicio son fijos de este proyecto.
    /// </summary>
    public static class ConfiguracionServidor
    {
        private const int Puerto = 5140;
        private const string Ruta = "/api";

        public static string Host { get; } = LeerHost();

        public static string EndpointUrl => $"http://{Host}:{Puerto}{Ruta}";

        private static string LeerHost()
        {
            string host = "localhost";
            try
            {
                string archivo = Path.Combine(AppContext.BaseDirectory, "servidor.properties");
                if (File.Exists(archivo))
                {
                    foreach (string linea in File.ReadAllLines(archivo))
                    {
                        string l = linea.Trim();
                        if (l.Length == 0 || l.StartsWith('#')) continue;
                        int i = l.IndexOf('=');
                        if (i > 0 && l[..i].Trim() == "servidor.host" && l[(i + 1)..].Trim().Length > 0)
                            host = l[(i + 1)..].Trim();
                    }
                }
            }
            catch { }
            string? env = Environment.GetEnvironmentVariable("EUREKABANK_HOST");
            return string.IsNullOrWhiteSpace(env) ? host : env.Trim();
        }
    }
}
