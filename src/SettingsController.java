import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.awt.*;


public class SettingsController {
    @FXML private javafx.scene.control.Button cancelButton;
    @FXML private TextFlow userNameField;
    @FXML private PasswordField newPassField1;
    @FXML private PasswordField newPassField2;
    @FXML private PasswordField currenPassConfirm;

    @FXML
    public void initialize()
    {
        Text text1;
        // TODO: Pass info about user around properly between functions
        userProfile cUser = new userProfile();

        //Try to get username from the UserProfile object
        text1 = new Text(cUser.getUsername());
        /*
            text1 = new Text("Example User");
            text1.setScaleX(1.2);
            text1.setScaleY(1.2);
        */

        userNameField.getChildren().add(text1);
    }

    @FXML protected void handleApplyButton(ActionEvent event) {
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }

    @FXML protected void handleChangePassButton(ActionEvent event){
        //Placeholder test
        if(newPassField1.getText().matches(newPassField2.getText()))
            System.out.println("The Passwords agree!");
    }

}