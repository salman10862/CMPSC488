package pennychain.controller;

import com.google.gson.Gson;
import pennychain.usr.UserSession;
import pennychain.db.Connection_Online;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadDbController {

    @FXML ListView<String> listView;

    @FXML Button loadButton;
    @FXML Button cancelButton;

    private UserSession session;

    public LoadDbController(UserSession session) {
        this.session = session;
    }

    @FXML public void initialize() {
        ArrayList<String> projectTitles = Connection_Online.getUserProjects(session.getCurrentUser());
        for(String i : projectTitles) {
            System.out.println(i);
            listView.getItems().add(i);
        }
    }

    @FXML protected void handleLoad(MouseEvent event) {
        Window stage = loadButton.getScene().getWindow();
        Gson gson = new Gson();
        //TODO finish this
    }

    @FXML protected void handleCancel(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartWindow.fxml"));
        Stage startWindow = new Stage();
        StartWindowController controller = new StartWindowController(session);
        loader.setController(controller);
        Parent root = loader.load();

        Scene startScene = new Scene(root, 400, 400);
        startWindow.setTitle("Pennychain - Start or Load a Project");
        startWindow.setScene(startScene);
        startWindow.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
