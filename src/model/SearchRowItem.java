/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.CarTO;
import Soap.ChildTO;
import Soap.CustomerTO;
import Soap.MembershipTO;
import Soap.RefuseTO;
import UtilityClasses.MyDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author tezk
 */
public class SearchRowItem extends CustomerTO {

    // These items are not intended to be edited
    StringProperty nameProperty;
    StringProperty surnameProperty;
    StringProperty partnerProperty;
    StringProperty childrenProperty;
    StringProperty carRegProperty;
    StringProperty memberNoProperty;
    StringProperty societyProperty;
   
    public void setChildren(CustomerTO fromCustomer) {
        if (childrenProperty == null)
            childrenProperty = new SimpleStringProperty();
        StringBuilder tempString = new StringBuilder();
        for (ChildTO eachChild : fromCustomer.getChildCollection()) {
            tempString.append(eachChild.getForename());
            tempString.append(", ");
        }
        if (tempString.length() >= 2) {
            tempString.delete(tempString.length() - 2, tempString.length());
        }
        childrenProperty.setValue(tempString.toString());
    }
    
    public void setCustomerTO(CustomerTO fromCustomer) {
        setCustomer(fromCustomer);
     
        refresh(fromCustomer);
    }
    
    public void refresh(CustomerTO customer) {
        // update properties from member values
        nameProperty = setValue(nameProperty, getForename());
        surnameProperty = setValue(surnameProperty, getSurname());
        StringBuilder tempString = new StringBuilder();
        for (CarTO eachCar : customer.getCarCollection()) {
            tempString.append(eachCar.getRegno());
            tempString.append(", ");
        }
        if (tempString.length() >= 2) {
            tempString.delete(tempString.length() - 2, tempString.length());
        }
        carRegProperty = setValue(carRegProperty, tempString.toString());

        memberNoProperty = setValue(memberNoProperty, "n/a");
        societyProperty = setValue(societyProperty, "");
        MembershipTO mem = customer.getMembership();

        if (mem != null && mem.getMembershipNo()!=null && !mem.getMembershipNo().isEmpty()) {
            if (mem.getJoinedDate()!=null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String date = df.format(MyDate.toDate(mem.getJoinedDate()));
                memberNoProperty.setValue(date);
            }
            societyProperty.setValue(mem.getSociety());
        }
        setChildren(customer);
    }
    
    public SearchRowItem() {
        
    }

    public SearchRowItem(CustomerTO fromCustomer) {
        setCustomerTO(fromCustomer);
    }

    public int getCustomerId() {
        return getId();
    }

    public void setCustomer(CustomerTO aCustomer) {
        setForename(aCustomer.getForename());
        setSurname(aCustomer.getSurname());
        setAddressId(aCustomer.getAddressId());
        setDob(aCustomer.getDob());
        setEmail(aCustomer.getEmail());
        setGiftAid(aCustomer.isGiftAid());
        setHobbies(aCustomer.getHobbies());
        setId(aCustomer.getId());
        setMembership(aCustomer.getMembership());
        setMiddlenames(aCustomer.getMiddlenames());
        setOccupation(aCustomer.getOccupation());
        setPartnerId(aCustomer.getPartnerId());
        setTelephoneOne(aCustomer.getTelephoneOne());
        setTelephoneTwo(aCustomer.getTelephoneTwo());
        setPhotography(aCustomer.isPhotography());
        setRefuse(aCustomer.getRefuse());
        getNotes().clear();
        getNotes().addAll(aCustomer.getNotes());
        getNextOfKin().clear();
        getNextOfKin().addAll(aCustomer.getNextOfKin());
        
        getCarCollection().clear();
        getCarCollection().addAll(aCustomer.getCarCollection());
        getChildCollection().clear();
        getChildCollection().addAll(aCustomer.getChildCollection());
        getImageCollection().clear();
        getImageCollection().addAll(aCustomer.getImageCollection());
        
        getCarDeleteCollection().addAll(aCustomer.getCarDeleteCollection());
        getImageDeleteCollection().addAll(aCustomer.getImageDeleteCollection());
        getChildDeleteCollection().addAll(aCustomer.getChildDeleteCollection());
        
        getVisitCollection().clear();
        getVisitCollection().addAll(aCustomer.getVisitCollection());
    }
    
    @Override
    public void setForename(String newName) {
        super.setForename(newName);
        if (getNameProperty()==null)
            setNameProperty(new SimpleStringProperty());
        getNameProperty().setValue(newName);
    }
    
    @Override
    public void setSurname(String newName) {
        super.setSurname(newName);
        if (getSurnameProperty()==null)
            setSurnameProperty(new SimpleStringProperty());
        getSurnameProperty().setValue(newName);
    }
    
  
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    public StringProperty getSurnameProperty() {
        return surnameProperty;
    }

    public StringProperty getPartnerProperty() {
        return partnerProperty;
    }

    public void setPartnerProperty(StringProperty partnerProperty) {
        this.partnerProperty = partnerProperty;
    }

    public void setSurnameProperty(StringProperty surnameProperty) {
        this.surnameProperty = surnameProperty;
    }

    public StringProperty getChildrenProperty() {
        return childrenProperty;
    }

    public void setChildrenProperty(StringProperty childrenProperty) {
        this.childrenProperty = childrenProperty;
    }

    public StringProperty getCarRegProperty() {
        return carRegProperty;
    }

    public void setCarRegProperty(StringProperty carRegProperty) {
        this.carRegProperty = carRegProperty;
    }

    public StringProperty getMemberNoProperty() {
        return memberNoProperty;
    }

    public void setMemberNoProperty(StringProperty memberNoProperty) {
        this.memberNoProperty = memberNoProperty;
    }

    public StringProperty getSocietyProperty() {
        return societyProperty;
    }

    public void setSocietyProperty(StringProperty societyProperty) {
        this.societyProperty = societyProperty;
    }

    @Override
    public RefuseTO getRefuse() {
        //System.err.println("SearchRowItem.getRefuse : "+super.getRefuse());
        return super.getRefuse(); //To change body of generated methods, choose Tools | Templates.
    }

    private StringProperty setValue(StringProperty string, String value) {
        if (string == null)
            string = new SimpleStringProperty(value);
        else
            string.setValue(value);
        return string;
    }
    
    @Override
    public String toString() {
        return getForename() + " " + getSurname();
    }

}
