package com.aristotle.member.ui.util;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class ViewHelper {

	
	public static void addNaviagationClickListener(AbstractClientConnector clientConnector, Button button, String viewName){
		button.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				clientConnector.getUI().getNavigator().navigateTo(viewName);
				
			}
		});

	}
}
