package com.busmanagementsystem.Database.Pojos;

import java.time.LocalTime;

public class Schedule {
    private String scheduleId;
    private int busId;
    private String driverId;
    private String startingLocation;
    private String destination;
    private LocalTime departureTime;
    private float price;

    public Schedule() {}

    public Schedule(String scheduleId, int busId, String driverId, String startingLocation,
                    String destination, LocalTime departureTime, float price) {
        this.scheduleId = scheduleId;
        this.busId = busId;
        this.driverId = driverId;
        this.startingLocation = startingLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
