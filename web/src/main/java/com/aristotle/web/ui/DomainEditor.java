package com.aristotle.web.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.Domain;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A simple example to introduce building forms. As your real application is
 * probably much more complicated than this example, you could re-use this form in
 * multiple places. This example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Virin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@SpringView
public class DomainEditor extends FormLayout {

	/**
	 * The currently edited customer
	 */
	private Domain domain;

	/* Fields to edit properties in Domain entity */
	TextField name = new TextField("Name");
	TextField nameAliases = new TextField("Name Aliases");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	@Autowired
	public DomainEditor() {

		addComponents(name, nameAliases, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			try{
				System.out.println("Name: "+domain.getName());
				System.out.println("getNameAliases: "+domain.getNameAliases());
				//dynamoService.saveDomain(domain);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			});
		//delete.addClickListener(e -> repository.delete(domain));
		cancel.addClickListener(e -> editDomain(domain));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editDomain(Domain c) {

		final boolean persisted = c.getId() != null;
		domain = c;
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(domain, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
