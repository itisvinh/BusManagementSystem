package com.busmanagementsystem.Interface;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// extending this class enable the ability to START the SearchFilter scene
// and OBSERVE the change event of the returned value
public abstract class Tickets_Routes_Base {
    // this var captures the returned search query
    protected StringProperty routesSearchFilterQuery = new SimpleStringProperty("");
    // chang this value to False if starting as strict search
    protected boolean startAsStrictSearch = false;

    protected Tickets_Routes_Base() {
        routesSearchFilterQuery.addListener(this::changed);
    }

    protected abstract void changed(ObservableValue<? extends String> observableValue, String s, String t1);

    protected void startSearchFilterStage() throws Exception{
        StringBuilder returnValueCapturer = new StringBuilder("");

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Util_Scenes/RouteSearchFilter_Scene.fxml"));
        Parent parent = fxmlLoader.load();

        RouteSearchFilter_Controller controller = ((RouteSearchFilter_Controller) fxmlLoader.getController());
        controller.setReturnSearchQuery(returnValueCapturer);
        controller.setStrictSearch(startAsStrictSearch);

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.showAndWait();

        tryAssignRoutesSearchFilterQuery(returnValueCapturer);
    }

    private boolean tryAssignRoutesSearchFilterQuery(StringBuilder str) {
        /*
        if (!str.toString().equals("")) {
            routesSearchFilterQuery.setValue(str.toString());
            return true;
        } else {
            return false;
        }
         */
        if (!routesSearchFilterQuery.getValue().equals(str.toString())) {
            routesSearchFilterQuery.setValue(str.toString());
            return true;
        } else {
            return false;
        }
    }

    public void searchForRoutes(ActionEvent actionEvent) {

        try {
            startSearchFilterStage();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.toString());
            alert.show();
        }
    }

}
