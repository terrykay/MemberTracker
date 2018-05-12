/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.InvoiceTO;
import Soap.ReceiptTO;
import UtilityClasses.MyDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class InvoiceRowItem extends InvoiceTO implements InvalidationListener {
    StringProperty invoiceTypeProperty;
    StringProperty invoiceAmountProperty;
    StringProperty invoicePaidProperty;
    
    InvoiceTO invoiceTO;
    
    final private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    public InvoiceRowItem() {
        setInvoice(new InvoiceTO());
    }
    
    public InvoiceRowItem(InvoiceTO anInvoice) {
        invoiceTO = anInvoice;
        setInvoice(anInvoice);
 //       invalidated((ObservableUnit)getUnitId());
    }
    
    public void setInvoice(InvoiceTO anInvoice) {
        invoiceTO = anInvoice;
        setAmount(anInvoice.getAmount());
        setDuedate(anInvoice.getDuedate());
        setType(anInvoice.getType());
        invoicenumber = anInvoice.getInvoicenumber();
        issuedate = anInvoice.getIssuedate();
        membershipchargeCollection = anInvoice.getMembershipchargeCollection();
        notes = anInvoice.getNotes();
        setReceiptCollection(anInvoice.getReceiptCollection());
    }

    public void setDueDate(XMLGregorianCalendar dueDate) {
        if (dueDate == null)
            return;
        if (invoiceTO != null)
            invoiceTO.setDuedate(dueDate);
        
        this.duedate = dueDate;
    }

    public void setType(String type) {
        this.type = type;
        if (invoiceTO != null)
            invoiceTO.setType(type);
        invoiceTypeProperty = setValue(invoiceTypeProperty, type);
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
        if (invoiceTO != null)
            invoiceTO.setAmount(amount);
        String value = "";
        if (amount != null)
            value = Double.toString(amount.doubleValue()/100);
       invoiceAmountProperty = setValue(invoiceAmountProperty, value);
    }
    
    public void setReceiptCollection(Collection <ReceiptTO> receipts) {
        this.receiptCollection = new ObservableList();
        this.receiptCollection.addAll(receipts);
       
        if (invoicePaidProperty == null)
            invoicePaidProperty = new SimpleStringProperty();
        ((ObservableList)this.receiptCollection).addListener(this);
        if (!receiptCollection.isEmpty())
            invoicePaidProperty = setValue(invoicePaidProperty, MyDate.longToDate(receiptCollection.get(0).getDate().toGregorianCalendar().getTime().getTime()));
    }
    
    public void addReceipt(ReceiptTO receipt) {
        receiptCollection.add(receipt);
        if (invoiceTO != null) {
            invoiceTO.getReceiptCollection().add(receipt);
        }
        invoicePaidProperty = setValue(invoicePaidProperty, MyDate.longToDate(receipt.getDate().toGregorianCalendar().getTime().getTime()));
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
            System.out.println("InvoiceRowItem: "+observable+" A receipt was added, marked");
            invoicePaidProperty = setValue(invoicePaidProperty, "paid");
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
