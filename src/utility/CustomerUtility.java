/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import Soap.CustomerTO;

/**
 *
 * @author tezk
 */
public class CustomerUtility {
    public static void copyCustomer(CustomerTO fromCustomer, CustomerTO toCustomer) {
        toCustomer.setAddressId(fromCustomer.getAddressId());
        toCustomer.getAmberWarningCollection().clear();
        toCustomer.getAmberWarningCollection().addAll(fromCustomer.getAmberWarningCollection());
        toCustomer.getCarCollection().clear();
        toCustomer.getCarCollection().addAll(fromCustomer.getCarCollection());
        toCustomer.getChildCollection().clear();
        toCustomer.getChildCollection().addAll(fromCustomer.getChildCollection());
        toCustomer.getCustomerCollection().clear();
        toCustomer.getCustomerCollection().addAll(fromCustomer.getCustomerCollection());
        toCustomer.setDob(fromCustomer.getDob());
        toCustomer.setEmail(fromCustomer.getEmail());
        toCustomer.setForename(fromCustomer.getForename());
        toCustomer.setGiftAid(fromCustomer.isGiftAid());
        toCustomer.setHobbies(fromCustomer.getHobbies());
        toCustomer.setId(fromCustomer.getId());
        toCustomer.getImageCollection().clear();
        toCustomer.getImageCollection().addAll(fromCustomer.getImageCollection());
        toCustomer.setMembership(fromCustomer.getMembership());
        toCustomer.setMiddlenames(fromCustomer.getMiddlenames());
        toCustomer.getNextOfKin().clear();
        toCustomer.getNextOfKin().addAll(fromCustomer.getNextOfKin());
        toCustomer.getNotes().clear();
        toCustomer.getNotes().addAll(fromCustomer.getNotes());
        toCustomer.setNotificationPreferences(fromCustomer.getNotificationPreferences());
        toCustomer.setOccupation(fromCustomer.getOccupation());
        toCustomer.setPartnerId(fromCustomer.getPartnerId());
        toCustomer.setPhotography(fromCustomer.isPhotography());
        toCustomer.setRefuse(fromCustomer.getRefuse());
        toCustomer.setSurname(fromCustomer.getSurname());
        toCustomer.setTelephoneOne(fromCustomer.getTelephoneOne());
        toCustomer.setTelephoneTwo(fromCustomer.getTelephoneTwo());
        toCustomer.getVisitCollection().clear();
        toCustomer.getVisitCollection().addAll(fromCustomer.getVisitCollection());
    }
}
