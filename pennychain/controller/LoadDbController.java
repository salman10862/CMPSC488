package pennychain.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import pennychain.usr.UserSession;
import pennychain.db.Connection_Online;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Type;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadDbController {

    @FXML ListView<String> listView;

    @FXML Button loadButton;
    @FXML Button cancelButton;

    private UserSession session;
    private HashMap<String, ArrayList<String>> sharedProjsMap;

    public LoadDbController(UserSession session) {
        this.session = session;
    }

    @FXML public void initialize() {
        sharedProjsMap = new HashMap<>();
        ArrayList<String> projectTitles = Connection_Online.getUserProjects(session.getCurrentUser());

        for(String i : projectTitles) {
            System.out.println(i);
            listView.getItems().add(i);
        }
        
        ArrayList<ArrayList<String>> sharedProjects =
            Connection_Online.getSharedUserProjects(session.getCurrentUser());

        for(ArrayList i : sharedProjects) {
            System.out.println(i);

            String key = i.get(0) + " (shared by " + i.get(1) + ")";
            sharedProjsMap.put(key, i);
            listView.getItems().add(key);
        }
    }

    @FXML protected void handleLoad(MouseEvent event) throws IOException {
        Gson gson = new Gson();
        String json = "";
        Type projObj = new TypeToken<Project>() {}.getType();
        String projName = listView.getSelectionModel().getSelectedItem();

        if(sharedProjsMap.containsKey(projName)) {
            ArrayList<String> value = sharedProjsMap.get(projName);
            json = Connection_Online.getProjectJson(value.get(1), value.get(0));
        } else {
            json = Connection_Online.getProjectJson(session.getCurrentUser(), projName);
        }
        
        Project project = gson.fromJson(json, projObj);
        
        MapWindowController controller = new MapWindowController(project, session);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapWindow.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);

        Stage mapStage = new Stage();
        mapStage.setTitle("Application - Map Window");
        mapStage.setScene(scene);
        mapStage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML protected void handleCancel(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartWindow.fxml"));
        Stage startWindow = new Stage();
        StartWindowController controller = new StartWindowController(session);
        loader.setController(controller);
        Parent root = loader.load();

        Scene startScene = new Scene(root, 400, 400);
        startWindow.setTitle("Pennychain - Start or Load a Project");
        startWindow.setScene(startScene);
        startWindow.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
