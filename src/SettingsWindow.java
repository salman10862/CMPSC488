import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SettingsWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SettingsTest.fxml"));

        Scene scene = new Scene(root, 600, 400);

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
