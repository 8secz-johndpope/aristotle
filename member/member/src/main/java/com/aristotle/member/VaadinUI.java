package com.aristotle.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import com.aristotle.core.persistance.State;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path="/test.html")
@Theme("valo")
public class VaadinUI extends UI {


	//private final NameSuggestBox nameSuggestBox;
	private final SuggestingComboBox suggestingComboBox;
	@Autowired
	private SuggestingContainer suggestingContainer;
	@Autowired
	private StateService stateService;
	private final ComboBox stateCombobox;

	public VaadinUI() {
		//nameSuggestBox = new NameSuggestBox();
		suggestingComboBox = new SuggestingComboBox();
		stateCombobox = new ComboBox("State");
		stateCombobox.setInvalidAllowed(false);
		stateCombobox.setNullSelectionAllowed(false);
		//suggestingContainer = new SuggestingContainer();
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		List<State> states = stateService.getAllStates();
		stateCombobox.addItems(states);
		suggestingComboBox.setImmediate(false);
		suggestingComboBox.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Selected item: " + event.getProperty().getValue(), Type.HUMANIZED_MESSAGE);
                // tell the custom container that a value has been selected. This is necessary to ensure that the
                // selected value is displayed by the ComboBox
				suggestingContainer.setSelectedCountryBean((CountryBean) event.getProperty().getValue());
				suggestingComboBox.setValue(((CountryBean) event.getProperty().getValue()).getName());
				
			}
		});
		suggestingComboBox.setContainerDataSource(suggestingContainer);
	        
		HorizontalLayout actions = new HorizontalLayout(stateCombobox, suggestingComboBox);
		setContent(actions);
		// Configure layouts and components
		actions.setSpacing(true);

	}

}
