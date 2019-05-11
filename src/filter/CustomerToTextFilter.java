/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import Soap.AddressTO;
import Soap.CustomerTO;
import UtilityClasses.MyDate;
import java.util.List;
import model.SearchRowItem;
import model.SoapHandler;
import utility.Utility;
import static utility.Utility.checkValidID;

/**
 *
 * @author tezk
 */
public class CustomerToTextFilter {

    private StringBuilder string;

    public String toString(List<SearchRowItem> customers, DisplayFilter filter) {
        string = new StringBuilder();

        if (filter.forename) {
            add("Forename, ");
        }
        if (filter.surname) {
            add("Surname, ");
        }
        if (filter.telephoneOne) {
            add("Tel one, ");
        }
        if (filter.telephoneTwo) {
            add("Tel two, ");
        }
        if (filter.email) {
            add("Email, ");
        }
        if (filter.address) {
            add(" address one, address two, town, county, postcode, country, ");
        }
        if (filter.insuranceExpiry) {
            add("Insurance expiry, ");
        }
        if (filter.pitchNumber) {
            add("Pitch number, ");
        }
        removeComma();
        addReturn();

        for (SearchRowItem eachCustomer : customers) {
            CustomerTO aCustomer = SoapHandler.getCustomerByID(eachCustomer.getId());
            if (filter.forename) {
                addNoReturn(aCustomer.getForename());
                addComma();
            }
            if (filter.surname) {
                addNoReturn(aCustomer.getSurname());
                addComma();
            }
            if (filter.telephoneOne) {
                addNoReturn(aCustomer.getTelephoneOne());
                addComma();
            }
            if (filter.telephoneTwo) {
                addNoReturn(aCustomer.getTelephoneTwo());
                addComma();
            }
            if (filter.email) {
                addNoReturn(aCustomer.getEmail());
                addComma();
            }

            if (filter.address) {
                AddressTO address = aCustomer.getAddressId();
                if (address != null) {
                    addComma();
                    addNoReturn(address.getAddressLineOne());
                    addComma();
                    addNoReturn(address.getAddressLineTwo());
                    addComma();
                    addNoReturn(address.getTown());
                    addComma();
                    addNoReturn(address.getCounty());
                    addComma();
                    addNoReturn(address.getPostcode());
                    addComma();
                    addNoReturn(address.getCountry());
                }
            }

            if (filter.insuranceExpiry) {
                if (aCustomer.getMembership() != null) {
                    addNoReturn(MyDate.toString(aCustomer.getMembership().getInsuranceExpiry()));
                    addComma();
                }
            }
            if (filter.pitchNumber) {
                if (aCustomer.getMembership() != null) {
                    add(aCustomer.getMembership().getPlotId());
                    addComma();
                }
            }
            removeComma();
            addReturn();
        }
       
        return string.toString();
    }

    private void add(String what) {
        if (what != null && what.length() > 0) {
            addNoReturn(what);
        }
    }

    private void addNoReturn(String what) {
        if (what != null) {
            string.append(what);
        }
    }

    private void addSpace() {
        string.append(" ");
    }

    private void addReturn() {
        string.append("\n");
    }

    private void addComma() {
        string.append(", ");
    }

    private void removeComma() {
        string.replace(string.length() - 2, string.length() - 1, "");
    }
}
