/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

// Esta interfaz fue creada para implementarse en mis funciones convertToCSV(), lo que hacen
// es convertir un objeto en una linea csv
public interface RowConverter<T> {
    String convertToCSV(T obj);
}