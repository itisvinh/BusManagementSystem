package com.busmanagementsystem.Interface;

import com.busmanagementsystem.Communicator;
import com.busmanagementsystem.Database.Pojos.*;
import com.busmanagementsystem.Database.Services.BusService;
import com.busmanagementsystem.Database.Services.RouteService;
import com.busmanagementsystem.Database.Services.TicketService;
import com.busmanagementsystem.Database.Services.Ticket_Seat_Service;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import java.util.stream.Collectors;

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
    @FXML
    private AnchorPane changeConfirmation;
    @FXML
    private Button searchTickets;
    private LinkedList<VBox> selectedSeats = new LinkedList<>();
    private ObservableList<ExtSeat> seats;
    private String currentScheduleID;
    private String currentBusID;
    private String currentTicketID;
    private String prevScheduleID;
    private String prevTicketID;
    private LinkedList<VBox> prevSelectedSeats;
    private ObservableList<ExtSeat> prevSeats;

    @Override
    public void searchForRoutes(ActionEvent actionEvent) {
        try {
            if (changeConfirmation.isVisible() && prevTicketID == null)
                prepareNotification("Ticket Service", "Select a ticket to change to difference schedule").showWarning();
            else
                startSearchFilterStage();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.toString());
            alert.show();
        }
    }

    // called whenever route search filter is changed
    @Override
    protected void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        // event when user click filter search and it returns a query
        if (!routesSearchFilterQuery.getValue().equals("##")) {
            displayAllSeatsEmpty();
            List<String> routeIDs = busService.loadBuses(comboBoxBus, routesSearchFilterQuery.getValue());
            // get only the first one in case of multi-return obj
            currentScheduleID = routeIDs.get(0);
            System.out.println(currentScheduleID);
            System.out.println("ticket - changed: " + routesSearchFilterQuery.getValue());
            comboBoxBus.setDisable(false);
            tabPane.setDisable(true);
            currentTicketID = null;
            currentBusID = null;
        }
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
        changeConfirmation.setVisible(false);
        prepareNotification("Notice", "Select a bus to view seats").showInformation();
    }

    private void displayAllSeatsEmpty() {
        for (Tab tab : tabPane.getTabs()) {
            GridPane gridPane = (GridPane) ((AnchorPane) tab.getContent() ).getChildren().get(0);
            for (Node node : gridPane.getChildren())
                ((FontIcon) ((VBox) node).getChildren().get(0)).setIconColor(Color.BLACK);
        }
    }

    // read all seats info from database into the seats list
    public void updateSeats() {
        Bus bus = (Bus) comboBoxBus.getSelectionModel().getSelectedItem();
        System.out.println(currentScheduleID + " - " + currentBusID);
        if (bus != null) {
            // load seats into the observable list
            seats = busService.loadSeats(currentScheduleID, currentBusID);
            System.out.println("buses size: " + seats.size());
            updateSeatsStatus();
            selectedSeats.clear();
            currentTicketID = null;
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
            default:
                fontIcon.setIconColor(Color.BLACK);
                break;
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
            tabPane.setDisable(false);
            currentBusID = bus.getBusID();
            updateSeats();
            clearSelection.setDisable(true);
        }
    }

    public void onActionClearSelection(ActionEvent event) {
        if (selectedSeats.size() > 0) {
            updateSeats();
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

    private Ticket getSelectedTicket(String bookingStatus) {
        Text text = (Text) selectedSeats.get(0).getChildren().get(1);
        int index = Integer.valueOf(text.getText()) - 1;
        ExtSeat seat = seats.get(index);
        Ticket ticket = new Ticket();
        ticket.setTicketID(seat.getTicketID());
        ticket.setBookingStatus(bookingStatus);
        return ticket;
    }

    public void onActionPurchaseTicket(ActionEvent event) {

            try {
                if (selectedSeats.size() > 0) {
                    LocalTime departureTime = getDepartureTime();
                    if (LocalTime.now().plusMinutes(5).isBefore(departureTime)) {
                        boolean canPurchase = true;
                        boolean allEmpty = true;
                        boolean allBooked = true;
                        for (VBox vBox : selectedSeats) {
                            Text text = (Text) vBox.getChildren().get(1);
                            int index = Integer.valueOf(text.getText()) - 1;
                            String bookingStatus = seats.get(index).getBookingStatus();

                            if (bookingStatus.equals("PURCHASED")) {
                                prepareNotification("Ticket Service", "Cannot Purchase seat [" + String.valueOf(index + 1) + "]!\nIt is already PURCHASED").showWarning();
                                canPurchase = false;
                            } else if (bookingStatus.equals("EMPTY"))
                                allBooked = false;
                            else
                                allEmpty = false;
                        }
                        if (canPurchase && allEmpty)
                            tryAddingNewTicket("PURCHASED");
                        else if (canPurchase && allBooked) {
                            int affected_rows = ticketService.editTicketBookingStatus(getSelectedTicket("PURCHASED"));

                            if (affected_rows > 0) {
                                updateSeats();
                                prepareNotification("Ticket_Seat Service", "Purchase ticket successfully").showInformation();
                            } else
                                prepareNotification("Ticket_Seat Service", "Cannot purchase the ticket").showWarning();
                        } else
                            prepareNotification("Ticket_Seat Service", "ALL seats of the ticket HAVE TO be selected").showWarning();

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
                    int index = Integer.valueOf(text.getText()) - 1;
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
                            prepareNotification("Ticket Service", "Cannot Book seat [" + String.valueOf(index + 1) + "]!\nIt is already PURCHASED").showWarning();
                            canAdd = false;
                        } else if (bookingStatus.equals("BOOKED")) {
                            prepareNotification("Ticket Service", "Cannot Book this seat [" + String.valueOf(index + 1) + "]!\nIt is already BOOKED").showWarning();
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
                System.out.println(ticket);
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
                currentTicketID = ticket.getTicketID();
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
        if (currentTicketID != null) {
            System.out.println(currentTicketID);
            /* check for selectedSeats for not null if wanted to HERE */
            boolean canRemove = true;
            for (VBox vBox : selectedSeats) {
                int index = Integer.valueOf(((Text) vBox.getChildren().get(1)).getText()) - 1;
                if (seats.get(index).getTicketID() == null || !seats.get(index).getTicketID().equals(currentTicketID)
                        || seats.get(index).getBookingStatus().equals("PURCHASED")) {
                    canRemove = false;
                    break;
                }
                System.out.println(currentTicketID + " - " + seats.get(index).getTicketID());
            }

            if (canRemove) {
                Ticket ticket = new Ticket();
                ticket.setTicketID(currentTicketID);
                int af_rows = ticketService.removeTicket(ticket);

                if (af_rows > 0) {
                    prepareNotification("Ticket Service", "Remove ticket successfully").showInformation();
                    updateSeats();
                }
                else
                    prepareNotification("Ticket Service", "Failed to remove ticket").showError();
            } else
                prepareNotification("Ticket Service", "Cannot decline EMPTY or PURCHASED ticket").showWarning();

        } else
            prepareNotification("Ticket Service", "Please select a ticket").showInformation();
    }

    private boolean allSelectedSeatsFromTheSameTicket() {
        boolean isTrue = true;
        int index = Integer.valueOf(((Text) selectedSeats.get(0).getChildren().get(1)).getText()) - 1;
        String ticketID = seats.get(index).getTicketID();

        for (VBox vBox : selectedSeats) {
            index =  Integer.valueOf(((Text) vBox.getChildren().get(1)).getText()) - 1;
            if (ticketID == null) {
                if (seats.get(index).getTicketID() != null) {
                    isTrue = false;
                    break;
                }
            } else
                if (!ticketID.equals(seats.get(index).getTicketID())) {
                    isTrue = false;
                    break;
                }
        }
        return isTrue;
    }

    public void onActionChangeSeats(ActionEvent event) {
        // if there is any seat being selected
        LocalTime departureTime = getDepartureTime();
        if (LocalTime.now().plusMinutes(60).isBefore(departureTime)) {
            if (selectedSeats.size() > 0) {
                if (allSelectedSeatsFromTheSameTicket() && canChangeWhere("BOOKED")) {
                    prevScheduleID = currentScheduleID;
                    prevSelectedSeats = (LinkedList<VBox>) selectedSeats.clone();
                    prevSeats = FXCollections.observableArrayList(seats);
                    prevTicketID = currentTicketID;
                    searchTickets.setDisable(true);
                    ticketFunctionalButtonArea.setDisable(true);
                    changeConfirmation.setVisible(true);
                    updateSeats();
                } else
                    prepareNotification("Ticket Service", "Can change BOOKED seats ONLY").showInformation();
            } else
                prepareNotification("Ticket Service", "Select seats to change").showInformation();
        } else
            prepareNotification("Ticket Service", "Tickets can ONLY be changed 1 HOUR before the time of [" + departureTime + "]").showInformation();

    }

    // check if selected seats can be changed with the status [bookingStatus]
    private boolean canChangeWhere(String bookingStatus) {
        for (var seat : selectedSeats) {
            int index = Integer.valueOf(((Text) seat.getChildren().get(1)).getText()) - 1;
            if (!seats.get(index).getBookingStatus().equals(bookingStatus))
                return false;
        }
        return true;
    }

    // update seats of a ticket on the same bus
    private boolean updateSeatsOfTicket(List<ExtSeat> old_seats, List<ExtSeat> new_seats) {
        boolean updated = true;
        int af_rows = ticketService.updateSeatsOfTicket(old_seats, new_seats);
        if (af_rows == old_seats.size())
            prepareNotification("Ticket Service", "Change successfully").showInformation();
        else if (af_rows <= 0) {
            prepareNotification("Ticket Service", "Failed to change seats").showError();
            updated = false;
        } else
            prepareNotification("Ticket Service", "SOME seats were failed to change").showWarning();
        return updated;
    }

    private boolean updateScheduleOfTicket() {
        var old_seats = VBoxToExtSeatList(prevSelectedSeats, prevSeats);
        var new_seats = VBoxToExtSeatList(selectedSeats, seats);
        if (updateSeatsOfTicket(old_seats, new_seats)) {
            if (ticketService.updateScheduleOfTicket(prevTicketID, currentScheduleID) == 1)
                return  true;
        }
        return false;
    }

    // free previous copy of current objs
    private void finalizingChanges() {
        changeConfirmation.setVisible(false);
        ticketFunctionalButtonArea.setDisable(false);
        updateSeats();
        prevTicketID = null;
        prevSelectedSeats = null;
        prevSeats = null;
        prevScheduleID = null;
        searchTickets.setDisable(false);
    }

    public void onActionConfirmChange(ActionEvent event) {
        if (selectedSeats.size() > 0) {
              // cap nhat cac ghe duoc chon
              if (canChangeWhere("EMPTY")) {
                    if (selectedSeats.size() == prevSelectedSeats.size() ) {
                        // change selected ones
                        System.out.println("Schedules: " + prevScheduleID + " - " + currentScheduleID);
                        if (prevScheduleID.equals(currentScheduleID)) {
                            // same bus
                            var old_seats = VBoxToExtSeatList(prevSelectedSeats, seats);
                            var new_seats = VBoxToExtSeatList(selectedSeats, seats);
                            if (updateSeatsOfTicket(old_seats, new_seats)) {
                                finalizingChanges();
                                prepareNotification("Ticket Service", "Change successfully").showInformation();
                            } else
                                prepareNotification("Ticket Service", "Failed to change").showError();

                            System.out.println("same bus");
                        } else {
                            // difference schedule
                            if (updateScheduleOfTicket()) {
                                finalizingChanges();
                                prepareNotification("Ticket Service", "Change successfully").showInformation();
                            } else
                                prepareNotification("Ticket Service", "Failed to change").showError();


                            System.out.println("difference schedule");
                            if (prevTicketID != null) {
                                System.out.println("ticket selected");
                            } else
                                prepareNotification("Ticket Service", "Select " + prevSelectedSeats.size() + " targeting seats to change into\nThe number of BOOKED seats have to be the SAME").showWarning();
                        }
                    } else
                        prepareNotification("Ticket Service", "Select " + prevSelectedSeats.size() + " targeting seats to change into").showWarning();
              } else
                  prepareNotification("Ticket Service", "PURCHASED and BOOKED seats cannot be selected").showWarning();

        } else
          prepareNotification("Ticket Service", "Select targeting seats to change into").showWarning();
    }

    private List<ExtSeat> VBoxToExtSeatList(List<VBox> sel_seats, List<ExtSeat> extSeats) {
        return sel_seats.stream()
                .map(i -> {
                    int index = Integer.valueOf(((Text) i.getChildren().get(1)).getText()) - 1;
                    return extSeats.get(index);
                }).sorted(Comparator.comparingInt(ExtSeat::getSeatNumber))
                .collect(Collectors.toList());
    }

    public void onActionCancelChange(ActionEvent event) {
        finalizingChanges();
    }

}
