/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import filter.DisplayFilter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLDisplayFilterController extends FXMLParentController implements Initializable {

    private DisplayFilter displayFilter = new DisplayFilter();
    
    @FXML
    private VBox checkboxContainer;
    @FXML
    private Button doneButton;
    
    public boolean doneSelected = false;

    public boolean isDoneSelected() {
        return doneSelected;
    }

    public void setDoneSelected(boolean doneSelected) {
        this.doneSelected = doneSelected;
    }
    

    public FXMLDisplayFilterController() {
        FXMLPath = "FXMLDisplayFilter.fxml";
    }

    public FXMLDisplayFilterController load() {
        FXMLDisplayFilterController controller = (FXMLDisplayFilterController) super.load();
        controller.getStage().setTitle("Please select columns");
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 0; i < DisplayFilter.fieldNames.length; i++) {
            CheckBox checkBox = new CheckBox(DisplayFilter.fieldNames[i]);
            checkBox.setId(DisplayFilter.fieldNames[i]);
            checkboxContainer.getChildren().add(checkBox);
        }

        setFields();
    }

    @FXML
    private void handleDoneButton(ActionEvent event) {
        doneSelected = true;
        stage.hide();
    }

    public void setDisplayFilter(DisplayFilter filter) {
        displayFilter = filter;
        setFields();
    }

    public DisplayFilter getDisplayFilter() {
        storeFields();
        return displayFilter;
    }
    
    private void setFields() {
        checkboxContainer.getChildren().forEach(reference -> {
            CheckBox checkBox = (CheckBox) reference;
            checkBox.setSelected(displayFilter.getValue(checkBox.getId()));
        });
    }
    
    private void storeFields() {
        checkboxContainer.getChildren().forEach(reference -> {
            CheckBox checkBox = (CheckBox) reference;
            displayFilter.setValue(checkBox.getId(), checkBox.isSelected());
        });
    }
}
