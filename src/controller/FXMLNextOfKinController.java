package controller;

import Soap.NextOfKinTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class FXMLNextOfKinController extends FXMLParentController implements Initializable {

    @FXML
    private TextField nextOfKinField;
    @FXML
    private TextField contactNoField;
    @FXML
    private TextField relationShipField;
    @FXML
    private CheckBox naturistAwareCheckbox;
    
    private NextOfKinTO nextOfKin = null;
    
    private boolean updated = false;
    
    public boolean isUpdated() { return updated; }

    public FXMLNextOfKinController() {
        FXMLPath = "FXMLNextOfKin.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void setUpdatedKey(KeyEvent event) {
        updated = true;
    }

    @FXML
    private void setUpdatedAction(ActionEvent event) {
        updated = true;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        updated = true;
        saveValues();
        stage.hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

    public NextOfKinTO getNextOfKin() {
        return nextOfKin;
    }
    
    public void setNextOfKin(NextOfKinTO nextOfKin) {
        this.nextOfKin = nextOfKin;
        nextOfKinField.setText(nextOfKin.getName());
        contactNoField.setText(nextOfKin.getContactNo());
        relationShipField.setText(nextOfKin.getRelationship());
        naturistAwareCheckbox.selectedProperty().set(nextOfKin.isAwareNaturist());
    }
   
    private void saveValues() {
        if (nextOfKin == null)
            nextOfKin = new NextOfKinTO();
        
        nextOfKin.setName(nextOfKinField.getText());
        nextOfKin.setContactNo(contactNoField.getText());
        nextOfKin.setRelationship(relationShipField.getText());
        nextOfKin.setAwareNaturist(naturistAwareCheckbox.selectedProperty().getValue());
    }
}
