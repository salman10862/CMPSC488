package pennychain.controller;

import pennychain.db.Connection_Online;
import pennychain.db.Hash;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CreateAccountWindowController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Button createAcctButton;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    @FXML private Label error1;
    @FXML private Label error2;

    @FXML private Parent root;

    @FXML protected void handleCreateAcctButton(MouseEvent event) {
        
        String firstName = firstNameField.getCharacters().toString();
        String lastName = lastNameField.getCharacters().toString();
        String newUsername = newUsernameField.getCharacters().toString();
        String newPassword = newPasswordField.getCharacters().toString();
        String newPasswordConfirm = 
            confirmPasswordField.getCharacters().toString();

        if(Connection_Online.userExists(newUsername)) {
            error1.setText("Username already exists");
            error1.setTextFill(Color.web("#FF0000"));
        }
        if(!newPassword.equals(newPasswordConfirm)) {
            error2.setText("Passwords do not match");
            error2.setTextFill(Color.web("#FF0000"));
        } else if(!Connection_Online.userExists(newUsername) &&
                  newPassword.equals(newPasswordConfirm)) {
            String hashAndSalt = 
                Hash.getHashAndSalt(newPasswordField.getCharacters());
            String[] saltWithSaltedHash = hashAndSalt.split(":");

            Connection_Online.addUserRecord(firstName, lastName, newUsername,
                    saltWithSaltedHash[0], saltWithSaltedHash[1]);

            error1.setText("Account created!");
            error1.setTextFill(Color.web("#00FF00"));
            error2.setText("");
        }
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

