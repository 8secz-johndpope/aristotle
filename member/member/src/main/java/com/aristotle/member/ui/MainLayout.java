package com.aristotle.member.ui;

import java.util.Iterator;

import com.aristotle.core.persistance.User;
import com.aristotle.member.ui.account.ContactView;
import com.aristotle.member.ui.account.PersonalDetailView;
import com.aristotle.member.ui.account.SecurityView;
import com.aristotle.member.ui.login.LoginView;
import com.aristotle.member.ui.login.RegisterView;
import com.aristotle.member.ui.util.NavigatorUtil;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.GenericFontIcon;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

public class MainLayout extends MainLayoutDesign implements ViewDisplay {

	private static final long serialVersionUID = 1L;
	private static final String STYLE_SELECTED = "selected";
    private Navigator navigator;
    private VaadinSessionUtil vaadinSessionUtil;
    
    public MainLayout(SpringViewProvider viewProvider, VaadinSessionUtil vaadinSessionUtil) {
    	this.vaadinSessionUtil = vaadinSessionUtil;
        this.navigator = new Navigator(UI.getCurrent(), (ViewDisplay) this);
        navigator.addProvider(viewProvider);
        NativeButton nativeButton = new NativeButton();
        nativeButton.setIcon(new GenericFontIcon(FontAwesome.GOOGLE_PLUS.getFontFamily(), FontAwesome.GOOGLE_PLUS.getCodepoint()));
        nativeButton.setStyleName("menu-button");
        nativeButton.setCaption("Manual");
        nativeButton.setWidth("100%");
        side_bar.addComponent(nativeButton);
        addNavigatorView(PersonalDetailView.NAVIAGATION_NAME, personDetailNativeButton);
        addNavigatorView(ContactView.NAVIAGATION_NAME, contactDetailNativeButton);
        addNavigatorView(HomeView.NAVIAGATION_NAME, volunteerDetailNativeButton);
        addNavigatorView(SecurityView.NAVIAGATION_NAME, securirtyNativeButton);
        volunteerDetailNativeButton.setVisible(false);
        

        logoutButton.setCaption("Logout");
        logoutButton.setDescription("Logout");
        logoutButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				vaadinSessionUtil.logout();
				navigator.navigateTo(LoginView.NAVIAGATION_NAME);
			}
		});        
        //addNavigatorView(domainPage.getNaviagationName(), menuButton1);
        
        //addNavigatorView(OrderView.VIEW_NAME, OrderView.class, menuButton2);
        //addNavigatorView(AboutView.VIEW_NAME, AboutView.class, menuButton3);
        //addNavigatorView("Test", OrderView.class, menuButton4);
        
        User loggedinUser = vaadinSessionUtil.getLoggedInUserFromSession();
       
        if(loggedinUser == null){
        	navigator.navigateTo(LoginView.NAVIAGATION_NAME);
        }else
        if (navigator.getState().isEmpty()) {
            navigator.navigateTo(HomeView.NAVIAGATION_NAME);
        } else {
            navigator.navigateTo(navigator.getState());
        }
    }

    private void doNavigate(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void addNavigatorView(String viewName, Button menuButton) {
        menuButton.addClickListener(event -> {
        	doNavigate(viewName);
        	menuButton.addStyleName("selected");
        });
        //menuButton.setData(viewClass.getName());
    }

    private void adjustStyleByData(Component component, Object data) {
        if (component instanceof Button) {
            if (data != null && data.equals(((Button) component).getData())) {
                component.addStyleName(STYLE_SELECTED);
            } else {
                component.removeStyleName(STYLE_SELECTED);
            }
        }
    }

    @Override
    public void showView(View view) {
        if (view instanceof Component) {
        	if(view instanceof LoginView || view instanceof RegisterView){
        		root_layout_main.removeAllComponents();
        		root_layout_main.addComponent((Component)view);
        		root_layout_main.setComponentAlignment((Component)view, Alignment.MIDDLE_CENTER);

        	}else{
        		root_layout_main.removeAllComponents();
        		root_layout_main.addComponent(layoutWithMenu);
        		scroll_panel.setContent((Component) view);
        		root_layout_main.setComponentAlignment(layoutWithMenu, Alignment.MIDDLE_CENTER);

                Iterator<Component> it = side_bar.iterator();
                while (it.hasNext()) {
                    adjustStyleByData(it.next(), view.getClass().getName());
                }	
                
                //Load USer Detail
                User user = vaadinSessionUtil.getLoggedInUserFromSession();
                if(user != null){
                	userNameLabel.setValue(user.getName());	
                }
        	}
            
        } else {
            throw new IllegalArgumentException("View is not a Component");
        }
    }
}
