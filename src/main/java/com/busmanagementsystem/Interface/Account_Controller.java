package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Employee;
import com.busmanagementsystem.Database.Services.EmployeeService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;

public class Account_Controller implements Initializable {
    @FXML
    public Label mailAddress;
    @FXML
    public Label phoneNumber;
    @FXML
    public Label role;
    @FXML
    private Label fullName;
    @FXML
    private Label birthDate;
    private EmployeeService employeeService = new EmployeeService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Communicator.currentEmployeeID !=  null) {
            Employee employee = employeeService.getEmployeeOf(Communicator.currentEmployeeID);
            fullName.setText(employee.getFirstName() + " " + employee.getLastName());
            birthDate.setText(employee.getBirthDate().toString());
            role.setText( (Communicator.startedAsAdmin) ? "Administrator" : "Employee");
            phoneNumber.setText(employee.getPhoneNumber());
            mailAddress.setText(employee.getEmail());

        } else
            prepareNotification("Employee service", "Cannot load current employee's info").showWarning();
    }
}
