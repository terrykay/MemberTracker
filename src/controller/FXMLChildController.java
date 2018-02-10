/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.ChildTO;
import UtilityClasses.MyDate;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLChildController extends FXMLParentController implements Initializable {

    private ChildTO childTO;

    public ChildTO getChildTO() {
        return childTO;
    }

    private boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private TextField fornameTextfield;
    @FXML
    private TextField surnameTextfield;
    @FXML
    private DatePicker DOBDatePicker;

    /**
     * Initializes the controller class.
     */
    public FXMLChildController() {
        FXMLPath = "FXMLChild.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DOBDatePicker.focusedProperty().addListener((obs, oldv, newv) -> {
            if (!newv) {
                DOBDatePicker.setValue(DOBDatePicker.getConverter().fromString(DOBDatePicker.getEditor().getText()));
            }
        });
    }

    public void setChildTO(ChildTO aChild) {
        childTO = aChild;
        System.err.println("Child set " + aChild.getForename());
        fornameTextfield.setText(aChild.getForename());
        surnameTextfield.setText(aChild.getSurname());
        DOBDatePicker.setValue(MyDate.toLocalDate(aChild.getDob()));
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        childTO.setForename(fornameTextfield.getText());
        childTO.setSurname(surnameTextfield.getText());
        childTO.setDob(MyDate.toXMLGregorianCalendar(DOBDatePicker.getValue()));

        updated = true;
        stage.hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

}
