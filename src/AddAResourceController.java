import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;


public class AddAResourceController{
    @FXML private Text actiontarget;
    @FXML private javafx.scene.control.Button cancelButton;


    @FXML
    public void initialize()
    {

    }


    @FXML protected void handleAddButton(ActionEvent event) {

    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();

    }

    @FXML protected void handleRemoveButton(ActionEvent event){

    }

}