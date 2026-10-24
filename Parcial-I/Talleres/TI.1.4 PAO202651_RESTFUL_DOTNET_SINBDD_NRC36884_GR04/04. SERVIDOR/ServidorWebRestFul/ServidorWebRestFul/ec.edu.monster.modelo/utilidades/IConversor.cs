namespace ec.edu.monster.modelo.utilidades
{
    /// <summary>
    /// Interfaz genérica para la estrategia de conversión de unidades.
    /// </summary>
    /// <typeparam name="T">Tipo de enumeración de la unidad.</typeparam>
    public interface IConversor<T>
    {
        double Convertir(double valor, T unidadInicial, T unidadFinal);
    }
}
