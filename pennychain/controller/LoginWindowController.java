package pennychain.controller;

import pennychain.db.Connection_Online;
import pennychain.db.Hash;
import pennychain.usr.UserSession;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

        CharSequence username = usernameField.getCharacters();

        if(!Connection_Online.userExists(username.toString())) {
            // throw some error
        }
        else {
            CharSequence saltedHashedPass = 
                Connection_Online.getHashedPass(username.toString());

            CharSequence salt = Connection_Online.getSalt(username.toString());

            boolean pwMatch = false;

            try {
                pwMatch = Hash.verifyPassword(
                        passwordField.getCharacters(),
                        salt.toString(),
                        saltedHashedPass.toString());
            }
            catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }

            if(pwMatch) {
                UserSession session = UserSession.getInstance();
                session.setCurrentUser(username.toString());
                // take user to start page
            }
            else {
                // show some error
            }
        }
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
