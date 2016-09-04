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
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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
	public ComboBox buildCountryComboBoxWithIsdCode(ContextHelp contextHelp, FontAwesome icon, String caption, String help) throws AppException{
		List<Location> countries = locationService.getAllCountries();
		for(Location oneLocation : countries){
			oneLocation.setName(oneLocation.getName() +"("+oneLocation.getIsdCode()+")");
		}
		BeanItemContainer<Location> countryContainer = new BeanItemContainer<Location>(Location.class);
		countryContainer.addAll(countries);
		
		return buildComboBox(contextHelp, icon, caption, help, countryContainer, "name");
	}
	public ComboBox buildCountryComboBox(ContextHelp contextHelp, FontAwesome icon, String caption, String help) throws AppException{
		List<Location> countries = locationService.getAllCountries();
		BeanItemContainer<Location> countryContainer = new BeanItemContainer<Location>(Location.class);
		countryContainer.addAll(countries);
		
		return buildComboBox(contextHelp, icon, caption, help, countryContainer, "name");
	}
	
	public ComboBox buildStateComboBox(ContextHelp contextHelp, FontAwesome icon, String caption, String help) throws AppException{
		List<Location> states = locationService.getAllStates();
		BeanItemContainer<Location> countryContainer = new BeanItemContainer<Location>(Location.class);
		countryContainer.addAll(states);
		
		return buildComboBox(contextHelp, icon, caption, help, countryContainer, "name");
	}
	public void loadLocationsToComboBox(ComboBox comboBox, List<Location> locations) throws AppException{
		BeanItemContainer<Location> locationContainer = new BeanItemContainer<Location>(Location.class);
		locationContainer.addAll(locations);
		comboBox.setContainerDataSource(locationContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("name");
	}
	public <T> ComboBox buildComboBox(ContextHelp contextHelp, FontAwesome icon, String caption, String help, BeanItemContainer<T> countryContainer, String itemCaptionPropertyId){
		ComboBox comboBox = new ComboBox();
		if(icon != null){
			comboBox.setIcon(icon);
		}
		comboBox.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		comboBox.setCaption(caption);
		comboBox.setContainerDataSource(countryContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId(itemCaptionPropertyId);
		comboBox.setVisible(true);
		comboBox.setInvalidAllowed(false);
		setId(comboBox, caption);
		
		contextHelp.addHelpForComponent(comboBox, help, Placement.RIGHT);

		return comboBox;
	}
	public ComboBox buildComboBox(ContextHelp contextHelp, String caption, String help, String...items){
		ComboBox comboBox = new ComboBox();
		comboBox.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		comboBox.setCaption(caption);
		for(String oneItem : items){
			comboBox.addItem(oneItem);
		}
		setId(comboBox, caption);
		comboBox.setInvalidAllowed(false);
		contextHelp.addHelpForComponent(comboBox, help, Placement.RIGHT);

		return comboBox;
	}
	public PopupDateField buildPopupDateField(ContextHelp contextHelp, String caption, String help){
		PopupDateField popupDateField = new PopupDateField(caption);
		popupDateField.setDateFormat("dd-MMM-yyyy");
		contextHelp.addHelpForComponent(popupDateField, help, Placement.RIGHT);
		setId(popupDateField, caption);
		return popupDateField;
	}
	public Label buildErrorlabel(){
		return buildErrorlabel("error_label", false);
	}
	public Label buildErrorlabel(String labelId, boolean visible){
		Label errorLabel = new Label();
		errorLabel.setVisible(visible);
		errorLabel.addStyleName(ValoTheme.NOTIFICATION_FAILURE);
		errorLabel.setId(labelId);
		return errorLabel;
	}
	public Label buildSuccessLabel(){
		return buildLabel("success_label", false);
	}
	public Label buildLabel(String labelId, boolean visible){
		return buildLabel(labelId, visible, ValoTheme.NOTIFICATION_SUCCESS);
	}
	public Label buildLabel(String labelId, boolean visible, String theme){
		Label successLabel = new Label();
		successLabel.setVisible(visible);
		if(theme != null){
			successLabel.addStyleName(theme);
		}
		successLabel.setId(labelId);
		return successLabel;
	}
	public Label buildWarningLabel(){
		Label warningLabel = new Label();
		warningLabel.setVisible(false);
		warningLabel.addStyleName(ValoTheme.NOTIFICATION_WARNING);
		warningLabel.setId("warning_label");
		return warningLabel;
	}
	public Button buildButton(String valotheme, FontAwesome icon, String caption){
		Button button = new Button(caption, icon);
		button.setStyleName(valotheme);
		setId(button, caption+" button");
		return button;
	}
	public void setLabelError(Label errorLabel, Exception ex){
		setLabelMessage(errorLabel, ex.getMessage());
	}
	public void setLabelMessage(Label errorLabel, String message){
		errorLabel.setValue(message);
		errorLabel.setVisible(true);
	}
	public void setTextFieldValue(TextField textField, String value){
		if(value == null){
			textField.setValue("");
		}else{
			textField.setValue(value);
		}
	}
	public void setTextAreaValue(TextArea textArea, String value){
		if(value == null){
			textArea.setValue("");
		}else{
			textArea.setValue(value);
		}
	}
	private void setId(Component component, String caption){
		String id  = caption.toLowerCase();
		id = id.replaceAll(" ", "_");
		id = id.replaceAll("/", "_");
		id = id.replaceAll("\\?", "_");
		component.setId(id);
	}
}
