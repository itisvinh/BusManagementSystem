package Entity;
import java.io.Serializable;
public class Customer implements Serializable{
    private String customerID;
    private String lastName;
    private String firstName;
    private boolean gender;
    private String phoneNumber;

    public Customer(){

    }

    public Customer(String customerID, String lastName, String firstName, boolean gender, String phoneNumber)
    {
        this.customerID = customerID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
