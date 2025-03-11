/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

// La interfaz RowMapper obliga a las clases que la implementen a tener el metodo mapRow()
// Lo que hace es tomar un arreglo de strings, que es la linea CSV partida por el character ";"
// Y luego lo que hace es devolver un objeto de tipo T.
public interface RowMapper<T> {
    T mapRow(String[] data);
}
