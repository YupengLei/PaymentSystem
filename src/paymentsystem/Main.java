/*
 * Copyright @ Yupeng Lei
 *
 * Licensed under the MIT License
 *
 * This is a desktop payment system application.
 *
 */

package paymentsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("SignupLogin.fxml"));
        primaryStage.setTitle("Payment System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
