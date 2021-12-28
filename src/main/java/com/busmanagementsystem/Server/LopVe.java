package Server;
import Entity.Ticket;
import com.busmanagementsystem.Database.Configs.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LopVe {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    private String ticketID;
    private String scheduleID;
    private String customerID;
    private String bookingStatus;
    private int price;

    public LopVe(){
        ketNoi();
    }
    public LopVe(String customerID){
        this.customerID = customerID;
        ketNoi();
    }

    public LopVe(String ticketID, String scheduleID, String customerID, String bookingStatus, int price)
    {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.customerID = customerID;
        this.bookingStatus = bookingStatus;
        this.price = price;
        ketNoi();
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
    private boolean ketNoi(){
        try{
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LopVe.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public List<Ticket> DanhSachVe(String idKH){
        //String sql = "SELECT v.`idve`, v.`idschedule`, v.`idghe`, v.`idKH`, v.`ma`, g.`tenghe`, l.ngay, l.giobatdau, l.giave FROM `ve` AS v, `schedule` AS l, `bus` AS bus, `ghe` AS g WHERE `idKH`='"+idKH+"' AND l.`idschedule`=v.`idschedule`  AND l.`ngay` >= CURDATE() AND l.idBus = p.idBus AND g.idghe = v.idghe";
        List<Ticket> DSVe = new ArrayList();
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                //DSVe.add(new Ticket(resultSet.getInt(1), resultSet.getInt(2),resultSet.getInt(3), resultSet.getString(4), resultSet.getString(5),  resultSet.getString(6),  resultSet.getString(7),  resultSet.getString(8),  resultSet.getString(9),  resultSet.getString(10)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopVe.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LopVe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return DSVe;
    }

    public String DatVe(String idKH, int idVe, int idGhe){
        String code = randomstr();
        String sql = "INSERT INTO `ve`(`idschedule`, `idghe`, `idKH`,`ma`) VALUES ('"+ idVe +"','"+ idGhe +"','"+ idKH +"','"+ code +"');";
        try {
            statement.execute(sql);
            return code;
        } catch (SQLException ex) {
            Logger.getLogger(LopVe.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LopVe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    public String randomstr() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

}
