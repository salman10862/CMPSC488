package pennychain.controller;

import pennychain.db.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
    @FXML private TextField emailField;
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Button createAcctButton;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    @FXML private Label error1;
    @FXML private Label error2;

    @FXML private Parent root;

    private boolean accountCreated = false;

    public CreateAccountWindowController(){}

    @FXML protected void handleCreateAcctButton(MouseEvent event) {
        
        String firstName = firstNameField.getCharacters().toString();
        String lastName = lastNameField.getCharacters().toString();
        String email = emailField.getCharacters().toString();
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

            try {
                String hashAndSalt = 
                    Hash.getHashAndSalt(newPasswordField.getCharacters());
                String[] saltWithSaltedHash = hashAndSalt.split(":");

                Connection_Online.addUserRecord(
                        firstName, 
                        lastName, 
                        newUsername, 
                        saltWithSaltedHash[1], 
                        saltWithSaltedHash[0],
                        email);

                accountCreated = true;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                System.err.println("Caught exception from Hash.getHashAndSalt: "
                        + e.getMessage());
                return;
            }

            error1.setText("Account created!");
            error1.setTextFill(Color.web("#00FF00"));
            error2.setText("");
        }
    }

    @FXML protected void handleOkButton(MouseEvent event) {
        if(!accountCreated) {
            error1.setText("Account not yet created!");
            error1.setTextFill(Color.web("#FF0000"));
            error2.setText("");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
            Stage stage = new Stage();
            LoginWindowController controller = new LoginWindowController(stage);
            loader.setController(controller);

            try {
                root = loader.load();
            }
            catch(IOException e) {
                System.err.println("Caught IOException: " + e.getMessage());
            }

            Scene scene = new Scene(root, 400, 400);
            stage.setTitle("Welcome!");
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML protected void handleCancelButton(MouseEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
        Stage stage = new Stage();
        LoginWindowController controller = new LoginWindowController(stage);
        loader.setController(controller);

        try {
            root = loader.load();
        }
        catch(IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}

