package com.busmanagementsystem.Background;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.Schedule;
import com.busmanagementsystem.Database.Services.BackgroundService;
import com.busmanagementsystem.Database.Services.RouteService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.controlsfx.control.Notifications;

import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundWorker {
    private static BackgroundWorker instance;
    private ScheduledExecutorService executorService;
    private BackgroundService backgroundService = new BackgroundService();
    private Schedule currentSchedule;
    private RouteService routeService = new RouteService();


    private BackgroundWorker() {
        firstScan();
        // 2 threads
        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(this::discardBookedSeats, 0, 5, TimeUnit.SECONDS);
    }

    public static BackgroundWorker start() {
        if (instance == null) {
            instance = new BackgroundWorker();
            Communicator.currentBackgroundWorker = instance;
        }
        return instance;
    }

    private void firstScan() {
        backgroundService.removeDeprecatedTickets();
        System.out.println("first scan succeeded");
    }

    public void restartWorker(ActionEvent event) {
        currentSchedule = null;
        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(this::discardBookedSeats, 0, 5, TimeUnit.SECONDS);
        System.out.println("Background thread: restarted");
    }

    public void discardBookedSeats() {
        try {
            if (currentSchedule == null) {
                currentSchedule = routeService.getUpcomingSchedule();
            }
            System.out.println("Background thread: " + currentSchedule);

            if (LocalTime.now().plusMinutes(30).isAfter(currentSchedule.getDepartureTime().toLocalTime())) {
                backgroundService.removeUnpurchasedTickets(currentSchedule.getScheduleID());
                // update seats
                if (Communicator.tickets_controller != null) {

                    Platform.runLater(() -> Communicator.tickets_controller.updateSeats());
                    System.out.println("Background thread: update");
                }
            }

        } catch (Exception ex) {
            Notifications notifications = Notifications.create()
                    .darkStyle()
                    .text("Background Worker")
                    .text("Exception occurred in background thread [" + Thread.currentThread().getName() + "]\nRestart all services?")
                    .onAction(this::restartWorker);
            notifications.showConfirm();
            System.out.println(ex);
        }
    }


}
