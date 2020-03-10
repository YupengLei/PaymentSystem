package paymentsystem;

import javafx.beans.property.*;

public class RequestData {
    private final IntegerProperty RTid;
    private final DoubleProperty Amount;
    private final StringProperty DateTime;
    private final StringProperty Memo;
    private final StringProperty Status;
    private final StringProperty Name;
    private final IntegerProperty Percentage;
    public RequestData(Integer RTid, Double Amount, String DateTime, String Memo, String Status, String Name,  Integer Percentage) {
        this.RTid = new SimpleIntegerProperty(RTid);
        this.Amount = new SimpleDoubleProperty(Amount);
        this.DateTime = new SimpleStringProperty(DateTime);
        this.Memo = new SimpleStringProperty(Memo);
        this.Status = new SimpleStringProperty(Status);
        this.Name = new SimpleStringProperty(Name);
        this.Percentage = new SimpleIntegerProperty(Percentage);
    }
    public Integer getRTid(){ return RTid.get();}
    public Double getAmount(){ return Amount.get();}
    public Object getDateTime(){ return DateTime.get();}
    public String getMemo(){return Memo.get();}
    public String getStatus(){return Status.get();}
    public String getName(){return Name.get();}
    public Integer getPercentage(){ return Percentage.get();}
}

