import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.*;

public class StartWindow extends Application {

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 400);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.prefWidthProperty().bind(primaryStage.widthProperty());
        vbox.setAlignment(Pos.CENTER);

        Text title = new Text("App Name"); // replace with real name once we decide on one
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vbox.getChildren().add(title);

        Button newProj = new Button("New Project");
        Button loadProjLocal = new Button("Load Project From File");
        Button loadProjDb = new Button("Load Project From Database");
        Button exit = new Button("Exit");

        List<Button> buttons = new ArrayList<>();
        buttons.add(newProj);
        buttons.add(loadProjLocal);
        buttons.add(loadProjDb);
        buttons.add(exit);

        for(Button i : buttons) {
            i.setMinWidth(200);
            vbox.getChildren().add(i);
        }

        root.setCenter(vbox);
        primaryStage.setTitle("Application - Start Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
