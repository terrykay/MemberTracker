/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.ImageTO;
import UtilityClasses.MyDate;
import java.awt.image.BufferedImage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class ImageRowItem extends ImageTO {

    BufferedImage theImage = null;

    StringProperty detailsProperty;
    StringProperty expiredProperty;
    StringProperty scannedProperty;

    public void set(ImageTO anImageTO) {
        id = anImageTO.getId();
        type = anImageTO.getType();
        details = anImageTO.getDetails();
        scanned = anImageTO.getScanned();
        expires = anImageTO.getExpires();
        filename = anImageTO.getFilename();
        url = anImageTO.getUrl();
        customerId = anImageTO.getCustomerId();
    }

    public ImageRowItem(ImageTO anImageTO) {
        set(anImageTO);
        detailsProperty = new SimpleStringProperty(anImageTO.getDetails());
        expiredProperty = new SimpleStringProperty();
        setExpires(anImageTO.getExpires());
        scannedProperty = new SimpleStringProperty();
        setScanned(anImageTO.getScanned());

        // Add listeners to update "plain" fields when UI fields are updated
        detailsProperty.addListener(t -> setDetails(detailsProperty.getValue()));
        expiredProperty.addListener(t
                -> setExpires(MyDate.toXMLGregorianCalendar(MyDate.stringToDate(expiredProperty.getValue())))
        );
        scannedProperty.addListener(t
                -> setScanned(MyDate.toXMLGregorianCalendar(MyDate.stringToDate(scannedProperty.getValue())))
        );

    }

    public BufferedImage getTheImage() {
        if (theImage == null) {
            //Retrieve image from server first time we open

        }
        return theImage;
    }

    public void setTheImage(BufferedImage theImage) {
        this.theImage = theImage;
    }

    public void setDetails(String newDetails) {
        super.setDetails(newDetails);
        getDetailsProperty().setValue(newDetails);
    }
    
    public StringProperty getDetailsProperty() {
        return detailsProperty;
    }

    public void setDetailsProperty(StringProperty detailsProperty) {
        this.detailsProperty = detailsProperty;
    }

    public void setExpires(XMLGregorianCalendar newDate) {
        super.setExpires(newDate);
                if (newDate != null) {
            expiredProperty.setValue(MyDate.longToDate(newDate.toGregorianCalendar().getTime().getTime()));
        }


    }
    
    public StringProperty getExpiredProperty() {
        return expiredProperty;
    }

    public void setExpiredProperty(StringProperty expiredProperty) {
        this.expiredProperty = expiredProperty;
    }

    public void setScanned(XMLGregorianCalendar newDate) {
        super.setScanned(newDate);
        if (newDate != null) {
            scannedProperty.setValue(MyDate.longToDate(newDate.toGregorianCalendar().getTime().getTime()));
        }
    }
    
    public StringProperty getScannedProperty() {
        return scannedProperty;
    }

    public void setScannedProperty(StringProperty scannedProperty) {
        this.scannedProperty = scannedProperty;
    }

    @Override
    public String toString() {
        return detailsProperty.getValue();
    }

}
