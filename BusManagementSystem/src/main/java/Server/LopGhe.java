package Server;
import Entity.Seat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LopGhe {
    private final Config config = new Config();
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    public LopGhe(){
        ketNoi();
    }

    private boolean ketNoi(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection();
            statement = connection.createStatement();
            return true;
        } catch (ClassNotFoundException | SQLException exception ){
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, exception);
        }
        return false;
    }

    public List<Seat> ListSeat(int idBus){
        //String sql = "SELECT '', ''
        //List(Seat) lsSeat = new ArrayList();
        try{
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                //lsSeat.add(new Seat(resultSet.getInt(1), resultSet.getString(2), ));
            }
        } catch (SQLException exception){
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, exception);
        }
        return lsSeat;
    }
    public ArrayList<Integer> DanhSachGheDaDat(int idBus){
        //String sql = "SELECT v.`idghe` FROM `ve` as v, `lichphim` as l WHERE v.`idlich`=l.idlich AND l.idphong='" + idBus + "'";
        ArrayList<Integer> DSGheDaDat = new ArrayList();
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DSGheDaDat.add(resultSet.getInt(1));
                System.out.println(resultSet.getInt(1));
            }


        } catch (SQLException ex) {
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return DSGheDaDat;

    }

    public int soHang(int idBus){
        int result = 0;
        String sql = "SELECT MAX(`hang`) FROM `ghe` WHERE `idBus`='" + idBus + "'";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    public int soCot(int idBus){
        int result = 0;
        String sql = "SELECT MAX(`cot`) FROM `ghe` WHERE `idBus`='" + idBus + "'";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LopGhe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
