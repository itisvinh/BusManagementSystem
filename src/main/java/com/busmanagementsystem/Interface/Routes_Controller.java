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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;


public class Routes_Controller extends Tickets_Routes_Base implements Initializable{
    RouteService routeService = new RouteService();
    @FXML
    public Label awaitingRoutes_label;
    @FXML
    public Label allRoutes_label;
    @FXML
    public Label activeRoutes_label;
    @FXML
    private Button addRoute;
    @FXML
    private Button removeRoute;
    @FXML
    private Button editRoute;
    @FXML
    private TableView<Schedule> routesTableView;
    @FXML
    private TextField searchBar;
    private String title = "Route Service";
    private ObservableList<Emp> list = FXCollections.observableArrayList();

    public static void setCellFactories(TableView<Schedule> tv) {
        String[] fields = new String[] {"scheduleID", "busID", "driverID", "startingLocation",
                                        "destination", "departureTime", "price"};

        int i = 0;
        for (TableColumn tableColumn : tv.getColumns())
            tableColumn.setCellValueFactory(new PropertyValueFactory(fields[i++]));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        if (Communicator.startedAsAdmin == false) {
            addRoute.setDisable(true);
            removeRoute.setDisable(true);
            editRoute.setDisable(true);
        }

        routeService.loadRoutes(routesTableView, "##");
        updateStatistics();
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

    private int startEditorStage(Schedule schedule) throws Exception{
        int[] affected_rows = new int[1];
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Util_Scenes/RouteEditor_Scene.fxml"));
        Parent parent = fxmlLoader.load();
        RouteEditor_Controller controller = (RouteEditor_Controller) fxmlLoader.getController();
        controller.setAffectedRowsCapturer(affected_rows);

        if (schedule != null)
            controller.launch(schedule);
        else
            controller.launch();

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        //Communicator.primaryStage.getScene().getRoot().setEffect(new GaussianBlur(5));
        stage.showAndWait();
        //Communicator.primaryStage.getScene().getRoot().setEffect(null);
        return affected_rows[0];
    }

    public void onActionAddRoute(ActionEvent event) {
        try {
            if (startEditorStage(null) > 0) {
                updateStatistics();
                routeService.loadRoutes(routesTableView, "##");
                prepareNotification(title, "Added successfully").showInformation();
                Communicator.currentBackgroundWorker.restartWorker(null);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void onActionEditRoute(ActionEvent event){
        try {
            Schedule schedule = routesTableView.getSelectionModel().getSelectedItem();
            System.out.println("beforeeee: " + schedule);
            if (schedule != null) {
                if (startEditorStage(schedule) > 0) {
                    routeService.loadRoutes(routesTableView, "##");
                    prepareNotification(title, "Edited successfully").showInformation();
                    Communicator.currentBackgroundWorker.restartWorker(null);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void onActionRemoveRoute(ActionEvent event){
        Schedule schedule = routesTableView.getSelectionModel().getSelectedItem();

        try {
            if (schedule != null) {
                String scheduleID = schedule.getScheduleID();
                System.out.println(scheduleID);
                if (routeService.deleteSchedule(scheduleID)) {
                    prepareNotification(title, "Removed successfully").showInformation();
                    updateStatistics();
                    routeService.loadRoutes(routesTableView, "##");
                    Communicator.currentBackgroundWorker.restartWorker(null);
                } else
                    prepareNotification(title, "Failed to remove").showWarning();
            } else
            prepareNotification(title, "Select a route to remove").showInformation();

        } catch (Exception ex) {
            System.out.println(ex);
            prepareNotification(title,"Exception encountered").showError();
        }
    }

    public void onMouseDoubleClickedTableView(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 2)
            onActionEditRoute(null);
    }

    public void updateStatistics() {
        int[] stats = routeService.getStatistics();
        allRoutes_label.setText(String.valueOf(stats[0]));
        awaitingRoutes_label.setText(String.valueOf(stats[2]));
        activeRoutes_label.setText(String.valueOf(stats[1]));
    }


    public void onKeyTypeFilterRoutes(KeyEvent keyEvent) {
        routeService.loadRoutesWithKey(routesTableView, searchBar.getText());
    }
}
