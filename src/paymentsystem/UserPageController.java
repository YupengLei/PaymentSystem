package paymentsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {
    @FXML
    private Label welcome;
    String UserSSN, UserName;
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void GetUserSSN(String SSN, String Name) {
        welcome.setText(Name);
        UserSSN = SSN;
        UserName = Name;
    }

    public void CheckBalance(ActionEvent event) throws SQLException, IOException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? AND Name = ?");
        ps.setString(1, UserSSN);
        ps.setString(2, UserName);
        rs = ps.executeQuery();
        while (rs.next()) {
            String Balance = Integer.toString(rs.getInt("Balance"));
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("BalancePage.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            BalancePageController bpc = loader.getController();
            bpc.SetBalance(Balance);
            bpc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Balance Page");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void Withdraw(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? AND Name = ?");
        ps.setString(1, UserSSN);
        ps.setString(2, UserName);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("WithdrawPage.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            BalancePageController bpc = loader.getController();
            bpc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Withdraw Page");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void DepositMoney(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? AND Name = ?");
        ps.setString(1, UserSSN);
        ps.setString(2, UserName);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DepositPage.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            BalancePageController bpc = loader.getController();
            bpc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Deposite Page");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void BalanceTransfer(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? AND Name = ?");
        ps.setString(1, UserSSN);
        ps.setString(2, UserName);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("BalanceTransfer.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            BalancePageController bpc = loader.getController();
            bpc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Balance Transfer");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void RequestMoney(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? AND Name = ?");
        ps.setString(1, UserSSN);
        ps.setString(2, UserName);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RequestMoney.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            BalancePageController bpc = loader.getController();
            bpc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Request Money");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void AccountInfo(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AccountInfoPage.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            AccountInfoController aic = loader.getController();
            aic.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Account Information");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void ChangePassword(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ChangePassword.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            AccountInfoController aic = loader.getController();
            aic.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Change Password");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void SendTransaction(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        rs = ps.executeQuery();

        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SendTransaction.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            SendTransactionController stc = loader.getController();
            stc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Send Transaction");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    void RequestTransaction(ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RequestTransaction.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            RequestTransactionController stc = loader.getController();
            stc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Request Transaction");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    void VerifiedIdentifiers (ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VerifiedIdentifiers.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            VerifiedIdentifiersController stc = loader.getController();
            stc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Verified Identifiers");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    void VerifyBankAccounts (ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VerifyBankAccounts.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            VerifyBankAccountsController stc = loader.getController();
            stc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Verify Bank Accounts");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    void PaymentHistory (ActionEvent event) throws IOException, SQLException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ?");
        ps.setString(1, UserSSN);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PaymentHistory.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            PaymentHistoryController stc = loader.getController();
            stc.getUserSSN(UserSSN, rs.getString("Name"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Payment History");
            stage.setScene(scene);
            stage.show();
        }
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    void Logout (ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("SignupLogin.fxml")));
        stage.setScene(scene);
        stage.show();
    }
}
