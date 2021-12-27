package com.busmanagementsystem.Database.Pojos;

import com.busmanagementsystem.Database.Configs.DBConnection;
import javafx.scene.control.ComboBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import static com.busmanagementsystem.Database.Services.Utilities.concatAll;

public class Bus {
//    int a;
//    int b;
//    int c;
//
//    public Bus(int a, int b, int c) throws Exception{
//        ComboBox comboBoxBus;
//        var list = new ArrayList<Bus>();
//        Connection conn = DBConnection.getConn();
//        Statement statement = conn.createStatement();
//        String sql = "select SeatID, SeatNumber, BusID, " +
//                "case when  if exists(select * from Seats_Tickets st where st.SeatID = seatID) as IsOccupied" +
//                "from Seats where BusID = " + comboBoxBus.getSelectionModel().getSelectedItem().toString();
//
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        while (resultSet.next()) {
//            Bus row;
//
//            list.add(row);
//        }
//
//        // interface
//        HashMap<String, FontIcon> map = new HashMap<>();
//
//        Node
//
//    }
//
//
//
//    public int getA() {
//        return a;
//    }
//
//    public void setA(int a) {
//        this.a = a;
//    }
//
//    public void setC(int c) {
//        this.c = c;
//    }
//
//    public int getB() {
//        return b;
//    }
//
//    public void setB(int b) {
//        this.b = b;
//    }
//
//    public int getC() {
//        return c;
//    }
}
