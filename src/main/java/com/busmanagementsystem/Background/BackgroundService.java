package com.busmanagementsystem.Background;

import com.busmanagementsystem.Database.Pojos.Schedule;
import com.busmanagementsystem.Database.Pojos.Ticket;
import com.busmanagementsystem.Database.Services.RouteService;
import com.busmanagementsystem.Database.Services.TicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.LocalTime;

public class BackgroundService {
    private RouteService routeService = new RouteService();
    private TicketService ticketService = new TicketService();

    public void removeDeprecatedTickets() {
        try {
            ObservableList<String> scheduleIDs = routeService.getScheduleIDsBefore(Time.valueOf(LocalTime.now()));
            ObservableList<Ticket> tickets = FXCollections.observableArrayList();

            for (String scheduleID : scheduleIDs) {
                ObservableList<Ticket> list = ticketService.getTickets(scheduleID);
                if (list != null && list.size() > 0)
                    tickets.addAll(list);
            }

            for (Ticket ticket : tickets)
                ticketService.removeTicket(ticket);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void removeUnpurchasedTickets(String scheduleID) {
        ObservableList<Ticket> tickets = ticketService.getTickets(scheduleID);

        if (tickets != null && tickets.size() > 0) {
            for (var ticket : tickets)
                if (ticket.getBookingStatus().equals("BOOKED"))
                    ticketService.removeTicket(ticket);
        }
    }

    public boolean removeTicketsOfDepartedSchedule(String scheduleID) {
        ObservableList<Ticket> tickets = ticketService.getTickets(scheduleID);

        if (tickets != null && tickets.size() > 0) {
            for (var ticket : tickets)
                ticketService.removeTicket(ticket);
            return true;
        } else
            return false;
    }
}
