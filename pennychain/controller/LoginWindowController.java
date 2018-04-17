package pennychain.controller;

import pennychain.controller.StartWindowController;
import pennychain.controller.CreateAccountWindowController;
import pennychain.db.Connection_Online;
import pennychain.db.Hash;
import pennychain.usr.UserSession;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    @FXML private Label errorMessage;

    @FXML private Parent root;
    private Stage primaryStage;

    public LoginWindowController() {}

    public LoginWindowController(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML public void initialize() {
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {
                if(e.getCode() == KeyCode.ENTER)
                    login(e);
            }
        });
    }

    @FXML protected void handleLoginButton(MouseEvent event) {
        login(event);
    }

    protected void login(Event event) {
        CharSequence username = usernameField.getCharacters();

        if(!Connection_Online.userExists(username.toString())) {
            errorMessage.setText("Username not found");
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
                
                StartWindowController controller = new StartWindowController(session);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StartWindow.fxml"));
                loader.setController(controller);

                try {
                    root = loader.load();
                }
                catch(IOException e) {
                    System.err.println("Caught IOException: " + e.getMessage());
                }

                Stage stage = new Stage();
                Scene scene = new Scene(root, 400, 400);
                stage.setTitle("Pennychain - Start or Load a Project");
                stage.setScene(scene);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            else {
                errorMessage.setText("Passwords do not match");
            }
        }
    }

    @FXML protected void handleNewAcctButton(MouseEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccountWindow.fxml"));
        CreateAccountWindowController controller = new CreateAccountWindowController();
        loader.setController(controller);

        try {
            root = loader.load();
        }
        catch(IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Scene scene = new Scene(root, 400, 400);
        Stage stage = new Stage();

        stage.setTitle("Create an Account");
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML protected void handleExitButton(MouseEvent event) {
        Platform.exit();
    }
}
