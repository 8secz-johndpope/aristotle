package com.aristotle.admin.jsf.convertors;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.Location;

@Component
@Scope("prototype")
public class LocationConvertor implements Converter {

    private List<Location> locations;

    public LocationConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println(this);
        if (locations == null) {
            return null;
        }
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                for (Location oneLocation : locations) {
                    if (oneLocation.getId().equals(id)) {
                        return oneLocation;
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
            return String.valueOf(((Location) object).getId());
        } else {
            return null;
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        System.out.println("Setting Locations");
        this.locations = locations;
    }


}
