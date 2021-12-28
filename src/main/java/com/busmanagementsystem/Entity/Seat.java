package Entity;
import javafx.fxml.FXML;
import java.io.Serializable;

public class Seat implements Serializable{
    private int idGhe;
    private String tenGhe;
    private int hang;
    private int cot;
    private int idBus;

    public Seat(){

    }

    public Seat(int idGhe, String tenGhe, int hang, int cot, int idBus)
    {
        this.idGhe = idGhe;
        this.tenGhe = tenGhe;
        this.hang = hang;
        this.cot = cot;
        this.idBus = idBus;
    }

    public int getIdGhe(){
        return idGhe;
    }
    public String getTenGhe(){
        return tenGhe;
    }

    public int getHang() {
        return hang;
    }

    public int getCot() {
        return cot;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdGhe(int idGhe) {
        this.idGhe = idGhe;
    }

    public void setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
    }

    public void setHang(int hang) {
        this.hang = hang;
    }

    public void setCot(int cot) {
        this.cot = cot;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }
}
