package com.aristotle.admin.jsf.convertors;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.DataPlugin;

@Component("jsfDataPluginConvertor")
public class DataPluginConvertor implements Converter {

    private List<DataPlugin> dataPlugins;

    public DataPluginConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println("value = " + value);
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                System.out.println("dataPlugins = " + dataPlugins.size());
                for (DataPlugin oneDataPlugin : dataPlugins) {
                    if (oneDataPlugin.getId().equals(id)) {
                        System.out.println("Match = " + id);
                        return oneDataPlugin;
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
        System.out.println("object = " + object);
        if (object != null) {
            if (object instanceof String) {
                return (String) object;
            }
            return String.valueOf(((DataPlugin) object).getId());
        } else {
            return null;
        }
    }

    public List<DataPlugin> getDataPlugins() {
        return dataPlugins;
    }

    public void setDataPlugins(List<DataPlugin> dataPlugins) {
        this.dataPlugins = dataPlugins;
    }

}
