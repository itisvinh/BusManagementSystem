package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;


public class Routes_Controller extends Tickets_Routes_Base implements Initializable{
    @FXML
    private Button addRoute;
    @FXML
    private Button removeRoute;
    @FXML
    private Button editRoute;
    @FXML
    private TableView<Emp> routesTableView;

    @FXML
    private TableColumn<Emp, String> name_col;
    @FXML
    private TableColumn<Emp, Integer> age_col;


    private ObservableList<Emp> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Communicator.startedAsAdmin == false) {
            addRoute.setDisable(true);
            removeRoute.setDisable(true);
            editRoute.setDisable(true);
        }

        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        age_col.setCellValueFactory(new PropertyValueFactory<>("age"));

        list.add(new Emp("kjdsbf", 23));
        list.add(new Emp("bfsds", 32));

        routesTableView.setItems(list);

        list.add(new Emp("kwdnf", 24));
    }

    @FXML
    public void functionButtonMouseEnter(MouseEvent mouseEvent){
        Button source = (Button) mouseEvent.getSource();
        ((FontIcon) source.getChildrenUnmodifiable().get(0)).setIconColor(Color.WHITE);
    }

    @FXML
    public void functionButtonMouseExit(MouseEvent mouseEvent){
        Button source = (Button) mouseEvent.getSource();
        ((FontIcon) source.getChildrenUnmodifiable().get(0)).setIconColor(Color.BLACK);
    }

    @Override
    protected void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        // event when user click filter search and it returns a query
        System.out.println("routes - changed");
    }
}
