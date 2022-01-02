package com.busmanagementsystem.Database.Pojos;

public class Ticket_Seat {
    private String ticketID;
    private String seatID;

    public Ticket_Seat() {
    }

    public Ticket_Seat(String ticketID, String seatID) {
        this.ticketID = ticketID;
        this.seatID = seatID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }
}
