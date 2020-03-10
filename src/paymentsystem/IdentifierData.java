package paymentsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IdentifierData {
    private final StringProperty Identifier;
    private final StringProperty Type;
    private final StringProperty Verified;
    public IdentifierData (String Identifier, String Type, String Verified) {
        this.Identifier = new SimpleStringProperty(Identifier);
        this.Type= new SimpleStringProperty(Type);
        this.Verified = new SimpleStringProperty(Verified);
    }
    public String getIdentifier(){return Identifier.get();}
    public String getType(){return Type.get();}
    public String getVerified(){return Verified.get();}
}
