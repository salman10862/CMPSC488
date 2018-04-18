package pennychain.controller;

import com.google.gson.reflect.TypeToken;
import pennychain.db.Connection_Online;
import pennychain.db.SendMail;
import pennychain.usr.UserSession;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, ArrayList<String>> sharedProjsMap;

    public SaveDbController(Project project, UserSession session) {
        this.project = project;
        this.session = session;
    }

    @FXML protected void initialize() {
        sharedProjsMap = new HashMap<>();
        ArrayList<String> projectTitles = Connection_Online.getUserProjects(session.getCurrentUser());

        for(String i : projectTitles) {
            listView.getItems().add(i);
        }

        ArrayList<ArrayList<String>> sharedProjects =
            Connection_Online.getSharedUserProjects(session.getCurrentUser());

        for(ArrayList i : sharedProjects) {
            System.out.println(i);

            String key = i.get(0) + " (shared by " + i.get(1) + ")";
            sharedProjsMap.put(key, i);
            listView.getItems().add(key);
        }
    }

    @FXML protected void handleSaveExisting(MouseEvent event) throws Exception {
        Gson gson = new Gson();
        String selectedProj = listView.getSelectionModel().getSelectedItem();
        String projName = selectedProj;

        if(sharedProjsMap.containsKey(selectedProj)) {
            ArrayList<String> value = sharedProjsMap.get(projName);
            projName = value.get(0);
        }

        String json = gson.toJson(project);

        if(Connection_Online.updateProject(json)) {  //initially arg was "project" instead of "json"

            //send notification email when project is saved
            if(session.isEmailEnabled()){
                String userEmail = Connection_Online.getUserEmail(session.getCurrentUser());
                SendMail.sendEmail(userEmail, projName);
            }

            Stage stage = (Stage) saveExisting.getScene().getWindow();
            stage.close();

        } else {
            throw new Exception("Something went wrong saving to DB...");
        }

        //send notification email to users project is shared with
        //get list of users project is shared with
        ArrayList<String> list = Connection_Online.getSharedWithForProject(session.getCurrentUser(), projName);

        //send notificatiion email to each user
        for(String uname: list){
            if(Connection_Online.emailEnabled(uname)){  //if user has email notifications enabled
                SendMail.sendEmail(Connection_Online.getUserEmail(uname), projName);
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
