package pennychain.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import pennychain.center.OptimizationRequest;

public class MapWindowController {

    @FXML private MenuItem aboutItem;
    @FXML private MenuItem exitItem;
    @FXML private MenuItem defineConstraintsItem;
    @FXML private MenuItem settingsItem;
    @FXML private WebEngine webEngine;
    @FXML private WebView webView;
    @FXML private Button lockMap;
    @FXML private Canvas transGrid;
    @FXML private BorderPane windowPane;

    private Project project;
    private Map currentMap;
    @FXML private ComboBox<String> resourceChooser;


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

        stage.setOnHidden(new EventHandler<WindowEvent>(){
                              @Override
                              public void handle(WindowEvent windowEvent){
                                  resourceChooser.setItems(FXCollections.observableArrayList(project.getStringsofResources()));
                                  System.out.println("Yep, it closed.");
                                  System.out.println(project.getStringsofResources());
                              }
                          }
        );
    }

    @FXML protected void lockMapListener(ActionEvent event){
        int GRID_SIZE =100; //TODO: Talk to group about design of setting initial gride size
        webEngine.executeScript("disable()");

        Double latitude = (Double) webEngine.executeScript("getLongitude()");
        Double longitude = (Double) webEngine.executeScript("getLatitude()");
        int zoom = (Integer) webEngine.executeScript("getZoom()");
        //TODO: Compute and add grid distance parameters to the Map
        Map map = new Map(GRID_SIZE, 800, 600, zoom, latitude, longitude);
        project.setMainMap(map);
        lockMap.setVisible(false);
        resourceChooser = new ComboBox<>();
        if(!project.getStringsofResources().isEmpty())
            resourceChooser.setItems(FXCollections.observableList(project.getStringsofResources()));
        resourceChooser.setVisible(true); //TODO: //Implement resourceBar

        //Added Transparency Layer
        StackPane layerPane = new StackPane();
        windowPane.setCenter(layerPane);
        transGrid.setOpacity(0);
        transGrid.setWidth(webView.getWidth());
        transGrid.setHeight(webView.getHeight());
        layerPane.getChildren().addAll(webView, transGrid);
        transGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Canvas clicked at" + mouseEvent.getX());
            }
        });
    }

    //This method (feel free to change the name as desired) is intended to be what is called when User tries to send an optimization request
    @FXML protected void sendOptimizationRequest() throws IOException{
        if(project.getMainMap() != null) {
            OptimizationRequest opreq = new OptimizationRequest(this.currentMap);
            this.currentMap = opreq.sendRequest("C:\\"); 
                                            //TODO: sendRequest requires user-defined path name for file storage (C drive is temporary filler)
            project.setMainMap(this.currentMap);
        }
    }

    @FXML protected void initialize() {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());
        resourceChooser = new ComboBox();
        if(project.getMainMap()  != null) {
            webEngine.executeScript("setPerspective(" + project.getMainMap().getLatitude() + ", " + project.getMainMap().getLongitude() + ", "
                    + project.getMainMap().getZoom() + ")");
            lockMap.setDisable(true);
            if(!project.getStringsofResources().isEmpty())
                resourceChooser.setItems(FXCollections.observableList(project.getStringsofResources()));
            resourceChooser.setVisible(true);
        }
    }

}
