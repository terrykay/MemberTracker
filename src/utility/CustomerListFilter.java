/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SearchRowItem;

/**
 *
 * @author tezk
 */
public class CustomerListFilter {

    private ObservableList<SearchRowItem> customerList;
    private ObservableList<SearchRowItem> filteredList;
    private List<CustomerFilter> filters;
    private boolean andFlag = true;

    public void setAnd(boolean value) {
        andFlag = value;
    }

    public void setOr(boolean value) {
        andFlag = !value;
    }

    public CustomerListFilter() {
        filters = new ArrayList();
    }

    public CustomerListFilter(ObservableList<SearchRowItem> aList) {
        this();
        customerList = aList;
        filteredList = aList;
    }

    public List<SearchRowItem> getCustomerList() {
        return filteredList;
    }

    public void setCustomerList(ObservableList<SearchRowItem> customerList) {
        this.customerList = customerList;
        filteredList = customerList;
    }

    public void addFilter(CustomerFilter aFilter) {
        filters.add(aFilter);
    }
    
    public List <CustomerFilter> getFilterList() {
        return filters;
    }

    public void clearFilterList() {
        filters.clear();
    }
    
    public ObservableList<SearchRowItem> run() {
        if (customerList == null)
            return null;
        
        filteredList = FXCollections.observableArrayList();
        Iterator<SearchRowItem> i = customerList.iterator();
        if (andFlag) {
            // And = Start with flag true, any are false, set false
            while (i.hasNext()) {
                boolean flag = true;
                SearchRowItem eachCustomer = i.next();
                for (CustomerFilter eachFilter : filters) {
                    if (!eachFilter.include(eachCustomer)) {
                        flag = false;                       
                    }

                }
                if (flag) {
                    filteredList.add(eachCustomer);
                }
            }
        } else {
            // Or - start with flag false, any true, set true
            while (i.hasNext()) {
                boolean flag = false;
                SearchRowItem eachCustomer = i.next();
                for (CustomerFilter eachFilter : filters) {
                    if (eachFilter.include(eachCustomer)) {
                        flag = true;
                    }
                }
                if (flag) {
                    filteredList.add(eachCustomer);
                }
            }
        }
        return filteredList;
    }

}
