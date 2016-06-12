package com.aristotle.member.ui.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.jonatan.contexthelp.ContextHelp;
import org.vaadin.jonatan.contexthelp.widgetset.client.ui.Placement;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.service.LocationService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class UiComponentsUtil {

	@Autowired
	private LocationService locationService;
	
	public TextField buildTextField(ContextHelp contextHelp, FontAwesome icon, String caption, String help){
		TextField textField = new TextField(caption);
		setId(textField, caption);
		textField.setIcon(icon);
		textField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		contextHelp.addHelpForComponent(textField, help, Placement.RIGHT);
		return textField;
	}
	
	public PasswordField buildPasswordField(ContextHelp contextHelp, FontAwesome icon, String caption, String help){
		PasswordField password = new PasswordField(caption);
		password.setIcon(icon);
		setId(password, caption);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		contextHelp.addHelpForComponent(password, help, Placement.RIGHT);
		return password;
	}
	public ComboBox buildCountryComboBox(ContextHelp contextHelp, FontAwesome icon, String caption, String help) throws AppException{
		List<Location> countries = locationService.getAllCountries();
		for(Location oneLocation : countries){
			oneLocation.setName(oneLocation.getName() +"("+oneLocation.getIsdCode()+")");
		}
		BeanItemContainer<Location> countryContainer = new BeanItemContainer<Location>(Location.class);
		countryContainer.addAll(countries);
		
		return buildComboBox(contextHelp, icon, caption, help, countryContainer, "name");
	}
	public ComboBox buildComboBox(ContextHelp contextHelp, FontAwesome icon, String caption, String help, BeanItemContainer countryContainer, String itemCaptionPropertyId) throws AppException{
		ComboBox countryCombobox = new ComboBox();
		countryCombobox.setIcon(icon);
		countryCombobox.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		countryCombobox.setCaption(caption);
		countryCombobox.setContainerDataSource(countryContainer);
		countryCombobox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		countryCombobox.setItemCaptionPropertyId(itemCaptionPropertyId);
		countryCombobox.setVisible(false);
		setId(countryCombobox, caption);
		return countryCombobox;
	}
	public Label buildErrorlabel(){
		Label errorLabel = new Label();
		errorLabel.setVisible(false);
		errorLabel.addStyleName(ValoTheme.NOTIFICATION_FAILURE);
		errorLabel.setId("error_label");
		return errorLabel;
	}
	public Label buildSuccessLabel(){
		Label successLabel = new Label();
		successLabel.setVisible(false);
		successLabel.addStyleName(ValoTheme.NOTIFICATION_SUCCESS);
		successLabel.setId("success_label");
		return successLabel;
	}
	private void setId(Component component, String caption){
		String id  = caption.toLowerCase();
		id = id.replaceAll(" ", "_");
		component.setId(id);
	}
}
