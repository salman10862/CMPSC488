package pennychain.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountWindow extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("CreateAccountWindow.fxml"));
        }
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        Scene scene = new Scene(root, 450, 400);

        stage.setTitle("Create An Account");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        Application.launch(args);
    }
}
