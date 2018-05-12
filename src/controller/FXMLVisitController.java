/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.UnitTO;
import Soap.VisitTO;
import UtilityClasses.MyDate;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utility.Utility;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLVisitController extends FXMLParentController implements Initializable {

    private boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    private VisitTO aVisit = null;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ChoiceBox<String> inChoiceBox;
    @FXML
    private HBox caravanMakeHBox;
    @FXML
    private TextField caravanMakeTextField;
    @FXML
    private HBox caravanModelHBox;
    @FXML
    private TextField caravanModelTextField;
    @FXML
    private HBox caravanLengthHBox;
    @FXML
    private TextField caravanLengthTextField;
    @FXML
    private CheckBox electricityCheckBox;

    private static final String SELECT = "Select";
    private static final String DAY_VISIT = "Day visit";
    private static final String FIVEK_VISIT = "5K run";
    private static final String CHALET = "Chalet";
    private static final String HIRE_VAN = "Hire van";
    private static final String MOTOR_HOME = "Motorhome";
    private static final String GUEST = "Guest";
    private static final String TENT = "Tent";
    private static final String CARAVAN = "Caravan";
    private static final String[] CHOICES = {SELECT, DAY_VISIT, FIVEK_VISIT, CHALET, HIRE_VAN, GUEST, TENT, CARAVAN, MOTOR_HOME};

    public FXMLVisitController() {
        FXMLPath = "FXMLVisit.fxml";
    }

    public FXMLVisitController load() {
        FXMLVisitController controller = (FXMLVisitController) super.load();
        controller.getStage().setTitle("Visit details");
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableCaravanFields();
        LocalDate today = MyDate.asLocalDate(new Date());
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        inChoiceBox.getItems().addAll(FXCollections.observableArrayList(CHOICES));
        inChoiceBox.getSelectionModel().select(0);
        inChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number oldv, Number newv) -> {
                    int selected = newv.intValue();
                    if (oldv.equals(0) && inChoiceBox.getItems().get(0).equals(SELECT)) {
                        inChoiceBox.getItems().remove(0);
                        if (selected > 0) {
                            selected--;
                            inChoiceBox.getSelectionModel().select(selected);
                        }
                        updated = true;
                    }
                    if (selected >= 0) {
                        String selectedValue = inChoiceBox.getItems().get(selected);
                        if (FIVEK_VISIT.equals(selectedValue) || DAY_VISIT.equals(selectedValue)) {
                            endDatePicker.setDisable(true);
                            // caravanMakeField stores what is displayed...
                            caravanMakeTextField.setText(selectedValue);
                        } else {
                            endDatePicker.setDisable(false);
                            if (CARAVAN.equals(selectedValue) || TENT.equals(selectedValue) || MOTOR_HOME.equals(selectedValue)) {
                                enableCaravanFields();
                            }
                        }
                    }
                }
        );

        startDatePicker.setOnAction(v -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (endDate.isBefore(startDate)) {
                endDatePicker.setValue(startDate);
            }
        });
        endDatePicker.setOnAction(v -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (endDate.isBefore(startDate)) {
                startDatePicker.setValue(endDate);
            }
        });
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (inChoiceBox.getSelectionModel().getSelectedIndex() == 0
                && inChoiceBox.getItems().get(0).equals(SELECT)) {
            Utility.showAlert("Please select", "You must select a unit type to continue", "Please set the unit type used");
            return;
        }
        saveValues();
        updated = true;
        getStage().hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        getStage().hide();
    }

    private void saveValues() {

        if (aVisit == null) {
            aVisit = new VisitTO();
        }
        if (aVisit.getUnitId() == null) {
            aVisit.setUnitId(new UnitTO());
        }

        aVisit.setStartDate(MyDate.toXMLGregorianCalendar(startDatePicker.getValue()));
        aVisit.setEndDate(MyDate.toXMLGregorianCalendar(endDatePicker.getValue()));
        aVisit.setType(inChoiceBox.getSelectionModel().getSelectedItem());
        // TODO: Save choice
        aVisit.getUnitId().setMake(caravanMakeTextField.getText());
        aVisit.getUnitId().setModel(caravanModelTextField.getText());
        aVisit.getUnitId().setDimensions(caravanLengthTextField.getText());
        aVisit.getUnitId().setElectricity(electricityCheckBox.isSelected());
    }

    public void setValues(VisitTO aVisit) {
        if (aVisit == null) {
            return;
        }
        this.aVisit = aVisit;
        if (aVisit.getStartDate() != null) {
            startDatePicker.setValue(MyDate.toLocalDate(aVisit.getStartDate()));
        }
        if (aVisit.getEndDate() != null) {
            endDatePicker.setValue(MyDate.toLocalDate(aVisit.getEndDate()));
        }

        int selected = inChoiceBox.getItems().indexOf(aVisit.getType());
        if (selected > 0) {
            inChoiceBox.getSelectionModel().select(selected);
        }

        if (aVisit.getUnitId() != null) {

            caravanMakeTextField.setText(aVisit.getUnitId().getMake());
            caravanModelTextField.setText(aVisit.getUnitId().getModel());
            caravanLengthTextField.setText(aVisit.getUnitId().getDimensions());
            if (aVisit.getUnitId().isElectricity() == null) {
                aVisit.getUnitId().setElectricity(false);
            }
            electricityCheckBox.setSelected(aVisit.getUnitId().isElectricity());
        }
    }

    private void enableCaravanFields() {
        caravanLengthHBox.setDisable(false);
        caravanMakeHBox.setDisable(false);
        caravanModelHBox.setDisable(false);

        electricityCheckBox.setDisable(false);
    }

    private void disableCaravanFields() {
        caravanLengthHBox.setDisable(true);
        caravanMakeHBox.setDisable(true);
        caravanModelHBox.setDisable(true);

        electricityCheckBox.setDisable(true);
    }
}
