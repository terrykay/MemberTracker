/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.CustomerTO;

/**
 *
 * @author tezk
 */
public class SoapHandler {

    private static String sessionId;
    private static String userId;

    public static String getSessionId() { return sessionId; }
    public static String getUserId() { return userId; }
    
    public static String saveCustomer(Soap.CustomerTO aCustomer) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        System.out.println("Saving customer "+aCustomer.getForename()+", sessionId = "+sessionId);
        return port.saveCustomer(aCustomer, sessionId);
    }

    /* public static java.util.List<Soap.CustomerTO> getCustomers() {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getCustomers();
    }*/
    public static Boolean deleteCustomer(Soap.CustomerTO who) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.deleteCustomer(who, sessionId);
    }

    public static Integer saveAddress(Soap.AddressTO addressTO) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.saveAddress(addressTO, sessionId);
    }

    public static CustomerTO getCustomerByID(Integer customerID) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getCustomerByID(customerID, sessionId);
    }

    /*public static java.util.List<Soap.EventTO> getEvents() {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getEvents();
    }

    public static Integer saveEvent(Soap.EventTO anEvent) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.saveEvent(anEvent);
    }

    public static Boolean deleteEvent(Soap.EventTO anEvent) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.deleteEvent(anEvent);
    }

    public static java.util.List<Soap.CustomerTO> getCustomersForEvent(int eventId) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getCustomersForEvent(eventId);
    }

    public static boolean saveCustomerIsAttendingEvent(Soap.CustomerIsAttendingEventTO customerIsAttendingEvent) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.saveCustomerIsAttendingEvent(customerIsAttendingEvent);
    }
*/
    public static java.util.List<Soap.CustomerTO> getCustomers() {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getCustomers(sessionId);
    }

    public static java.util.List<Soap.CustomerTO> getDisplayCustomers() {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.getDisplayCustomers(sessionId);
    }
    
    public static String login(String userId, String password) {
        sessionId = loginUser(userId, password);

        return sessionId;

    }

    public static boolean isSessionValid() {
        if (SoapHandler.sessionId == null)
            return false;
        return checkIsValidSession(SoapHandler.sessionId);
    }
    

    
    private static String loginUser(java.lang.String username, java.lang.String password) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        userId = username;
        return port.loginUser(username, password);
    }

    private static boolean checkIsValidSession(java.lang.String sessionID) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.checkIsValidSession(sessionID);
    }

    public static String removePartner(int customerID) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        return port.removePartner(customerID, sessionId);
    }

    public static Boolean logout(java.lang.String sessionId) {
        Soap.Idtrackerws_Service service = new Soap.Idtrackerws_Service();
        Soap.Idtrackerws port = service.getIdtrackerwsPort();
        userId = null;
        return port.logout(sessionId);
    }
}
