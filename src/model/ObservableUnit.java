/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.UnitTO;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 *
 * @author tezk
 */
public class ObservableUnit extends UnitTO implements Observable {
    List <InvalidationListener> listenerList = new ArrayList();

    public ObservableUnit(UnitTO aUnit) {
        setElectricity(false);
        if (aUnit == null)
            return;
        setId(aUnit.getId());
        setMake(aUnit.getMake());
        setModel(aUnit.getModel());
        setDimensions(aUnit.getDimensions());
        setElectricity(aUnit.isElectricity());
    }
    
    @Override
    public void setModel(String value) {
        super.setModel(value); //To change body of generated methods, choose Tools | Templates.
        notifyChange();
    }

    @Override
    public void setMake(String value) {
        super.setMake(value); //To change body of generated methods, choose Tools | Templates.
        notifyChange();
    }

    @Override
    public void setDimensions(String value) {
        super.setDimensions(value); //To change body of generated methods, choose Tools | Templates.
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
