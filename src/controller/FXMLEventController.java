/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import IDTrackerTO.EventPriceTO;
import IDTrackerTO.EventTO;
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
public class FXMLEventController extends FXMLParentController implements Initializable {

    private final String TITLE = "Add event";
    private EventTO anEvent;

    @FXML
    private TextField eventTitleTextField;
    @FXML
    private DatePicker startDateDatePicker;
    @FXML
    private DatePicker endDateDatePicker;

    public boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    public FXMLEventController() {
        FXMLPath = "FXMLEvent.fxml";
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        anEvent = null;
        startDateDatePicker.onActionProperty().addListener((obs, oldv, newv) -> {
            if (endDateDatePicker.getValue() == null) {
                endDateDatePicker.setValue(startDateDatePicker.getValue());
            }
        });
        startDateDatePicker.focusedProperty().addListener((obs, oldv, newv) -> {
            if (!newv) {
                startDateDatePicker.setValue(startDateDatePicker.getConverter().fromString(startDateDatePicker.getEditor().getText()));
            }
            if (endDateDatePicker.getValue() == null) {
                endDateDatePicker.setValue(startDateDatePicker.getValue());
            }
        });
        endDateDatePicker.focusedProperty().addListener((obs, oldv, newv) -> {
            if (!newv) {
                endDateDatePicker.setValue(endDateDatePicker.getConverter().fromString(endDateDatePicker.getEditor().getText()));
            }
        });

    }

    public FXMLParentController load() {
        FXMLParentController controller = super.load();
        stage.setTitle(TITLE);
        return controller;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        saveEvent();
        updated = true;
        stage.hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

    public void setEvent(EventTO anEvent) {
        this.anEvent = anEvent;
        eventTitleTextField.setText(anEvent.getEventTitle());
  //      startDateDatePicker.setValue(MyDate.toLocalDate(anEvent.getStartDate()));
   //     endDateDatePicker.setValue(MyDate.toLocalDate(anEvent.getEndDate()));
    }

    private void saveEvent() {
        if (anEvent == null) {
            anEvent = new EventTO();
        }
        anEvent.setEventTitle(eventTitleTextField.getText());
    //    anEvent.setStartDate(MyDate.toXMLGregorianCalendar(startDateDatePicker.getValue()));
   //     anEvent.setEndDate(MyDate.toXMLGregorianCalendar(endDateDatePicker.getValue()));
        // TODO: Better handling of the event pricing, set price to 0.00 for now
        EventPriceTO price = new EventPriceTO();
        price.setCost(0);
        price.setDescription("Default, testing purposes only");
        //price.setEvent(anEvent);
        anEvent.getEventPriceCollection().add(price);
    }
}
