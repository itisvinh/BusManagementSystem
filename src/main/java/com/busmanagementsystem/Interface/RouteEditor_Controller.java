package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Bus;
import com.busmanagementsystem.Database.Pojos.Employee;
import com.busmanagementsystem.Database.Pojos.Schedule;
import com.busmanagementsystem.Database.Services.BusService;
import com.busmanagementsystem.Database.Services.EmployeeService;
import com.busmanagementsystem.Database.Services.RouteService;
import javafx.collections.ObservableList;
import javafx.css.converter.EffectConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.sql.Driver;
import java.sql.Time;
import java.util.PrimitiveIterator;
import java.util.ResourceBundle;

import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;

/*
Route Editor supports 3 mode:
        1) Add new schedule
        2) View mode
        3) Editor mode
** 2,3: view & editor mode will be automatically set base on the type of LogIn account (Admin or Seller)
*/
public class RouteEditor_Controller implements Initializable {
    private EditorMode editorMode;
    @FXML
    public Button cancelButton;
    @FXML
    public Button addButton;
    @FXML
    public TextField routeID;
    @FXML
    public SearchableComboBox busID;
    @FXML
    public SearchableComboBox driverID;
    @FXML
    public TextField startingLocation;
    @FXML
    public TextField destination;
    @FXML
    public TextField departureTime;
    @FXML
    public TextField price;
    @FXML
    private AnchorPane editorArea;
    @FXML
    private Button editorModeStatus;
    private BusService busService = new BusService();
    private EmployeeService employeeService = new EmployeeService();
    private RouteService routeService = new RouteService();
    private int[] affected_rows;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeID.setEditable(false);
    }

    // call this method will set the editor as view/editor mode
    public void launch(Schedule schedule) {
        editorMode = EditorMode.EDITOR;
        setContent(schedule);
        addButton.setText("EDIT");
    }

    // call this method will set the editor as ADD NEW mode
    public void launch() {
        editorMode = EditorMode.ADD_NEW;
        editorModeStatus.setText("ADDER MODE IS ON");
        addButton.setText("ADD");
        routeID.setText("Route ID will be assigned automatically");
        routeID.setFocusTraversable(false);
        // load stuff
        busService.loadAllBuses(busID);
        employeeService.loadAllDrivers(driverID);
    }

    private void setContent(Schedule schedule) {
        busService.loadAllBuses(busID);
        employeeService.loadAllDrivers(driverID);

        loadSchedule(schedule);
        // if logged in as a seller -> not allow editing
        if (!Communicator.startedAsAdmin) {
            setUneditable();
            addButton.setDisable(true);
            editorModeStatus.setText("VIEW MODE IS ON");
            return;
        }
        editorModeStatus.setText("EDITOR MODE IS ON");
    }

    // load data from the arg to the interface controls
    private void loadSchedule(Schedule schedule) {
        routeID.setText(schedule.getScheduleID());
//        busID.setPromptText(schedule.getBusID());
//        driverID.setPromptText(schedule.getDriverID());
        startingLocation.setText(schedule.getStartingLocation());
        destination.setText(schedule.getDestination());
        departureTime.setText(schedule.getDepartureTime().toString());
        price.setText(String.valueOf(schedule.getPrice()));

        for (int i = 0; i < busID.getItems().size(); i++)
            if (((Bus) busID.getItems().get(i)).getBusID().equals(schedule.getBusID())) {
                busID.getSelectionModel().select(i);
                break;
            }

        for (int i = 0; i < driverID.getItems().size(); i++)
            if ( ((Employee)driverID.getItems().get(i)).getEmployeeID().equals(schedule.getDriverID())) {
                driverID.getSelectionModel().select(i);
                break;
            }
    }

    // set all the interface controls to  be uneditable
    private void setUneditable() {
        routeID.setEditable(false);
        busID.setEditable(false);
        driverID.setEditable(false);
        startingLocation.setEditable(false);
        destination.setEditable(false);
        departureTime.setEditable(false);
        price.setEditable(false);
    }

    // extract data from the interface controls into a Schedule Object
    private Schedule extractSchedule() throws IllegalArgumentException{
        Schedule schedule = new Schedule();
        schedule.setScheduleID(routeID.getText());
        schedule.setBusID( ((Bus) busID.getSelectionModel().getSelectedItem()).getBusID());
        schedule.setDriverID( ((Employee) driverID.getSelectionModel().getSelectedItem()).getEmployeeID());
        schedule.setStartingLocation(startingLocation.getText());
        schedule.setDestination(destination.getText());
        schedule.setDepartureTime(Time.valueOf(departureTime.getText()));
        schedule.setPrice(Float.valueOf(price.getText()));
        return schedule;
    }

    public void onActionCancel(ActionEvent event) {
        ((Stage) editorModeStatus.getScene().getWindow()).close();
    }

    public void setAffectedRowsCapturer(int[] capturer) {
        this.affected_rows = capturer;
    }

    public void onActionAdd(ActionEvent event) {
        if (editorMode == EditorMode.EDITOR) {
            // update
            try {
                Schedule schedule = extractSchedule();
                affected_rows[0] = routeService.updateSchedule(schedule);

                if (affected_rows[0] > 0)
                    onActionCancel(null);
                else
                    prepareNotification("Route Service", "Failed attempting to edit").showWarning();

            } catch (Exception ex) {
                System.out.println(ex);
                prepareNotification("Route Service", "PLease fill all the fields and\nreassure the time format").showError();
            }
        } else {
            // add new
            try {
                Schedule schedule = extractSchedule();
                affected_rows[0] = routeService.addNewSchedule(schedule);

                if (affected_rows[0] > 0)
                    onActionCancel(null);
                else
                    prepareNotification("Route Service", "Failed attempting to add").showWarning();

            } catch (Exception ex) {
                System.out.println(ex);
                prepareNotification("Route Service", "PLease fill all the fields and\nreassure the time format").showError();
            }
        }
    }


}
