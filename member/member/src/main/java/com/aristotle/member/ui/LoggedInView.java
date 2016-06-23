package com.aristotle.member.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public abstract class LoggedInView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected VaadinSessionUtil vaadinSessionUtil;


	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	public abstract String getNaviagationName();

}
