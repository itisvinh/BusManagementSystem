package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Bus;
import com.busmanagementsystem.Database.Pojos.ExtSeat;
import com.busmanagementsystem.Database.Services.BusService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;

public class Tickets_Controller extends Tickets_Routes_Base implements Initializable {
    private BusService busService = new BusService();
    @FXML
    private AnchorPane ticketFunctionalButtonArea;
    @FXML
    private ComboBox<Bus> comboBoxBus;
    @FXML
    private TabPane tabPane;
    private LinkedList<VBox> selectedSeats = new LinkedList<>();
    private ObservableList<ExtSeat> seats;
    private String currentScheduleID;
    private String currentBusID;

    @Override
    protected void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        // event when user click filter search and it returns a query
        List<String> routeIDs = busService.loadBuses(comboBoxBus, routesSearchFilterQuery.getValue());
        currentScheduleID = routeIDs.get(0);
        System.out.println(currentScheduleID);
        System.out.println("ticket - changed: " + routesSearchFilterQuery.getValue());
        comboBoxBus.setDisable(false);
    }
    // event for combobox bus
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startAsStrictSearch = true;
        if (Communicator.startedAsAdmin == true) {
            for (var child : ticketFunctionalButtonArea.getChildren())
                child.setDisable(true);
        }
        comboBoxBus.setDisable(true);
        tabPane.setDisable(true);
    }

    private void updateSeats() {
        Bus bus = (Bus) comboBoxBus.getSelectionModel().getSelectedItem();

        if (bus != null) {
            // load seats into the observable list
            seats = busService.loadSeats(currentScheduleID, currentBusID);
            System.out.println("buses size: " + seats.size());
            updateSeatsStatus();
        }
    }
    
    private void updateSeatsStatus() {
        for (Tab tab : tabPane.getTabs()) {
            AnchorPane anchorPane = (AnchorPane) tab.getContent();
            GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);

            for (Node node : gridPane.getChildren()) {
                VBox vBox = (VBox) node;
                FontIcon fontIcon = (FontIcon) vBox.getChildren().get(0);
                Text text = (Text) vBox.getChildren().get(1);
                int index = Integer.valueOf(text.getText()) - 1;
                setSeatColorIndicators(index, fontIcon);
            }
        }
    }

    private void setSeatColorIndicators(int index, FontIcon fontIcon) {
        switch (seats.get(index).getBookingStatus()) {
            case "BOOKED":
                fontIcon.setIconColor(Color.rgb(144, 139, 47));
                break;
            case "PURCHASED":
                fontIcon.setIconColor(Color.rgb(35, 117, 27));
                break;
            case "EMPTY":
                fontIcon.setIconColor(Color.BLACK);
        }
    }

    public void onMouseClickSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        ((FontIcon) vBox.getChildren().get(0)).setIconColor(Color.RED);
        selectedSeats.add(vBox);
    }

    public void onMouseEnterSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: green; -fx-border-radius: 15");
    }

    public void onMouseExitSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: transparent;");
    }

    public void onActionSelectBus(ActionEvent event) {
        Bus bus = (Bus) comboBoxBus.getSelectionModel().getSelectedItem();
        if (bus != null) {
            currentBusID = bus.getBusID();
            updateSeats();
            tabPane.setDisable(false);
        }
    }

    public void onActionClearSelection(ActionEvent event) {
        if (selectedSeats.size() > 0) {
            for (Node node : selectedSeats) {
                VBox vBox = (VBox) node;
                Text text = (Text) vBox.getChildren().get(1);
                FontIcon fontIcon = (FontIcon) vBox.getChildren().get(0);
                setSeatColorIndicators(Integer.valueOf(text.getText()), fontIcon);
            }
            selectedSeats.clear();
        }
    }



}
