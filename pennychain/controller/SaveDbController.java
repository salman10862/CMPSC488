package pennychain.controller;

import pennychain.db.Connection_Online;
import pennychain.usr.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.gson.Gson;

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
        //TODO
    }

    @FXML protected void handleSaveExisting(MouseEvent event) {
        String selectedProj = listView.getSelectionModel().getSelectedItem();
        // handle saving existing project
    }

    @FXML protected void handleSaveNew(MouseEvent event) {
        Gson gson = new Gson();
        String json = gson.toJson(project);
        // open new window to specify proj name
    }

    @FXML protected void handleCancel(MouseEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
