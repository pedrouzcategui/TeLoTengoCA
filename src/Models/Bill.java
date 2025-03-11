/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill {
    private int id;
    private int salesmanId;
    private Date createdAt;

    public Bill() {
        this.setId(0);
        this.setSalesmanId(0);
        this.setCreatedAt(new Date());
    }
    public Bill(int id, int salesmanId, Date createdAt) {
        this.setId(id);
        this.setSalesmanId(salesmanId);
        this.setCreatedAt(createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(int salesmanId) {
        this.salesmanId = salesmanId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // Utility function
    public String getBillDateAsString(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
        return sdf.format(createdAt);
    }
}