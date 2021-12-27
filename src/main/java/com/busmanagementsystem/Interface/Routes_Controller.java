package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Schedule;
import com.busmanagementsystem.Database.Services.RouteService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;


public class Routes_Controller extends Tickets_Routes_Base implements Initializable{
    RouteService routeService = new RouteService();
    @FXML
    private Button addRoute;
    @FXML
    private Button removeRoute;
    @FXML
    private Button editRoute;
    @FXML
    private TableView<Schedule> routesTableView;


    private ObservableList<Emp> list = FXCollections.observableArrayList();

    public static void setCellFactories(TableView<Schedule> tv) {
        String[] fields = new String[] {"scheduleID", "busID", "driverID", "startingLocation",
                                        "destination", "departureTime", "price"};

        int i = 0;
        for (TableColumn tableColumn : tv.getColumns())
            tableColumn.setCellValueFactory(new PropertyValueFactory(fields[i++]));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (Communicator.startedAsAdmin == false) {
            addRoute.setDisable(true);
            removeRoute.setDisable(true);
            editRoute.setDisable(true);
        }
        /*
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        age_col.setCellValueFactory(new PropertyValueFactory<>("age"));

        list.add(new Emp("kjdsbf", 23));
        list.add(new Emp("bfsds", 32));

        routesTableView.setItems(list);

        list.add(new Emp("kwdnf", 24));
        */

        routeService.loadRoutes(routesTableView, "##");
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
        routeService.loadRoutes(routesTableView, routesSearchFilterQuery.getValue());
        System.out.println("routes - changed: " + routesSearchFilterQuery.getValue());
    }

    private void startEditorStage() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Util_Scenes/RouteEditor_Scene.fxml"));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        Communicator.primaryStage.getScene().getRoot().setEffect(new GaussianBlur(5));
        stage.showAndWait();
        Communicator.primaryStage.getScene().getRoot().setEffect(null);

    }

    public void onActionAddRoute(ActionEvent event) {
        try {
            startEditorStage();
        } catch (Exception ex) {

        }
    }
    public void djfkjab() {

    }
}
