package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.*;
import com.busmanagementsystem.Interface.Tickets_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.busmanagementsystem.Database.Services.Utilities.sqlString;

public class TicketService {
    private boolean ColumnsAdded = false;
    private Ticket_Seat_Service ticketSeatService = new Ticket_Seat_Service();

    public String createNextTicketID() {
        Statement statement = null;
        ResultSet resultSet = null;
        String ticketID = "";

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select TicketID from Tickets");

            var ticketIDs_str = new ArrayList<String>();

            while (resultSet.next())
                ticketIDs_str.add(resultSet.getString(1));

            if (ticketIDs_str.size() == 0)
                return "T001";
            else {

                var ticketIDs = ticketIDs_str.stream()
                        .map(i -> Integer.valueOf(i.substring(1)))
                        .sorted()
                        .collect(Collectors.toList());
                int number = ticketIDs.get(ticketIDs.size() - 1) + 1;
                ticketID += "T";

                if (number < 10)
                    ticketID += "00" + String.valueOf(number);
                else if (number < 100)
                    ticketID += "0" + String.valueOf(number);
                else
                    ticketID += String.valueOf(number);
            }

        } catch (Exception ex) {
            System.out.println(ex);
            //return null;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return ticketID;
    }

    public Ticket addNewTicket(Ticket ticket) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;
        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("insert into Tickets values (?, ?, ?, ?)");

            String ticketID = createNextTicketID();
            statement.setString(1, ticketID);
            statement.setString(2, ticket.getScheduleID());
            statement.setString(3, ticket.getCustomerID());
            statement.setString(4, ticket.getBookingStatus());

            affected_rows = statement.executeUpdate();

            if (affected_rows > 0) {
                ticket.setTicketID(ticketID);
                return ticket;
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return ticket;
    }

    private void loadColumns(TableView<ExtTicket> tableView, ResultSet resultSet) throws SQLException {
        /**********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         **********************************/

        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            col.setStyle("-fx-alignment: CENTER;");

            tableView.getColumns().addAll(col);

            ColumnsAdded = true;
            Tickets_Controller.setCellFactories(tableView);
        }
    }

    private void loadDataIntoTableView(TableView<ExtTicket> tableView, ResultSet resultSet) throws SQLException{
        ObservableList<ExtTicket> data = FXCollections.observableArrayList();
        /********************************
         * Data added to ObservableList *
         ********************************/

        System.out.println("inside loading: " + resultSet.getFetchSize());
        while (resultSet.next()) {
            ExtTicket ticket = new ExtTicket();
            ticket.setTicketID(resultSet.getString("TicketID"));
            ticket.setBusID(resultSet.getString("BusID"));
            ticket.setScheduleID(resultSet.getString("ScheduleID"));
            ticket.setCustomerName(resultSet.getString("CustomerName"));
            ticket.setPhoneNumber(resultSet.getString("PhoneNumber"));
            ticket.setFrom(resultSet.getString("From"));
            ticket.setTo(resultSet.getString("To"));
            ticket.setBusPlateNumber(resultSet.getString("BusPlateNumber"));
            ticket.setDepartureTime(resultSet.getTime("DepartureTime"));
            data.add(ticket);
            System.out.println("load data");
        }

        tableView.setItems(data);
    }

    public void loadTicketsOfCustomer(TableView<ExtTicket> tableView, String customerID) {
        Statement statement = null;
        ResultSet resultSet = null;
        ObservableList<ExtTicket> tickets = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(" select TicketID, t.ScheduleID, StartingLocation as 'From', Destination as 'To', DepartureTime, " +
                    " s.BusID, BusPlateNumber, CONCAT(FirstName, ' ', LastName) as CustomerName, PhoneNumber " +
                    " from Tickets t join Schedules s on t.ScheduleID = s.ScheduleID " +
                    " join Customers c on t.CustomerID = c.CustomerID " +
                    " join Buses b on s.BusID = b.BusID " +
                    " where t.CustomerID = " + sqlString(customerID));


            if (!ColumnsAdded)
                loadColumns(tableView, resultSet);

            loadDataIntoTableView(tableView, resultSet);
        System.out.println("after loading");
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

    // editTicketBookingStatus
    public int editTicketBookingStatus(Ticket ticket) {
        Statement statement = null;
        int affected_rows = 0;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.createStatement();
            return statement.executeUpdate("update Tickets" +
                    " set BookingStatus = " + sqlString(ticket.getBookingStatus()) +
                    " where TicketID = " + sqlString(ticket.getTicketID()));

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
        }
        return affected_rows;
    }

    public int removeTicket(Ticket ticket) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;

        try {
            Connection conn = DBConnection.getConn();
            ticketSeatService.deleteTicket_Seat(ticket.getTicketID());

            statement = conn.prepareStatement("delete from Tickets where TicketID = ?");
            statement.setString(1, ticket.getTicketID());
            int af_rows = statement.executeUpdate();
            if (af_rows > 0)
                affected_rows += af_rows;


        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affected_rows;
    }

    public int updateSeatsOfTicket(List<ExtSeat> old_seats, List<ExtSeat> new_seats) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;

        try {
            if (old_seats.size() == new_seats.size() && old_seats.size() > 0) {
                String ticketID = old_seats.get(0).getTicketID();
                Connection conn = DBConnection.getConn();
                Iterator<ExtSeat> new_iter = new_seats.iterator();

                for (var seat : old_seats) {
                    statement = conn.prepareStatement("update Tickets_Seats set SeatID = ? where TicketID = ? and SeatID = ?");
                    statement.setString(2, ticketID);
                    statement.setString(3, seat.getSeatID());
                    statement.setString(1, new_iter.next().getSeatID());
                    affected_rows += statement.executeUpdate();
                    statement.close();
                }
            } else
                throw new NullPointerException("The 2 input list have to have the same size");

        } catch (Exception ex) {
            System.out.println(ex);
            affected_rows = -1;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affected_rows;
    }

    public int updateScheduleOfTicket(String ticketID, String scheduleID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("update Tickets set ScheduleID = ? where TicketID = ?");
            statement.setString(1, scheduleID);
            statement.setString(2, ticketID);
            affected_rows = statement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
            affected_rows = -1;
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affected_rows;
    }

    public ObservableList<Ticket> getTickets(String scheduleID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select * from Tickets where ScheduleID = ?");
            statement.setString(1, scheduleID);
            resultSet = statement.executeQuery();

            ObservableList<Ticket> tickets = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketID(resultSet.getString("TicketID"));
                ticket.setScheduleID(resultSet.getString("ScheduleID"));
                ticket.setCustomerID(resultSet.getString("CustomerID"));
                ticket.setBookingStatus(resultSet.getString("BookingStatus"));
                tickets.add(ticket);
            }

            if (tickets.size() > 0)
                return tickets;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return null;
    }

}

