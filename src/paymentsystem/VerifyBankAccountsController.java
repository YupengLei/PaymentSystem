package paymentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class VerifyBankAccountsController implements Initializable {
    @FXML
    private Label welcome;
    String UserSSN, UserName;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSSN;
    @FXML
    private Label PBAconf;
    @FXML
    private Label ABAconf;
    @FXML
    private TextField txtPBAID;
    @FXML
    private TextField txtPBANumber;
    @FXML
    private TextField txtverifyAmountPBA;
    @FXML
    private TextField txtverifyAmountABA;
    @FXML
    private TableColumn<PBAData, String> colBankID;
    @FXML
    private TableColumn<PBAData, String> colBANumber;
    @FXML
    private TableColumn<PBAData, String> colPBAVerified;
    @FXML
    private TableColumn<ABAData, String> colABAID;
    @FXML
    private TableColumn<ABAData, String> colABANumber;
    @FXML
    private TableColumn<ABAData, String> colABAVerified;
    @FXML
    private TableView<PBAData> tblPBAData;
    @FXML
    private TableView<ABAData> tblABAData;
    @FXML
    private TextField txtBankID;
    @FXML
    private TextField txtBANumber;
    private ObservableList<PBAData>dataPBA;
    private ObservableList<ABAData>dataABA;

    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    @FXML
    public void LinkPrimaryAccount (ActionEvent event) throws SQLException {
        if(txtPBAID.getText().isEmpty() || txtPBANumber.getText().isEmpty()){
            PBAconf.setText("Please fill in all required info.");
        }else {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("UPDATE user_account SET BankID =?, BANumber =? WHERE SSN =?");
            ps.setString(1, txtPBAID.getText());
            ps.setString(2, txtPBANumber.getText());
            ps.setString(3, UserSSN);
            ps.executeUpdate();
            PreparedStatement ps2 = con.prepareStatement("INSERT bank_account  (BankID, BANumber) VALUES (?, ?)");
            ps2.setString(1, txtPBAID.getText());
            ps2.setString(2, txtPBANumber.getText());
            ps2.executeUpdate();
            ps.close();
            ps2.close();
            con.close();
        }
    }

    @FXML
    public void UnlinkPrimaryAccount (ActionEvent event) throws SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = con.prepareStatement("UPDATE user_account SET BankID =?, BANumber =? WHERE SSN =?");
        ps.setString(1, null);
        ps.setString(2, null);
        ps.setString(3, UserSSN);
        ps.executeUpdate();
        PreparedStatement ps2 = con.prepareStatement("DELETE FROM bank_account  WHERE BankID =? AND BANumber =?");
        ps2.setString(1, txtBankID.getText());
        ps2.setString(2, txtBANumber.getText());
        ps2.executeUpdate();
        ps.close();
        ps2.close();
        con.close();
    }

    @FXML
    public void VerifyPrimaryAccount (ActionEvent event) throws SQLException {
        if (txtverifyAmountPBA.getText().isEmpty() || Double.parseDouble(txtverifyAmountPBA.getText()) < 0) {
            PBAconf.setText("Please Enter A Valid Amount");
        } else {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
            ps.setString(1, UserSSN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double NewBalance = (rs.getDouble("Balance") + Double.parseDouble(txtverifyAmountPBA.getText()));
                PreparedStatement ps1 = con.prepareStatement("UPDATE user_account SET Balance =?, PBAVerified =? WHERE SSN = ?");
                ps1.setDouble(1, NewBalance);
                ps1.setString(2, "Yes");
                ps1.setString(3, UserSSN);
                ps1.executeUpdate();
                ps1.close();
            }
            PBAconf.setText("Deposit Done. Account Has Been Verified.");
            ps.close();
            rs.close();
            con.close();
        }
    }

    @FXML
    public void LoadPrimaryAccount (ActionEvent event) throws SQLException {
        Connection con = DbConnection.Connection();
        dataPBA = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN =?");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
                if(rs.getString("BankID") != null) {
                    dataPBA.add(new PBAData(rs.getString("BankID"), rs.getString("BANumber"), rs.getString("PBAVerified")));
                    colBankID.setCellValueFactory(new PropertyValueFactory<>("BankID"));
                    colBANumber.setCellValueFactory(new PropertyValueFactory<>("BANumber"));
                    colPBAVerified.setCellValueFactory(new PropertyValueFactory<>("PBAVerified"));
                    tblPBAData.setItems(null);
                    tblPBAData.setItems(dataPBA);
                }
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void LinkAdditionalAccount (ActionEvent event) throws SQLException {
        if(txtBankID.getText().isEmpty() || txtBANumber.getText().isEmpty()){
            ABAconf.setText("Please fill in all required info.");
        }else {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("INSERT has_additional  (SSN, BankID, BANumber) VALUES (?, ?, ?)");
            ps.setString(1, UserSSN);
            ps.setString(2, txtBankID.getText());
            ps.setString(3, txtBANumber.getText());
            ps.executeUpdate();
            PreparedStatement ps2 = con.prepareStatement("INSERT bank_account  (BankID, BANumber) VALUES (?, ?)");
            ps2.setString(1, txtBankID.getText());
            ps2.setString(2, txtBANumber.getText());
            ps2.executeUpdate();
            ps.close();
            ps2.close();
            con.close();
        }
    }


    @FXML
    public void LoadAdditionalAccount (ActionEvent event) throws SQLException {
        Connection con = DbConnection.Connection();
        dataABA = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM has_additional WHERE SSN =?");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataABA.add(new ABAData(rs.getString("BankID"),  rs.getString("BANumber"), rs.getString("Verified")));
            colABAID.setCellValueFactory(new PropertyValueFactory<>("BankID"));
            colABANumber.setCellValueFactory(new PropertyValueFactory<>("BANumber"));
            colABAVerified.setCellValueFactory(new PropertyValueFactory<>("Verified"));
            tblABAData.setItems(null);
            tblABAData.setItems(dataABA);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void UnlinkAdditionalAccount (ActionEvent event) throws SQLException {
        if (tblABAData.getSelectionModel().getSelectedItem() != null) {
            ABAData selectedAccount = tblABAData.getSelectionModel().getSelectedItem();
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM has_additional WHERE BankID =? AND BANumber =? AND SSN =?");
            ps.setString(1, selectedAccount.getBankID());
            ps.setString(2, selectedAccount.getBANumber());
            ps.setString(3, UserSSN);
            ps.executeUpdate();
            PreparedStatement ps2 = con.prepareStatement("DELETE FROM bank_account  WHERE BankID =? AND BANumber =?");
            ps2.setString(1, selectedAccount.getBankID());
            ps2.setString(2, selectedAccount.getBANumber());
            ps2.executeUpdate();
            ps.close();
            ps2.close();
            con.close();
        }
    }

    @FXML
    public void VerifyAdditionalAccount (ActionEvent event) throws SQLException {
        if (txtverifyAmountABA.getText().isEmpty() || Double.parseDouble(txtverifyAmountABA.getText()) < 0) {
            PBAconf.setText("Please Enter A Valid Amount");
        }else {
            if (tblABAData.getSelectionModel().getSelectedItem() != null) {
                ABAData selectedAccount = tblABAData.getSelectionModel().getSelectedItem();
                Connection con = DbConnection.Connection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
                ps.setString(1, UserSSN);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    double NewBalance = (rs.getDouble("Balance") + Double.parseDouble(txtverifyAmountABA.getText()));
                    PreparedStatement ps1 = con.prepareStatement("UPDATE user_account SET Balance =? WHERE SSN = ?");
                    ps1.setDouble(1, NewBalance);
                    ps1.setString(2, UserSSN);
                    ps1.executeUpdate();
                    PreparedStatement ps2 = con.prepareStatement("UPDATE has_additional SET Verified =? WHERE SSN = ? AND BankID =? AND BANumber =?");
                    ps2.setString(1, "Yes");
                    ps2.setString(2, UserSSN);
                    ps2.setString(3, selectedAccount.getBankID());
                    ps2.setString(4, selectedAccount.getBANumber());
                    ps2.executeUpdate();
                    ps1.close();
                    ps2.close();
                }
                ABAconf.setText("Deposit Done. Account Has Been Verified.");
                ps.close();
                rs.close();
                con.close();
            }else{
                ABAconf.setText("Please select one account from table.");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

}
