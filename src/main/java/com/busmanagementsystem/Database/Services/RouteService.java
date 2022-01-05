package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.Schedule;
import com.busmanagementsystem.Database.Pojos.Ticket;
import com.busmanagementsystem.Interface.Routes_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;
import static com.busmanagementsystem.Database.Services.Utilities.sqlString;

public class RouteService {
    private boolean ColumnsAdded = false;
    private TicketService ticketService = new TicketService();

    //public static ObservableList<String>
    private void loadColumns(TableView tableView, ResultSet resultSet) throws SQLException {
        /**********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         **********************************/

        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            col.setStyle("-fx-alignment: CENTER;");

            tableView.getColumns().addAll(col);

            ColumnsAdded = true;
            Routes_Controller.setCellFactories(tableView);
        }
    }

    private void loadDataIntoTableView(TableView tableView, ResultSet resultSet) throws SQLException{
        ObservableList<Schedule> data = FXCollections.observableArrayList();
        /********************************
         * Data added to ObservableList *
         ********************************/
        /*
        while(resultSet.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i =1 ; i <= resultSet.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(resultSet.getString(i ));
            }
            System.out.println("Row [1] added "+row );
            data.add(row);
        }
        //FINALLY ADDED TO TableView
        tableView.setItems(data);
         */

        while (resultSet.next()) {
            Schedule schedule = new Schedule();

            schedule.setScheduleID(resultSet.getString("ScheduleID"));
            schedule.setBusID(resultSet.getString("BusID"));
            schedule.setDriverID(resultSet.getString("DriverID"));
            schedule.setStartingLocation(resultSet.getString("StartingLocation"));
            schedule.setDestination(resultSet.getString("Destination"));
            schedule.setDepartureTime(resultSet.getTime("DepartureTime"));
            schedule.setPrice(resultSet.getFloat("Price"));

            data.add(schedule);
        }

        tableView.setItems(data);
    }

    private String[] split(String filters) {
        var tokens = new ArrayList<String>(3);
        String token = "";

        for (char i : (filters + '#').toCharArray())
            if (i == '#') {
                tokens.add(token);
                token = "";
            } else
                token += i;

        return tokens.stream().toArray(String[]::new);
    }

    public ResultSet getRoutes(String filters, Statement statement) throws  SQLException{
        String default_SQL = "select ScheduleID, BusID, DriverID, StartingLocation, Destination, DepartureTime, Price from Schedules";

        String[] filter = split(filters);
        var map = new HashMap<String, String>(3);
        map.put("StartingLocation", filter[0]);
        map.put("Destination", filter[1]);
        map.put("DepartureTime", filter[2]);

        boolean whereAdded = false;
        for (var entry : map.entrySet())
            if (!entry.getValue().equals("")) {
                if (!whereAdded) {
                    default_SQL += concatAll(" where ", entry.getKey(), " = ", sqlString(entry.getValue()));
                    whereAdded = true;
                    continue;
                }
                default_SQL += concatAll(" and ", entry.getKey(), " = ", sqlString(entry.getValue()));
            }

        ResultSet resultSet = statement.executeQuery(default_SQL);
            return resultSet;
    }

    // [filters] strictly follows the "[StartingLocation]#[Destination]#[DepartureTime]" format
    public void loadRoutes(TableView tableView, String filters){
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = getRoutes(filters, statement);

            if (!ColumnsAdded)
                loadColumns(tableView, resultSet);

            loadDataIntoTableView(tableView, resultSet);

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    private void populateComboBox(ComboBox comboBox, String default_SQL) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection conn = DBConnection.getConn();
            String SQL = default_SQL;
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SQL);

            while (resultSet.next())
                comboBox.getItems().add(resultSet.getString(1));

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    public void getStartingLocations(ComboBox comboBox, String destination, String departureTime) {
        String default_SQL = "select distinct StartingLocation from Schedules";

        if (destination != null && departureTime != null) {
            default_SQL += concatAll(" where Destination=", sqlString(destination),
                    " and DepartureTime=", sqlString(departureTime));
        } else if (destination != null)
            default_SQL += concatAll(" where Destination=", sqlString(destination));
        else if (departureTime != null)
            default_SQL += concatAll(" where DepartureTime=", sqlString(departureTime));


        populateComboBox(comboBox, default_SQL);
    }

    public void getDestination(ComboBox comboBox, String startingLocation, String departureTime) {
        String default_SQL = "select distinct Destination from Schedules";

        if (startingLocation != null && departureTime != null) {
            default_SQL += concatAll(" where StartingLocation=", sqlString(startingLocation),
                                            " and DepartureTime=", sqlString(departureTime));
        } else if (startingLocation != null)
            default_SQL += concatAll(" where StartingLocation=", sqlString(startingLocation));
        else if (departureTime != null)
            default_SQL += concatAll(" where DepartureTime=", sqlString(departureTime));

        populateComboBox(comboBox, default_SQL);
    }

    public void getDepartureTime(ComboBox comboBox, String startingLocation, String destination) {
        String default_SQL = "select distinct DepartureTime from Schedules";

        if (startingLocation != null && destination != null) {
            default_SQL += concatAll(" where StartingLocation=", sqlString(startingLocation),
                    " and Destination=", sqlString(destination));
        } else if (startingLocation != null)
            default_SQL += concatAll(" where StartingLocation=", sqlString(startingLocation));
        else if (destination != null)
            default_SQL += concatAll(" where Destination=", sqlString(destination));

        populateComboBox(comboBox, default_SQL);
    }

    public int[] getStatistics() {
        int[] stats = new int[] { 0, 0, 0};
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            String sql_count = "select COUNT(ScheduleID) from schedules ";

            resultSet = statement.executeQuery(sql_count);
            if (resultSet.next())
                stats[0] = resultSet.getInt(1);
            resultSet.close();

            resultSet = statement.executeQuery(sql_count + "where DepartureTime <= CONVERT(time, GETDATE())");
            if (resultSet.next())
                stats[1] = resultSet.getInt(1);

            stats[2] = stats[0] - stats[1];

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return stats;
    }

//    private String nextScheduleID() {
//        int[] stats = getStatistics();
//        String scheduleID = "S";
//        if (stats[0] < 10)
//            scheduleID += "0";
//        scheduleID += String.valueOf(stats[0] + 1);
//        return scheduleID;
//    }

    public int addNewSchedule(Schedule schedule) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affectedRows = 0;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("insert into Schedules" +
                    " values (?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, createNextScheduleID());
            statement.setString(2, schedule.getBusID());
            statement.setString(3, schedule.getDriverID());
            statement.setString(4, schedule.getStartingLocation());
            statement.setString(5, schedule.getDestination());
            statement.setTime(6, schedule.getDepartureTime());
            statement.setFloat(7, schedule.getPrice());

            affectedRows = statement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affectedRows;
    }

    public int updateSchedule(Schedule schedule) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affectedRows = 0;

        try {
            Connection conn = DBConnection.getConn();
            String sql = concatAll("update Schedules ",
                                        " set BusID = ?, DriverID = ?, StartingLocation = ?, ",
                                        "Destination = ?, DepartureTime = ?, Price = ? ",
                                        " where ScheduleID = ?");
            statement = conn.prepareStatement(sql);

            statement.setString(2, schedule.getBusID());
            statement.setString(3, schedule.getDriverID());
            statement.setString(4, schedule.getStartingLocation());
            statement.setString(5, schedule.getDestination());
            statement.setTime(6, schedule.getDepartureTime());
            statement.setFloat(7, schedule.getPrice());

            affectedRows = statement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affectedRows;
    }

    public boolean deleteSchedule(String scheduleID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select TicketID from Tickets where ScheduleID = ?");
            statement.setString(1, scheduleID);
            resultSet = statement.executeQuery();

            // get a list of ticketIDs having @scheduleID = scheduleID
            List<Ticket> tickets = new LinkedList<>();
            while (resultSet.next()) {
                Ticket t = new Ticket();
                t.setTicketID(resultSet.getString("TicketID"));
                tickets.add(t);
            }
            System.out.println("list: " + tickets.size());

            // remove the tickets having the ticketIDs accordingly to the list
            int af_rows = 0;
            if (tickets.size() > 0) {
                for (var ticket : tickets)
                    af_rows += ticketService.removeTicket(ticket);
            }

            statement.close();
            statement = conn.prepareStatement("delete from Schedules where ScheduleID = ?");
            statement.setString(1, scheduleID);
            if (statement.executeUpdate() > 0)
                return true;

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return false;
    }

    public Time getDepartureTimeOf(String scheduleID) {
        Statement statement = null;
        ResultSet resultSet = null;
        Time time = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select DepartureTime from Schedules where ScheduleID = " + sqlString(scheduleID));
            if (resultSet.next())
                time = resultSet.getTime("DepartureTime");

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return time;
    }

    public String createNextScheduleID() {
        Statement statement = null;
        ResultSet resultSet = null;
        String customerID = "";

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select ScheduleID from Schedules");

            var scheduleIDs_str = new ArrayList<String>();

            while (resultSet.next())
                scheduleIDs_str.add(resultSet.getString(1));

            if (scheduleIDs_str.size() == 0)
                return "S01";
            else {

                var scheduleIDs = scheduleIDs_str.stream()
                        .map(i -> Integer.valueOf(i.substring(1)))
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println(scheduleIDs);

                int number = scheduleIDs.get(scheduleIDs.size() - 1) + 1;
                customerID += "S";
                customerID += (number < 10) ? "0" + String.valueOf(number) : String.valueOf(number);
            }

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return customerID;
    }
}
