package pennychain.controller;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.FutureTask;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import javafx.util.Duration;
import pennychain.center.OptimizationRequest;
import pennychain.usr.UserSession;
import pennychain.db.Connection_Online;
import sun.awt.Mutex;


public class MapWindowController {
    @FXML private MenuBar menuBar;

    @FXML private MenuItem newProject;
    @FXML private MenuItem saveAsItem;
    @FXML private MenuItem saveToDbItem;
    @FXML private MenuItem aboutItem;
    @FXML private MenuItem exitItem;
    @FXML private MenuItem defineConstraintsItem;
    @FXML private MenuItem addAResourceItem;
    @FXML private MenuItem settingsItem;
    @FXML private MenuItem logoutItem;
    @FXML private MenuItem shareItem;

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
    EventHandler<MouseEvent> mouseListener;

    private int currentZoom;

    private StackPane layerPane;
    @FXML private ComboBox<String> resourceChooser = new ComboBox<>();
    private ChoiceBox<Integer> grid_size_selection;
    private Text grid_size_text;

    public MapWindowController(Project project, UserSession userSessionn)
    {
        this.userSession = userSessionn;
        this.project = project;
        this.currentMap = project.getMainMap();
    }

    @FXML protected void handleNewProject(ActionEvent event) throws IOException {
        MapWindowController newController = new MapWindowController(new Project(userSession), userSession);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapWindow.fxml"));
        loader.setController(newController);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Application - Map Window");
        stage.setScene(scene);
        stage.show();
        ((Node) menuBar).getScene().getWindow().hide();
    }

    @FXML protected void handleSaveAs(ActionEvent event) throws Exception {
        Window stage = menuBar.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Project");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
        fileChooser.setInitialFileName("*.JSON");
        
        File file = fileChooser.showSaveDialog(stage);
        if(file != null) {
            if(file.getName().endsWith(".JSON")) {
                try {
                    Writer out = new FileWriter(file);
                    project.projectToFile(out);
                    out.close();
                }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                throw new Exception(file.getName()
                        + " does not have valid extension. File type must be .JSON");
            }
        }
    }

    @FXML protected void handleSaveToDb(ActionEvent event) throws IOException {
        SaveDbController controller = new SaveDbController(project, userSession);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveDbWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 400);

        Stage stage = new Stage();
        stage.setTitle("Save to Database");
        stage.setScene(scene);
        stage.show();
    }
        
    @FXML protected void handleShare(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Share Project");
        dialog.setHeaderText("Allow Another User Access to Your Project");
        dialog.setContentText("Enter the username you wish to share this project with: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && Connection_Online.userExists(result.get())) {
            project.shareWithUser(result.get());
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Share Error");
            alert.setHeaderText("Oops! There was a problem sharing your project.");
            alert.setContentText("Either the username you provided does not exist, or " +
                    "a connection to the database could not be established.");
            alert.showAndWait();
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
            root = FXMLLoader.load(getClass().getResource("AboutWindow.fxml"));
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
            }
        });
    }


    public void pressMouse(double local_x, double local_y, Point2D screen_coordinates, int i){
        Event.fireEvent(webView, new MouseEvent(MouseEvent.MOUSE_RELEASED,
                local_x, local_y, screen_coordinates.getX(), screen_coordinates.getY(), MouseButton.PRIMARY, 2,
                false, false, false, false, true, false, false, false, false, true, null));
        Event.fireEvent(webView, new MouseEvent(MouseEvent.MOUSE_PRESSED,
                local_x, local_y, screen_coordinates.getX(), screen_coordinates.getY(), MouseButton.PRIMARY, 2,
                false, false, false, false, false, false, false, false, false, false, null));
        System.out.println("Pressing cell " + i);
        Event.fireEvent(webView, new MouseEvent(MouseEvent.MOUSE_RELEASED,
                local_x, local_y, screen_coordinates.getX(), screen_coordinates.getY(), MouseButton.PRIMARY, 1,
                false, false, false, false, true, false, false, false, false, true, null));
        Event.fireEvent(webView, new MouseEvent(MouseEvent.MOUSE_PRESSED,
                local_x, local_y, screen_coordinates.getX(), screen_coordinates.getY(), MouseButton.PRIMARY, 1,
                false, false, false, false, false, false, false, false, false, false, null));
        //try{
          //  Thread.sleep(500);
        //} catch (Exception e){}
    }

    // NOTE: MouseEvents need to be fired twice to be captured
    public void advanceMouse(int i) {
        ArrayList<Point> cell_centers = currentMap.getGridCenters();
        if(i<cell_centers.size()){
            System.out.println("Trying to advance mouse to cell + " +  i);
            double local_x = cell_centers.get(i).getX();
            double local_y = cell_centers.get(i).getY();
            Point2D screen_coordinates = webView.localToScreen(local_x, local_y);
            final int final_int = i;
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                        try{
                            Timeline t = new Timeline(new KeyFrame(Duration.millis(50),
                                    ae -> pressMouse(local_x, local_y, screen_coordinates, final_int)));
                            t.play();
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    return null;
                }
            };

            Thread thread = new Thread(task);
            Platform.runLater(thread);

            System.out.println("OUT THE ROBOT");
        }
        else{
            //Added Transparency Layer
            layerPane = new StackPane();

            transGrid.setWidth(webView.getWidth());
            transGrid.setHeight(webView.getHeight());
            layerPane.getChildren().addAll(webView, transGrid);
            windowPane.setCenter(layerPane);
        }
    }



    @FXML protected void lockMapListener() throws AWTException, InterruptedException{
        toolbar.getItems().remove(lockMap);


        if(project.getMainMap()!=null) {
            toolbar.getItems().remove(grid_size_selection);
            toolbar.getItems().remove(grid_size_text);
            currentMap = project.getMainMap();
            currentZoom = currentMap.getZoom();

            //Added Transparency Layer
            layerPane = new StackPane();

            transGrid.setWidth(webView.getWidth());
            transGrid.setHeight(webView.getHeight());
            layerPane.getChildren().addAll(webView, transGrid);
            windowPane.setCenter(layerPane);
        } else {

            // Set the size of the overlay Grid if this is a new Map
            webEngine.executeScript("addCenterEvents()");
            int GRID_SIZE = grid_size_selection.getSelectionModel().getSelectedItem();


            // Set "center" of Map
            Double longitude = (Double) webEngine.executeScript("getLongitude()");
            Double latitude = (Double) webEngine.executeScript("getLatitude()");
            int zoom = (Integer) webEngine.executeScript("getZoom()");

            //Initialize other Map items
            Map map = new Map(GRID_SIZE, webView.getWidth(), webView.getHeight()-26, zoom, latitude, longitude);
            project.setMainMap(map);
            currentMap = project.getMainMap();
            currentZoom = zoom;
            currentMap.initializeGrid();
            Object m = new Object();
             class Countdown {
                 private int count = -1;
                 public synchronized int addCount() {  count++; return count;}
             }

             Countdown count = new Countdown();


            EventHandler<WebEvent <String>> latlng = new EventHandler<WebEvent<String>>() {
                @Override
                public void handle(WebEvent<String> stringWebEvent)  {
                    String event_string = stringWebEvent.getData();
                    String[] temp_points = event_string.split("/", 2);
                    Double lat_val = Double.valueOf(temp_points[0]);
                    Double long_val = Double.valueOf(temp_points[1]);
                    final int c = count.addCount();
                    System.out.println("Cell " + c + "has been pressed");
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            project.getMainMap().getGridLats()[c] = lat_val;
                            project.getMainMap().getGridLongs()[c] = long_val;
                            return null;
                        }
                    };

                    Thread thread = new Thread(task);
                    Platform.runLater(thread);
                    }
                };

            webEngine.setOnAlert(latlng);

            Thread.sleep(500);

            for(int i=0; i<project.getMainMap().getGridCenters().size() + 1; i++) {
                advanceMouse(i);
            }

            // UI cleanup
            windowPane.setCursor(Cursor.DEFAULT);
            toolbar.getItems().remove(grid_size_selection);
            toolbar.getItems().remove(grid_size_text);

        }

        //Disable GoogleMaps UI
        webEngine.executeScript("disable()");


        if(!project.getStringsofResources().isEmpty())
            resourceChooser.getItems().addAll(FXCollections.observableList(project.getStringsofResources()));

        resourceChooser.setVisible(true);


        resourceChooserListener();
         mouseListener = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Check that there is a resource to draw on the map
                System.out.println("Grid Longitudes are:");
                if(project.getMainMap().getGridLats()[project.getMainMap().getGridLats().length-1] == 0)
                {
                    int index = project.getMainMap().getGridLats().length-1;
                    for(int i = index; project.getMainMap().getGridLats()[index]==0 ;i--){
                        index=i;
                    }
                    for(int i = index; i<project.getMainMap().getGridLats().length; i++){
                        advanceMouse(i);
                    }
                }
                for(int i=0; i<currentMap.getGridLongs().length; i++)
                    System.out.println(currentMap.getGridLongs()[i] + " " + currentMap.getGridLats()[i]);

                if(!resourceChooser.getSelectionModel().isEmpty()) {
                    int selected = resourceChooser.getSelectionModel().getSelectedIndex();
                    projResource selected_Resource = project.getProjResourceList().get(selected);

                    double x_click = mouseEvent.getX();
                    double y_click = mouseEvent.getY();

                    if (currentMap.isInGrid(y_click)) {
                        double[] grid_coordinates = currentMap.getGridCoordinates(x_click, y_click);

                        double cell_length = currentMap.getCell_length();
                        double cell_width = currentMap.getCell_width();

                        int cell_x = (int) (grid_coordinates[0] / cell_width);
                        int cell_y = (int) (grid_coordinates[1] / cell_length);

                        // Scenario 1: User specifies this cell can never contain this projResource
                        if (mouseEvent.isControlDown()) {
                            removeSquare(x_click, y_click);
                            selected_Resource.blockCoordinate(cell_x, cell_y);
                            drawSquare(x_click, y_click, Color.BLACK);
                        } else {
                            // Scenario 2: User specifies that projResource is currently in this cell
                            if (selected_Resource.getValueAtGrid(cell_x, cell_y) != 1) {
                                drawSquare(mouseEvent.getX(), mouseEvent.getY(), selected_Resource.getColor());
                                selected_Resource.placeCoordinate(cell_x, cell_y);
                            }
                            // Scenario 3: projResource is not currently at this location, but possibly could be placed here
                            else if (selected_Resource.getValueAtGrid(cell_x, cell_y) == 1) {
                                removeSquare(x_click, y_click);
                                selected_Resource.removeCoordinate(cell_x, cell_y);
                            }
                        }
                    }
                }
            }
        };
        transGrid.setOnMouseClicked(mouseListener);

        //Enable map interactions
        zoomInButton.setDisable(false);
        zoomOutButton.setDisable(false);
        optimizeButton.setDisable(false);
        addAResourceItem.setDisable(false);
        defineConstraintsItem.setDisable(false);
    }

    private  void drawSquare(double x, double y, Color rColor){
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

    @FXML protected void sendOptimizationRequest() throws IOException{
        if(project.getMainMap() != null) {
            String[] routes = new OptimizationRequest(project.getProjResourceList()).sendRequest(project.getOptimizationPath(), project.getMainMap());
            //for(int i = 0; i < routes.length; i++) {
            //    String[] route = routes[i].split(",");
            //    String origin = route[0];
            //    String destination = route[1];
            //    webEngine.executeScript("displayRoutes("+origin+","+destination+")");
            //}
        }
        //String origin = "40.2042945,-76.7471177";
        //String destination = "40.2007294,-76.7550847";
        //webEngine.executeScript("displayRoutes("+origin+","+destination+")");
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

    // Enable editing for the central WebView
    private void reEnableMapEdit(){
        webView.setDisable(false);
        transGrid.setDisable(false);
        layerPane.setDisable(false);
        transGrid.toFront();
        System.out.println(layerPane.getChildren());
    }


    /// *********** FOR RECEIVING OPTIMIZER RESULTS
    private void setOptimizedResults(ArrayList<projResource> results){
        ComboBox<String> optimizedResults = new ComboBox<>();
        ArrayList<String> optimized_list = new ArrayList<>();
        for (projResource r:
             results) {
            optimized_list.add(r.getLabel() + " (RESULT)");
        }
        optimizedResults.getItems().setAll(optimized_list);
        toolbar.getItems().add(optimizedResults);
        optimizedResults.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Remove all current rectangles from canvas
                // Remove all current rectangles from the canvas
                GraphicsContext gc = transGrid.getGraphicsContext2D();
                gc.clearRect(0,0, transGrid.getWidth(), transGrid.getHeight());

                // Get corresponding optimizedResource
                projResource selectedResult = results.get(optimizedResults.getSelectionModel().getSelectedIndex());

                //DRAW IT
                ArrayList<Integer> placement_coordinates = selectedResult.getCoordinates();
                for(int i =0; i<placement_coordinates.size(); i=i+2){
                    drawSquare(placement_coordinates.get(i)*currentMap.getCell_width() + 1, (placement_coordinates.get(i+1)+1)*currentMap.getCell_length(), selectedResult.getColor());
                }
                transGrid.setOnMouseClicked(null);
            }
        });
    }

    //Handle resourceChooser ComboBox selections
    private void resourceChooserListener(){
        resourceChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Remove all current rectangles from the canvas
                GraphicsContext gc = transGrid.getGraphicsContext2D();
                gc.clearRect(0,0, transGrid.getWidth(), transGrid.getHeight());

                // Get corresponding projResource
                projResource selectedResource = project.getProjResourceList().get(resourceChooser.getSelectionModel().getSelectedIndex());

                // Fill in canvas for the selected projResource
                ArrayList<Integer> placement_coordinates = selectedResource.getCoordinates();
                ArrayList<Integer> block_coordinates = selectedResource.getBlockedCoordinates();
                for(int i =0; i<placement_coordinates.size(); i=i+2){
                    System.out.println("Attempt to draw coordinates:" + placement_coordinates.get(i) + " " + placement_coordinates.get(i+1));
                    drawSquare(placement_coordinates.get(i)*currentMap.getCell_width(), (placement_coordinates.get(i+1))*currentMap.getCell_length(), selectedResource.getColor());
                }
                for(int i=0; i<block_coordinates.size(); i=i+2){
                    System.out.println("Attempt to draw blocks");
                    drawSquare(block_coordinates.get(i)*currentMap.getCell_width()+1, (block_coordinates.get(i+1)+1)*currentMap.getCell_length(),Color.BLACK);
                }
                transGrid.setOnMouseClicked(mouseListener);
            }
        });
    }

    @FXML protected void handleLogout(ActionEvent event) throws IOException{
        userSession = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
        LoginWindowController controller = new LoginWindowController();
        loader.setController(controller);
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Application - Login Window");
        stage.setScene(scene);
        stage.show();
        ((Node) menuBar).getScene().getWindow().hide();
    }


    @FXML protected void initialize(){
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Add listener to activate when the webEngine has successfully loaded the .html file
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {

                            //Default case on loading an existing Map
                            if(project.getMainMap()  != null) {
                                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                                    @Override
                                    public void handle(WebEvent<String> stringWebEvent) {
                                        if (stringWebEvent.toString().equals("LOADED")) {
                                            System.out.println("CURRENTLY OPENING EXISTING PROJECT");
                                            currentZoom = project.getMainMap().getZoom();
                                            webEngine.executeScript("setPerspective(" + project.getMainMap().getLatitude() + ", "
                                                    + project.getMainMap().getLongitude() + ", " + currentZoom
                                                    + ")");
                                            try{
                                                lockMapListener();
                                                windowPane.getScene().setCursor(Cursor.CROSSHAIR);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                    }
                                    }
                                });
                            }
                            //Alternative case, new Project and no Map
                            else{
                                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                                    @Override
                                    public void handle(WebEvent<String> stringWebEvent) {
                                        System.out.println(stringWebEvent.getData());
                                    }
                                });
                                zoomInButton.setDisable(true);
                                zoomOutButton.setDisable(true);
                                optimizeButton.setDisable(true);
                                defineConstraintsItem.setDisable(true);
                                addAResourceItem.setDisable(true);

                                // Display a list of Grid Size options for the user to select
                                ArrayList<Integer> grid_size_list = new ArrayList<>();
                                grid_size_list.add(8); grid_size_list.add(16);
                                grid_size_list.add(32); grid_size_list.add(64);
                                grid_size_list.add(128); grid_size_list.add(256);
                                grid_size_selection = new ChoiceBox<Integer>();
                                grid_size_text = new Text("Grid Size: ");
                                grid_size_selection.setItems(FXCollections.observableList(grid_size_list));
                                grid_size_selection.setValue(Integer.valueOf(8));
                                toolbar.getItems().add(grid_size_text);
                                toolbar.getItems().add(grid_size_selection);
                            }
                        }
                    }
                });

        // Load GoogleMaps html file
        webEngine.load(getClass().getResource("googlemap.html").toString());
    }
}


