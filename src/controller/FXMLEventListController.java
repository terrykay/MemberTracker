/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import IDTrackerTO.EventTO;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.EventRowItem;
import model.SoapHandler;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLEventListController extends FXMLParentController implements Initializable {

    ObservableList<EventRowItem> eventList;
    private final String TITLE = "Event list";

    @FXML
    private TableView<EventRowItem> eventTable;
    @FXML
    private TableColumn<EventRowItem, String> titleColumn;
    @FXML
    private TableColumn<EventRowItem, String> startColumn;
    @FXML
    private TableColumn<EventRowItem, String> endColumn;
    @FXML
    private TableColumn<EventRowItem, String> attendeesColumn;

    public FXMLEventListController() {
        FXMLPath = "FXMLEventList.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final ContextMenu contextMenu = new ContextMenu();

        eventList = FXCollections.observableArrayList();
        eventTable.setItems(eventList);
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
        attendeesColumn.setCellValueFactory(cellData -> cellData.getValue().getAttendeesProperty());

        MenuItem delete = new MenuItem("Delete");
        MenuItem view = new MenuItem("View/edit");
        MenuItem viewAttendees = new MenuItem("View attendees");
        contextMenu.getItems().addAll(view, viewAttendees, delete);
        delete.setOnAction(e -> handleDelete());
        view.setOnAction(e -> handleView());
        viewAttendees.setOnAction(e -> handleViewAttendeesButton(null));
        eventTable.setContextMenu(contextMenu);
    }

    public FXMLParentController load() {
        if (controller == null || controller.getStage()==null) {
            super.load();
            stage.setTitle(TITLE);
        }
        return controller;
    }

    public void loadEvents() {
  /*      List<EventTO> allEvents = SoapHandler.getEvents();
        // Sort into reverse date order
        allEvents.sort((EventTO e1, EventTO e2) -> {
            return e2.getStartDate().compare(e1.getStartDate());
        } );
        
        eventList.clear();
        if (allEvents != null) {
            for (EventTO eachEvent : allEvents) {
                eventList.add(new EventRowItem(eachEvent));
                System.out.println("Price for "+eachEvent.getEventTitle()+" = "+eachEvent.getEventPriceCollection().get(0).getId());
            }
        }*/
        
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.DELETE) {
            handleDelete();
        }
    }

    @FXML
    private void handleMouseClickOnTable(MouseEvent event) {
        int clicks = event.getClickCount();
        MouseButton button = event.getButton();
        if (clicks < 2) {
            return;
        }
        if (!button.equals(MouseButton.PRIMARY)) {
            return;
        }

        handleViewAttendeesButton(null);

    }

    @FXML
    private void handleKeyReleased(KeyEvent event) {
    }

    private boolean editEvent(EventTO anEvent) {
        FXMLEventController controller = new FXMLEventController();
        controller = (FXMLEventController) controller.load();
        controller.setEvent(anEvent);
        controller.getStage().showAndWait();
        if (controller.isUpdated()) {
  //          Integer id = SoapHandler.saveEvent(anEvent);
  //          anEvent.setId(id);
  //          System.out.println("event ID is : " + id);
            loadEvents();
            return true;
        }
        return false;
    }

    @FXML
    private void handleAddNewButton(ActionEvent event) {
        EventTO anEvent = new EventTO();
        if (editEvent(anEvent)) {
            loadEvents();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        stage.hide();
    }

    @FXML
    private void handleViewAttendeesButton(ActionEvent event) {
        FXMLSearchCustomerController controller = new FXMLSearchCustomerController();
        controller = (FXMLSearchCustomerController) controller.load();
        controller.setPickAndReturn(true);
        EventRowItem whichEvent = eventTable.getSelectionModel().getSelectedItem();
        if (whichEvent != null) {
 //           controller.showForEvent(whichEvent);
            controller.getStage().showAndWait();
            loadEvents();
          /*  List <EventTO>newList = SoapHandler.getEvents();
            for (EventTO eachEvent : newList ) {
                System.err.println("Name "+eachEvent.getEventTitle());
                System.err.println("Prices "+eachEvent.getEventPriceCollection());
            }*/
        }
    }

    private void handleView() {
        EventRowItem whichItem = eventTable.getSelectionModel().getSelectedItem();
 //       editEvent(whichItem);
        loadEvents();
    }

    private void handleDelete() {
        EventRowItem whichItem = eventTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please confirm");
        alert.setHeaderText("If you click OK, this action cannot be reversed");
        alert.setContentText(whichItem + " will be removed. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            eventList.remove(whichItem);

 //           SoapHandler.deleteEvent(whichItem);
            loadEvents();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

}
