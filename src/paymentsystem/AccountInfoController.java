package paymentsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountInfoController implements Initializable {

    @FXML
    private Label welcome;
    String UserSSN, UserName;
    @FXML
    private TextField ussn;
    @FXML
    private TextField uname;
    @FXML
    private TextField ubalance;
    @FXML
    private TextField uPBAID;
    @FXML
    private TextField uPBANumber;
    @FXML
    private TextField uPBAVerified;
    @FXML
    private PasswordField oldpass;
    @FXML
    private PasswordField newpass;
    @FXML
    private PasswordField passretype;
    @FXML
    private Label changepassconf;

    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    public void AccountInfo(ActionEvent event) throws SQLException{
        Connection con = DbConnection.Connection();
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        ps1 = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps1.setString(1, UserSSN);
        rs1 = ps1.executeQuery();
        while(rs1.next()){
            ussn.setText(rs1.getString("SSN"));
            uname.setText(rs1.getString("Name"));
            ubalance.setText(rs1.getString("Balance"));
            uPBAID.setText(rs1.getString("BankID"));
            uPBANumber.setText(rs1.getString("BANumber"));
            uPBAVerified.setText(rs1.getString("PBAVerified"));

        }
        ps1.close();
        rs1.close();
        con.close();
    }


    public void ChangePassword(ActionEvent event) throws SQLException{
        if(!newpass.getText().equals(passretype.getText())){
            changepassconf.setText("Password Confirmation Failed");
            passretype.setText("");
        }
        else if(oldpass.getText().isEmpty()||newpass.getText().isEmpty()||passretype.getText().isEmpty()){
            changepassconf.setText("Please Fill Up Everything Correctly.");
        }
        else{
            Connection con = DbConnection.Connection();
            PreparedStatement ps = con.prepareStatement("UPDATE user_account SET Password = ? WHERE SSN ='"+UserSSN+"' AND Password ='"+oldpass.getText()+"'");
            ps.setString(1, newpass.getText());
            int i = ps.executeUpdate();
            if(i>0){
                changepassconf.setText("Password Changed.");
            }
            else{
                changepassconf.setText("Wrong Password.");
            }
            oldpass.setText("");
            newpass.setText("");
            passretype.setText("");
            ps.close();
            con.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
