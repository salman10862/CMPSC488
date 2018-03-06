package pennychain.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;

public class AboutWindowController {

    @FXML private Button okButton;

    @FXML protected void handleOkButton(MouseEvent event) {

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
