package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Background.BackgroundWorker;
import com.busmanagementsystem.Communicator;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWorkingArea_Controller implements Initializable {
    private BackgroundWorker backgroundWorker;
    private double xOffset, yOffset;
    @FXML
    private Button exit, maximize, hide;
    @FXML
    private Pane mainSceneArea;
    // native vars
    private Node homeNode, routesNode, ticketsNode, accountNode, aboutNode;
    // recently clicked button
    private Button currMainMenuButton;
    private Button prevHoverMainMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialized");
        homeButtonMouseClick(null);
        backgroundWorker = BackgroundWorker.start();
    }
    // native method - apply fade transition to a node within [duration], change node's
    // opacity from [fromValue] to [toValue]

    private void applyFadeTransition(Node node, Duration duration, Double fromValue, Double toValue){
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.play();
    }

    @FXML
    public void mainMenuButtonMouseEnter(MouseEvent mouseEvent) {

        if ((Button)mouseEvent.getSource() != currMainMenuButton) {
            ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: rgba(0, 255, 255, 0.2);");
        }
    }

    @FXML
    public void mainMenuButtonMouseExit(MouseEvent mouseEvent) {

        if ((Button)mouseEvent.getSource() != currMainMenuButton) {
            ((Button) mouseEvent.getSource()).setStyle("-fx-background-color: transparent;");
            applyFadeTransition((Button) mouseEvent.getSource(), Duration.seconds(0.3), 0.3, 1.0);
        }
//        prevHoverMainMenuButton = (Button) mouseEvent.getSource();
//        applyFadeTransition((Button)mouseEvent.getSource(), Duration.seconds(0.3), 0.3, 1.0);

    }
    // native method

    private void mainMenuButtonHighlight(Button source){
        if (currMainMenuButton != null)
            currMainMenuButton.setStyle("-fx-background-color: transparent;");
        currMainMenuButton = source;
        currMainMenuButton.setStyle("-fx-background-color: #8661c1;");
    }
    @FXML
    public void routesButtonMouseClick(MouseEvent mouseEvent) throws Exception{
        mainMenuButtonHighlight((Button) mouseEvent.getSource());
        try {
            if (routesNode == null) {
                routesNode = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Routes_Scene.fxml"));
                anchorChildFitParent(routesNode);
            }// remove all currently added nodes from mainSceneArea
            mainSceneArea.getChildren().clear();
            // add node to mainSceneArea
            mainSceneArea.getChildren().add(routesNode);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    @FXML
    public void ticketsButtonMouseClick(MouseEvent mouseEvent){
        mainMenuButtonHighlight((Button) mouseEvent.getSource());
        try {
            if (ticketsNode == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Tickets_Scene.fxml"));
                ticketsNode = (AnchorPane) fxmlLoader.load();
                Communicator.tickets_controller = fxmlLoader.getController();
                anchorChildFitParent(ticketsNode);
            }
            // remove all currently added nodes from mainSceneArea
            mainSceneArea.getChildren().clear();
            // add node to mainSceneArea
            mainSceneArea.getChildren().add(ticketsNode);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    @FXML
    public void accountButtonMouseClick(MouseEvent mouseEvent){
        mainMenuButtonHighlight((Button) mouseEvent.getSource());
        try {
            if (accountNode == null) {
                accountNode = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Account_Scene.fxml"));
                anchorChildFitParent(accountNode);
            }// remove all currently added nodes from mainSceneArea
            mainSceneArea.getChildren().clear();
            // add node to mainSceneArea
            mainSceneArea.getChildren().add(accountNode);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    public void aboutButtonMouseClick(MouseEvent mouseEvent){
        mainMenuButtonHighlight((Button) mouseEvent.getSource());
        try {
            if (aboutNode == null) {
                aboutNode = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainWorkingArea_Sub_Scenes/example.fxml"));
                anchorChildFitParent(aboutNode);
            }
            // remove all currently added nodes from mainSceneArea
            mainSceneArea.getChildren().clear();
            // add node to mainSceneArea
            mainSceneArea.getChildren().add(aboutNode);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // native method
    private void anchorChildFitParent(Node node){
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
    }

    @FXML
    public void homeButtonMouseClick(MouseEvent mouseEvent){
        if (currMainMenuButton != null) {
            currMainMenuButton.setStyle("-fx-background-color: transparent");
            currMainMenuButton = null;
        }
        try {
            if (homeNode == null) {
                homeNode = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainWorkingArea_Sub_Scenes/example.fxml"));
                anchorChildFitParent(homeNode);
            }
            // remove all currently added nodes from mainSceneArea
            mainSceneArea.getChildren().clear();
            // add node to mainSceneArea
            mainSceneArea.getChildren().add(homeNode);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    @FXML
    public void exit(MouseEvent mouseEvent){
        Platform.exit();
    }

    @FXML
    public void minimize(MouseEvent mouseEvent){
        if (Communicator.primaryStage.isMaximized())
            Communicator.primaryStage.setMaximized(false);
        else
            Communicator.primaryStage.setMaximized(true);
    }

    @FXML
    public void hide(MouseEvent mouseEvent){
        Communicator.primaryStage.setIconified(true);
    }
    @FXML
    public void controlButtonMouseEnter(MouseEvent mouseEvent){
        Button source = (Button)mouseEvent.getSource();
        if (source == exit)
            source.setStyle("-fx-background-color: #c40935;");
        else if (source == maximize)
            source.setStyle("-fx-background-color: #04b64b;");
        else if (source == hide)
            source.setStyle("-fx-background-color: #daae04;");

        ((FontIcon)source.getChildrenUnmodifiable().get(0)).setIconColor(Color.rgb(227, 216, 227));

    }

    @FXML
    public void controlButtonMouseExit(MouseEvent mouseEvent){
        Button source = (Button)mouseEvent.getSource();
        source.setStyle("-fx-background-color: transparent;");
        ((FontIcon)source.getChildrenUnmodifiable().get(0)).setIconColor(Color.BLACK);
    }

    @FXML
    public void controlPaneMouseDrag(MouseEvent mouseEvent) {
        Communicator.primaryStage.setX(mouseEvent.getScreenX() - xOffset);
        Communicator.primaryStage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @FXML
    public void controlPaneMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }
}
