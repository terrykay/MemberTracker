/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import Soap.CustomerTO;
import model.SearchRowItem;

/**
 *
 * @author tezk
 */
public interface CustomerFilter {
    public boolean include(SearchRowItem aCustomer);
}
