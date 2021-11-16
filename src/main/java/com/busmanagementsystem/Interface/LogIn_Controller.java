package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ToggleSwitch;


public class LogIn_Controller {
    // Stage location
    private double yOffset, xOffset;
    // sign in modes toggle switch
    private final String emp = "Employee";
    private final String admin = "Administrator";
    private String currMode = emp;
    // text field focus
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private TextField currTextField;
    //
    @FXML
    private Button signIn, cancel;
    @FXML
    private ToggleSwitch toggleSwitch;
    @FXML
    private Pane controlsWrapper;

    @FXML
    public void cancelButtonMouseClick(MouseEvent mouseEvent) {
        Platform.exit();

    }

    @FXML
    public void buttonMouseEnter(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #f2e9e4;");
    }

    @FXML
    public void buttonMouseExit(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: transparent;");
    }

    @FXML
    public void toggleSwitchMouseClick(MouseEvent mouseEvent) {
        currMode = toggleSwitch.isSelected() ? admin : emp;
        toggleSwitch.setText(currMode);
    }

    @FXML
    public void textFieldMouseClick(MouseEvent mouseEvent) {
        if (currTextField != (TextField) mouseEvent.getSource()) {
            if (currTextField != null)
                currTextField.setStyle("-fx-border-color: transparent;");
            currTextField = (TextField) mouseEvent.getSource();
            currTextField.setStyle("-fx-border-color: #2a9d8f;");
        }
    }

    @FXML
    public void outsideOfTextFieldMouseClick(MouseEvent mouseEvent){
        if (currTextField != null) {
            currTextField.setStyle("-fx-border-color: transparent;");
            controlsWrapper.requestFocus();
            currTextField = null;
        }
    }


    @FXML
    public void MouseDrag(MouseEvent mouseEvent) {
        Communicator.primaryStage.setX(mouseEvent.getScreenX() - xOffset);
        Communicator.primaryStage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @FXML
    public void MousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

}
