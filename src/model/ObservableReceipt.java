/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.ReceiptTO;
import Soap.UnitTO;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class ObservableReceipt extends ReceiptTO implements Observable {
    List <InvalidationListener> listenerList = new ArrayList();
    
    public ObservableReceipt(ReceiptTO aReceipt) {
        if (aReceipt == null)
            return;
        amount = aReceipt.getAmount();
        date = aReceipt.getDate();
        notes = aReceipt.getNotes();
        receiptnumber = aReceipt.getReceiptnumber();
        receivedby = aReceipt.getReceivedby();       
    }

    public void setAmount(Integer amount) {
        super.setAmount(amount);
        notifyChange();
    }

    public void setDate(XMLGregorianCalendar date) {
        super.setDate(date);
        notifyChange();
    }

    public void setNotes(String notes) {
        super.setNotes(notes);
        notifyChange();
    }

    public void setReceiptnumber(Integer receiptnumber) {
        super.setReceiptnumber(receiptnumber);
        notifyChange();
    }

    public void setReceivedby(String receivedby) {
        super.setReceivedby(receivedby);
        notifyChange();
    }

    private void notifyChange() {
        for (InvalidationListener each : listenerList) {
            each.invalidated(this);
        }
    }
    
    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }
    
    public void unbind() {
        listenerList.clear();
    }
}
