import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MapWindow extends Application {
    private Project project;

    @Override
    public void start(Stage stage) throws IOException {
        if(project == null)
            this.project = new Project("PROJECT NOT LINKED",new userProfile() );

        MapWindowController controller = new MapWindowController(project);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);

        stage = new Stage();
        stage.setTitle("Application - Map Window");
        stage.setScene(scene);
        stage.show();
        /*
        Parent root = null;

        try{
            root = FXMLLoader.load(getClass().getResource("MapWindow.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }


        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Application - Map Window");
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {

        Application.launch(args);
    }
}
