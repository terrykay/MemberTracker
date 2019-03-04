/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.ChildTO;
import UtilityClasses.MyDate;
import UtilityClasses.MyFXDate;
import java.util.Date;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class ChildRowItem extends ChildTO {

    StringProperty forenameProperty;
    StringProperty surnameProperty;
    StringProperty dobProperty;
    StringProperty ageProperty;

    private void setAgeFromDOB() {
        ageProperty.setValue(getDob() == null ? "" : "" + MyFXDate.getYearsBetween(MyDate.toDate(getDob()), new Date(System.currentTimeMillis())));
    }

    @Override
    public void setForename(String newForename) {
        super.setForename(newForename);
        forenameProperty.setValue(newForename);
    }
    
    @Override
    public void setSurname(String newSurname) {
        super.setSurname(newSurname);
        surnameProperty.setValue(newSurname);
    }
    
    @Override
    public void setDob(XMLGregorianCalendar newCal) {
        super.setDob(newCal);
        dobProperty.setValue(newCal == null ? "" : MyFXDate.longToDate(newCal.toGregorianCalendar().getTime().getTime()));
        System.out.println("dob = "+newCal);
        setAgeFromDOB();
    }
    
    public void set(ChildTO aChild) {
        id = aChild.getId();
        customerId = aChild.getCustomerId();
        forename = aChild.getForename();
        surname = aChild.getSurname();
        dob = aChild.getDob();
    }

    public ChildRowItem(ChildTO aChild) {
        //super(aChild);
        set(aChild);

        forenameProperty = new SimpleStringProperty(aChild.getForename());
        surnameProperty = new SimpleStringProperty(aChild.getSurname());
        dobProperty = new SimpleStringProperty();
        dobProperty.setValue(aChild.getDob() == null ? "" : MyFXDate.longToDate(aChild.getDob().toGregorianCalendar().getTime().getTime()));
        ageProperty = new SimpleStringProperty();
        setAgeFromDOB();
//ageProperty.setValue(aChild.getDob()==null?"":""+MyFXDate.getYearsBetween(aChild.getDob(), new Date(System.currentTimeMillis())));

        forenameProperty.addListener(t -> setForename(forenameProperty.getValue()));
        surnameProperty.addListener(t -> setSurname(surnameProperty.getValue()));
        dobProperty.addListener(t -> {
            setDob(MyDate.toXMLGregorianCalendar(MyFXDate.stringToDate(dobProperty.getValue())));
            setAgeFromDOB();
        });
    }

    public StringProperty getForenameProperty() {
        return forenameProperty;
    }

    public void setForenameProperty(StringProperty forenameProperty) {
        this.forenameProperty = forenameProperty;
    }

    public StringProperty getSurnameProperty() {
        return surnameProperty;
    }

    public void setSurnameProperty(StringProperty surnameProperty) {
        this.surnameProperty = surnameProperty;
    }

    public StringProperty getDobProperty() {
        return dobProperty;
    }

    public void setDobProperty(StringProperty dobProperty) {
        this.dobProperty = dobProperty;
    }

    public StringProperty getAgeProperty() {
        return ageProperty;
    }

    public void setAgeProperty(StringProperty ageProperty) {
        this.ageProperty = ageProperty;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.forenameProperty);
        hash = 23 * hash + Objects.hashCode(this.surnameProperty);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChildRowItem other = (ChildRowItem) obj;
        if (!Objects.equals(this.forenameProperty, other.forenameProperty)) {
            return false;
        }
        if (!Objects.equals(this.surnameProperty, other.surnameProperty)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return forenameProperty.getValue();
    }

}
