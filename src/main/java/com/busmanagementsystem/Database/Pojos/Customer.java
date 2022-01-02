package com.busmanagementsystem.Database.Pojos;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;

public class Customer {
    private String customerID;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;

    public Customer (Customer c) {
        this(c.customerID, c.firstName, c.lastName, c.gender, c.phoneNumber);
    }

    public Customer() {
    }
    public Customer(String customerID, String firstName, String lastName, String gender, String phoneNumber) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void setValues(Customer c) {
        setValues(c.customerID, c.firstName, c.lastName, c.gender, c.phoneNumber);
    }
    public void setValues(String customerID, String firstName, String lastName, String gender, String phoneNumber) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return concatAll("[ ID: ", customerID, " ]", " [ Name: ", firstName, " ", lastName, " ]", " [ Phone: ", phoneNumber, " ]");
    }


}
