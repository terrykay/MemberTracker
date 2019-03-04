package controller;

import Soap.ElectricitychargeTO;
import Soap.InvoiceTO;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.SearchRowItem;
import model.SoapHandler;
import utility.Utility;

public class FXMLCreateInvoicesController extends FXMLParentController implements Initializable {

    private static final String TITLE = "Create invoices";
    ObservableList<SearchRowItem> customerRowItemList;
    
   
    @FXML
    private TableView<SearchRowItem> customerTable;
    @FXML
    private TableColumn<SearchRowItem, String> nameColumn;
    
    public FXMLCreateInvoicesController() {
        FXMLPath = "FXMLCreateInvoices.fxml";
    }
    
    @Override
    public FXMLParentController load() {
        controller = super.load();

        controller.getStage().setTitle(TITLE);
        return controller;
    }
    
    public void setCustomerList(ObservableList <SearchRowItem> customers) {
        customerRowItemList = FXCollections.observableArrayList();
        
        if (customers == null)
            return;
        
        int total = 0;
        for (SearchRowItem eachCustomer : customers) {
            if (eachCustomer.getMembership() != null &&
                    eachCustomer.getMembership().isElectricityHookup() != null &&
                    eachCustomer.getMembership().isElectricityHookup()) {
                System.out.println("> "+eachCustomer.getForename()+" "+eachCustomer.getSurname());
                total++;
                customerRowItemList.add(eachCustomer);
            }
        }
        System.out.println("Total = "+total);
        customerTable.setItems(customerRowItemList);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(item -> { return new SimpleStringProperty(item.getValue().getForename()+" "+item.getValue().getSurname());} );
        
    }    

    @FXML
    private void handleCreateButton(ActionEvent event) {
   /*     for (SearchRowItem eachCustomer : customerRowItemList) {
            ElectricitychargeTO charge = new ElectricitychargeTO();
            charge.setYear(Integer.toString(new Date().getYear()));
            InvoiceTO invoice = new InvoiceTO();
            invoice.setAmount(1);
            charge.getInvoiceList().add(invoice);

            // If customer has an ID, ie: exists in DB, read all of their data
            if (eachCustomer.getCustomerId() != 0)
                eachCustomer.setCustomer(SoapHandler.getCustomerByID(eachCustomer.getCustomerId()));
            
            // For now only add one electricity charge per customer
            // TODO: update
            if (eachCustomer.getMembership().getElectricitychargeCollection().isEmpty()) {
                eachCustomer.getMembership().getElectricitychargeCollection().add(charge);
            } 
        }*/
   
        Utility.showAlert("Not currently implemented", "This is a long running process", "To be imlemented soon");
        getStage().hide();
    }
    
}
