package paymentsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtSSN;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label mssg;
    @FXML
    private void Login (ActionEvent event) throws SQLException, IOException {
        Connection con = DbConnection.Connection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user_account WHERE SSN = ? and Password = ?");
        ps.setString(1, txtSSN.getText());
        ps.setString(2, txtPassword.getText());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserPage.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            UserPageController upc = loader.getController();
            upc.GetUserSSN(txtSSN.getText(), rs.getString("Name"));
            stage.setTitle("User Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
            mssg.setText("");
        } else {
            mssg.setText("Wrong Password Or UserID");
        }
    }

        @FXML
        public void Signup (ActionEvent event) throws IOException{
            Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Signup.fxml")));
                stage.setScene(scene);
                stage.show();
        }

        @Override
        public void initialize (URL url, ResourceBundle rb){
        // TODO
    }

}



