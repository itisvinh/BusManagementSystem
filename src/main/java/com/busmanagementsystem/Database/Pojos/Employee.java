package com.busmanagementsystem.Database.Pojos;

import java.sql.Date;
import java.time.LocalDateTime;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;

public class Employee {
    private String employeeID;
    private String lastName;
    private String firstName;
    private String position;
    private Date birthDate;
    private String phoneNumber;
    private String email;
    private String address;

    public Employee() {}

    public Employee(String employeeID, String lastName, String firstName, String position,
                    Date birthDate, String phoneNumber, String email, String address) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.position = position;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return concatAll("[ ID: ", employeeID, " ] ", " [ Name: ", firstName, " ", lastName , " ] ");
    }
}
