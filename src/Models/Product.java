/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class Product {
    private int id;
    private String name;
    private float price;
    private int quantity;
    private int quantity_alarm;

    public Product() {
        this.setId(0);
        this.setName("");
        this.setPrice(0);
        this.setQuantity(0);
        this.setQuantityAlarm(0);
    }
    public Product(int id, String name, float price, int quantity, int quantity_alarm) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setQuantityAlarm(quantity_alarm);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityAlarm() {
        return quantity_alarm;
    }

    public void setQuantityAlarm(int quantity_alarm) {
        this.quantity_alarm = quantity_alarm;
    }
}