package com.busmanagementsystem.Database.Pojos;

public class Ticket {
    private String ticketID;
    private String scheduleID;
    private String customerID;
    private String bookingStatus;

    public Ticket() {
    }

    public Ticket(Ticket t) {
        this(t.ticketID, t.scheduleID, t.customerID, t.bookingStatus);
    }

    public Ticket(String ticketID, String scheduleID, String customerID, String bookingStatus) {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.customerID = customerID;
        this.bookingStatus = bookingStatus;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
