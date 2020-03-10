package paymentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PaymentHistoryController implements Initializable {
    @FXML
    private Label lblName;
    @FXML
    private Label lblSSN;
    @FXML
    private TextField txtIdentifier;
    @FXML
    private TextField txtNameRequest;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtTotalRequest;
    @FXML
    private DatePicker chosenDate;
    @FXML
    private DatePicker chosenDateRequest;
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
    private ObservableList<SendData> data;
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
    private ObservableList<RequestData> dataRequest;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label welcome;
    String UserSSN, UserName;
    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    @FXML
    public void LoadTransactionData(ActionEvent event) throws SQLException {
        sendtransaction.setItems(null);
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ?");
        ps.setString(1, UserSSN);
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
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadRequestData(ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?)");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadLast30 (ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND DateTime > NOW() - INTERVAL 30 DAY ");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(null);
            sendtransaction.setItems(data);

        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadLast90 (ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND DateTime > NOW() - INTERVAL 90 DAY ");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(null);
            sendtransaction.setItems(data);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadLastYear (ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND DateTime > NOW() - INTERVAL 1 YEAR ");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(null);
            sendtransaction.setItems(data);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void searchIdentifier (ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND Identifier =?");
        ps.setString(1, UserSSN);
        ps.setString(2, txtIdentifier.getText());
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(null);
            sendtransaction.setItems(data);
        }
        PreparedStatement ps1=con.prepareStatement("SELECT SUM(Amount) FROM send_transaction WHERE SSN= ? AND Identifier =?");
        ps1.setString(1, UserSSN);
        ps1.setString(2, txtIdentifier.getText());
        ResultSet rs1=ps1.executeQuery();
        while(rs1.next()) {
            txtTotal.setText(rs1.getString("SUM(Amount)"));
        }
        txtIdentifier.setText("");
        ps.close();
        rs.close();
        ps1.close();
        rs1.close();
        con.close();
    }

    @FXML
    private void searchDate (ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM send_transaction WHERE SSN= ? AND DATE(DateTime) =?");
        ps.setString(1, UserSSN);
        ps.setTimestamp(2, Timestamp.valueOf(chosenDate.getValue().atTime(0,0)));
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            data.add(new SendData(rs.getInt("STid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"),  rs.getString("Cancelled"), rs.getString("Identifier")));
            sSTid.setCellValueFactory(new PropertyValueFactory<>("STid"));
            sAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            sTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            sMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            sCancelled.setCellValueFactory(new PropertyValueFactory<>("Cancelled"));
            sIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            sendtransaction.setItems(null);
            sendtransaction.setItems(data);
        }
        PreparedStatement ps1=con.prepareStatement("SELECT SUM(Amount) FROM send_transaction WHERE SSN= ? AND DATE(DateTime) =?");
        ps1.setString(1, UserSSN);
        ps1.setTimestamp(2, Timestamp.valueOf(chosenDate.getValue().atTime(0,0)));
        ResultSet rs1=ps1.executeQuery();
        while(rs1.next()) {
            txtTotal.setText(rs1.getString("SUM(Amount)"));
        }
        ps.close();
        rs.close();
        ps1.close();
        rs1.close();
        con.close();
        chosenDate.setValue(null);
    }

    @FXML
    private void LoadLast30Request (ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND (t.DateTime > NOW() - INTERVAL 30 DAY) AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?)");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"), rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadLast90Request (ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND (t.DateTime > NOW() - INTERVAL 90 DAY) AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?)");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void LoadLastYearRequest (ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND (t.DateTime > NOW() - INTERVAL 1 YEAR ) AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?)");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    private void SearchNameRequest (ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?) AND u.Name=? ");
        ps.setString(1, UserSSN);
        ps.setString(2, txtNameRequest.getText());
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        PreparedStatement ps1=con.prepareStatement("SELECT SUM(t.Amount * f.Percentage /100) FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?) AND u.Name=? ");
        ps1.setString(1, UserSSN);
        ps1.setString(2, txtNameRequest.getText());
        ResultSet rs1=ps1.executeQuery();
        while(rs1.next()) {
            txtTotalRequest.setText(rs1.getString("SUM(t.Amount * f.Percentage /100)"));
        }
        txtNameRequest.setText("");
        ps.close();
        rs.close();
        ps1.close();
        rs1.close();
        con.close();
    }

    @FXML
    private void SearchDateRequest (ActionEvent event) throws SQLException{
        requesttransaction.setItems(null);
        Connection con = DbConnection.Connection();
        dataRequest = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?) AND DATE(t.DateTime) =? ");
        ps.setString(1, UserSSN);
        ps.setTimestamp(2, Timestamp.valueOf(chosenDateRequest.getValue().atTime(0,0)));
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataRequest.add(new RequestData(rs.getInt("RTid"), rs.getDouble("Amount"), rs.getString("DateTime"), rs.getString("Memo"), rs.getString("Status"), rs.getString("Name"),  rs.getInt("Percentage")));
            rRTid.setCellValueFactory(new PropertyValueFactory<>("RTid"));
            rAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            rTime.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            rMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
            rName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            rStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            rPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));
            requesttransaction.setItems(dataRequest);
        }
        PreparedStatement ps1=con.prepareStatement("SELECT SUM(t.Amount * f.Percentage /100) FROM request_transaction t, request_from f, user_account u WHERE t.RTid = f.RTid AND t.SSN=u.SSN AND f.Identifier IN (SELECT e.Identifier FROM electronic_address e WHERE e.SSN= ?) AND DATE(t.DateTime) =? ");
        ps1.setString(1, UserSSN);
        ps1.setTimestamp(2, Timestamp.valueOf(chosenDateRequest.getValue().atTime(0,0)));
        ResultSet rs1=ps1.executeQuery();
        while(rs1.next()) {
            txtTotalRequest.setText(rs1.getString("SUM(t.Amount * f.Percentage /100)"));
        }
        chosenDateRequest.setValue(null);
        ps.close();
        rs.close();
        ps1.close();
        rs1.close();
        con.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
