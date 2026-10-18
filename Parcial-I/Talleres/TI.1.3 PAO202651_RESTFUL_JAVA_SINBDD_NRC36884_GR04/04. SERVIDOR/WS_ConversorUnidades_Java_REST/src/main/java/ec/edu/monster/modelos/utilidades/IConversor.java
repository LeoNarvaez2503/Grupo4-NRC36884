/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ec.edu.monster.modelos.utilidades;

/**
 *
 * @author MeatPuppets
 */
public interface IConversor<T> {
    public double convertir(double valor, T unidadInicial, T unidadFinal);
    
}
