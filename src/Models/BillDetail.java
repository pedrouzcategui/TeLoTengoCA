/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class BillDetail {
    private int id;
    private int billId;
    private int itemId;
    private int quantitySold;

    public BillDetail() {
        this.setId(0);
        this.setBillId(0);
        this.setItemId(0);
        this.setQuantitySold(0);
    }
    public BillDetail(int id, int billId, int itemId, int quantitySold) {
        this.setId(id);
        this.setBillId(billId);
        this.setItemId(itemId);
        this.setQuantitySold(quantitySold);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}