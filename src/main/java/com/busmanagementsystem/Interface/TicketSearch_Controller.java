package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Customer;
import com.busmanagementsystem.Database.Pojos.ExtTicket;
import com.busmanagementsystem.Database.Services.CustomerService;
import com.busmanagementsystem.Database.Services.TicketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import javax.xml.stream.events.Comment;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;

public class TicketSearch_Controller implements Initializable {
    @FXML
    public SearchableComboBox<Customer> comboBoxCustomer;
    @FXML
    public TableView<ExtTicket> customerTableView;
    @FXML
    public Button cancelButton;
    @FXML
    public Button selectButton;
    private CustomerService customerService = new CustomerService();
    /*
        <CONTROLSFX V11.1.1>
        <DUE TO A BUG IN THE SEARCHABLE COMBOBOX CLASS THAT MAKES THE [ONACTION] EVENT FIRE 3 TIMES>
        --It holds the last fire time of the comboBox.
        --This will restrict every [ONACTION] event of the Searchable ComboBox
        to fire at least 0.5 sec apart.
    */
    private LocalDateTime prevComboBoxFireTime = LocalDateTime.now();
    private TicketService ticketService = new TicketService();
    private ExtTicket extTicketCapturer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerService.getAllCustomers(comboBoxCustomer);
    }

    public void setExtTicketCapturer(ExtTicket capturer) {
        this.extTicketCapturer = capturer;
    }

    /*
        <CONTROLSFX V11.1.1>
        <DUE TO A BUG IN THE SEARCHABLE COMBOBOX CLASS THAT MAKES THE [ONACTION] EVENT FIRE 3 TIMES>
        --This will restrict every [ONACTION] event of the Searchable ComboBox
        to fire at least 0.5 sec apart.
    */
    private boolean canFireNow() {
        if (prevComboBoxFireTime.plusNanos(500000000).isBefore(LocalDateTime.now())) {
            prevComboBoxFireTime = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public void onActionSelectCustomer(ActionEvent event) throws Exception{
        if (canFireNow()) {
            System.out.println("fire");
            Customer customer = comboBoxCustomer.getSelectionModel().getSelectedItem();
            System.out.println(customer);
            if (customer != null) {
                String customerID = customer.getCustomerID();
                ticketService.loadTicketsOfCustomer(customerTableView, customerID);
            }
        }
    }

    public void onActionCancel(ActionEvent event) {
        ((Stage) this.cancelButton.getScene().getWindow()).close();
    }

    public void onActionSelect(ActionEvent event) {
        if (comboBoxCustomer.getSelectionModel().getSelectedItem() != null) {
            ExtTicket ticket = customerTableView.getSelectionModel().getSelectedItem();
            System.out.println(ticket);
            if (ticket != null) {
                if (extTicketCapturer != null) {
                    extTicketCapturer.setValues(ticket);
                    onActionCancel(null);
                }
            } else
                prepareNotification("Ticket Service", "PLease select a ticket").showWarning();
        } else
            prepareNotification("Ticket Service", "Please select a customer first").showWarning();
    }


}
