import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MapWindow extends Application {

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        /* Begin menu bar */
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        Menu fileMenu                  = new Menu("File");
        MenuItem menuItem_new          = new MenuItem("New");
        MenuItem menuItem_loadFromFile = new MenuItem("Load From File");
        MenuItem menuItem_loadFromDb   = new MenuItem("Load From Database");
        MenuItem menuItem_save         = new MenuItem("Save");
        MenuItem menuItem_saveas       = new MenuItem("Save as...");
        MenuItem menuItem_exit         = new MenuItem("Exit");

        Menu toolsMenu                 = new Menu("Tools");
        MenuItem menuItem_options      = new MenuItem("Options");

        Menu helpMenu                  = new Menu("Help");
        MenuItem menuItem_about        = new MenuItem("About");

        fileMenu.getItems().addAll(menuItem_new, menuItem_loadFromFile,
                menuItem_loadFromDb, new SeparatorMenuItem(), menuItem_save,
                menuItem_saveas, new SeparatorMenuItem(), menuItem_exit);

        toolsMenu.getItems().addAll(menuItem_options);

        helpMenu.getItems().addAll(menuItem_about);

        menuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);
        /* end menu bar */

        primaryStage.setTitle("Application - Map Window"); // name of app??
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
