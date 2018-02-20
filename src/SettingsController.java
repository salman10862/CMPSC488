import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class SettingsController {
    @FXML private Text actiontarget;
    @FXML private TextFlow userNameField;

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
        actiontarget.setText("Test");
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        actiontarget.setText("Test");
    }

    @FXML protected void handleChangePassButton(ActionEvent event){
        actiontarget.setText("test");
    }

}