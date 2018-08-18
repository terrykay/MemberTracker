/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.InvoiceTO;
import Soap.ReceiptTO;
import UtilityClasses.MyDate;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.InvoiceRowItem;
import static utility.Utility.penceToPounds;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLInvoiceViewController extends FXMLParentController implements Initializable {
    // Currently only supports electricity charges
    public static final String INVOICE_CHOICE_ELECTRICAL = "Electricity";
    
    InvoiceTO invoice;
    
    @FXML
    private DatePicker raisedDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField amountTextField;
    @FXML
    private CheckBox paidCheckBox;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private TextField invoiceTypeTextField;

    public FXMLInvoiceViewController() {
        FXMLPath = "FXMLInvoiceView.fxml";
    }
    
    public FXMLInvoiceViewController load() {
        FXMLInvoiceViewController controller = (FXMLInvoiceViewController) super.load();
        controller.getStage().setTitle("Invoice");
        return controller;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        amountTextField.textProperty().addListener((e, oldv, newv) -> {
            if (newv != null) {
                if (newv.contains(".")) {
                    if (newv.indexOf('.') < newv.length() - 3) {
                        amountTextField.setText(oldv);
                        return;
                    }
                }
                amountTextField.setText(newv.replaceAll("[^\\d.]", ""));
            }
        });
        invoiceTypeTextField.setText(INVOICE_CHOICE_ELECTRICAL);
    }    

    @FXML
    private void handleRaisedDateChanged(ActionEvent event) {
        updated = true;
    }

    @FXML
    private void handleDueDateChanged(ActionEvent event) {
        updated = true;
    }

    @FXML
    private void handlePaidToggled(ActionEvent event) {
        updated = true;
        
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        saveInvoice();
        stage.hide();
    }
    
    public void setInvoice(InvoiceRowItem invoice) {
        this.invoice = invoice;
        amountTextField.setText(penceToPounds(invoice.getAmount()));
        raisedDatePicker.setValue(MyDate.toLocalDate(invoice.getIssuedate()));
        dueDatePicker.setValue(MyDate.toLocalDate(invoice.getDuedate()));
        if (invoice.getReceiptCollection() != null && invoice.getReceiptCollection().size() > 0) 
            paidCheckBox.setSelected(true);
        notesTextArea.setText(invoice.getNotes());
    }
    
    private void saveInvoice() {
        invoice.setAmount((int)(Double.parseDouble(amountTextField.getText())*100));
        invoice.setIssuedate(MyDate.toXMLGregorianCalendar(raisedDatePicker.getValue()));
        invoice.setDuedate(MyDate.toXMLGregorianCalendar(dueDatePicker.getValue()));
        if (paidCheckBox.isSelected() &&  invoice.getReceiptCollection().isEmpty()) {
            ReceiptTO newReceipt = new ReceiptTO();
            newReceipt.setAmount(invoice.getAmount());
            newReceipt.setDate(MyDate.toXMLGregorianCalendar(new Date()));
            newReceipt.setNotes(notesTextArea.getText());
            invoice.getReceiptCollection().add(newReceipt);
        }
        invoice.setNotes(notesTextArea.getText());
    }
}
