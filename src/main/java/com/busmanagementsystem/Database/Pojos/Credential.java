package com.busmanagementsystem.Database.Pojos;

public class Credential {
    private String username;
    private String password;
    private String employeeId;

    public Credential() {

    }
    public Credential(String username, String password, String employeeId) {
        this.username = username;
        this.password = password;
        this.employeeId = employeeId;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
