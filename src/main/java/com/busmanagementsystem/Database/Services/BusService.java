package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.Bus;
import com.busmanagementsystem.Database.Pojos.ExtSeat;
import com.busmanagementsystem.Database.Pojos.Schedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;
import static com.busmanagementsystem.Database.Services.Utilities.sqlString;

public class BusService {

    public List<String> loadBuses(ComboBox comboBox, String filters) {
        RouteService routeService = new RouteService();
        List<String> routeIDs = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            // resultSet of routes
            resultSet = routeService.getRoutes(filters, statement);

            var buses_list = new ArrayList<String>(10);
            while (resultSet.next()) {
                buses_list.add(resultSet.getString("BusID"));
                routeIDs.add(resultSet.getString("ScheduleID"));
            }
            // close current resultSet
            resultSet.close();

            var busesID = buses_list.stream().map(i -> "'" + i + "'").collect(Collectors.joining(", "));
            // resultSet of buses
            resultSet = statement.executeQuery(concatAll("select * from Buses where BusID in (", busesID, ")"));

            ObservableList<Bus> buses = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Bus bus = new Bus();
                bus.setBusID(resultSet.getString("BusID"));
                bus.setBusPlateNumber(resultSet.getString("BusPlateNumber"));
                buses.add(bus);
            }
            comboBox.setItems(buses);

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return routeIDs;
    }

    public void loadAllBuses(ComboBox comboBox) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            String default_sql = "select * from Buses";
            resultSet = statement.executeQuery(default_sql);

            ObservableList<Bus> buses = FXCollections.observableArrayList();
            while (resultSet.next())
                buses.add(new Bus(resultSet.getString("BusID"), resultSet.getString("BusPlateNumber")));

            comboBox.setItems(buses);

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    public ObservableList<ExtSeat> loadSeats(String scheduleID, String busID) {
        Statement statement = null;
        ResultSet resultSet = null;
        ObservableList<ExtSeat> seats = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();

            resultSet = statement.executeQuery("select t.TicketID, s.SeatID, s.SeatNumber, s.BusID, t.CustomerID, t.BookingStatus, t.ScheduleID, sc.DepartureTime " +
                    " from Tickets_Seats ts right join Seats s on ts.SeatID = s.SeatID " +
                    " left join (select * from Tickets where ScheduleID = " + sqlString(scheduleID) + ") t on t.TicketID = ts.TicketID " +
                    " left join Schedules sc on sc.ScheduleID = t.ScheduleID " +
                    " where s.BusID = " + sqlString(busID) +
                    " order by s.SeatNumber " );


            while (resultSet.next()) {
                ExtSeat seat = new ExtSeat();
                seat.setTicketID(resultSet.getString("TicketID"));
                seat.setBusID(resultSet.getString("BusID"));
                seat.setSeatID(resultSet.getString("SeatID"));
                seat.setSeatNumber(resultSet.getInt("SeatNumber"));
                seat.setScheduleID(resultSet.getString("ScheduleID"));
                if (resultSet.getString("BookingStatus") == null)
                    seat.setBookingStatus("EMPTY");
                else
                    seat.setBookingStatus(resultSet.getString("BookingStatus"));
                seat.setCustomerID(resultSet.getString("CustomerID"));
                seat.setDepartureTime(resultSet.getTime("DepartureTime"));
                seats.add(seat);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return seats;
    }

    public Bus getBusOf(String busID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select * from Buses where BusID = ?");
            statement.setString(1, busID);
            resultSet = statement.executeQuery();

            Bus bus = null;
            if (resultSet.next()) {
                bus = new Bus();
                bus.setBusID(resultSet.getString("BusID"));
                bus.setBusPlateNumber(resultSet.getString("BusPlateNumber"));
            }
            if (bus != null)
                return bus;

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return null;
    }
}
