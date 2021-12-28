package Server;
import Entity.Account;
import com.busmanagementsystem.Database.Configs.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LopTaiKhoan {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    public boolean dangNhap(String user, String password){
        try {
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            String sql = "SELECT * FROM taikhoan WHERE taikhoan = '"+ user +"' AND matkhau ='"+ password +"'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopTaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public Account thongTin(String taikhoan){
        try {
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            String sql = "SELECT `taikhoan`, `ten`, `matkhau` FROM taikhoan WHERE taikhoan = '"+ taikhoan +"'";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            return new Account(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        } catch (SQLException ex) {
            Logger.getLogger(LopTaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Account();
    }

    public boolean dangNhapAdmin(String user, String password){
        try {
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            String sql = "SELECT * FROM taikhoan WHERE taikhoan = '"+ user +"' AND matkhau ='"+ password +"' AND admin='1'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopTaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
