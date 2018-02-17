import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartWindowController {

    @FXML private Button newProjectButton;
    @FXML private Button exitButton;

    @FXML protected void handleNewProjectButton(MouseEvent event) throws IOException {
        Parent root = null;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("MapWindow.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Stage stage = new Stage();
        stage.setTitle("Application - Map Window");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML protected void handleExitButton(MouseEvent event) {
        Platform.exit();
    }
}
