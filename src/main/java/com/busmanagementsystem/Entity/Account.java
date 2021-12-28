package Entity;
import java.io.Serializable;

public class Account implements Serializable {
    private String userID;
    private String ten;
    private String password;
    private boolean admin;

    public Account(){

    }
    public Account(String userID, String ten, boolean admin)
    {
        this.userID =  userID;
        this.ten = ten;
        this.admin = admin;
    }
    public Account(String userID, String ten, String password)
    {
        this.userID = userID;
        this.ten = ten;
        this.password = password;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
