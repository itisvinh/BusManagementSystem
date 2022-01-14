package com.busmanagementsystem;

import com.busmanagementsystem.Database.Pojos.Employee;
import javafx.scene.Scene;
import javafx.stage.Stage;

// this class holds all the static global info variables
public class Communicator {
    private Communicator(){
    }

    public static Stage primaryStage;
    public static Scene currentScene;
    public static boolean startedAsAdmin = true;
    public static String currentEmployeeID;
}
