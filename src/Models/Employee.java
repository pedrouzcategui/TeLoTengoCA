/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class Employee {
    private int id;
    private String name;
    private char gender;
    private char role;
    private String username;
    private String password;

    // Los constructores por omision NO llevan parametros/argumentos en su definicion
    public Employee() {
        this.setId(0);
        this.setName("");
        this.setGender('M');
        this.setUsername("");
        this.setPassword("");
        this.setRole('V');
    }

    public Employee(int id, String name, char gender, char role, String username, String password) {
        this.setId(id);
        this.setName(name);
        this.setGender(gender);
        this.setUsername(username);
        this.setPassword(password);
        this.setRole(role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getRole(){
        return role;
    }

    public void setRole(char role){
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
    public boolean checkPassword(String password) {
        return this.getPassword().equals(password);
    }
}
