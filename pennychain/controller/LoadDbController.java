package pennychain.controller;

import com.google.gson.Gson;
import pennychain.usr.UserSession;
import pennychain.db.Connection_Online;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadDbController {

    @FXML ListView<String> listView;

    @FXML Button loadButton;
    @FXML Button cancelButton;

    private UserSession session;

    public LoadDbController(UserSession session) {
        this.session = session;
    }

    @FXML void initialize(){
        //TODO populate ListView with project names by querying db
    }

    @FXML protected void handleLoad(MouseEvent event) {
       Window stage = loadButton.getScene().getWindow();
       Gson gson = new Gson();
       //TODO finish this
    }
}
