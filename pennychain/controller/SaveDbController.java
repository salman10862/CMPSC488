package pennychain.controller;

import pennychain.db.Connection_Online;
import pennychain.db.SendMail;
import pennychain.usr.UserSession;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.gson.Gson;

import javax.mail.Session;

public class SaveDbController {

    @FXML private ListView<String> listView;

    @FXML private Button saveExisting;
    @FXML private Button saveNew;
    @FXML private Button cancel;

    private Project project;
    private UserSession session;

    public SaveDbController(Project project, UserSession session) {
        this.project = project;
        this.session = session;
    }

    @FXML protected void initialize() {
        ArrayList<String> projectTitles = Connection_Online.getUserProjects(session.getCurrentUser());
        for(String i : projectTitles) {
            listView.getItems().add(i);
        }
    }

    @FXML protected void handleSaveExisting(MouseEvent event) {
        String selectedProj = listView.getSelectionModel().getSelectedItem();
        // handle saving existing project



        //send notification email to project owner
        if(session.isEmailEnabled()){
            String userEmail = Connection_Online.getUserEmail(session.getCurrentUser());
            String projName = listView.getSelectionModel().getSelectedItem();

            SendMail.sendEmail(userEmail, projName);
        }

        //send notification email to users project is shared with
        String project = selectedProj;

        if(project.contains("(")){
            project = project.split("(")[0];    //remove the shared by portion to get project name by itself
        }

        //get list of users project is shared with
        ArrayList<String> list = Connection_Online.getSharedWithForProject(session.getCurrentUser(), project);

        //send notificatiion email to each user
        for(String uname: list){
            if(Connection_Online.emailEnabled(uname)){  //if user has email notifications enabled
                SendMail.sendEmail(Connection_Online.getUserEmail(uname), project);
            }
        }
    }

    @FXML protected void handleSaveNew(MouseEvent event) throws IOException {
        SaveNewDbController controller = new SaveNewDbController(project, session);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveNewDbWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene saveNewDbScene = new Scene(root, 500, 150);

        Stage saveNewDbStage = new Stage();
        saveNewDbStage.setTitle("Save New Project");
        saveNewDbStage.setScene(saveNewDbScene);
        saveNewDbStage.show();
    }

    @FXML protected void handleCancel(MouseEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
