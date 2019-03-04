/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

/**
 *
 * Booleans for different fields to support filtering, with sensible defaults
 */
public class DisplayFilter {

    public boolean forename = true;
    public boolean surname = true;
    public boolean telephoneOne = true;
    public boolean telephoneTwo = true;
    public boolean address = true;
    public boolean email = true;
    public boolean insuranceExpiry = false;
    public boolean membershipType = false;
    public boolean pitchNumber = false;

    public static final String FORENAME = "Forename";
    public static final String SURNAME = "Surname";
    public static final String TELEPHONE_ONE = "Telephone one";
    public static final String TELEPHONE_TWO = "Telephone two";
    public static final String ADDRESS = "Address";
    public static final String EMAIL = "Email";
    public static final String INSURANCE_EXPIRY = "Insurance expiry";
    public static final String MEMBERSHIP_TYPE = "Membership type";
    public static final String PITCH_NUMBER = "Pitch number";
    public static String[] fieldNames = {FORENAME, SURNAME, TELEPHONE_ONE, TELEPHONE_TWO, ADDRESS, EMAIL, INSURANCE_EXPIRY, MEMBERSHIP_TYPE, PITCH_NUMBER};

    public void setAll(Boolean value) {
        forename = value;
        surname = value;
        telephoneOne = value;
        telephoneTwo = value;
        address = value;
        email = value;
        insuranceExpiry = value;
        membershipType = value;
        pitchNumber = value;
    }

    public void setValue(String string, boolean value) {
        switch (string) {
            case FORENAME:
                forename = value;
                break;
            case SURNAME:
                surname = value;
                break;
            case TELEPHONE_ONE:
                telephoneOne = value;
                break;
            case TELEPHONE_TWO:
                telephoneTwo = value;
                break;
            case ADDRESS:
                address = value;
                break;
            case EMAIL:
                email = value;
                break;
            case INSURANCE_EXPIRY:
                insuranceExpiry = value;
                break;
            case MEMBERSHIP_TYPE:
                membershipType = value;
                break;
            case PITCH_NUMBER:
                pitchNumber = value;
                break;
        }
    }

    public boolean getValue(String string) {
        boolean flag = false;
        switch (string) {
            case FORENAME:
                flag = forename;
                break;
            case SURNAME:
                flag = surname;
                break;
            case TELEPHONE_ONE:
                flag = telephoneOne;
                break;
            case TELEPHONE_TWO:
                flag = telephoneTwo;
                break;
            case ADDRESS:
                flag = address;
                break;
            case EMAIL:
                flag = email;
                break;
            case INSURANCE_EXPIRY:
                flag = insuranceExpiry;
                break;
            case MEMBERSHIP_TYPE:
                flag = membershipType;
                break;
            case PITCH_NUMBER:
                flag = pitchNumber;
                break;
        }
        return flag;
    }
    
    public byte[] asByteArray() {
        byte[] byteArray = new byte[fieldNames.length];
        
        for (int i=0; i<fieldNames.length; i++) {
            byteArray[i] = getValue(fieldNames[i]) ? (byte)1 : 0;
        }
        
        return byteArray;
    }
    
    public void fromByteArray(byte[] byteArray) {
        setAll(false);
        for (int i=0; i<fieldNames.length; i++) {
            setValue(fieldNames[i], byteArray[i] > 0);
        }
    }
}
