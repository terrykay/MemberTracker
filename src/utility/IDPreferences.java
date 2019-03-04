/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.prefs.Preferences;

/**
 *
 * @author tezk
 */
public class IDPreferences {
    private static IDPreferences idPreferences=null;
    public static IDPreferences getInstance() {
        if (idPreferences==null)
            idPreferences = new IDPreferences();
        return idPreferences;
    }
    
  
    protected IDPreferences() {
        
    }
     
    
    public String getProperty(String what) {
        String value = null;
        Preferences prefs = Preferences.userNodeForPackage(IDPreferences.class);
        value = prefs.get(what, "");
        System.err.println("Preference get : "+what+" = "+value);
        return value;
    }
    
    public void setProperty(String what, String value) {
        String oldValue = getProperty(what);
        Preferences prefs = Preferences.userNodeForPackage(IDPreferences.class);
        prefs.put(what, value);
    }

} 
