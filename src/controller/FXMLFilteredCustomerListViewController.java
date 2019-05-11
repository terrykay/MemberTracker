/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UtilityClasses.MyDate;
import filter.DisplayFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.FilteredSearchRowItem;
import model.SearchRowItem;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLFilteredCustomerListViewController extends FXMLParentController implements Initializable {

    public FXMLFilteredCustomerListViewController() {
        FXMLPath = "FXMLFilteredCustomerListView.fxml";
    }

    public FXMLFilteredCustomerListViewController load() {
        FXMLFilteredCustomerListViewController controller = (FXMLFilteredCustomerListViewController) super.load();
        controller.getStage().setTitle("Filtered column list");
        return controller;
    }

    @FXML
    private TableView<SearchRowItem> customerTable;

    List<TableColumn<SearchRowItem, String>> tableColumnList;

    List<String> tableItems = new ArrayList();

    ObservableList<SearchRowItem> items;

    public void setItems(ObservableList<SearchRowItem> items) {
        this.items = items;
        customerTable.setItems(items);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setTable() {

    }

    public void addColumn(String columnName) {
        tableItems.add(columnName);

        TableColumn<SearchRowItem, String> tableColumn = new TableColumn();
        tableColumn.setText(columnName);

        switch (columnName) {
            case DisplayFilter.FORENAME:
                tableColumn.setCellValueFactory(t -> t.getValue().getNameProperty());
                break;
            case DisplayFilter.SURNAME:
                tableColumn.setCellValueFactory(t -> t.getValue().getSurnameProperty());
                break;
            case DisplayFilter.TELEPHONE_ONE:
                tableColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTelephoneOne()));
                break;
            case DisplayFilter.TELEPHONE_TWO:
                tableColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTelephoneTwo()));
                break;
            case DisplayFilter.ADDRESS:
                tableColumn.setCellValueFactory(t -> {
                    if (t.getValue().getAddressId() != null) {
                        return new SimpleStringProperty(t.getValue().getAddressId().getPostcode());
                    } else {
                        return null;
                    }
                });
                break;
            case DisplayFilter.EMAIL:
                tableColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getEmail()));
                break;
            case DisplayFilter.INSURANCE_EXPIRY:
                tableColumn.setCellValueFactory(t -> {
                    SearchRowItem value = (SearchRowItem) t.getValue();
                    if (value.getMembership() != null) {
                        return new SimpleStringProperty(MyDate.toString(value.getMembership().getInsuranceExpiry()));
                    } else {
                        return null;
                    }
                });
                break;
            case DisplayFilter.MEMBERSHIP_TYPE:
                tableColumn.setCellValueFactory(t -> {
                    SearchRowItem value = (SearchRowItem) t.getValue();
                    if (value.getMembership() != null) {
                        return new SimpleStringProperty(value.getMembership().getType());
                    } else {
                        return null;
                    }
                });
                break;
            case DisplayFilter.PITCH_NUMBER:
                tableColumn.setCellValueFactory(t -> {
                    SearchRowItem value = (SearchRowItem) t.getValue();
                    if (value.getMembership() != null) {
                        return new SimpleStringProperty(value.getMembership().getPlotId());
                    } else {
                        return null;
                    }
                });
                break;
        }

        // Add a TableColumn to Table
        customerTable.getColumns().add(tableColumn);
    }
}
