package Entity;
import java.io.Serializable;

public class Bus implements Serializable {
    private String busID;
    private String busPlateNumber;

    public Bus(){

    }
    public Bus (String busID, String busPlateNumber)
    {
        this.busID = busID;
        this.busPlateNumber = busPlateNumber;
    }
    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public String getBusPlateNumber() {
        return busPlateNumber;
    }

    public void setBusPlateNumber(String busPlateNumber) {
        this.busPlateNumber = busPlateNumber;
    }
}
