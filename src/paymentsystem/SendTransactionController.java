package paymentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SendTransactionController implements Initializable {
    @FXML
    private Button loadSendInfo;
    @FXML
    private Button cancelRequest;
    @FXML
    private TableView<SendData> sendtransaction;
    @FXML
    private TableColumn<SendData,Integer> sSTid;
    @FXML
    private TableColumn<SendData,Double> sAmount;
    @FXML
    private TableColumn<SendData, String> sTime;
    @FXML
    private TableColumn<SendData,String> sMemo;
    @FXML
    private TableColumn<SendData,String> sCancelled;
    @FXML
    private TableColumn<SendData,String> sIdentifier;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSSN;
    @FXML
    private Label msg;
    private ObservableList<SendData>data;
    @FXML
    private Label welcome;
    String UserSSN, UserName;
    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    @FXML
    public void LoadTransactionData(ActionEvent event) throws SQLException{
        sendtransaction.setItems(null);
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND Cancelled= ? AND DateTime > NOW() - INTERVAL 10 MINUTE");
        ps.setString(1, UserSSN);
        ps.setString(2, "No");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(data);
            msg.setText("Load info successfully.");
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void CancelTransaction (ActionEvent event) throws SQLException{
        if (sendtransaction.getSelectionModel().getSelectedItem() != null) {
            SendData selectedSend = sendtransaction.getSelectionModel().getSelectedItem();
            Connection con = DbConnection.Connection();
            PreparedStatement ps1 = con.prepareStatement("UPDATE send_transaction SET Cancelled=? WHERE STid = ?");
            ps1.setString(1, "Yes");
            ps1.setInt(2, selectedSend.getSTid());
            ps1.executeUpdate();
            PreparedStatement ps2 =con.prepareStatement("SELECT * FROM send_transaction s, user_account u WHERE s.SSN=u.SSN AND STid =? ");
            ps2.setInt(1, selectedSend.getSTid());
            ResultSet rs2=ps2.executeQuery();
            while(rs2.next()){
                double newSenderBalance = rs2.getDouble("Amount") + rs2.getDouble("Balance");
                PreparedStatement ps3=con.prepareStatement("UPDATE user_account SET Balance=? WHERE SSN=?");
                ps3.setDouble(1, newSenderBalance);
                ps3.setString(2, UserSSN);
                ps3.executeUpdate();
                PreparedStatement ps4=con.prepareStatement("SELECT * FROM user_account u, electronic_address e WHERE u.SSN =e.SSN AND e.Identifier = (SELECT s.Identifier FROM send_transaction s WHERE s.STid=?)");
                ps4.setInt(1, selectedSend.getSTid());
                ResultSet rs4=ps4.executeQuery();
                while(rs4.next()){
                    double newRecipientBalance = rs4.getDouble("Balance") - rs2.getDouble("Amount");
                    PreparedStatement ps5=con.prepareStatement("UPDATE user_account u JOIN  electronic_address e ON u.SSN=e.SSN SET u.Balance=? WHERE e.Identifier=(SELECT s.Identifier FROM send_transaction s WHERE s.STid=?)");
                    ps5.setDouble(1, newRecipientBalance);
                    ps5.setInt(2, selectedSend.getSTid());
                    ps5.executeUpdate();
                    msg.setText("Cancel transaction successfully.");
                    ps5.close();
                }
                ps3.close();
                ps4.close();
                rs4.close();
            }
            ps1.close();
            ps2.close();
            rs2.close();
            con.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
