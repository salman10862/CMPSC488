package pennychain.controller;

import pennychain.db.Connection_Online;
import pennychain.usr.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.gson.Gson;

public class SaveNewDbController {

    @FXML private TextField newProjName;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Project project;
    private UserSession session;

    public SaveNewDbController(Project project, UserSession session) {
        this.project = project;
        this.session = session;
    }

    @FXML protected void handleSave(MouseEvent event) throws Exception {
        String name = newProjName.getCharacters().toString();
        project.setProjLabel(name);

        Gson gson = new Gson();
        String json = gson.toJson(project);
        if(Connection_Online.addProjectRecord(json)) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } else {
            throw new Exception("Something went wrong saving to DB...");
        }
    }

    @FXML protected void handleCancel(MouseEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
