package com.busmanagementsystem.Database.Pojos;

import java.sql.Time;

public class ExtSeat {
    private String seatID;
    private String busID;
    private String ticketID;
    private String scheduleID;
    private String customerID;
    private int seatNumber;
    private String bookingStatus;
    private Time departureTime;

    public ExtSeat() {}


    public ExtSeat(String seatID, String busID, String ticketID, String scheduleID, String customerID, int seatNumber, String bookingStatus, Time departureTime) {
        this.seatID = seatID;
        this.busID = busID;
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.customerID = customerID;
        this.seatNumber = seatNumber;
        this.bookingStatus = bookingStatus;
        this.departureTime = departureTime;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
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

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "ExtSeat{" +
                "seatID='" + seatID + '\'' +
                ", busID='" + busID + '\'' +
                ", ticketID='" + ticketID + '\'' +
                ", scheduleID='" + scheduleID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", seatNumber=" + seatNumber +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", departureTime=" + departureTime +
                '}';
    }
}
