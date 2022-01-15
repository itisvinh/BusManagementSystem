package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;
import com.busmanagementsystem.Database.Pojos.Ticket;
import com.busmanagementsystem.Database.Pojos.Ticket_Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Ticket_Seat_Service {
    public int addNewTicket_Seat(Ticket_Seat ticket_seat) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("insert into Tickets_Seats values (?, ?)");

            statement.setString(1, ticket_seat.getTicketID());
            statement.setString(2, ticket_seat.getSeatID());

            affected_rows = statement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affected_rows;
    }

    public int deleteTicket_Seat(String ticketID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int affected_rows = 0;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("delete from Tickets_Seats where TicketID = ?");
            statement.setString(1, ticketID);

            affected_rows = statement.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return affected_rows;
    }

    public List<Integer> getSeatsNumbersOf(String ticketID) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = DBConnection.getConn();
            statement = conn.prepareStatement("select SeatNumber " +
                    " from Tickets_Seats ts join Seats s on ts.SeatID = s.SeatID " +
                    " where TicketID = ?" +
                    " order by SeatNumber");
            statement.setString(1, ticketID);
            resultSet = statement.executeQuery();

            List<Integer> seatNumbers = new ArrayList<>();
            while (resultSet.next())
                seatNumbers.add(resultSet.getInt("SeatNumber"));

            if (seatNumbers.size() > 0)
                return seatNumbers;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try { statement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
        return null;
    }
}
