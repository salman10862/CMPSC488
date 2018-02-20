import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.List;


public class ConstraintController{
    @FXML private Text actiontarget;
    @FXML private ListView cTypeList;
    @FXML private ListView cList;

    @FXML
    public void initialize()
    {
        List<String> values = Arrays.asList("Global", "Resource-tied", "District-tied");

        cTypeList.setItems(FXCollections.observableList(values));
    }

    @FXML protected void handleApplyButton(ActionEvent event) {
        actiontarget.setText("Test");
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        actiontarget.setText("Test");
    }

    @FXML protected void handleChangePassButton(ActionEvent event){
        actiontarget.setText("test");
    }

}