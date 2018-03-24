package pennychain.controller;

import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConstraintController{
    @FXML private Text actiontarget;
    @FXML private ListView cTypeList;
    @FXML private ListView cList;
    @FXML private javafx.scene.control.Button cancelButton;

    @FXML private TableView tView;

    private Project project;
    private Map currentMap;

    private int[] desired_resourceList;
    private projResource  selected_Resource;

    public ConstraintController(Project project)
    {
        this.project = project;
        this.currentMap = project.getMainMap();
        this.desired_resourceList = new int[project.getProjResourceList().size()];
    }

    @FXML
    public void initialize()
    {
        List<String> type_values = Arrays.asList("Global", "Resource-tied", "District-tied");

        //TODO: Construct within the Project object a list of constraints associated, link it up to this
        List<String> global_values = Arrays.asList("Budget");

        //cTypeList.setItems(FXCollections.observableList(type_values));

        //TODO: Edit this to work with table view

        cTypeList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String t = (String) cTypeList.getSelectionModel().getSelectedItem();
                if(t.equals("Global"))
                    cList.setItems(FXCollections.observableList(global_values));
                else if (t.equals("Resource-tied")) {
                    ArrayList<projResource> list = project.getProjResourceList();
                    ArrayList<String> str_lst = new ArrayList<>();
                    for (projResource res:
                         list) {
                        String str = res.getLabel() + "   Desired Amount: " + res.getDesired_amnt();
                        str_lst.add(str);
                    }
                    cList.setItems(FXCollections.observableList(str_lst)); //TODO: Iterate over different types
                    cList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            selected_Resource = project.getProjResourceList().get(cList.getFocusModel().getFocusedIndex());
                        }
                    });
                }else
                    cList.setItems(null);   //TODO: Update with District implementation
            }
        });

    }


    @FXML protected void handleApplyButton(ActionEvent event) {

    }

    @FXML protected void handleCancelButton(ActionEvent event) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();

    }

    @FXML protected void handleEditButton(ActionEvent event){

    }

}