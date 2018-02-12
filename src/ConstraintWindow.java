import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ConstraintWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ConstraintWindow.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Resources");
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
