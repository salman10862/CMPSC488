import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    @FXML private Button lockMap;

    private Project project;
    private Map currentMap;

    public MapWindowController(Project project)
    {
        this.project = project;
        this.currentMap = project.getMainMap();
    }

    @FXML protected void handledefineConstraintsItem(ActionEvent event) throws IOException {

        ConstraintController controller = new ConstraintController(project);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConstraintWindow.fxml"));

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        Stage stage = new Stage();
        stage.setTitle("Project Constraints");
        stage.setScene(scene);
        stage.show();

    }

    @FXML protected void handleSettingsItem(ActionEvent event) throws IOException {

        SettingsController controller = new SettingsController(project);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsTest.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
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

    @FXML protected void handleAddAResource(ActionEvent event) throws IOException{

        AddAResourceController controller = new AddAResourceController(project);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAResourceWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 590, 363);

        Stage stage = new Stage();
        stage.setTitle("Add a Resource");
        stage.setScene(scene);
        stage.show();

    }

    @FXML protected void lockMapListener(ActionEvent event){
        int GRID_SIZE =100; //TODO: Talk to group about design of setting initial gride size
        webEngine.executeScript("disable()");
        Double latitude = (Double) webEngine.executeScript("getLongitude()");
        Double longitude = (Double) webEngine.executeScript("getLatitude()");
        int zoom = (Integer) webEngine.executeScript("getZoom()");
        Map map = new Map(GRID_SIZE, 800, 600, zoom, latitude, longitude);
        project.setMainMap(map);
    }


    @FXML protected void initialize() {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());
        if(project.getMainMap() != null) {
            webEngine.executeScript("setPerspective(" + project.getMainMap().getLatitude() + ", " + project.getMainMap().getLongitude() + ", "
                    + project.getMainMap().getZoom() + ")");
            lockMap.setDisable(true);
        }
    }

}
