package paymentsystem;

import javafx.beans.property.*;


public class SendData {
    private final IntegerProperty STid;
    private final DoubleProperty Amount;
    private final StringProperty DateTime;
    private final StringProperty Memo;
    private final StringProperty Cancelled;
    private final StringProperty Identifier;

    public SendData(Integer STid, Double Amount, String DateTime, String Memo, String Cancelled, String Identifier) {
        this.STid = new SimpleIntegerProperty(STid);
        this.Amount = new SimpleDoubleProperty(Amount);
        this.DateTime = new SimpleStringProperty(DateTime);
        this.Memo = new SimpleStringProperty(Memo);
        this.Cancelled = new SimpleStringProperty(Cancelled);
        this.Identifier = new SimpleStringProperty(Identifier);
    }

    public Integer getSTid(){ return STid.get();}
    public Double getAmount(){ return Amount.get();}
    public Object getDateTime(){ return DateTime.get();}
    public String getMemo(){return Memo.get();}
    public String getCancelled(){return Cancelled.get();}
    public String getIdentifier(){return Identifier.get();}
}












