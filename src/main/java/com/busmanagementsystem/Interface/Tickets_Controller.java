package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.*;
import com.busmanagementsystem.Database.Services.BusService;
import com.busmanagementsystem.Database.Services.RouteService;
import com.busmanagementsystem.Database.Services.TicketService;
import com.busmanagementsystem.Database.Services.Ticket_Seat_Service;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;
import static com.busmanagementsystem.Database.Services.Utilities.prepareNotification;

public class Tickets_Controller extends Tickets_Routes_Base implements Initializable {
    private RouteService routeService = new RouteService();
    private BusService busService = new BusService();
    private TicketService ticketService = new TicketService();
    private Ticket_Seat_Service ticketSeatService = new Ticket_Seat_Service();
    @FXML
    private AnchorPane ticketFunctionalButtonArea;
    @FXML
    private ComboBox<Bus> comboBoxBus;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button viewInfo;
    @FXML
    private Button clearSelection;
    private LinkedList<VBox> selectedSeats = new LinkedList<>();
    private ObservableList<ExtSeat> seats;
    private String currentScheduleID;
    private String currentBusID;

    // called whenever route search filter is changed
    @Override
    protected void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        // event when user click filter search and it returns a query
        List<String> routeIDs = busService.loadBuses(comboBoxBus, routesSearchFilterQuery.getValue());
        // get only the first one in case of multi-return obj
        currentScheduleID = routeIDs.get(0);
        System.out.println(currentScheduleID);
        System.out.println("ticket - changed: " + routesSearchFilterQuery.getValue());
        comboBoxBus.setDisable(false);
    }
    // event for combobox bus
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startAsStrictSearch = true;
        if (Communicator.startedAsAdmin == true) {
            for (var child : ticketFunctionalButtonArea.getChildren())
                child.setDisable(true);
            viewInfo.setDisable(true);
        }
        comboBoxBus.setDisable(true);
        tabPane.setDisable(true);
        clearSelection.setDisable(true);
        viewInfo.setDisable(true);
        prepareNotification("Notice", "Select a bus to view seats").showInformation();
    }

    // read all seats info from database into the seats list
    private void updateSeats() {
        Bus bus = (Bus) comboBoxBus.getSelectionModel().getSelectedItem();
        System.out.println(currentScheduleID + " - " + currentBusID);
        if (bus != null) {
            // load seats into the observable list
            seats = busService.loadSeats(currentScheduleID, currentBusID);
            System.out.println("buses size: " + seats.size());
            updateSeatsStatus();
            System.out.println(seats);
        }
    }

    // update all seats color
    private void updateSeatsStatus() {
        for (Tab tab : tabPane.getTabs()) {
            AnchorPane anchorPane = (AnchorPane) tab.getContent();
            GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);

            for (Node node : gridPane.getChildren()) {
                VBox vBox = (VBox) node;
                FontIcon fontIcon = (FontIcon) vBox.getChildren().get(0);
                Text text = (Text) vBox.getChildren().get(1);
                int index = Integer.valueOf(text.getText()) - 1;
                setSeatColorIndicators(index, fontIcon);
            }
        }
    }

    private void setSeatColorIndicators(int index, FontIcon fontIcon) {
        switch (seats.get(index).getBookingStatus()) {
            case "BOOKED":
                fontIcon.setIconColor(Color.rgb(144, 139, 47));
                break;
            case "PURCHASED":
                fontIcon.setIconColor(Color.rgb(35, 117, 27));
                break;
            case "EMPTY":
                fontIcon.setIconColor(Color.BLACK);
        }
    }

    public void onMouseClickSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        ((FontIcon) vBox.getChildren().get(0)).setIconColor(Color.RED);
        selectedSeats.add(vBox);
        clearSelection.setDisable(false);
    }

    public void onMouseEnterSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: green; -fx-border-radius: 15");
    }

    public void onMouseExitSeat(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.setStyle("-fx-border-color: transparent;");
    }

    public void onActionSelectBus(ActionEvent event) {
        Bus bus = (Bus) comboBoxBus.getSelectionModel().getSelectedItem();
        if (bus != null) {
            currentBusID = bus.getBusID();
            updateSeats();
            tabPane.setDisable(false);
        }
    }

    public void onActionClearSelection(ActionEvent event) {
        if (selectedSeats.size() > 0) {
            for (Node node : selectedSeats) {
                VBox vBox = (VBox) node;
                Text text = (Text) vBox.getChildren().get(1);
                FontIcon fontIcon = (FontIcon) vBox.getChildren().get(0);
                setSeatColorIndicators(Integer.valueOf(text.getText()), fontIcon);
            }
            selectedSeats.clear();
            clearSelection.setDisable(true);
        }
    }

    private String startCustomerSelectorStage() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Util_Scenes/CustomerSelector_Scene.fxml"));
        Parent parent = fxmlLoader.load();
        CustomerSelector_Controller controller = (CustomerSelector_Controller) fxmlLoader.getController();

        Customer customer = new Customer();
        controller.setCustomerCapturer(customer);

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.showAndWait();

        if (customer.getCustomerID() != null && !customer.getCustomerID().isBlank())
            return customer.getCustomerID();
        return null;
    }

    public void onActionPurchaseTicket(ActionEvent event) {

            try {
                if (selectedSeats.size() > 0) {
                    LocalTime departureTime = getDepartureTime();
                    if (LocalTime.now().plusMinutes(5).isBefore(departureTime)) {
//                        boolean canAdd = true;
//                        for (VBox vBox : selectedSeats) {
//                            Text text = (Text) vBox.getChildren().get(1);
//                            int index = Integer.valueOf(text.getText()) - 1;
//                            String bookingStatus = seats.get(index).getBookingStatus();
//
//                            if (bookingStatus.equals("PURCHASED")) {
//                                prepareNotification("Ticket Service", "Cannot Book seat [" + index + "]!\nIt is already PURCHASED").showWarning();
//                                canAdd = false;
//                            } else if (bookingStatus.equals("BOOKED")) {
//                                prepareNotification("Ticket Service", "Cannot Book this seat [" + index + "]!\nIt is already BOOKED").showWarning();
//                                canAdd = false;
//                            }
//                        }
//                        if (canAdd)
//                            tryAddingNewTicket("PURCHASED");

                    } else
                        prepareNotification("Ticket Service", "These seats can only be purchased\n 5 MINUTES PRIOR the time of: " + departureTime.toString()).showWarning();
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
    }

    private void tryAddingNewTicket(String bookingStatus) throws Exception{
        // try to add or select a customer
        String customerID = "";
        if ((customerID = startCustomerSelectorStage()) != null) {
            //added successfully , return customerID
            prepareNotification("Customer Service", "Selected Customer [ " + customerID + " ]").showInformation();

            // try add new ticket with corresponding customerID
            Ticket ticket = new Ticket("", currentScheduleID, customerID, bookingStatus);
            ticket = ticketService.addNewTicket(ticket);
            String ticketID = ticket.getTicketID();

            if (!ticketID.isBlank()) {
                // add new tickets_seats
                prepareNotification("Ticket Service", "Added new Ticket successfully").showInformation();
                int affected_rows = 0;
                for (VBox selectedSeat : selectedSeats) {
                    Text text = (Text) selectedSeat.getChildren().get(1);
                    int index = Integer.valueOf(text.getText());
                    String seatID = seats.get(index).getSeatID();

                    affected_rows += ticketSeatService.addNewTicket_Seat(new Ticket_Seat(ticketID, seatID));
                }
                if (affected_rows > 0) {
                    // added tickets _ seats successfully , reload
                    updateSeats();
                    prepareNotification("Ticket_Seat Service", "Assigning seats to ticket successfully").showInformation();
                } else
                    prepareNotification("Ticket_Seat Service", "Failed to assign seats to ticket").showWarning();

            } else
                prepareNotification("Ticket Service", "Failed to add new Ticket").showWarning();
        }
    }

    // get departureTime from seats list
    private LocalTime getDepartureTime() {
        Time time = routeService.getDepartureTimeOf(currentScheduleID);
        return (time != null) ? time.toLocalTime() : null;
    }

    // implement: p -> ko dc book
    public void onActionBookTicket(ActionEvent event) {
        try {
            if (selectedSeats.size() > 0) {
                // notice here
                LocalTime departureTime = getDepartureTime();
                if (LocalTime.now().plusMinutes(5).isBefore(departureTime)) {
                    boolean canAdd = true;
                    for (VBox vBox : selectedSeats) {
                        Text text = (Text) vBox.getChildren().get(1);
                        int index = Integer.valueOf(text.getText()) - 1;
                        String bookingStatus = seats.get(index).getBookingStatus();

                        if (bookingStatus.equals("PURCHASED")) {
                            prepareNotification("Ticket Service", "Cannot Book seat [" + index + "]!\nIt is already PURCHASED").showWarning();
                            canAdd = false;
                        } else if (bookingStatus.equals("BOOKED")) {
                            prepareNotification("Ticket Service", "Cannot Book this seat [" + index + "]!\nIt is already BOOKED").showWarning();
                            canAdd = false;
                        }
                    }
                    if (canAdd)
                        tryAddingNewTicket("BOOKED");

                } else
                    prepareNotification("Ticket Service", "These seats can only be booked\n 1 HOUR PRIOR the time of: " + departureTime.toString()).showWarning();
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private ExtTicket startTicketSearchStage() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainWorkingArea_Sub_Scenes/Util_Scenes/TicketSearch_Scene.fxml"));
        Parent parent = fxmlLoader.load();
        TicketSearch_Controller controller = (TicketSearch_Controller) fxmlLoader.getController();

        ExtTicket ticket = new ExtTicket();
        controller.setExtTicketCapturer(ticket);

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.showAndWait();

        if (ticket.getTicketID() == null)
            return null;
        return ticket;
    }

    private void autoSelectSeats(String ticketID) {
        selectedSeats.clear();
        for (Tab tab : tabPane.getTabs()) {
            AnchorPane anchorPane = (AnchorPane) tab.getContent();
            GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);
            for (Node node : gridPane.getChildren()) {
                VBox vBox = (VBox) node;
                Text text = (Text) vBox.getChildren().get(1);
                int index = Integer.valueOf(text.getText()) - 1;

                if (seats.get(index).getTicketID() != null && seats.get(index).getTicketID().equals(ticketID)) {
                    FontIcon fontIcon = (FontIcon) vBox.getChildren().get(0);
                    fontIcon.setIconColor(Color.RED);
                    selectedSeats.add(vBox);
                }
            }
        }
    }

    public void onActionSearchTickets(ActionEvent event) {
        try {
            ExtTicket ticket = startTicketSearchStage();
            if (ticket != null) {
                // set currentScheduleID
                currentScheduleID = ticket.getScheduleID();
                // enable comboBoxBus
                comboBoxBus.setDisable(false);
                // load buses into comboBoxBus
                busService.loadBuses(comboBoxBus, concatAll(ticket.getFrom(), "#", ticket.getTo(), "#", ticket.getDepartureTime().toString()));
                // select matched busID of the list
                for (Bus bus : comboBoxBus.getItems())
                    if (bus.getBusID().equals(ticket.getBusID())) {
                        comboBoxBus.getSelectionModel().select(bus);
                        break;
                    }
                updateSeats();
                autoSelectSeats(ticket.getTicketID());
                clearSelection.setDisable(false);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void setCellFactories(TableView<ExtTicket> tv) {
        String[] fields = new String[] {"ticketID", "scheduleID", "from", "to", "departureTime",
                "busID", "busPlateNumber", "customerName", "phoneNumber"};

        int i = 0;
        for (TableColumn tableColumn : tv.getColumns())
            tableColumn.setCellValueFactory(new PropertyValueFactory(fields[i++]));
    }

    public void onActionDeclineTicket(ActionEvent event) {

    }
}
