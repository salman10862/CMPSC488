package pennychain.controller;

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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AddAResourceController{
    Project project;

    @FXML private Text actiontarget;
    @FXML private Button cancelButton;
    @FXML private ListView rList;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField labelField;
    @FXML private Button AddButton;

    private Color newRColor;

    public AddAResourceController(Project project)
    {
        this.project = project;
    }

    @FXML
    public void initialize()
    {
        if(project == null)
            project = new Project("PROJECT NOT LINKED",new userProfile() );
        ArrayList<String> resourceList = project.getStringsofResources();
        rList.setItems(FXCollections.observableList(resourceList));

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newRColor = colorPicker.getValue();
            }
        });
    }



    @FXML protected void handleAddButton(ActionEvent event) {
        Stage s = (Stage) AddButton.getScene().getWindow();
        if(!labelField.getText().equals("")) {
            project.addProjResource(new projResource(labelField.getText(), newRColor));
            rList.setItems(FXCollections.observableList(project.getStringsofResources()));
            s.hide();
        }
    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.hide();
    }


}