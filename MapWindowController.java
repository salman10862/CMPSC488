import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapWindowController {

    @FXML private MenuItem aboutItem;
    @FXML private MenuItem exitItem;
    @FXML private MenuItem defineConstraintsItem;
    @FXML private MenuItem settingsItem;
    @FXML private WebEngine webEngine;
    @FXML private WebView webView;

    @FXML protected void handledefineConstraintsItem(ActionEvent event) throws IOException {

        Parent root = null;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("SettingsTest.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " +  e.getMessage());
        }

        Stage stage = new Stage();
        stage.setTitle("Project Constraints");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    @FXML protected void handleSettingsItem(ActionEvent event) throws IOException {

        Parent root = null;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("ConstraintWindow.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    @FXML protected void handleAboutItem(ActionEvent event) throws IOException {

        Parent root = null;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("AboutWindow.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }

    @FXML protected void handleExitItem(ActionEvent event) {
        Platform.exit();
    }

    @FXML protected void initialize() {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());
    }
}
