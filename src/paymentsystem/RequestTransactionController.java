package paymentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestTransactionController {

    @FXML
    private TableView<RequestData> requesttransaction;
    @FXML
    private TableColumn<RequestData,Integer> rRTid;
    @FXML
    private TableColumn<RequestData,Double> rAmount;
    @FXML
    private TableColumn<RequestData, String> rTime;
    @FXML
    private TableColumn<RequestData,String> rMemo;
    @FXML
    private TableColumn<RequestData,String> rStatus;
    @FXML
    private TableColumn<RequestData,String> rName;
    @FXML
    private TableColumn<RequestData,Integer> rPercentage;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSSN;
    @FXML
    private Label msg;
    private ObservableList<RequestData>data;
    @FXML
    private Label welcome;
    String UserSSN, UserName;

    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    @FXML
    private void LoadRequest(ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Status=? AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?)");
        ps.setString(1, "Pending");
        ps.setString(2, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(data);
            msg.setText("Info Load successfully.");
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void acceptRequest (ActionEvent event) throws SQLException{
        if (requesttransaction.getSelectionModel().getSelectedItem() != null) {
            RequestData selectedRequest = requesttransaction.getSelectionModel().getSelectedItem();
            Connection con = DbConnection.Connection();
            PreparedStatement ps1 = con.prepareStatement("UPDATE request_from f JOIN electronic_address e ON f.Identifier=e.Identifier SET f.Status=? WHERE f.RTid = ? AND e.SSN=?");
            ps1.setString(1, "Accepted");
            ps1.setInt(2, selectedRequest.getRTid());
            ps1.setString(3, UserSSN);
            ps1.executeUpdate();
            PreparedStatement ps3 = con.prepareStatement("SELECT * FROM user_account u WHERE u.SSN IN (SELECT t.SSN FROM request_transaction t WHERE t.RTid = ? )");
            ps3.setInt(1, selectedRequest.getRTid());
            ResultSet rs3 = ps3.executeQuery();
            if(rs3.next()){
                double newBalance = selectedRequest.getAmount()*selectedRequest.getPercentage()/100 + rs3.getDouble("Balance");
                PreparedStatement ps2 = con.prepareStatement("UPDATE user_account u SET u.Balance =? WHERE u.SSN IN (SELECT t.SSN FROM request_transaction t WHERE t.RTid = ? )");
                ps2.setDouble(1, newBalance );
                ps2.setInt(2, selectedRequest.getRTid());
                ps2.executeUpdate();
                PreparedStatement ps5 = con.prepareStatement("SELECT * FROM user_account WHERE SSN =?");
                ps5.setString(1, UserSSN);
                ResultSet rs5 = ps5.executeQuery();
                if(rs5.next()) {
                    double newBalance2 = rs5.getDouble("Balance") - selectedRequest.getAmount() * selectedRequest.getPercentage() / 100;
                    PreparedStatement ps4 = con.prepareStatement("UPDATE user_account SET Balance =? WHERE SSN =?");
                    ps4.setDouble(1, newBalance2);
                    ps4.setString(2, UserSSN);
                    ps4.executeUpdate();
                    msg.setText("Request Accepted.");
                    ps2.close();
                    ps5.close();
                    ps4.close();
                    rs5.close();
                }
            }
            ps1.close();
            rs3.close();
            con.close();
        }
    }

    @FXML
    public void rejectRequest (ActionEvent event) throws SQLException{
        if (requesttransaction.getSelectionModel().getSelectedItem() != null) {
            RequestData selectedRequest = requesttransaction.getSelectionModel().getSelectedItem();
            Connection con = DbConnection.Connection();
            PreparedStatement ps1 = con.prepareStatement("UPDATE request_from f JOIN electronic_address e ON f.Identifier = e.Identifier SET f.Status =? WHERE f.RTid = ? AND e.SSN =?");
            ps1.setString(1, "Rejected");
            ps1.setInt(2, selectedRequest.getRTid());
            ps1.setString(3, UserSSN);
            ps1.executeUpdate();
            msg.setText("Request Rejected.");
            ps1.close();
            con.close();
        }
    }

    public void initialize() throws SQLException {
        // TODO
    }
}