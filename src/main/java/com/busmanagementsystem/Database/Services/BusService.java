package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;

public class BusService {
    public void loadBuses(ComboBox comboBox, String filters) {
        RouteService routeService = new RouteService();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            // resultSet of routes
            resultSet = routeService.getRoutes(filters, statement);

            var buses_list = new ArrayList<String>(10);
            while (resultSet.next())
                buses_list.add(resultSet.getString("BusID"));
            // close current resultSet
            resultSet.close();

            var busesID = buses_list.stream().map(i -> "'" + i + "'").collect(Collectors.joining(", "));
            // resultSet of buses
            resultSet = statement.executeQuery(concatAll("select * from Buses where BusID in (", busesID, ")"));
            while(resultSet.next())
                comboBox.getItems().add(concatAll(resultSet.getString("BusID"), " [", resultSet.getString("BusPlateNumber"), "]"));

        } catch (Exception ex) {
        System.out.println(ex);
        //return null;
    } finally {
        try { statement.close(); } catch (Exception e1) {}
        try { resultSet.close(); } catch (Exception e2) {}
    }
    }
}
