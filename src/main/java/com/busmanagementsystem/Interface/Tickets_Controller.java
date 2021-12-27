package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Services.BusService;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Tickets_Controller extends Tickets_Routes_Base implements Initializable {
    BusService busService = new BusService();
    @FXML
    private AnchorPane ticketFunctionalButtonArea;
    @FXML
    private SearchableComboBox comboBoxBus;
    @FXML
    private TabPane tabPane;

    @Override
    protected void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        // event when user click filter search and it returns a query
        busService.loadBuses(comboBoxBus, routesSearchFilterQuery.getValue());
        System.out.println("ticket - changed: " + routesSearchFilterQuery.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startAsStrictSearch = true;
        if (Communicator.startedAsAdmin == true) {
            for (var child : ticketFunctionalButtonArea.getChildren())
                child.setDisable(true);
        }
    }

    public void onMouseClickSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        ((FontIcon) vBox.getChildren().get(0)).setIconColor(Color.RED);
    }

    public void onMouseEnterSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: green; -fx-border-radius: 15");
    }

    public void onMouseExitSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: transparent;");
    }

//    public void kwjdbgjwb() {
//        for (Tab tab : tabPane.getTabs()) {
//            AnchorPane anchorPane;//lay dc
//
//            HashMap<String, FontIcon> map = new HashMap<>();
//
//            GridPane gridPane = anchorPane.getChildren()[0];
//            for (Node fontIcon : gridPane.getChildren()) {
//                    map.put(fontIcon.getId(), (FontIcon) fontIcon);
//            }
//        }
//
//        var list = new ArrayList<Seat>();
//
//        for (Seat seat : list) {
//            FontIcon fontIcon = map.get(seat.getSeatID());
//            if (seat.isOccupied())
//                fontIcon.setIconColor(Color.GREY);
//        }
//
//    }
}
