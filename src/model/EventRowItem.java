/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import IDTrackerTO.EventTO;
import UtilityClasses.MyDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class EventRowItem extends EventTO {
    StringProperty titleProperty, startDateProperty, endDateProperty, attendeesProperty;
    
    public void setEventTitle(String newTitle) {
   //     super.setEventTitle(newTitle);
        titleProperty.setValue(newTitle);
    }
    
    public void setStartDate(XMLGregorianCalendar newDate) {
    //    super.setStartDate(newDate);
        startDateProperty.setValue(MyDate.longToDate(MyDate.toDate(newDate).getTime()));
    }
    
    public void setEndDate(XMLGregorianCalendar newDate) {
  //      super.setEndDate(newDate);
        endDateProperty.setValue(MyDate.longToDate(MyDate.toDate(newDate).getTime()));
    }
    
    public EventRowItem(EventTO anEvent) {
        titleProperty = new SimpleStringProperty();
        startDateProperty = new SimpleStringProperty();
        endDateProperty = new SimpleStringProperty();
        attendeesProperty = new SimpleStringProperty();
        setEventTitle(anEvent.getEventTitle());
        setStartDate(anEvent.getStartDate());
        setEndDate(anEvent.getEndDate());
        Integer count = anEvent.getAttendees();
        attendeesProperty.setValue(count==null?"0":""+count);
        
        
        setId(anEvent.getId());      
        getEventPriceCollection().addAll(anEvent.getEventPriceCollection());
       
    }

    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    public void setTitleProperty(StringProperty titleProperty) {
        this.titleProperty = titleProperty;
    }

    public StringProperty getStartDateProperty() {
        return startDateProperty;
    }

    public void setStartDateProperty(StringProperty startDateProperty) {
        this.startDateProperty = startDateProperty;
    }

    public StringProperty getEndDateProperty() {
        return endDateProperty;
    }

    public void setEndDateProperty(StringProperty endDateProperty) {
        this.endDateProperty = endDateProperty;
    }

    public StringProperty getAttendeesProperty() {
        return attendeesProperty;
    }

    public void setAttendeesProperty(StringProperty attendeesProperty) {
        this.attendeesProperty = attendeesProperty;
    }
    
    public void setAttendees(int no) {
        super.setAttendees(no);
        attendeesProperty.setValue(""+no);
    }
    
    @Override
    public String toString() {
   //     return eventTitle;
   return "";
    }
    
}
