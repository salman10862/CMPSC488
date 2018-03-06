package pennychain.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class SettingsWindow extends Application {
    private Project project;


    @Override
    public void start(Stage stage) throws Exception {
        //TODO: Pass a Project from the MapWindow into here
        if(project == null)
            project = new Project();

        SettingsController sc = new SettingsController(project);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsTest.fxml"));
        loader.setController(sc);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }


    static {
        System.setProperty("java.net.useSystemProxies", "true");
    }

    public static void main(String[] args) {

        launch(args);
    }
}

