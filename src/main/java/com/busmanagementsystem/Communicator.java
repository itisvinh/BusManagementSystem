package com.busmanagementsystem;

import javafx.scene.Scene;
import javafx.stage.Stage;

// this class holds all the static global info variables
public class Communicator {
    private Communicator(){
    }

    public static Stage primaryStage;
    public static Scene currentScene;
    public static boolean startedAsAdmin = true;
}
