package com.busmanagementsystem;

import com.busmanagementsystem.Background.BackgroundWorker;
import com.busmanagementsystem.Database.Pojos.Employee;
import com.busmanagementsystem.Interface.Tickets_Controller;
import javafx.scene.Scene;
import javafx.stage.Stage;

// this class holds all the static global info variables
public class Communicator {
    private Communicator(){
    }

    public static Stage primaryStage;
    public static Scene currentScene;
    public static boolean startedAsAdmin = true;
    public static Tickets_Controller tickets_controller;
    public static BackgroundWorker currentBackgroundWorker;
    public static String currentEmployeeID;
}
