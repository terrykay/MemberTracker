/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import Soap.AddressTO;
import Soap.CustomerTO;
import Soap.NotificationPreferencesTO;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import login.LoginTest;
import model.SoapHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utility.SSLUtilities;

/**
 *
 * @author tezk
 */
public class CustomerTest {
    
    private String sessionId;
    
    public CustomerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
                                SSLUtilities.trustAllHostnames();
                        SSLUtilities.trustAllHttpsCertificates();

        sessionId = LoginTest.loginAndGetSession();
    }
    
    @After
    public void tearDown() {
        LoginTest.logout(sessionId);
    }

    @Test
    public void testAddUser() {
        CustomerTO aCustomer = getCustomer();
        
        String customerId = SoapHandler.saveCustomer(aCustomer);
        
        assertNotNull(customerId);
        
        CustomerTO customerByID = SoapHandler.getCustomerByID(Integer.parseInt(customerId));
        
        assertEquals(aCustomer.getForename(), customerByID.getForename());
        
        SoapHandler.deleteCustomer(customerByID);
        
        customerByID = SoapHandler.getCustomerByID(Integer.parseInt(customerId));
        assertNull(customerByID);
    }
    
    @Test
    public void testAddUserWithNotificationPreferences() {
        CustomerTO aCustomer = getCustomer();
        NotificationPreferencesTO np = getNP();
        
        np.setCustomerId(aCustomer.getId());
        aCustomer.setNotificationPreferences(np);
        
         String customerId = SoapHandler.saveCustomer(aCustomer);
        
        CustomerTO customerByID = SoapHandler.getCustomerByID(Integer.parseInt(customerId));
        
        assertTrue(customerByID.getNotificationPreferences().isEmail());
        assertTrue(customerByID.getNotificationPreferences().isSms());
        assertTrue(customerByID.getNotificationPreferences().isPost());
        
        SoapHandler.deleteCustomer(customerByID);
        
        customerByID = SoapHandler.getCustomerByID(Integer.parseInt(customerId));
        assertNull(customerByID);
        
    }
    
    private CustomerTO getCustomer() {
        CustomerTO aCustomer = new CustomerTO();
        
        aCustomer.setSurname("Testsurname");
        aCustomer.setForename("Testforename");
        aCustomer.setAddressId(new AddressTO());
        
        return aCustomer;
    }
    
    private NotificationPreferencesTO getNP() {
        NotificationPreferencesTO newNP = new NotificationPreferencesTO();
        
        newNP.setEmail(Boolean.TRUE);
        newNP.setPost(Boolean.TRUE);
        newNP.setSms(Boolean.TRUE);
        
        return newNP;
    }
}
