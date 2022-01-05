package com.busmanagementsystem;

import com.busmanagementsystem.Database.Pojos.Credential;
import com.busmanagementsystem.Database.Services.CredentialService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Interface/LogIn_Scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Communicator.primaryStage = stage;
        Communicator.currentScene = scene;
    }

    public static void entry(String[] args) {
        launch();
    }
}
