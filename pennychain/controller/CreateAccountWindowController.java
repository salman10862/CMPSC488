package pennychain.controller;

import java.io.IOException;

import com.mongodb.client.MongoDatabase;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CreateAccountWindowController {

    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Button createAcctButton;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    @FXML private Parent root;

    @FXML protected void handleCreateAcctButton(MouseEvent event) {
        //do something
    }

    @FXML protected void handleOkButton(MouseEvent event) {
        // do something
        //MongoDatabase
    }

    @FXML protected void handleCancelButton(MouseEvent event) {

        Node source = (Node) event.getSource();

        try {
            root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }

        source.getScene().setRoot(root);
    }
}

