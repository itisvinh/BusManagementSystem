package com.busmanagementsystem.Database.Services;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Utilities {
    private Utilities() {
    }

    public static String concatAll(String... args) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String i : args)
            stringBuilder.append(i);

        return  stringBuilder.toString();
    }
    public static String sqlString(Object object) {
        return concatAll("'", object.toString().trim(), "'");
    }

    public static Notifications prepareNotification(String title, String content) {
        Notifications notifications = Notifications.create();
        notifications.text(content)
                .title("Route Service")
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .darkStyle();
        return  notifications;
    }
}
