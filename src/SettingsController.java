import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.awt.*;


public class SettingsController{
    @FXML private javafx.scene.control.Button cancelButton;
    @FXML private TextFlow userNameField;
    @FXML private PasswordField newPassField1;
    @FXML private PasswordField newPassField2;
    @FXML private PasswordField currenPassConfirm;
    //@FXML private double gridValue;
    @FXML private javafx.scene.control.TextField gridField;

    private Project project;
    private userProfile cUser;
    private Map cMap;

    public SettingsController(Project project)
    {
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

    @FXML protected void handleOptimizerApply(ActionEvent event){
        cMap.changeGrid_size(Integer.parseInt(gridField.getText()));

        System.out.println(cMap.getGrid_size());// Diagnostic message
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