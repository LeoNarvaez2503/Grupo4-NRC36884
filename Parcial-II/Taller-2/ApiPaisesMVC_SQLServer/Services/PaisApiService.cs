using ApiPaisesMVC_SQLServer.Models;
using Newtonsoft.Json.Linq;

namespace ApiPaisesMVC_SQLServer.Services;

public interface IPaisApiService
{
    string UltimaFuente { get; }
    Task<List<Pais>> ObtenerTodosLosPaisesAsync();
    Task<List<Pais>> ObtenerPaisesPorNombreAsync(string nombre);
    List<Pais> ParsearPaises(string json);
}

public class PaisApiService : IPaisApiService
{
    private const string PrimaryBase = "https://restcountries.com/v3.1";
    private const string FallbackBase = "https://www.apicountries.com";

    private readonly HttpClient _httpClient;

    public string UltimaFuente { get; private set; } = "restcountries.com";

    public PaisApiService(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.Timeout = TimeSpan.FromSeconds(15);
    }

    public async Task<List<Pais>> ObtenerTodosLosPaisesAsync()
    {
        string json = await ConsultarConRespaldoAsync(
            $"{PrimaryBase}/all?fields=name,capital,region,subregion,population,area,currencies",
            $"{FallbackBase}/countries");
        return ParsearPaises(json);
    }

    public async Task<List<Pais>> ObtenerPaisesPorNombreAsync(string nombre)
    {
        if (string.IsNullOrWhiteSpace(nombre))
        {
            return [];
        }

        string encoded = Uri.EscapeDataString(nombre.Trim());
        string json = await ConsultarConRespaldoAsync(
            $"{PrimaryBase}/name/{encoded}",
            $"{FallbackBase}/name/{encoded}");
        return ParsearPaises(json);
    }

    public List<Pais> ParsearPaises(string json)
    {
        var lista = new List<Pais>();
        if (string.IsNullOrWhiteSpace(json))
        {
            return lista;
        }

        JToken root;
        try
        {
            root = JToken.Parse(json);
        }
        catch
        {
            return lista;
        }

        if (root is JArray array)
        {
            foreach (JToken item in array)
            {
                if (item is JObject obj)
                {
                    Pais? pais = MapearObjetoPais(obj);
                    if (pais != null && !string.IsNullOrWhiteSpace(pais.Nombre))
                    {
                        lista.Add(pais);
                    }
                }
            }
        }
        else if (root is JObject singleObj)
        {
            if (singleObj["success"]?.Value<bool>() == false)
            {
                return lista;
            }
            Pais? pais = MapearObjetoPais(singleObj);
            if (pais != null && !string.IsNullOrWhiteSpace(pais.Nombre))
            {
                lista.Add(pais);
            }
        }

        return lista;
    }

    private async Task<string> ConsultarConRespaldoAsync(string primaryUrl, string fallbackUrl)
    {
        string? primaryBody = await TryGetStringAsync(primaryUrl);
        if (EsRespuestaValida(primaryBody))
        {
            UltimaFuente = "restcountries.com (v3.1)";
            return primaryBody!;
        }

        string? fallbackBody = await TryGetStringAsync(fallbackUrl);
        if (EsRespuestaValida(fallbackBody))
        {
            UltimaFuente = "apicountries.com (respaldo público)";
            return fallbackBody!;
        }

        UltimaFuente = "No disponible";
        return string.Empty;
    }

    private async Task<string?> TryGetStringAsync(string url)
    {
        try
        {
            using var req = new HttpRequestMessage(HttpMethod.Get, url);
            req.Headers.TryAddWithoutValidation("Accept", "application/json");
            req.Headers.TryAddWithoutValidation("User-Agent", "ApiPaisesMVC_SQLServer/1.0");
            using HttpResponseMessage res = await _httpClient.SendAsync(req);
            if (!res.IsSuccessStatusCode)
            {
                return null;
            }
            return await res.Content.ReadAsStringAsync();
        }
        catch
        {
            return null;
        }
    }

    private static bool EsRespuestaValida(string? body)
    {
        if (string.IsNullOrWhiteSpace(body))
        {
            return false;
        }
        string trimmed = body.Trim();
        if (trimmed.StartsWith('{') &&
            (trimmed.Contains("\"success\":false") || trimmed.Contains("\"success\": false")))
        {
            return false;
        }
        return trimmed.StartsWith('[') || trimmed.StartsWith('{');
    }

    private static Pais? MapearObjetoPais(JObject obj)
    {
        string nombre = ExtraerNombre(obj);
        if (string.IsNullOrWhiteSpace(nombre))
        {
            return null;
        }

        string capital = ExtraerCapital(obj);
        string? region = obj["region"]?.ToString();
        if (string.IsNullOrWhiteSpace(region))
        {
            region = "Sin región";
        }
        string? subRegion = obj["subregion"]?.ToString();
        if (string.IsNullOrWhiteSpace(subRegion))
        {
            subRegion = "N/D";
        }

        long poblacion = obj["population"]?.Type is JTokenType.Integer or JTokenType.Float
            ? obj["population"]!.Value<long>()
            : 0L;
        double area = obj["area"]?.Type is JTokenType.Integer or JTokenType.Float
            ? obj["area"]!.Value<double>()
            : 0.0;
        string moneda = ExtraerMoneda(obj);

        return new Pais
        {
            Nombre = Truncar(nombre, 100),
            Capital = Truncar(capital, 100),
            Region = Truncar(region, 100),
            SubRegion = Truncar(subRegion, 100),
            Poblacion = poblacion,
            Area = area,
            Moneda = Truncar(moneda, 100)
        };
    }

    private static string ExtraerNombre(JObject obj)
    {
        JToken? nameToken = obj["name"];
        if (nameToken == null || nameToken.Type == JTokenType.Null)
        {
            return string.Empty;
        }
        if (nameToken.Type == JTokenType.String)
        {
            return nameToken.ToString();
        }
        if (nameToken is JObject nameObj)
        {
            return nameObj["common"]?.ToString()
                ?? nameObj["official"]?.ToString()
                ?? string.Empty;
        }
        return string.Empty;
    }

    private static string ExtraerCapital(JObject obj)
    {
        JToken? capToken = obj["capital"];
        if (capToken == null || capToken.Type == JTokenType.Null)
        {
            return "Sin capital";
        }
        if (capToken.Type == JTokenType.String)
        {
            string val = capToken.ToString();
            return string.IsNullOrWhiteSpace(val) ? "Sin capital" : val;
        }
        if (capToken is JArray capArr && capArr.Count > 0)
        {
            string val = capArr[0]?.ToString() ?? string.Empty;
            return string.IsNullOrWhiteSpace(val) ? "Sin capital" : val;
        }
        return "Sin capital";
    }

    private static string ExtraerMoneda(JObject obj)
    {
        JToken? curToken = obj["currencies"];
        if (curToken == null || curToken.Type == JTokenType.Null)
        {
            return "N/D";
        }

        var monedas = new List<string>();
        if (curToken is JArray curArr)
        {
            foreach (JToken item in curArr)
            {
                if (item is JObject curObj)
                {
                    string nombre = curObj["name"]?.ToString() ?? string.Empty;
                    string codigo = curObj["code"]?.ToString() ?? string.Empty;
                    string texto = FormatearMoneda(nombre, codigo);
                    if (!string.IsNullOrWhiteSpace(texto))
                    {
                        monedas.Add(texto);
                    }
                }
            }
        }
        else if (curToken is JObject curMap)
        {
            foreach (JProperty prop in curMap.Properties())
            {
                if (prop.Value is JObject curObj)
                {
                    string nombre = curObj["name"]?.ToString() ?? string.Empty;
                    string texto = FormatearMoneda(nombre, prop.Name);
                    if (!string.IsNullOrWhiteSpace(texto))
                    {
                        monedas.Add(texto);
                    }
                }
            }
        }

        return monedas.Count == 0 ? "N/D" : string.Join(", ", monedas);
    }

    private static string FormatearMoneda(string nombre, string codigo)
    {
        if (!string.IsNullOrWhiteSpace(nombre) && !string.IsNullOrWhiteSpace(codigo))
        {
            return $"{nombre} ({codigo})";
        }
        return !string.IsNullOrWhiteSpace(nombre) ? nombre : codigo;
    }

    private static string Truncar(string s, int max) =>
        s.Length <= max ? s : s[..max];
}
