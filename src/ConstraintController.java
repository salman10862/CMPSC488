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


public class ConstraintController{
    @FXML private Text actiontarget;
    @FXML private ListView cTypeList;
    @FXML private ListView cList;
    @FXML private javafx.scene.control.Button cancelButton;


    @FXML
    public void initialize()
    {
        List<String> type_values = Arrays.asList("Global", "Resource-tied", "District-tied");

        //TODO: Construct within the Project object a list of constraints associated, link it up to this
        List<String> global_values = Arrays.asList("Budget");

        cTypeList.setItems(FXCollections.observableList(type_values));

        cTypeList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String t = (String) cTypeList.getSelectionModel().getSelectedItem();
                if(t.equals("Global"))
                    cList.setItems(FXCollections.observableList(global_values));
                else if (t.equals("Reousrce-tied"))
                    cList.setItems(null);
                else
                    cList.setItems(null);

                System.out.println("Constraint Click registered");
            }
        });

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