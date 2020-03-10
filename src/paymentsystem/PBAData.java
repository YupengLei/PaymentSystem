package paymentsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PBAData {
    private final StringProperty BankID;
    private final StringProperty BANumber;
    private final StringProperty PBAVerified;
    public PBAData(String BankID, String BANumber, String PBAVerified) {
        this.BankID = new SimpleStringProperty(BankID);
        this.BANumber = new SimpleStringProperty(BANumber);
        this.PBAVerified = new SimpleStringProperty(PBAVerified);
    }
    public String getBankID(){return BankID.get();}
    public String getBANumber(){return BANumber.get();}
    public String getPBAVerified(){return PBAVerified.get();}
}