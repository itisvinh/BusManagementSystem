package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
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
import java.util.ArrayList;
import java.util.HashMap;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;
import static com.busmanagementsystem.Database.Services.Utilities.sqlString;

public class RouteService {
    private boolean ColumnsAdded = false;

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
            //System.out.println("Column ["+i+"] ");
        }
    }

    private void loadDataIntoTableView(TableView tableView, ResultSet resultSet) throws SQLException{
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        /********************************
         * Data added to ObservableList *
         ********************************/
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
    public void loadRoutes(TableView tableView, String filters) {
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
}
