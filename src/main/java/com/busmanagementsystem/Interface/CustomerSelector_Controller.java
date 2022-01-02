package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Database.Pojos.Customer;
import com.busmanagementsystem.Database.Services.CustomerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;

public class CustomerSelector_Controller implements Initializable {
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField gender;
    @FXML
    public TextField phoneNumber;
    @FXML
    public ComboBox<Customer> comboBoxCustomer;
    @FXML
    public RadioButton radioButtonChoose;
    @FXML
    public RadioButton radioButtonAdd;
    @FXML
    public AnchorPane infoArea;
    @FXML
    private ToggleGroup option;
    private CustomerService customerService = new CustomerService();
    private Customer customer;

    public void onActionCancel(ActionEvent event) {
        ((Stage) this.infoArea.getScene().getWindow()).close();
    }

    private boolean validate() {
        if (firstName.getText().isBlank() || lastName.getText().isBlank()
                || phoneNumber.getText().isBlank() || gender.getText().isBlank())
            return false;
        return true;
    }

    public void setCustomerCapturer(Customer capturer) {
        customer = capturer;
    }

    private boolean addCustomer(Customer c) {
        if (customerService.addNewCustomer(c) != null) {
            customer.setValues(c);
            System.out.println("outside: " + customer);
            return true;
        }
        return false;
    }

    public void onActionSelect(ActionEvent event) {
        RadioButton radioButton = (RadioButton) option.getSelectedToggle();

        if (radioButton == radioButtonChoose) {
            if (comboBoxCustomer.getSelectionModel().getSelectedItem() != null) {
                customer.setValues(comboBoxCustomer.getSelectionModel().getSelectedItem());
                onActionCancel(null);
            }
        } else if (radioButton == radioButtonAdd) {
            if (validate()) {
                if (addCustomer(extractIntoCustomer()))
                    onActionCancel(null);
                else
                    prepareNotification("Customer Service", "Failed to add new Customer\nPLease check again.").showWarning();
            } else
                prepareNotification("Customer Service", "Please make sure to fill all info.").showWarning();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerService.getAllCustomers(comboBoxCustomer);
        comboBoxCustomer.setDisable(true);
        infoArea.setDisable(false);
    }

    public void onActionRadioButton(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();

        if (radioButton == radioButtonChoose) {
            comboBoxCustomer.setDisable(false);
            infoArea.setDisable(true);
        } else if (radioButton == radioButtonAdd) {
            comboBoxCustomer.setDisable(true);
            infoArea.setDisable(false);
        }
    }


    private Customer extractIntoCustomer() {
        Customer c = new Customer();
        c.setLastName(lastName.getText());
        c.setFirstName(firstName.getText());
        c.setGender(gender.getText());
        c.setPhoneNumber(phoneNumber.getText());
        return c;
    }
}
