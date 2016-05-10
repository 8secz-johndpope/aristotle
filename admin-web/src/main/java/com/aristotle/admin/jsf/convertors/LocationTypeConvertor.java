package com.aristotle.admin.jsf.convertors;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.LocationType;

@Component("jsfLocationTypeConvertor")
public class LocationTypeConvertor implements Converter {

    private List<LocationType> locationTypes;

    public LocationTypeConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                for (LocationType oneLocationType : locationTypes) {
                    if (oneLocationType.getId().equals(id)) {
                        return oneLocationType;
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            if (object instanceof String) {
                return (String) object;
            }
            return String.valueOf(((LocationType) object).getId());
        } else {
            return null;
        }
    }

    public List<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<LocationType> locationTypes) {
        this.locationTypes = locationTypes;
    }


}
