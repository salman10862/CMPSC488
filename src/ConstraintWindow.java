import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ConstraintWindow extends Application {
    private Project project;

    @Override
    public void start(Stage stage) throws Exception {
        if(project == null)
            this.project = new Project("INVALID PROJECT", new userProfile());

        ConstraintController controller = new ConstraintController(project);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConstraintWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        stage = new Stage();
        stage.setTitle("Resources");
        stage.setScene(scene);
        stage.show();
        /*Parent root = FXMLLoader.load(getClass().getResource("ConstraintWindow.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Resources");
        stage.setScene(scene);
        stage.show();*/
    }


    static {
        System.setProperty("java.net.useSystemProxies", "true");
    }

    public static void main(String[] args) {

        launch(args);
    }
}
