package com.aristotle.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.aristotle.core.persistance.Domain;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path="/test.html")
@Theme("valo")
public class MemberListUI extends UI {

	private final DomainEditor editor;

	private final Grid grid;

	private final TextField filter;

	private final Button addNewBtn;

        @Autowired
	public MemberListUI(DomainEditor domainEditor) {
		this.editor = domainEditor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New Domain", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		//grid.setColumns("id", "name", "nameAliases");

		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listDomains(e.getText()));

		// Connect selected Domain to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editDomain((Domain) grid.getSelectedRow());
			}
		});

		addNewBtn.setStyleName(ValoTheme.BUTTON_LINK);

		// Instantiate and edit new Domain the new button is clicked
		addNewBtn.addClickListener(e -> {
			Domain d = new Domain();
			d.setName("www.mydomain.com");
			d.setNameAliases("Test aLIASES");
			editor.editDomain(d);});

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listDomains(filter.getValue());
		});

		// Initialize listing
		listDomains(null);
	}

	// tag::listDomains[]
	private void listDomains(String text) {
		try{
			if (StringUtils.isEmpty(text)) {
				
			}
			else {
				//grid.setContainerDataSource(new BeanItemContainer(Domain.class, repo.findByLastNameStartsWithIgnoreCase(text)));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	// end::listDomains[]
}
