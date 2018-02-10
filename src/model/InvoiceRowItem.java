/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.InvoiceTO;
import Soap.ReceiptTO;
import Soap.UnitTO;
import UtilityClasses.MyDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class InvoiceRowItem extends InvoiceTO implements InvalidationListener {
    StringProperty invoiceTypeProperty;
    StringProperty invoiceAmountProperty;
    StringProperty invoicePaidProperty;
    
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    public InvoiceRowItem() {
        setInvoice(new InvoiceTO());
    }
    
    public InvoiceRowItem(InvoiceTO anInvoice) {
        
        setInvoice(anInvoice);
 //       invalidated((ObservableUnit)getUnitId());
    }
    
    public void setInvoice(InvoiceTO anInvoice) {
        amount = anInvoice.getAmount();
        setDuedate(anInvoice.getDuedate());
        setType(anInvoice.getType());
        invoicenumber = anInvoice.getInvoicenumber();
        issuedate = anInvoice.getIssuedate();
        membershipchargeCollection = anInvoice.getMembershipchargeCollection();
        notes = anInvoice.getNotes();
        receiptCollection = new ObservableList();
        for (ReceiptTO each : anInvoice.getReceiptCollection()) {
            ObservableReceipt observableReceipt = new ObservableReceipt(each);
            observableReceipt.addListener(this);
        }    
        ((ObservableList)receiptCollection).addListener(this);
    }

    public void setDueDate(XMLGregorianCalendar dueDate) {
        if (dueDate == null)
            return;
        
        this.duedate = dueDate;
    }

    public void setType(String type) {
        this.type = type;
        invoiceTypeProperty = setValue(invoiceTypeProperty, type);
    }
    
    public void setReceiptCollection(Collection <ReceiptTO> receipts) {
        this.receiptCollection = new ArrayList(receipts);
        if (receiptCollection.isEmpty())
            return;
        invoicePaidProperty = setValue(invoicePaidProperty, MyDate.longToDate(receiptCollection.get(0).getDate().toGregorianCalendar().getTime().getTime()));
    }
    
    private StringProperty setValue(StringProperty string, String value) {
        if (string == null)
            string = new SimpleStringProperty(value);
        else
            string.setValue(value);
        return string;
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof ObservableReceipt) {
            System.out.println("InvoiceRowItem: "+observable+" A receipt was updated");
        } else if (observable instanceof ObservableList) {
            System.out.println("InvoiceRowItem: "+observable+" A receipt was added");
        }
   /*     if (observable instanceof ObservableUnit) {
            ObservableUnit aUnit = (ObservableUnit) observable;
            System.out.println("Values in invalidated : " + aUnit.getMake() + ", "+aUnit.getModel());
            String aDimension = aUnit.getDimensions();
            if (aDimension == null || aDimension.isEmpty())
                aDimension = "n/a";
            setDimensionsProperty(setValue(getDimensionsProperty(), aUnit.getDimensions()));
            String makeDisplay = aUnit.getMake();
            if (aUnit.getModel() != null && !aUnit.getModel().isEmpty())
                makeDisplay = makeDisplay.concat(" "+aUnit.getModel());
            inProperty = setValue(inProperty, makeDisplay);
            
        }*/
    }

    public StringProperty getInvoiceTypeProperty() {
        return invoiceTypeProperty;
    }

    public StringProperty getInvoiceAmountProperty() {
        return invoiceAmountProperty;
    }

    public StringProperty getInvoicePaidProperty() {
        return invoicePaidProperty;
    }
    
    
}
