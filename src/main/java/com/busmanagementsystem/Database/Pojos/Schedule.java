package com.busmanagementsystem.Database.Pojos;

import java.sql.Time;
import java.time.LocalTime;

public class Schedule {
    private String scheduleID;
    private String busID;
    private String driverID;
    private String startingLocation;
    private String destination;
    private Time departureTime;
    private float price;

    public Schedule() {}

    public Schedule(String scheduleId, String busId, String driverId, String startingLocation,
                    String destination, Time departureTime, float price) {
        this.scheduleID = scheduleId;
        this.busID = busId;
        this.driverID = driverId;
        this.startingLocation = startingLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
    }



    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleID='" + scheduleID + '\'' +
                ", busID='" + busID + '\'' +
                ", driverID='" + driverID + '\'' +
                ", startingLocation='" + startingLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", price=" + price +
                '}';
    }
}
