package com.busmanagementsystem.Database.Pojos;

import java.sql.Time;

// ExtTicket contain the fields of joining tables
public class ExtTicket {
    private String ticketID, scheduleID, busID;
    private String from, to;
    private String busPlateNumber;
    private String customerName, phoneNumber;
    private Time departureTime;

    public ExtTicket() {
    }

    public ExtTicket(ExtTicket t) {
        this(t.ticketID, t.scheduleID, t.busID, t.from, t.to, t.busPlateNumber, t.customerName, t.phoneNumber, t.departureTime);
    }

    public ExtTicket(String ticketID, String scheduleID, String busID, String from,
                     String to, String busPlateNumber, String customerName, String phoneNumber, Time departureTime) {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.busID = busID;
        this.from = from;
        this.to = to;
        this.busPlateNumber = busPlateNumber;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.departureTime = departureTime;
    }

    public void setValues(ExtTicket t) {
        setValues(t.ticketID, t.scheduleID, t.busID, t.from, t.to,
                t.busPlateNumber, t.customerName, t.phoneNumber, t.departureTime);

    }

    public void setValues(String ticketID, String scheduleID, String busID, String from, String to,
                          String busPlateNumber, String customerName, String phoneNumber, Time departureTime) {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.busID = busID;
        this.from = from;
        this.to = to;
        this.busPlateNumber = busPlateNumber;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.departureTime = departureTime;
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

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBusPlateNumber() {
        return busPlateNumber;
    }

    public void setBusPlateNumber(String busPlateNumber) {
        this.busPlateNumber = busPlateNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "ExtTicket{" +
                "ticketID='" + ticketID + '\'' +
                ", scheduleID='" + scheduleID + '\'' +
                ", busID='" + busID + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", busPlateNumber='" + busPlateNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", departureTime=" + departureTime +
                '}';
    }
}
