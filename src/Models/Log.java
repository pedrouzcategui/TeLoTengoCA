/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

public class Log {
    private Date createdAt;
    private int userId;
    private String username;
    private String action;

    public Log(){
        this.setCreatedAt(new Date());
        this.setUserId(0);
        this.setUsername("");
        this.setAction("");
    }
    
    public Log(Date createdAt, int userId, String username, String action) {
        this.setCreatedAt(createdAt);
        this.setUserId(userId);
        this.setUsername(username);
        this.setAction(action);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedAtAsString() {
        return createdAt.toString();
    }
    
    
}
