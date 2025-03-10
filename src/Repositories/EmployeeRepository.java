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

    @Override
    public List<Employee> getAll() {
        return csvReader.getAll();
    }

    @Override
    public Optional<Employee> findById(int id) {
        return getAll().stream()
                .filter(employee -> employee.getId() == id)
                .findFirst();
    }
    
    public Optional<Employee> findByUsername(String username){
        return getAll().stream()
                .filter(employee -> employee.getUsername().equals(username))
                .findFirst();
    }
    
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

    /**
     * Edita un empleado del archivo CSV y lo guarda
     */
    public boolean update(Employee updatedEmployee) {
        List<Employee> employees = getAll(); // 1️⃣ Read all employees from CSV

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == updatedEmployee.getId()) { // 2️⃣ Find the employee by ID
                employees.set(i, updatedEmployee); // 3️⃣ Replace the old entry with the new data
                csvReader.writeToCSV(employees, employeeConverter); // 4️⃣ Write back everything to CSV
                return true; // ✅ Update was successful
            }
        }
        return false; // ❌ Employee not found
    }

    public boolean delete(int id) {
        List<Employee> employees = getAll(); // 1️⃣ Read all employees from CSV

        boolean removed = employees.removeIf(e -> e.getId() == id); // 2️⃣ Remove employee by ID

        if (removed) {
            csvReader.writeToCSV(employees, employeeConverter); // 3️⃣ Overwrite CSV with updated list
        }
        return removed; // ✅ Return true if employee was removed, false otherwise
    }

}
