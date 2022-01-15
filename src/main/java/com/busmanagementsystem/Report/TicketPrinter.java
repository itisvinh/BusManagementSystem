package com.busmanagementsystem.Report;

import com.busmanagementsystem.Database.Pojos.ExtTicket;
import com.busmanagementsystem.Database.Services.Ticket_Seat_Service;
import javafx.concurrent.Task;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import com.busmanagementsystem.Database.Configs.DBConnection;
import org.controlsfx.control.Notifications;

import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TicketPrinter {
    private Ticket_Seat_Service ticketSeatService = new Ticket_Seat_Service();
    private int numberOfSeats;
    private String from;
    private String to;
    private String plateNumber;
    private String customerName;
    private String phone;
    private String departureTime;
    private float price;
    private String ticketID;


    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public TicketPrinter() {
        from = to = plateNumber = customerName = phone = "";
        price = 0;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private String createListOfSeatsNumbers(String ticketID) {
        List<Integer> list = ticketSeatService.getSeatsNumbersOf(ticketID);
        if (list != null) {
            numberOfSeats = list.size();
            return list.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public void print() {
        try {
            JasperDesign design = JRXmlLoader.load("C:/Ticket.jrxml");
            JasperReport report = JasperCompileManager.compileReport(design);
            var map = new HashMap<String, Object>();
            map.put("From", from);
            map.put("To", to);
            map.put("PlateNumber", plateNumber);
            map.put("Tel", phone);
            map.put("Name", customerName);
            map.put("Date", LocalDate.now().toString() + " " + departureTime);
            map.put("Seat", createListOfSeatsNumbers(ticketID));
            map.put("Price", (double)price * numberOfSeats);

            JasperPrint print = JasperFillManager.fillReport(report, map, new JREmptyDataSource());
//            print.setPageHeight(500);
//            print.setPageWidth(500);
            JasperViewer.viewReport(print, false);
            // JasperPrintManager.printReport(print, true);

        } catch (JRException jrException) {
            System.out.println(jrException);
            Notifications notifications = Notifications.create().title("Printer service")
                    .text("The ticket form [Ticket.jrxml] cannot be found at the default path [C:/]")
                    .darkStyle()
                    .hideAfter(Duration.seconds(5));
            notifications.showWarning();

        } catch (Exception ex) {
            System.out.println(ex);
            Notifications notifications = Notifications.create().title("Printer service")
                    .text("Cannot print ticket")
                    .darkStyle()
                    .hideAfter(Duration.seconds(5));
            notifications.showWarning();
        }
    }


}