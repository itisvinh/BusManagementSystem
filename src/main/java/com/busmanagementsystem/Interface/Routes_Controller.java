package com.busmanagementsystem.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;


public class Routes_Controller implements Initializable {
    @FXML
    private TableView<Emp> routesTableView;

    @FXML
    private TableColumn<Emp, String> name_col;
    @FXML
    private TableColumn<Emp, Integer> age_col;

    private ObservableList<Emp> list = FXCollections.observableArrayList();

    public void doing(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
