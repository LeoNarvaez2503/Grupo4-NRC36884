namespace ServidorDotNet_Grupo4.modelos.utilidades
{
    /// <summary>
    /// Interfaz genérica para los conversores de unidades.
    /// </summary>
    /// <typeparam name="T">Tipo de enumeración de la unidad.</typeparam>
    public interface IConversor<T>
    {
        double Convertir(double valor, T unidadInicial, T unidadFinal);
    }
}
