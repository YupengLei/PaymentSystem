package paymentsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private Label lblErrors;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtSsn;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    public void SignupConfirm (ActionEvent event) throws SQLException, IOException{
        Connection con = DbConnection.Connection();
        if(txtSsn.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty() || txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty() || txtSsn.getText().length() != 9 || txtPhone.getText().length() != 10 ||!(txtSsn.getText().matches("[0-9]+")) || !(txtEmail.getText().matches("\\b[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}\\b"))) {
            lblErrors.setText("Please enter your credentials correctly.");
        }else{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
            ps.setString(1, txtSsn.getText());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                lblErrors.setText("Account exist. Please refill your ssn again.");
            }else{
                PreparedStatement ps1 =con.prepareStatement("INSERT INTO user_account (SSN, Password, Name) VALUES(?, ?, ?)");
                ps1.setString(1, txtSsn.getText());
                ps1.setString(2, txtPassword.getText());
                ps1.setString(3, txtName.getText());
                ps1.executeUpdate();
                PreparedStatement ps2=con.prepareStatement("INSERT INTO electronic_address (Identifier, SSN, Type) VALUES(?, ?, ?)");
                ps2.setString(1, txtEmail.getText());
                ps2.setString(2, txtSsn.getText());
                ps2.setString(3, "Email");
                ps2.executeUpdate();
                PreparedStatement ps3=con.prepareStatement("INSERT INTO electronic_address (Identifier, SSN, Type) VALUES(?, ?, ?)");
                ps3.setString(1, txtPhone.getText());
                ps3.setString(2, txtSsn.getText());
                ps3.setString(3, "Phone");
                ps3.executeUpdate();
                lblErrors.setText("Sign up successfully. Now please log in...");
                ps.close();
                rs.close();
                ps1.close();
                ps2.close();
                ps3.close();
                Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("SignupLogin.fxml")));
                    stage.setScene(scene);
                    stage.show();
            }
      }
    }
}