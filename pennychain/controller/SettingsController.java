package pennychain.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import pennychain.db.Hash;
import pennychain.db.Connection_Online;
import pennychain.usr.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.paint.*;

import java.security.spec.InvalidParameterSpecException;

//import java.awt.*;


public class SettingsController{
    @FXML private Button cancelButton;
    @FXML private Button changePassButton;
    @FXML private Label userNameField;
    @FXML private PasswordField newPassField1;
    @FXML private PasswordField newPassField2;
    @FXML private PasswordField currentPassConfirm;
    @FXML private Label pwChangeResponse;
    //@FXML private double gridValue;
    @FXML private javafx.scene.control.TextField gridField;
    
    @FXML private ToggleButton emailButton;

    private Project project;
    private UserSession session;
    private userProfile cUser;
    private Map cMap;

    public SettingsController(UserSession session, Project project)
    {
        this.session = session;
        this.project = project;
    }

    @FXML
    public void initialize()
    {
        Text text1;
        // TODO: Pass info about user around properly between functions
        if(project == null)
            this.project = new Project("PROJECT NOT LINKED",new userProfile() );

        cUser = project.getUserProfile();
        cMap = project.getMainMap();

        gridField.setText(Integer.toString(project.getMainMap().getGrid_size()));

        //Try to get username from the UserProfile object
        userNameField.setText(cUser.getUsername());

    }

    @FXML protected void handleApplyButton(ActionEvent event) {

    }

    @FXML protected void handleOptimizerApply(ActionEvent event){
        cMap.changeGrid_size(Integer.parseInt(gridField.getText()));

        System.out.println(cMap.getGrid_size());// Diagnostic message
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }


    @FXML protected void handleChangePassButton(MouseEvent event) throws java.security.NoSuchAlgorithmException,
    java.security.spec.InvalidKeySpecException
    {
        String salt = Connection_Online.getSalt(session.getCurrentUser()).toString();
        String saltedHashedPass = Connection_Online.getHashedPass(session.getCurrentUser()).toString();


        if(Hash.verifyPassword(currentPassConfirm.getCharacters(), salt, saltedHashedPass)) {
            String newPass1 = newPassField1.getCharacters().toString();
            String newPass2 = newPassField2.getCharacters().toString();

            if(newPass1.equals(newPass2)) {
                String newSaltAndSaltedHashedPw = 
                    Hash.getHashAndSalt(newPassField2.getCharacters());

                String[] base64Strings = newSaltAndSaltedHashedPw.split(":");
                //Connection_Online.modifyRecord(); method not written yet!

                pwChangeResponse.setText("Password updated");
                pwChangeResponse.setTextFill(Color.web("00FF00"));
            }
            else {
                pwChangeResponse.setText("New passwords do not match");
                pwChangeResponse.setTextFill(Color.web("FF0000"));
            }
        }
        else {
            pwChangeResponse.setText("Current password is incorrect");
            pwChangeResponse.setTextFill(Color.web("FF0000"));
        }

    }
    
        @FXML protected void handleEmailButton(ActionEvent event){
        if(emailButton.getText().equals("Notifications On"))
            emailButton.setText("Notifications Off");
        else if(emailButton.getText().equals("Notifications Off"))
            emailButton.setText("Notifications On");
    }
}
