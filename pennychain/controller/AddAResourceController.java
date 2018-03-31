package pennychain.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AddAResourceController{
    Project project;

    @FXML private Text actiontarget;
    @FXML private Button cancelButton;
    @FXML private ListView rList;
    @FXML private ChoiceBox<String> valueChoice;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField labelField;
    @FXML private Button AddButton;

    private Color newRColor;
    private int type_flag;

    public AddAResourceController(Project project)
    {
        this.project = project;
    }

    @FXML
    public void initialize()
    {
        // Debugging scenario, when there is no project defined
        if(project == null)
            project = new Project("PROJECT NOT LINKED",new userProfile() );

        // Display the list of current projResources on the left
        ArrayList<String> resourceList = project.getStringsofResources();
        rList.setItems(FXCollections.observableList(resourceList));
        // Handle Resource Type
        ArrayList<String> rTypeList = new ArrayList<>();
        rTypeList.add("Generic Resource");
        rTypeList.add("Distribution Endpoint");
        valueChoice.setItems(FXCollections.observableList(rTypeList));
        valueChoice.getSelectionModel().selectFirst();
        valueChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                type_flag = t1.intValue();
            }
        });

        // Handle color picking
        newRColor = Color.WHITE.deriveColor(0,1,1,0.3);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Adjust any color selected to be slightly opaque
                newRColor = colorPicker.getValue().deriveColor(0,1,1,0.3);
            }
        });
    }



    @FXML protected void handleAddButton(ActionEvent event) {
        Stage s = (Stage) AddButton.getScene().getWindow();
        if(!labelField.getText().equals("")) {
            projResource newResource = new projResource(labelField.getText(), newRColor);
            //TODO: Check math more vigorously
            newResource.initializePlacement((int) ( project.getMainMap().getWidth() / project.getMainMap().getCell_width() ), (int) ( project.getMainMap().getLength() / project.getMainMap().getCell_length()) );
            newResource.setrType(type_flag);

            project.addProjResource(newResource);
            rList.setItems(FXCollections.observableList(project.getStringsofResources()));
            s.hide();
        }
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.hide();
    }


}