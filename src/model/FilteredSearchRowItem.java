/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import filter.DisplayFilter;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author tezk
 */
public class FilteredSearchRowItem {
    Map <String, StringProperty> itemMap = new HashMap();
    
    ObservableList <SearchRowItem> itemList;
    
    private DisplayFilter displayFilter;
    
    public StringProperty getProperty(String property) {
        return itemMap.get(property);
    }
    
    public void setProperty(String property, String value) {
        StringProperty get = itemMap.get(property);
        if (get == null) {
            get = new SimpleStringProperty();
            itemMap.put(property, get);
        }
       get.setValue(value);
    }
    
    public void setFilter(DisplayFilter filter) {
        displayFilter = filter;
        setColumns();
    }
    
    private void setColumns() {
        
    }
}