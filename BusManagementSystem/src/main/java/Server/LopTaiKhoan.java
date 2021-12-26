package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LopTaiKhoan {
    private final Config config = new Config();
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet;

    public boolean dangNhap(String user, String password){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(config.DB_URL, config.DB_USER, config.DB_PASS);
            statement = connection.createStatement();
            String sql = "SELECT * FROM taikhoan WHERE taikhoan = '"+ user +"' AND matkhau ='"+ password +"'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                return true;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LopTaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
