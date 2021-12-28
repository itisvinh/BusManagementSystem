package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Entity.Bus;
import com.busmanagementsystem.Database.Configs.DBConnection;

public class LopBus {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    public LopBus(){
        ketNoi();
    }
    public boolean ThemBus(String ten, int soghe, int idp){
        String sql = "INSERT INTO  `phong`(`ten`, `soghe`, `idphong`) VALUES ('" + ten + "','" + soghe + "','" + id + "')";
        try {
            statement.execute(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean ketNoi(){
        try {
            connection = DBConnection.getConn();
            statement = connection.createStatement();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Bus> DanhSachBus(){
        String sql = "SELECT p.`idphong`, p.`tenphong`, p.`soghe`, p.`idrap`, r.`tenrap` FROM `phong` AS p, `rap` AS r  WHERE r.`idrap` =  p.`idrap` LIMIT 100;";
        List<Bus> DSBus = new ArrayList();
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DSBus.add(new Bus());
            }
        } catch (SQLException ex) {
            Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return DSBus;
    }

    public void LuuBus(TableModel model){
        String sql = "";
        for (int i = 0; i < model.getRowCount(); i++) {
            sql += "UPDATE `phong` SET `tenphong`=\"" + model.getValueAt(i, 1) + "\",`soghe`=\"" + model.getValueAt(i, 2) + "\",`idrap`=\"" + model.getValueAt(i, 3) + "\" WHERE `idphong` = \"" + model.getValueAt(i, 0) + "\";";
        }
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void XoaBus(JTable table){
        String sql = "";
        int[] selectedRows = table.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            sql += "DELETE FROM `phong` WHERE `idBus`='" + table.getModel().getValueAt(selectedRows[i], 0) + "';";
        }
        try {
            statement.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(LopBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
