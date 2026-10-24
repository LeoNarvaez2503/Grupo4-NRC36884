using System;
using System.Globalization;
using ec.edu.monster.modelo;

namespace ec.edu.monster.vista
{
    public class ConsolaView
    {
        public void MostrarBanner()
        {
            Console.WriteLine(@"   /\_______/\");
            Console.WriteLine(@"  /   o   o   \     ╔═══════════════════════════════════════╗");
            Console.WriteLine(@" (   == ^ ==   )    ║   MONSTERS INC. - CONVERSOR SOAP      ║");
            Console.WriteLine(@"  )   \___/   (     ║                                       ║");
            Console.WriteLine(@" (  (_______)  )    ║   Personaje: James P. Sullivan        ║");
            Console.WriteLine(@"  \___________/     ╚═══════════════════════════════════════╝");
            Console.WriteLine();
        }

        public string LeerTexto(string etiqueta)
        {
            Console.Write($"{etiqueta}: ");
            return Console.ReadLine()?.Trim() ?? string.Empty;
        }

        public string LeerContrasenia(string etiqueta)
        {
            Console.Write($"{etiqueta}: ");
            if (Console.IsInputRedirected)
            {
                return Console.ReadLine()?.Trim() ?? string.Empty;
            }

            string password = string.Empty;
            while (true)
            {
                var key = Console.ReadKey(intercept: true);
                if (key.Key == ConsoleKey.Enter)
                {
                    Console.WriteLine();
                    break;
                }
                if (key.Key == ConsoleKey.Backspace)
                {
                    if (password.Length > 0)
                    {
                        password = password.Substring(0, password.Length - 1);
                        Console.Write("\b \b");
                    }
                }
                else if (!char.IsControl(key.KeyChar))
                {
                    password += key.KeyChar;
                    Console.Write("*");
                }
            }
            return password;
        }

        public double LeerDouble(string etiqueta)
        {
            while (true)
            {
                Console.Write($"{etiqueta}: ");
                string? input = Console.ReadLine()?.Trim();
                if (!string.IsNullOrEmpty(input))
                {
                    string normalizado = input.Replace(',', '.');
                    if (double.TryParse(normalizado, NumberStyles.Float, CultureInfo.InvariantCulture, out double valor))
                    {
                        return valor;
                    }
                }
                Console.WriteLine(" [!] Por favor ingrese un número válido.");
            }
        }

        public int LeerEntero(string etiqueta, int min, int max)
        {
            while (true)
            {
                Console.Write($"{etiqueta} [{min}-{max}]: ");
                string? input = Console.ReadLine()?.Trim();
                if (int.TryParse(input, out int valor) && valor >= min && valor <= max)
                {
                    return valor;
                }
                Console.WriteLine($" [!] La opción debe estar entre {min} y {max}.");
            }
        }

        public void MostrarMenuPrincipal()
        {
            Console.WriteLine();
            Console.WriteLine("--- MENÚ PRINCIPAL ---");
            Console.WriteLine("1. Conversor de Longitudes");
            Console.WriteLine("2. Conversor de Masa");
            Console.WriteLine("3. Conversor de Temperatura");
            Console.WriteLine("4. Salir");
        }

        public string SeleccionarUnidadLongitud(string momento)
        {
            Console.WriteLine($"\nSeleccione Unidad {momento}:");
            var valores = Enum.GetValues<UnidadLongitud>();
            for (int i = 0; i < valores.Length; i++)
            {
                Console.WriteLine($"{i + 1}. {valores[i]} ({valores[i].GetEtiqueta()})");
            }
            int seleccion = LeerEntero("Opción", 1, valores.Length);
            return valores[seleccion - 1].ToString();
        }

        public string SeleccionarUnidadMasa(string momento)
        {
            Console.WriteLine($"\nSeleccione Unidad {momento}:");
            var valores = Enum.GetValues<UnidadMasa>();
            for (int i = 0; i < valores.Length; i++)
            {
                Console.WriteLine($"{i + 1}. {valores[i]} ({valores[i].GetEtiqueta()})");
            }
            int seleccion = LeerEntero("Opción", 1, valores.Length);
            return valores[seleccion - 1].ToString();
        }

        public string SeleccionarUnidadTemperatura(string momento)
        {
            Console.WriteLine($"\nSeleccione Unidad {momento}:");
            var valores = Enum.GetValues<UnidadTemperatura>();
            for (int i = 0; i < valores.Length; i++)
            {
                Console.WriteLine($"{i + 1}. {valores[i]} ({valores[i].GetEtiqueta()})");
            }
            int seleccion = LeerEntero("Opción", 1, valores.Length);
            return valores[seleccion - 1].ToString();
        }

        public void MostrarResultado(double resultado)
        {
            Console.WriteLine();
            Console.WriteLine("==================================================");
            Console.WriteLine($"Resultado: {resultado.ToString(CultureInfo.InvariantCulture)}");
            Console.WriteLine("==================================================");
        }

        public void MostrarMensaje(string mensaje)
        {
            Console.WriteLine($" > {mensaje}");
        }

        public void MostrarError(string error)
        {
            var colorPrevio = Console.ForegroundColor;
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine($" [X] Error: {error}");
            Console.ForegroundColor = colorPrevio;
        }
    }
}
