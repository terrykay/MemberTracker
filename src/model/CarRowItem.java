/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Soap.CarTO;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author tezk
 * 
 * To wrap a CarTO to allow use in JavaFX tables, which needs observable properties
 */
public class CarRowItem extends CarTO {
    StringProperty regNoProperty;
    StringProperty makeProperty;
    StringProperty modelProperty;
    StringProperty colourProperty;
    
    public void set(CarTO aCar) {
        // CarRowItem extends CarTO, set parent CarTO
        idcar = aCar.getIdcar();
        regno = aCar.getRegno();
        colour = aCar.getColour();
        customerId = aCar.getCustomerId();
        dateAdded = aCar.getDateAdded();
        make = aCar.getMake();
        model = aCar.getModel();
    }
    
    @Override
    public void setRegno(String newNo) {
        super.setRegno(newNo);
        regNoProperty.setValue(newNo);
    }
    
    @Override
    public void setMake(String newMake) {
        super.setMake(newMake);
        makeProperty.setValue(newMake);
    }
    
    @Override
    public void setModel(String newModel) {
        super.setModel(newModel);
        modelProperty.setValue(newModel);
    }
    
    @Override
    public void setColour(String newColour) {
        super.setColour(newColour);
        colourProperty.setValue(newColour);
    }
    
    public CarRowItem (CarTO aCarTO) {
        //super(aCarTO);
        set(aCarTO);
        
        regNoProperty = new SimpleStringProperty(aCarTO.getRegno());
        makeProperty = new SimpleStringProperty(aCarTO.getMake());
        modelProperty = new SimpleStringProperty(aCarTO.getModel());
        colourProperty = new SimpleStringProperty(aCarTO.getColour());
        
        // Add listeners to update "plain" fields when UI fields are updated
        regNoProperty.addListener(t -> setRegno(regNoProperty.getValue()));
        makeProperty.addListener(t -> setMake(makeProperty.getValue()));
        modelProperty.addListener(t -> setModel(modelProperty.getValue()));
        colourProperty.addListener(t -> setColour(colourProperty.getValue()));
    }

    public StringProperty getRegNoProperty() {
        return regNoProperty;
    }

    public void setRegNoProperty(StringProperty regNoProperty) {
        this.regNoProperty = regNoProperty;
    }

    public StringProperty getMakeProperty() {
        return makeProperty;
    }

    public void setMakeProperty(StringProperty makeProperty) {
        this.makeProperty = makeProperty;
    }

    public StringProperty getModelProperty() {
        return modelProperty;
    }

    public void setModelProperty(StringProperty modelProperty) {
        this.modelProperty = modelProperty;
    }

    public StringProperty getColourProperty() {
        return colourProperty;
    }

    public void setColourProperty(StringProperty colourProperty) {
        this.colourProperty = colourProperty;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.regNoProperty);
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
        final CarRowItem other = (CarRowItem) obj;
        if (!Objects.equals(this.regNoProperty, other.regNoProperty)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return getRegno();
    }
}
