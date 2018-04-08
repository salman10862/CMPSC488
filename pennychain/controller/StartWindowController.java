package pennychain.controller;

import pennychain.usr.UserSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class StartWindowController {

    @FXML private Button newProjectButton;
    @FXML private Button loadFromFileButton;
    @FXML private Button loadFromDbButton;
    @FXML private Button exitButton;
    
    private UserSession userSession;

    public StartWindowController(UserSession session) {
        this.userSession = session;
    }

    @FXML protected void handleNewProjectButton(MouseEvent event) throws IOException {
        Project project = new Project(userSession);

        MapWindowController controller = new MapWindowController(project, userSession);

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

    @FXML protected void handleLoadFromFile(MouseEvent event) throws IOException {
        Window stage = loadFromFileButton.getScene().getWindow();
        Gson gson = new Gson();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.JSON")
        );
        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {
            if(file.getName().endsWith(".JSON")) {
                JsonReader reader = null;
                try {
                    reader = new JsonReader(new FileReader(file));
                } catch(FileNotFoundException e) {
                    System.err.println("FILE NOT FOUND: " + e.getMessage());
                }
                Type type = new TypeToken<Project>() {}.getType();
                Project project = gson.fromJson(reader, type);

                MapWindowController controller = new MapWindowController(project, userSession);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("MapWindow.fxml"));
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root, 800, 600);

                Stage newStage = new Stage();
                newStage.setTitle("Application - Map Window");
                newStage.setScene(scene);
                newStage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        }
    }

    @FXML protected void handleLoadFromDb(MouseEvent event) throws IOException {
        LoadDbController loadDbController = new LoadDbController(userSession);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadDbWindow.fxml"));
        loader.setController(loadDbController);
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 400);

        Stage loadFromDbStage = new Stage();
        loadFromDbStage.setTitle("Load Project from Database");
        loadFromDbStage.setScene(scene);
        loadFromDbStage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML protected void handleExitButton(MouseEvent event) {
        Platform.exit();
    }
}
