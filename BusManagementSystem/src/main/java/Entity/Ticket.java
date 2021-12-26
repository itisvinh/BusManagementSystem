package Entity;
import java.io.Serializable;

public class Ticket implements Serializable {
    private String ticketID;
    private String scheduleID;
    private String customerID;
    private String bookingStatus;
    private int price;
    public Ticket(){

    }
    public Ticket(String ticketID, String scheduleID, String customerID, String bookingStatus, int price)
    {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.customerID = customerID;
        this.bookingStatus = bookingStatus;
        this.setPrice(price);
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
