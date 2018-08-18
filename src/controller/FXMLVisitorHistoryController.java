/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.VisitTO;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.VisitRowItem;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLVisitorHistoryController extends FXMLParentController implements Initializable {

    @FXML
    private TableView<VisitRowItem> visitTable;
    @FXML
    private TableColumn<VisitRowItem, String> startColumn;
    @FXML
    private TableColumn<VisitRowItem, String> endColumn;
    @FXML
    private TableColumn<VisitRowItem, String> durationColumn;
    @FXML
    private TableColumn<VisitRowItem, String> inColumn;
    @FXML
    private TableColumn<VisitRowItem, String> dimensionsColumn;
    
    private List<VisitRowItem> deleteList = new ArrayList();

    public List <VisitRowItem> getDeleteList() { return deleteList; }
    
    public FXMLVisitorHistoryController() {
        FXMLPath = "FXMLVisitorHistory.fxml";
    }
    
    private boolean updated = false;
    
    public boolean isUpdated() { return updated; }
    
    ObservableList <VisitRowItem> visitList;
    
    public List <VisitRowItem> getItems() { return visitList; }
    
    public void setItems(List <VisitTO> visits) {
        for (VisitTO eachVisit : visits) {
            if (!(eachVisit instanceof VisitRowItem))
                eachVisit = new VisitRowItem(eachVisit);
            visitList.add((VisitRowItem)eachVisit);
        }
    }
    
    public FXMLVisitorHistoryController load() {
        FXMLVisitorHistoryController controller = (FXMLVisitorHistoryController) super.load();
        controller.getStage().setTitle("Recorded visits");
        return controller;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        visitList = FXCollections.observableArrayList();
        
        visitTable.setItems(visitList);
        
        startColumn.setCellValueFactory(value -> value.getValue().getStartDateProperty());
        endColumn.setCellValueFactory(value -> value.getValue().getEndDateProperty());
        durationColumn.setCellValueFactory(value -> {
            StringProperty durationProperty = value.getValue().getDurationProperty();
            if ("0".equals(durationProperty.getValue()))
                    durationProperty.setValue("day visit");
            return durationProperty;
        });
        inColumn.setCellValueFactory(value -> value.getValue().getInProperty());
        dimensionsColumn.setCellValueFactory(value -> value.getValue().getDimensionsProperty());
        
        visitTable.setContextMenu(new myContextMenu());
    }    
    
    private class myContextMenu extends ContextMenu {

        public myContextMenu() {
            MenuItem view = new MenuItem("View");
            MenuItem delete = new MenuItem("Delete");

            getItems().addAll(view, delete);
            delete.setOnAction(event -> {
                handleDelete();
            });
            view.setOnAction(event -> {
                handleTableClicked(null);
            });
        }
    }
    
    private void handleDelete() {
        VisitRowItem selectedItem = visitTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Please confirm");
        alert.setHeaderText("If you click OK, this action cannot be reversed");
        alert.setContentText("Visit will be removed. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            visitList.remove(selectedItem);

            deleteList.add(selectedItem);
            updated = true;
        }
    }
    
    @FXML
    private void handleAddNewButton(ActionEvent event) {
        FXMLVisitController controller = new FXMLVisitController();
        controller = (FXMLVisitController) controller.load();
        
        VisitRowItem aVisit = new VisitRowItem();
        controller.setValues(aVisit);
        
        controller.getStage().showAndWait();
        if (controller.isUpdated()) {
            visitList.add(aVisit);
            visitTable.refresh();
            updated = true;
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        updated = true;
        getStage().hide();
    }

    @FXML
    private void handleTableClicked(MouseEvent event) {

        if (event == null || event.getButton().equals(MouseButton.PRIMARY)) {
            VisitRowItem selection = (VisitRowItem) visitTable.getSelectionModel().getSelectedItem();
            if (event == null || event.getClickCount() >= 2) {
                editVisit(selection);
            }
        }
    }
    
    public void editVisit(VisitRowItem selection) {
        FXMLVisitController controller = new FXMLVisitController();
        controller = (FXMLVisitController) controller.load();
        controller.setValues(selection);
        controller.getStage().showAndWait();
    }
    
}
