using ApiPaisesMVC_SQLServer.Models;
using Newtonsoft.Json.Linq;

namespace ApiPaisesMVC_SQLServer.Services;

public interface IUniversidadApiService
{
    Task<List<Universidad>> ObtenerUniversidadesPorPaisAsync(string pais);
}

public class UniversidadApiService : IUniversidadApiService
{
    private readonly HttpClient _httpClient;

    public UniversidadApiService(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.Timeout = TimeSpan.FromSeconds(15);
    }

    public async Task<List<Universidad>> ObtenerUniversidadesPorPaisAsync(string pais)
    {
        var lista = new List<Universidad>();
        string queryPais = string.IsNullOrWhiteSpace(pais) ? "Ecuador" : pais.Trim();
        string url = $"http://universities.hipolabs.com/search?country={Uri.EscapeDataString(queryPais)}";

        try
        {
            string json = await _httpClient.GetStringAsync(url);
            JArray array = JArray.Parse(json);
            foreach (JToken item in array)
            {
                if (item is not JObject obj)
                {
                    continue;
                }
                string nombre = obj["name"]?.ToString() ?? string.Empty;
                if (string.IsNullOrWhiteSpace(nombre))
                {
                    continue;
                }
                string dominio = obj["domains"] is JArray dArr && dArr.Count > 0
                    ? dArr[0]?.ToString() ?? "N/D"
                    : "N/D";
                string web = obj["web_pages"] is JArray wArr && wArr.Count > 0
                    ? wArr[0]?.ToString() ?? "N/D"
                    : "N/D";
                string country = obj["country"]?.ToString() ?? queryPais;

                lista.Add(new Universidad
                {
                    Nombre = nombre.Length <= 200 ? nombre : nombre[..200],
                    Dominio = dominio.Length <= 150 ? dominio : dominio[..150],
                    SitioWeb = web.Length <= 300 ? web : web[..300],
                    Pais = country.Length <= 100 ? country : country[..100]
                });
            }
        }
        catch
        {
            return lista;
        }

        return lista;
    }
}
