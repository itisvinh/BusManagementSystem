package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.Bus;
import com.busmanagementsystem.Database.Pojos.Employee;
import com.busmanagementsystem.Interface.Emp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeeService {
    public void loadAllDrivers(ComboBox comboBox) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select * from Employees where EmployeeID like 'DRV%'");

            ObservableList<Employee> employees = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Employee emp = new Employee();
                emp.setEmployeeID(resultSet.getString("employeeID"));
                emp.setFirstName(resultSet.getString("firstName"));
                emp.setLastName(resultSet.getString("lastName"));

                employees.add(emp);
            }

            comboBox.setItems(employees);

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    public Employee getEmployeeOf(String employeeID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select * from Employees where EmployeeID = ?");
            statement.setString(1, employeeID);

            resultSet = statement.executeQuery();

            Employee employee = null;
            if (resultSet.next()) {
                employee = new Employee();
                employee.setEmployeeID(resultSet.getString("EmployeeID"));
                employee.setFirstName(resultSet.getString("FirstName"));
                employee.setLastName(resultSet.getString("LastName"));
                employee.setEmail(resultSet.getString("Email"));
                employee.setPhoneNumber(resultSet.getString("PhoneNumber"));
                employee.setPosition(resultSet.getString("Position"));
                employee.setBirthDate(resultSet.getDate("BirthDate"));
                employee.setAddress(resultSet.getString("Address"));
            }

            if (employee != null)
                return employee;

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return null;
    }
}
