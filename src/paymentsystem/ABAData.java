package paymentsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ABAData {

    private final StringProperty BankID;
    private final StringProperty BANumber;
    private final StringProperty Verified;

    public ABAData(String BankID, String BANumber, String Verified) {
        this.BankID = new SimpleStringProperty(BankID);
        this.BANumber = new SimpleStringProperty(BANumber);
        this.Verified = new SimpleStringProperty(Verified);

    }

    public String getBankID(){return BankID.get();}
    public String getBANumber(){return BANumber.get();}
    public String getVerified(){return Verified.get();}

}
