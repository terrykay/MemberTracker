/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.InvoiceTO;
import Soap.UnitTO;
import Soap.VisitTO;
import UtilityClasses.MyDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tezk
 */
public class VisitRowItem extends VisitTO implements InvalidationListener {
    StringProperty startDateProperty;
    StringProperty endDateProperty;
    StringProperty durationProperty;
    StringProperty inProperty;
    StringProperty dimensionsProperty;
    
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    public VisitRowItem() {
        setVisit(new VisitTO());
    }
    
    public VisitRowItem(VisitTO aVisit) {
        this();
         
        setVisit(aVisit);
        invalidated((ObservableUnit)getUnitId());
    }
    
    public void setVisit(VisitTO aVisit) {
        setId(aVisit.getId());
        setCustomerId(aVisit.getCustomerId());
        setStartDate(aVisit.getStartDate());
        setEndDate(aVisit.getEndDate());
        UnitTO aUnit = aVisit.getUnitId();
        if (aUnit == null)
            aUnit = new UnitTO();
        ObservableUnit observableUnit = new ObservableUnit(aUnit);
        observableUnit.addListener(this);
        setUnitId(observableUnit);
        setInvoiceCollection(aVisit.getInvoiceCollection());
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

    public StringProperty getInProperty() {
        return inProperty;
    }

    public void setInProperty(StringProperty inProperty) {
        this.inProperty = inProperty;
    }

    public StringProperty getDimensionsProperty() {
        return dimensionsProperty;
    }

    public void setDimensionsProperty(StringProperty dimensionsProperty) {
        this.dimensionsProperty = dimensionsProperty;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(XMLGregorianCalendar endDate) {
        if (endDate == null)
            return;
        
        String date = df.format(MyDate.toDate(endDate));
        setEndDateProperty(setValue(getStartDateProperty(), date));
        this.endDate = endDate;
        setDurationProperty();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<InvoiceTO> getInvoiceCollection() {
        return invoiceCollection;
    }

    public void setInvoiceCollection(List<InvoiceTO> invoiceCollection) {
        this.invoiceCollection = invoiceCollection;
    }

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
        if (startDate == null)
            return;
        
        String date = df.format(MyDate.toDate(startDate));
        setStartDateProperty(setValue(getStartDateProperty(), date));
        this.startDate = startDate;
        setDurationProperty();
    }

    public UnitTO getUnitId() {
        return unitId;
    }

    public void setUnitId(UnitTO unitId) {
        this.unitId = unitId;
    }

    public StringProperty getDurationProperty() {
        return durationProperty;
    }
    
    private void setDurationProperty() {
        if (getStartDate()!=null && getEndDate()!=null) {
            int daysBetween = MyDate.getDaysBetween(getStartDate(), getEndDate());
            durationProperty = setValue(getDurationProperty(), String.valueOf(daysBetween));
        }
    }
    
    public void setDimensions(String dimension) {
        if (getUnitId() == null)
            setUnitId(new UnitTO());
        getUnitId().setDimensions(dimension);
        
        setDimensionsProperty(setValue(getDimensionsProperty(), dimension));
    }
    
    
    
    private StringProperty setValue(StringProperty string, String value) {
        if (string == null)
            string = new SimpleStringProperty(value);
        else
            string.setValue(value);
        return string;
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof ObservableUnit) {
            ObservableUnit aUnit = (ObservableUnit) observable;
            System.out.println("Values in invalidated : " + aUnit.getMake() + ", "+aUnit.getModel());
            String aDimension = aUnit.getDimensions();
            if (aDimension == null || aDimension.isEmpty())
                aDimension = "n/a";
            setDimensionsProperty(setValue(getDimensionsProperty(), aUnit.getDimensions()));
            String makeDisplay = aUnit.getMake();
            if (aUnit.getModel() != null && !aUnit.getModel().isEmpty())
                makeDisplay = makeDisplay.concat(" "+aUnit.getModel());
            inProperty = setValue(inProperty, makeDisplay);
            
        }
    }
}
