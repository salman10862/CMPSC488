package pennychain.controller;

import java.io.IOException;

import javafx.application.Platform;
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

public class LoginWindowController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private Button loginButton;
    @FXML private Button newAcctButton;
    @FXML private Button exitButton;

    @FXML private Parent root;

    @FXML protected void handleLoginButton(MouseEvent event) {
        //1. Lookup user in our local list
        // String username = ~somelist~.search(usernameField.getText());
        //2.
    }

    @FXML protected void handleNewAcctButton(MouseEvent event) {

        Node source = (Node) event.getSource();
        
        try {
            root = FXMLLoader.load(getClass().getResource("CreateNewAccountWindow.fxml"));
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }

        source.getScene().setRoot(root);

    }

    @FXML protected void handleExitButton(MouseEvent event) {
        Platform.exit();
    }
}
