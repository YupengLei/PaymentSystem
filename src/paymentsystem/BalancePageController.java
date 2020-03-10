package paymentsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BalancePageController implements Initializable {

    @FXML
    private TextField balanceinfo;
    @FXML
    private TextField withdrawamount;
    @FXML
    private TextField depositamount;
    @FXML
    private Label welcome;
    String UserSSN, UserName;
    String BalanceS;
    @FXML
    private TextField receiverid;
    @FXML
    private TextField trnasamount;
    @FXML
    private TextField memo;
    @FXML
    private TextField memoRequest;
    @FXML
    private PasswordField retypepass;
    @FXML
    private Label dipositconf;
    @FXML
    private Label withdrawinfo;
    @FXML
    private Label transferconf;
    @FXML
    private TextField requestamount;
    @FXML
    private TextField requestee1;
    @FXML
    private TextField requestee2;
    @FXML
    private TextField requestee3;
    @FXML
    private TextField percentage1;
    @FXML
    private TextField percentage2;
    @FXML
    private TextField percentage3;
    @FXML
    private Label requestconf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void getUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    public void SetBalance(String Bal) {
        BalanceS = Bal;
        balanceinfo.setText(BalanceS);

    }

    @FXML
    public void WithdrawMoney(ActionEvent event) throws SQLException, NumberFormatException  {
            if (withdrawamount.getText().isEmpty() || Double.parseDouble(withdrawamount.getText()) <=0) {
                withdrawinfo.setText("Please Enter A Valid Amount");
            } else {
                Connection con = DbConnection.Connection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
                ps.setString(1, UserSSN);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if(rs.getString("PBAVerified").equals("No")){
                        withdrawinfo.setText("You have not verified your primary bank account yet.");
                    }else {

                        double NewBalance = (rs.getDouble("Balance") - Double.parseDouble(withdrawamount.getText()));
                        if (NewBalance < 0) {
                            withdrawinfo.setText("Your Account Balance Is Low, can not withdraw that amount.");
                        } else {
                            ps = con.prepareStatement("UPDATE user_account SET Balance =? WHERE SSN = ?");
                            ps.setDouble(1, NewBalance);
                            ps.setString(2, UserSSN);
                            ps.executeUpdate();
                            withdrawinfo.setText("Withdraw Successfully to your primary bank account.");
                        }
                    }
                }
                ps.close();
                rs.close();
                con.close();
            }
        withdrawamount.setText("");
    }

    @FXML
    public void Deposit(ActionEvent event) throws SQLException, NumberFormatException{
            if (depositamount.getText().isEmpty() || Double.parseDouble(depositamount.getText()) <= 0) {
                dipositconf.setText("Please Enter A Valid Amount");
            } else {
                Connection con = DbConnection.Connection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
                ps.setString(1, UserSSN);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if(rs.getString("PBAVerified")==null) {
                        dipositconf.setText("You have not verified your primary bank account yet.");
                    }else {
                        int NewBalance = (rs.getInt("Balance") + Integer.parseInt(depositamount.getText()));
                        ps = con.prepareStatement("UPDATE user_account SET Balance =? WHERE SSN = ?");
                        ps.setInt(1, NewBalance);
                        ps.setString(2, UserSSN);
                        ps.executeUpdate();
                        dipositconf.setText("Deposited successfully from your primary bank account.");
                    }
                }
                ps.close();
                rs.close();
                con.close();
            }
        depositamount.setText("");
    }

    @FXML
    public void TransferMoney(ActionEvent event) throws SQLException, NumberFormatException {
        if (trnasamount.getText().isEmpty() || Double.parseDouble(trnasamount.getText()) <= 0 || retypepass.getText().isEmpty() || receiverid.getText().isEmpty()) {
            transferconf.setText("Please Fill Up Everything Correctly.");
        }else {
            Connection con = DbConnection.Connection();
            PreparedStatement psSelf = con.prepareStatement("SELECT * FROM user_account u, electronic_address e WHERE u.SSN=e.SSN AND  u.SSN =? AND e.Type=?");
            psSelf.setString(1, UserSSN);
            psSelf.setString(2, "Email");
            ResultSet rsSelf = psSelf.executeQuery();
            if(rsSelf.next()) {
                if (rsSelf.getString("Identifier").equals(receiverid.getText())) {
                    transferconf.setText("You can't send money to yourself. Re-enter the recipient ID.");
                } else {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? and Password = ?");
                    ps.setString(1, UserSSN);
                    ps.setString(2, retypepass.getText());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("PBAVerified").equals("No")) {
                            transferconf.setText("You have not verified your primary bank account yet.");
                        } else {
                            double newBalance = rs.getDouble("Balance") - Double.parseDouble(trnasamount.getText());
                            if (newBalance < 0) {
                                transferconf.setText("You Dont Have Enough Money in your TIJN account To send. The entire payment would be funded from primary bank account ");
                                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM user_account u JOIN electronic_address e ON u.SSN=e.SSN WHERE e.Identifier = ?");
                                ps2.setString(1, receiverid.getText());
                                ResultSet rs2 = ps2.executeQuery();
                                if (rs2.next()) {  //identifier is in the database
                                    double receiverNewBalance = rs2.getDouble("Balance") + Double.parseDouble(trnasamount.getText());
                                    PreparedStatement psSaveToSend = con.prepareStatement("INSERT INTO send_transaction (Amount, DateTime, Memo, SSN, Identifier) VALUES( ?, ?, ?, ? ,?) ");
                                    psSaveToSend.setDouble(1, Double.parseDouble(trnasamount.getText()));
                                    psSaveToSend.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                                    psSaveToSend.setString(3, memo.getText());
                                    psSaveToSend.setString(4, UserSSN);
                                    psSaveToSend.setString(5, receiverid.getText());
                                    psSaveToSend.executeUpdate();
                                    ps2 = con.prepareStatement("UPDATE user_account u JOIN electronic_address e ON u.SSN = e.SSN SET u.Balance = ? WHERE e.Identifier = ?");
                                    ps2.setDouble(1, receiverNewBalance);
                                    ps2.setString(2, receiverid.getText());
                                    ps2.executeUpdate();
                                    ps2.close();
                                    rs2.close();
                                    transferconf.setText("Send Successfully");
                                    receiverid.setText("");
                                    trnasamount.setText("");
                                    retypepass.setText("");
                                    memo.setText("");
                                } else {
                                    System.out.println("test6");
                                    transferconf.setText("The recipient has not signed up yet. ");
                                }

                            } else {
                                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM user_account u JOIN electronic_address e ON u.SSN=e.SSN WHERE e.Identifier = ?");
                                ps2.setString(1, receiverid.getText());
                                ResultSet rs2 = ps2.executeQuery();
                                if (rs2.next()) {  //recipient ID in the database
                                    double receiverNewBalance = rs2.getDouble("Balance") + Double.parseDouble(trnasamount.getText());
                                    PreparedStatement psSaveToSend = con.prepareStatement("INSERT INTO send_transaction (Amount, DateTime, Memo, SSN, Identifier) VALUES( ?, ?, ?, ? ,?)");
                                    psSaveToSend.setDouble(1, Double.parseDouble(trnasamount.getText()));
                                    psSaveToSend.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                                    psSaveToSend.setString(3, memo.getText());
                                    psSaveToSend.setString(4, UserSSN);
                                    psSaveToSend.setString(5, receiverid.getText());
                                    psSaveToSend.executeUpdate();
                                    ps2 = con.prepareStatement("UPDATE user_account u JOIN electronic_address e ON u.SSN = e.SSN SET u.Balance = ? WHERE e.Identifier = ?");
                                    ps2.setDouble(1, receiverNewBalance);
                                    ps2.setString(2, receiverid.getText());
                                    ps2.executeUpdate();
                                    ps2.close();
                                    rs2.close();
                                    ps = con.prepareStatement("UPDATE user_account SET Balance = ? WHERE SSN = ?");
                                    ps.setDouble(1, newBalance);
                                    ps.setString(2, UserSSN);
                                    ps.executeUpdate();
                                    transferconf.setText("Transfer Successfully");
                                    receiverid.setText("");
                                    trnasamount.setText("");
                                    retypepass.setText("");
                                    memo.setText("");
                                } else {
                                    transferconf.setText("The recipient has not sign up yet.");
                                }
                            }
                        }
                    } else {
                        transferconf.setText("Wrong Password Transaction Failed.");
                    }
                    ps.close();
                    rs.close();
                }
            }
            psSelf.close();
            rsSelf.close();
            con.close();
        }
    }

    @FXML
    public void RequestMoney (ActionEvent event) throws SQLException, NumberFormatException {
            if (requestamount.getText().isEmpty() || Double.parseDouble(requestamount.getText()) <= 0 || requestee1.getText().isEmpty() || percentage1.getText().isEmpty()) {
                requestconf.setText("Please Enter Valid Info");
            }
             else {
                Connection con = DbConnection.Connection();
                PreparedStatement psRequestTransaction = con.prepareStatement("INSERT INTO request_transaction (Amount, DateTime, Memo, SSN) VALUES( ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                psRequestTransaction.setDouble(1, Double.parseDouble(requestamount.getText()));
                psRequestTransaction.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                psRequestTransaction.setString(3, memoRequest.getText());
                psRequestTransaction.setString(4, UserSSN);
                psRequestTransaction.executeUpdate();
                int i=0;
                ResultSet rs1 = psRequestTransaction.getGeneratedKeys();
                if(rs1.next()){
                i = rs1.getInt(1);
                }
                    if (Integer.parseInt(percentage1.getText()) == 100 && percentage2.getText().isEmpty() && percentage3.getText().isEmpty()) {
                        PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                        psRequestFrom.setInt(1, i);
                        psRequestFrom.setString(2, requestee1.getText());
                        psRequestFrom.setInt(3, Integer.parseInt(percentage1.getText()));
                        psRequestFrom.executeUpdate();
                        psRequestTransaction.close();
                        psRequestFrom.close();
                        rs1.close();
                    }
                if (!percentage2.getText().isEmpty() && percentage3.getText().isEmpty() && Integer.parseInt(percentage1.getText()) + Integer.parseInt(percentage2.getText())== 100 ) {
                    PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom.setInt(1, i);
                    psRequestFrom.setString(2, requestee1.getText());
                    psRequestFrom.setInt(3, Integer.parseInt(percentage1.getText()));
                    psRequestFrom.executeUpdate();
                    PreparedStatement psRequestFrom2 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom2.setInt(1, i);
                    psRequestFrom2.setString(2, requestee2.getText());
                    psRequestFrom2.setInt(3, Integer.parseInt(percentage2.getText()));
                    psRequestFrom2.executeUpdate();
                    psRequestTransaction.close();
                    psRequestFrom.close();
                    psRequestFrom2.close();
                    rs1.close();
                }
                if (!percentage2.getText().isEmpty() && !percentage3.getText().isEmpty() && Integer.parseInt(percentage1.getText()) + Integer.parseInt(percentage2.getText())+ Integer.parseInt(percentage3.getText())== 100 ) {
                    PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom.setInt(1, i);
                    psRequestFrom.setString(2, requestee1.getText());
                    psRequestFrom.setInt(3, Integer.parseInt(percentage1.getText()));
                    psRequestFrom.executeUpdate();
                    PreparedStatement psRequestFrom2 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom2.setInt(1, i);
                    psRequestFrom2.setString(2, requestee2.getText());
                    psRequestFrom2.setInt(3, Integer.parseInt(percentage2.getText()));
                    psRequestFrom2.executeUpdate();
                    PreparedStatement psRequestFrom3 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom3.setInt(1, i);
                    psRequestFrom3.setString(2, requestee3.getText());
                    psRequestFrom3.setInt(3, Integer.parseInt(percentage3.getText()));
                    psRequestFrom3.executeUpdate();
                    psRequestTransaction.close();
                    psRequestFrom.close();
                    psRequestFrom2.close();
                    psRequestFrom3.close();
                    rs1.close();
                }
                requestconf.setText("Request Sent");
                    requestamount.setText("");
                    requestee1.setText("");
                    percentage1.setText("");
                    requestee2.setText("");
                    percentage2.setText("");
                    requestee3.setText("");
                    percentage3.setText("");
                    memoRequest.setText("");
                    con.close();
                }
    }

    @FXML
    public void splitBills (ActionEvent event) throws SQLException, NumberFormatException {
            if (requestamount.getText().isEmpty() || Integer.parseInt(requestamount.getText()) <= 0 || requestee1.getText().isEmpty() ) {
                requestconf.setText("Please Enter Valid Info");
            }
            else {
                Connection con = DbConnection.Connection();
                PreparedStatement psRequestTransaction = con.prepareStatement("INSERT INTO request_transaction (Amount, DateTime, Memo, SSN) VALUES( ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                psRequestTransaction.setDouble(1, Double.parseDouble(requestamount.getText()));
                psRequestTransaction.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                psRequestTransaction.setString(3, memoRequest.getText());
                psRequestTransaction.setString(4, UserSSN);
                psRequestTransaction.executeUpdate();
                int i=0;
                ResultSet rs1 = psRequestTransaction.getGeneratedKeys();
                if(rs1.next()){
                    i = rs1.getInt(1);}
                if ( requestee2.getText().isEmpty() && requestee3.getText().isEmpty()) {
                    PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom.setInt(1, i);
                    psRequestFrom.setString(2, requestee1.getText());
                    psRequestFrom.setInt(3, 100);
                    psRequestFrom.executeUpdate();
                    psRequestTransaction.close();
                    psRequestFrom.close();
                    rs1.close();
                }
                if (!requestee2.getText().isEmpty() && requestee3.getText().isEmpty() ) {
                    PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom.setInt(1, i);
                    psRequestFrom.setString(2, requestee1.getText());
                    psRequestFrom.setInt(3, 50);
                    psRequestFrom.executeUpdate();
                    PreparedStatement psRequestFrom2 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom2.setInt(1, i);
                    psRequestFrom2.setString(2, requestee2.getText());
                    psRequestFrom2.setInt(3, 50);
                    psRequestFrom2.executeUpdate();
                    psRequestTransaction.close();
                    psRequestFrom.close();
                    psRequestFrom2.close();
                    rs1.close();
                }
                if (!requestee2.getText().isEmpty() && !requestee3.getText().isEmpty()) {
                    PreparedStatement psRequestFrom = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom.setInt(1, i);
                    psRequestFrom.setString(2, requestee1.getText());
                    psRequestFrom.setInt(3, 100/3);
                    psRequestFrom.executeUpdate();
                    PreparedStatement psRequestFrom2 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom2.setInt(1, i);
                    psRequestFrom2.setString(2, requestee2.getText());
                    psRequestFrom2.setInt(3, 100/3);
                    psRequestFrom2.executeUpdate();
                    PreparedStatement psRequestFrom3 = con.prepareStatement("INSERT INTO request_from (RTid, Identifier, Percentage) VALUES(?, ?, ?)");
                    psRequestFrom3.setInt(1, i);
                    psRequestFrom3.setString(2, requestee3.getText());
                    psRequestFrom3.setInt(3, 100/3);
                    psRequestFrom3.executeUpdate();
                    psRequestTransaction.close();
                    psRequestFrom.close();
                    psRequestFrom2.close();
                    psRequestFrom3.close();
                    rs1.close();
                }
                requestconf.setText("Request Sent");
                requestamount.setText("");
                requestee1.setText("");
                percentage1.setText("");
                requestee2.setText("");
                percentage2.setText("");
                requestee3.setText("");
                percentage3.setText("");
                memoRequest.setText("");
                con.close();
            }
    }
}





