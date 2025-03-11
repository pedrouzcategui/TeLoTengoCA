/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repositories;

import Interfaces.Repository;
import Interfaces.RowConverter;
import Interfaces.RowMapper;
import Models.Employee;
import Utils.CSVReader;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository implements Repository<Employee> {
    private static final String FILE_NAME = "EMPLEADOS.CSV";

    // Esta funcion toma cada linea y la convierte en un objeto de empleado, eso es lo que hace un mapper.
    private static final RowMapper<Employee> employeeMapper = (String[] data) -> {
        if(data.length == 6){
            int id = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            char gender = data[2].trim().charAt(0);
            char role = data[3].trim().charAt(0);
            String username = data[4].trim();
            String password = data[5].trim();
            return new Employee(id, name, gender, role, username, password);
        }
        return null;
    };

    // Esta funcion toma un empleado y lo convierte en una linea para CSV;
    private static final RowConverter<Employee> employeeConverter = (Employee e) ->
            e.getId() + ";" + e.getName() + ";" + e.getGender() + ";" + e.getRole() + ";" + e.getUsername() + ";" + e.getPassword();

    private static final CSVReader<Employee> csvReader = new CSVReader<>(FILE_NAME, employeeMapper, ";");

    /**
     * Devuelve todos los empleados del CSV
     * @return List<Employee>
     */
    
    @Override
    public List<Employee> getAll() {
        return csvReader.getAll();
    }

    /**
     * 
     * @param id
     * @return Optional<Employee>
     * 
     * Devuelve un objeto de empleado si lo consigue, sino devuelve null
     */
    
    @Override
    public Optional<Employee> findById(int id) {
        return getAll().stream()
                .filter(employee -> employee.getId() == id)
                .findFirst();
    }
    
    /**
     * 
     * @param username
     * @return Optional<Employee>
     * 
     * Devuelve el objeto usuario (si existe), o null si no lo encuentra.
     */
    
    public Optional<Employee> findByUsername(String username){
        return getAll().stream()
                .filter(employee -> employee.getUsername().equals(username))
                .findFirst();
    }
    
    /**
     * Solo para DEBUG: Imprime todos los username de los empleados
     */
    public void printAll(){
        List<Employee> employees = getAll();
        for (Employee employee : employees){
            System.out.println(employee.getUsername());
        }
    }
    
    /**
     * Añade un nuevo empleado al archivo CSV
     */
    public void add(Employee newEmployee){
        List<Employee> employees = getAll();
        employees.add(newEmployee);
        csvReader.writeToCSV(employees, employeeConverter);
    }

}
