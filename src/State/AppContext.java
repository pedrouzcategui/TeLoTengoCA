/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package State;

import Models.Employee;

/**
 *
 * @author pedro
 */


// Esta clase aplica el patron de diseño Singleton para persistir la "sesion" del usuario en la app
public class AppContext {
    private static AppContext instance;
    
    private AppContext(){}
    
    public static synchronized AppContext getInstance(){
        if(instance == null){
            instance = new AppContext();
        }
        return instance;
    }
    
    private Employee employee;
    
    public Employee getCurrentEmployee(){
        return employee;
    }
    
    public void setCurrentEmployee(Employee employee){
        this.employee = employee;
    }
}
