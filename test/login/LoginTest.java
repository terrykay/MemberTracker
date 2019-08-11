/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import UtilityClasses.Hash;
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
public class LoginTest {

    public LoginTest() {
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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLogin() {
        String sessionId = loginAndGetSession();
        assertNotNull(sessionId);
        assertTrue(SoapHandler.isSessionValid());
        logout(sessionId);
        assertFalse(SoapHandler.isSessionValid());
    }

    public static String loginAndGetSession() {
        String username = "test";
        String password = Hash.sha256("test");
        return SoapHandler.login(username, password);
    }
    
    public static void logout(String sessionId) {
        SoapHandler.logout(sessionId);
    }

}
