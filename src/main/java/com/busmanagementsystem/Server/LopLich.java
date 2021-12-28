package Server;
import Entity.Schedule;
import com.busmanagementsystem.Database.Configs.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LopLich {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    private int idlich;
    private String tenlich;
    private int giaVe;

    public LopLich(){
        ketNoi();
    }

    private boolean ketNoi(){
        try {
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            return true;
        } catch (ClassNotFoundException | SQLException exception ){
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, exception);
        }
        return false;
    }

    public int getIdlich() {
        return idlich;
    }

    public void setIdlich(int idlich) {
        this.idlich = idlich;
    }

    public String getTenlich() {
        return tenlich;
    }

    public void setTenlich(String tenlich) {
        this.tenlich = tenlich;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(int giaVe) {
        this.giaVe = giaVe;
    }
    public List<Schedule> DanhSachLich(int idBus, int idDriver){
        String sql = "SELECT l.`idlich`, l.`ngay`, l.`giobatdau`, l.`giave` FROM `lich` AS l WHERE ";
        List<Schedule> DSLich = new ArrayList();
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DSLich.add(new Schedule(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopLich.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LopLich.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return DSLich;
    }

}
