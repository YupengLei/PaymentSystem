package paymentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class VerifiedIdentifiersController implements Initializable {

    @FXML
    private Label welcome;
    String UserSSN, UserName;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSSN;
    @FXML
    private TextField codePhone;
    @FXML
    private TextField codeEmail;
    @FXML
    private Button btnConfirmEmail;
    @FXML
    private Button btnConfirmPhone;
    @FXML
    private Label msg;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TableView<IdentifierData> tblIDData;
    private ObservableList<IdentifierData> dataID;
    @FXML
    private TableColumn<PBAData, String> colIdentifier;
    @FXML
    private TableColumn<PBAData, String> colType;
    @FXML
    private TableColumn<PBAData, String> colVerified;

    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    @FXML
    public void VerifyEmail (ActionEvent event) throws SQLException {
        if(codeEmail.getText().equals("1234")) {
            Connection con = DbConnection.Connection();
            PreparedStatement psEmail = con.prepareStatement("UPDATE electronic_address SET Verified =? WHERE SSN = ? AND Type = ?");
            psEmail.setString(1, "Yes");
            psEmail.setString(2, UserSSN);
            psEmail.setString(3, "Email");
            psEmail.executeUpdate();
            codeEmail.setText("");
            msg.setText("verify email successfully.");
            psEmail.close();
            con.close();
        }
    }

    @FXML
    public void VerifyPhone (ActionEvent event) throws SQLException {
        if(codePhone.getText().equals("1234")) {
            Connection con = DbConnection.Connection();
            PreparedStatement psPhone = con.prepareStatement("UPDATE electronic_address SET Verified =? WHERE SSN =? AND Type =?");
            psPhone.setString(1, "Yes");
            psPhone.setString(2, UserSSN);
            psPhone.setString(3, "Phone");
            psPhone.executeUpdate();
            codePhone.setText("");
            msg.setText("verfify phone number successfully. ");
            psPhone.close();
            con.close();
        }
    }

    @FXML
    public void addEmail (ActionEvent event) throws SQLException {
        if(txtEmail.getText().isEmpty() ){
            msg.setText("Please fill in all required info.");
        }else {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("INSERT electronic_address (Identifier, SSN, Type) VALUES (?, ?, ?)");
            ps.setString(1, txtEmail.getText());
            ps.setString(2, UserSSN);
            ps.setString(3, "Email");
            ps.executeUpdate();
            txtEmail.setText("");
            msg.setText("add email successfully.");
            ps.close();
            con.close();
        }
    }

    @FXML
    public void addPhone (ActionEvent event) throws SQLException {
        if(txtPhone.getText().isEmpty() ){
            msg.setText("Please fill in all required info.");
        }else {
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("INSERT electronic_address (Identifier, SSN, Type) VALUES (?, ?, ?)");
            ps.setString(1, txtPhone.getText());
            ps.setString(2, UserSSN);
            ps.setString(3, "Phone");
            ps.executeUpdate();
            txtPhone.setText("");
            msg.setText("add phone number successfully.");
            ps.close();
            con.close();
        }
    }

    @FXML
    public void LoadIdentifierData (ActionEvent event) throws SQLException {
        Connection con = DbConnection.Connection();
        dataID= FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM electronic_address WHERE SSN =?");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            dataID.add(new IdentifierData(rs.getString("Identifier"),  rs.getString("Type"), rs.getString("Verified")));
            colIdentifier.setCellValueFactory(new PropertyValueFactory<>("Identifier"));
            colType.setCellValueFactory(new PropertyValueFactory<>("Type"));
            colVerified.setCellValueFactory(new PropertyValueFactory<>("Verified"));
            tblIDData.setItems(null);
            tblIDData.setItems(dataID);
        }
        ps.close();
        rs.close();
        con.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

}
