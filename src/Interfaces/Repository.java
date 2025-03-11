/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import java.util.List;
import java.util.Optional;

// Esta interfaz es un "contrato" que debe cumplir mis otros repositorios, ya que todos de manera obligatoria deben tener los metodos getAll() y findById(), el genérico representa el tipo que le inyecto.
public interface Repository<T> {
    List<T> getAll();
    Optional<T> findById(int id);
}
