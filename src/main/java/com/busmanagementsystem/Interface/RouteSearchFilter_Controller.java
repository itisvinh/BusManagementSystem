package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Database.Services.RouteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

//              < this scene is designed to be reusable by other scenes >
// startup option:
//  -- strict & non-strict filter search
//  -- capture return value (String)
public class RouteSearchFilter_Controller implements Initializable{
    RouteService routeService = new RouteService();
    @FXML
    private Pane backgroundPane;
    @FXML
    private Button cancel;
    @FXML
    private Button search;
    @FXML
    private SearchableComboBox comboBoxFrom;
    @FXML
    private SearchableComboBox comboBoxTo;
    @FXML
    private SearchableComboBox comboBoxTime;
    // return value by assigning the value to the passed in object reference
    private StringBuilder returnSearchQuery;
    /*
        <CONTROLSFX V11.1.1>
        <DUE TO A BUG IN THE SEARCHABLE COMBOBOX CLASS THAT MAKES THE [ONACTION] EVENT FIRE 3 TIMES>
        --It holds the last fire time of the comboBox.
        --This will restrict every [ONACTION] event of the Searchable ComboBox
        to fire at least 0.5 sec apart.
    */
    private LocalDateTime prevComboBoxFireTime;
    // if the value is true: all the 3 comboBoxes have to be selected to search
    private boolean isStrictSearch = false;

    public void setStrictSearch(boolean strictSearch) {
        isStrictSearch = strictSearch;

        if (isStrictSearch) {
            search.setDisable(true);
            comboBoxTime.setDisable(true);
        }
        initializeComboBoxValues();
    }

    public void setReturnSearchQuery(StringBuilder returnSearchQuery) {
        this.returnSearchQuery = returnSearchQuery;
    }

    public void setCancelButtonVisibility(boolean isVisible) {
        this.cancel.setVisible(isVisible);
    }

    private String extractTextFromComboBox() {
        String filter = "", temp;
        filter += (((temp = (String) comboBoxFrom.getSelectionModel().getSelectedItem()) != null) ? temp : "") + "#";
        filter += (((temp = (String) comboBoxTo.getSelectionModel().getSelectedItem()) != null) ? temp : "") + "#";
        filter += ((temp = (String) comboBoxTime.getSelectionModel().getSelectedItem()) != null) ? temp : "";
        return filter;
    }
    public void onActionCancel(ActionEvent actionEvent) {
        returnSearchQuery.append("##");
        ((Stage)this.backgroundPane.getScene().getWindow()).close();
    }

    public void onActionSearch(ActionEvent actionEvent) {
        //checking for validation
        returnSearchQuery.append(extractTextFromComboBox());
        ((Stage)this.backgroundPane.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prevComboBoxFireTime = LocalDateTime.now();
        // FXCollections.observableArrayList("value 1", "value 2", "value 3")
    }

    private void initializeComboBoxValues() {
        updateComboBoxFromValues(null, null);
        updateComboBoxToValues(null, null);

        if (!isStrictSearch)
            updateComboBoxTimeValues(null, null);
    }

    // only update the comboBox if it is not selected
    private boolean updateComboBoxFromValues(Object destination, Object departureTime) {

        if (comboBoxFrom.getSelectionModel().getSelectedIndex() < 0) {
            comboBoxFrom.getItems().clear();
            routeService.getStartingLocations(comboBoxFrom, (String) destination, (String) departureTime);
            System.out.println("updating from");
            return true;
        } else {
            return false;
        }
    }

    // only update the comboBox if it is not selected
    private boolean updateComboBoxToValues(Object startingLocation, Object departureTime) {
        if (comboBoxTo.getSelectionModel().getSelectedIndex() < 0) {
            comboBoxTo.getItems().clear();
            routeService.getDestination(comboBoxTo, (String) startingLocation, (String) departureTime);
            System.out.println("updating to");
            return true;
        } else {
            return false;
        }
    }

    // only update the comboBox if it is not selected
    private boolean updateComboBoxTimeValues(Object startingLocation, Object destination) {
        if (comboBoxTime.getSelectionModel().getSelectedIndex() < 0) {
            comboBoxTime.getItems().clear();
            routeService.getDepartureTime(comboBoxTime, (String) startingLocation, (String) destination);
            System.out.println("updating time");
            return true;
        } else {
            return false;
        }
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

    // enable comboBox Time only if both From and To comboBoxes are selected
    private boolean tryEnableComboBoxTime() {
        if (comboBoxFrom.getSelectionModel().getSelectedIndex() > -1
            && comboBoxTo.getSelectionModel().getSelectedIndex() > -1) {
            comboBoxTime.setDisable(false);
            return true;
        } else {
            return false;
        }
    }

    // only try enabling the search button if isStrictSearch is set to be True
    private boolean tryEnableSearch() {
        if (isStrictSearch && search.isDisabled()) {
            search.setDisable(false);
            return true;
        } else {
            return false;
        }
    }

    public void onActionComboBoxFrom(ActionEvent actionEvent) {
        if (canFireNow()) {
            if (isStrictSearch) {
                if (tryEnableComboBoxTime()) {
                    // From & To are selected -> update comboBox Time values
                    updateComboBoxTimeValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                            comboBoxTo.getSelectionModel().getSelectedItem());

                    System.out.println("from - strict search: enable time");
                } else {
                    // To is not selected -> update comboBox To values
                    updateComboBoxToValues(comboBoxFrom.getSelectionModel().getSelectedItem(), null);

                    System.out.println("from - strict search: updating to");
                }
            } else {
                System.out.println("from - normal search");
                updateComboBoxToValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                        comboBoxTime.getSelectionModel().getSelectedItem());
                updateComboBoxTimeValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                        comboBoxTo.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void onActionComboBoxTo(ActionEvent actionEvent) {
        if (canFireNow()) {
            if (isStrictSearch) {
                if (tryEnableComboBoxTime()) {
                    updateComboBoxTimeValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                            comboBoxTo.getSelectionModel().getSelectedItem());
                    // From & To are selected -> update comboBox Time values
                    System.out.println("to - strict search: enable time");
                } else {
                    // From is not selected -> update comboBox From values
                    updateComboBoxFromValues(comboBoxTo.getSelectionModel().getSelectedItem(), null);

                    System.out.println("time - strict search: updating from");
                }
            } else {
                System.out.println("to - normal search");
                updateComboBoxFromValues(comboBoxTo.getSelectionModel().getSelectedItem(),
                                        comboBoxTime.getSelectionModel().getSelectedItem());
                updateComboBoxTimeValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                        comboBoxTo.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void onActionComboBoxTime(ActionEvent actionEvent) {
        if (canFireNow()) {
            if (isStrictSearch) {
                tryEnableSearch();
                System.out.println("time - strict search");
            }
            else {
                System.out.println("time - normal search");
                updateComboBoxToValues(comboBoxFrom.getSelectionModel().getSelectedItem(),
                                    comboBoxTime.getSelectionModel().getSelectedItem());
                updateComboBoxFromValues(comboBoxTo.getSelectionModel().getSelectedItem(),
                                    comboBoxTime.getSelectionModel().getSelectedItem());
            }
        }
    }

}
