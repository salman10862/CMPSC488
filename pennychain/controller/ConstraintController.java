package pennychain.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class ConstraintController{
    @FXML private Text actiontarget;
    @FXML private ListView cTypeList;
    @FXML private ListView cList;
    @FXML private javafx.scene.control.Button cancelButton;

    @FXML private TableView tView;

    private Project project;
    private Map currentMap;
    private boolean newConstraintType;

    private int[] desired_resourceList;
    private projResource  selected_Resource;

    private int selected_r_Cell;

    public ConstraintController(Project project)
    {
        this.project = project;
        this.currentMap = project.getMainMap();
        this.desired_resourceList = new int[project.getProjResourceList().size()];
    }

    @FXML
    public void initialize()
    {
        List<String> type_values = Arrays.asList("Implicit", "Explicit");

        cTypeList.setItems(FXCollections.observableList(type_values));

        ArrayList<String> population_tags = new ArrayList<>();

        cTypeList.getSelectionModel().selectionModeProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1.equals("Implicit")) {
                    newConstraintType = true;
                    //cList.setItems();
                }
                else {
                    newConstraintType = false;
                }
            }
        });

        if(project.isOptimization_implicit())
            cTypeList.getSelectionModel().selectFirst();
        else
            cTypeList.getSelectionModel().selectLast();


    }


    @FXML protected void handleApplyButton(ActionEvent event) {

    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();

    }

    @FXML protected void handleEditButton(ActionEvent event){


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Cell" + selected_r_Cell);
        dialog.setHeaderText("Enter the resource flow value for this resource:");

        Optional<String> result = dialog.showAndWait();
        String entered;

        if (result.isPresent()) {
            entered = result.get();
        }


    }

}