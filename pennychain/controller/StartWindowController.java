package pennychain.controller;

import pennychain.usr.UserSession;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartWindowController {

    @FXML private Button newProjectButton;
    @FXML private Button exitButton;
    
    private UserSession userSession;

    public StartWindowController(UserSession session) {
        this.userSession = session;
    }

    @FXML protected void handleNewProjectButton(MouseEvent event) throws IOException {
        Project project = new Project();

        MapWindowController controller = new MapWindowController(project);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Application - Map Window");
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML protected void handleExitButton(MouseEvent event) {
        Platform.exit();
    }
}
