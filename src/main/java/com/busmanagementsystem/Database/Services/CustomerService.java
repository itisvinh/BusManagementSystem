package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    public void getAllCustomers(ComboBox comboBox) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select * from Customers");

            ObservableList<Customer> customers = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getString("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setPhoneNumber(resultSet.getString("PhoneNumber"));
                customer.setGender(resultSet.getString("Gender"));
                customers.add(customer);
            }
            comboBox.setItems(customers);

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    public String createNextCustomerID() {
        Statement statement = null;
        ResultSet resultSet = null;
        String customerID = "";

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select CustomerID from Customers");

            var customerIDs_str = new ArrayList<String>();

            while (resultSet.next())
                customerIDs_str.add(resultSet.getString(1));

            if (customerIDs_str.size() == 0)
                return "C01";
            else {

                var customerIDs = customerIDs_str.stream()
                        .map(i -> Integer.valueOf(i.substring(1)))
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println(customerIDs);

                int number = customerIDs.get(customerIDs.size() - 1) + 1;
                customerID += "C";
                customerID += (number < 10) ? "0" + String.valueOf(number) : String.valueOf(number);
            }

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return customerID;
    }

    public Customer addNewCustomer(Customer customer) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;
        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("insert into Customers values (?, ?, ?, ?, ?)");

            String customerID = createNextCustomerID();
            statement.setString(1, customerID);
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getFirstName());
            statement.setString(4, customer.getGender());
            statement.setString(5, customer.getPhoneNumber());

            affected_rows = statement.executeUpdate();

            if (affected_rows > 0) {
                customer.setCustomerID(customerID);
                return customer;
            }
            System.out.println("inside services: " + customer);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return customer;
    }

    public Customer getCustomerOf(String customerID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select * from Customers where CustomerID = ?");
            statement.setString(1, customerID);
            resultSet = statement.executeQuery();

            Customer customer = null;
            if (resultSet.next()) {
                customer = new Customer();
                customer.setCustomerID(resultSet.getString("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName((resultSet.getString("LastName")));
                customer.setPhoneNumber(resultSet.getString("PhoneNumber"));
                customer.setGender(resultSet.getString("Gender"));
            }
            if (customer != null)
                return customer;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return null;
    }
}
