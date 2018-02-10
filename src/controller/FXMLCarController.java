/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.CarTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLCarController extends FXMLParentController implements Initializable {

    public FXMLCarController() {
        FXMLPath = "FXMLCar.fxml";
    }
    private CarTO carTO;

    @FXML
    private TextField regNoField;
    @FXML
    private TextField makeField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField colourField;

    private boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    public CarTO getCarTO() {
        return carTO;
    }

    public void setCarTO(CarTO carTO) {
        this.carTO = carTO;
        regNoField.setText(carTO.getRegno());
        makeField.setText(carTO.getMake());
        modelField.setText(carTO.getModel());
        colourField.setText(carTO.getColour());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        regNoField.textProperty().addListener((e, oldv, newv) -> {
            if (newv!=null) {
                regNoField.setText(newv.toUpperCase().trim().replace(" ", ""));
            }
        });
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (carTO == null) {
            carTO = new CarTO();
        }

        carTO.setRegno(regNoField.getText());
        carTO.setMake(makeField.getText());
        carTO.setModel(modelField.getText());
        carTO.setColour(colourField.getText());

        updated = true;
        stage.hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

}
