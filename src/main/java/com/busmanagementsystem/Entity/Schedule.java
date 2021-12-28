package Entity;
import java.io.Serializable;

public class Schedule implements Serializable {
    private String scheduleID;
    private String busID;
    private String driverID;
    private String startingPoint;
    private String destination;
    private String departureTime;
    private int price;

    public Schedule(){

    }
    public Schedule(String scheduleID, String busID, String driverID, String startingPoint, String destination, String departureTime, int price)
    {
        this.scheduleID = scheduleID;
        this.busID = busID;
        this.driverID = driverID;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.departureTime = departureTime;
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

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        price = price;
    }
}
