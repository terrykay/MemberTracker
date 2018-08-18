/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 *
 * @author tezk
 */
public class ObservableList <E> extends ArrayList <E> implements Observable {
    List <InvalidationListener> listenerList = new ArrayList();
    
    private void notifyChange() {
        for (InvalidationListener each : listenerList) {
            each.invalidated(this);
        }
    }
    
    @Override
    public boolean add(E item) {
        super.add(item);
        notifyChange();
        return true;
    }
    
    public boolean addAll(List <E> items) {
        super.addAll(items);
        notifyChange();
        return true;
    }
    
    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listenerList.remove(listener);
    }
    
    public void dispose() {
        listenerList.clear();
    }
}
