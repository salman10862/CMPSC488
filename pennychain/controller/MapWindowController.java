package pennychain.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import pennychain.center.OptimizationRequest;
import pennychain.usr.UserSession;


public class MapWindowController {

    @FXML private MenuBar menuBar;

    @FXML private MenuItem saveAsItem;
    @FXML private MenuItem aboutItem;
    @FXML private MenuItem exitItem;
    @FXML private MenuItem defineConstraintsItem;
    @FXML private MenuItem addAResourceItem;
    @FXML private MenuItem settingsItem;

    @FXML private WebEngine webEngine;
    @FXML private WebView webView;

    @FXML private Button lockMap;
    @FXML private Button zoomInButton;
    @FXML private Button zoomOutButton;
    @FXML private Button optimizeButton;

    @FXML private Canvas transGrid;
    @FXML private BorderPane windowPane;
    @FXML private ToolBar toolbar;

    private Project project;
    private Map currentMap;
    private UserSession userSession;

    private int currentZoom;

    private StackPane layerPane;
    @FXML private ComboBox<String> resourceChooser = new ComboBox<>();


    public MapWindowController(Project project, UserSession userSessionn)
    {
        this.userSession = userSessionn;
        this.project = project;
        this.currentMap = project.getMainMap();
    }

    @FXML protected void handleSaveAs(ActionEvent event) {
        Window stage = menuBar.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Project");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
        File file = fileChooser.showSaveDialog(stage);
        try {
            OutputStream out = new FileOutputStream(file);
            project.projectToFile(out);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
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

        SettingsController controller = new SettingsController(userSession, project);
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
                                  resourceChooser.getItems().setAll(project.getStringsofResources());

                                  System.out.println("Yep, it closed.");
                                  System.out.println(project.getStringsofResources());
                              }
                          }
        );
    }

    @FXML protected void lockMapListener(MouseEvent event){
        int GRID_SIZE =40; //TODO: Talk to group about design of setting initial gride size
        //Disable GoogleMaps UI
        webEngine.executeScript("disable()");

        // Set "center" of Map
        Double latitude = (Double) webEngine.executeScript("getLongitude()");
        Double longitude = (Double) webEngine.executeScript("getLatitude()");
        int zoom = (Integer) webEngine.executeScript("getZoom()");

        //TODO: Compute and add grid distance parameters to the Map
        Map map = new Map(GRID_SIZE, webView.getWidth(), webView.getHeight(), zoom, latitude, longitude);
        project.setMainMap(map);
        currentMap = project.getMainMap();
        currentZoom = zoom;
        lockMap.setVisible(false);
        if(!project.getStringsofResources().isEmpty())
            resourceChooser.getItems().addAll(FXCollections.observableList(project.getStringsofResources()));

        resourceChooser.setVisible(true);

        //Added Transparency Layer
        layerPane = new StackPane();

        transGrid.setWidth(webView.getWidth());
        transGrid.setHeight(webView.getHeight());
        layerPane.getChildren().addAll(webView, transGrid);
        windowPane.setCenter(layerPane);

        currentMap.initializeGrid();


        transGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Check that there is a resource to draw on the map
                if(!resourceChooser.getSelectionModel().isEmpty()) {
                    int selected = resourceChooser.getSelectionModel().getSelectedIndex();
                    projResource selected_Resource = project.getProjResourceList().get(selected);

                    double x_click = mouseEvent.getX();
                    double y_click = mouseEvent.getY();

                    double[] grid_coordinates = currentMap.getGridCoordinates(x_click, y_click);

                    double cell_length = currentMap.getCell_length();
                    double cell_width = currentMap.getCell_width();

                    int cell_x = (int) (grid_coordinates[0]/cell_width);
                    int cell_y = (int) (grid_coordinates[1]/cell_length);

                    if(mouseEvent.isControlDown()) {
                        removeSquare(x_click, y_click);
                        Color rColor = Color.BLACK;
                        GraphicsContext gc =transGrid.getGraphicsContext2D();
                        //GraphicsContext gc =
                        //selected_Resource.blockCoordinate(cell_x, cell_y);
                    }
                    else {
                        if (selected_Resource.getValueAtGrid(cell_x, cell_y) != 1) {
                            drawSquare(mouseEvent.getX(), mouseEvent.getY(), selected_Resource);
                            selected_Resource.placeCoordinate(cell_x, cell_y);
                        } else if (selected_Resource.getValueAtGrid(cell_x, cell_y) == 1) {
                            removeSquare(x_click, y_click);
                            selected_Resource.removeCoordinate(cell_x, cell_y);
                        }
                        //if(mouseEvent.isSecondaryButtonDown())
                    }
                        //TODO: Say that this resource CANNOT be here

                }
            }
        });

        //Enable map interactions
        zoomInButton.setDisable(false);
        zoomOutButton.setDisable(false);
        optimizeButton.setDisable(false);
        addAResourceItem.setDisable(false);
        defineConstraintsItem.setDisable(false);
    }

    private  void drawSquare(double x, double y, projResource selected_Resource){
        Color rColor = selected_Resource.getColor();
        GraphicsContext gc = transGrid.getGraphicsContext2D();
        gc.setFill(rColor);

        System.out.println("Trying to draw cell");
        double[] coordinates = currentMap.getGridCoordinates(x, y);
        gc.fillRect(coordinates[0],coordinates[1],currentMap.getCell_width(),currentMap.getCell_length());
    }

    private void removeSquare(double x, double y){
        GraphicsContext gc = transGrid.getGraphicsContext2D();
        double[] coordinates = currentMap.getGridCoordinates(x, y);
        System.out.println("Trying to clear cell");
        gc.clearRect(coordinates[0], coordinates[1], currentMap.getCell_width(), currentMap.getCell_length());
    }

    //This method (feel free to change the name as desired) is intended to be what is called when User tries to send an optimization request
    @FXML protected void sendOptimizationRequest() throws IOException{
        if(project.getMainMap() != null) {
            OptimizationRequest opreq = new OptimizationRequest(this.currentMap);
            this.currentMap = opreq.sendRequest(project.getOptimizationPath());
            project.setMainMap(this.currentMap);
        }
    }

    @FXML protected void handleZoomIn(){
        int zoom = currentMap.getZoom();
        if(currentZoom < 19) {
            currentZoom++;
            transGrid.setScaleX(currentMap.googleZoomScales[currentZoom] / currentMap.googleZoomScales[zoom]);
            transGrid.setScaleY(currentMap.googleZoomScales[currentZoom] / currentMap.googleZoomScales[zoom]);
            webEngine.executeScript("zoomIn()");
            layerPane.setDisable(true);
            if(currentZoom == 19)
                zoomInButton.setDisable(true);
        }
    }

    @FXML protected void handleZoomOut(){
        int zoom = currentMap.getZoom();
        if(currentZoom > zoom) {
            currentZoom--;
            transGrid.setScaleX(currentMap.googleZoomScales[currentZoom]/currentMap.googleZoomScales[zoom]);
            transGrid.setScaleY(currentMap.googleZoomScales[currentZoom]/currentMap.googleZoomScales[zoom]);
            webEngine.executeScript("zoomOut()");
            if(currentZoom == zoom)
                reEnableMapEdit();
            if(zoomInButton.isDisabled())
                zoomInButton.setDisable(false);
        }
    }

    private void reEnableMapEdit(){
        webView.setDisable(false);
        transGrid.setDisable(false);
        layerPane.setDisable(false);
        transGrid.toFront();
        System.out.println(layerPane.getChildren());
    }

    @FXML protected void initialize() {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());

        //Default case on loading an existing Map
        if(project.getMainMap()  != null) {
            webEngine.executeScript("setPerspective(" + project.getMainMap().getLatitude() + ", "
                    + project.getMainMap().getLongitude() + ", " + project.getMainMap().getZoom()
                    + ")");
            lockMap.setDisable(true);
            currentZoom = project.getMainMap().getZoom();
            if(!project.getStringsofResources().isEmpty()) {
                resourceChooser.getItems().addAll(FXCollections.observableList(project.getStringsofResources()));

            }
            resourceChooser.setVisible(true);

            resourceChooser.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
        }
        //Alternative case, new Project and no Map
        else{
            zoomInButton.setDisable(true);
            zoomOutButton.setDisable(true);
            optimizeButton.setDisable(true);
            defineConstraintsItem.setDisable(true);
            addAResourceItem.setDisable(true);

        }
    }

}
