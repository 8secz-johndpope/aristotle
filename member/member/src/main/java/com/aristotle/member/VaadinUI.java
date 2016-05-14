package com.aristotle.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aristotle.core.persistance.State;
import com.aristotle.core.service.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path="/test.html")
@Theme("valo")
public class VaadinUI extends UI {

	@Value("${user_search_end_point}")
	private String searchEndPoint;
	private HttpUtil httpUtil = new HttpUtil();
	private JsonParser jsonParser = new JsonParser();
	
	//private final NameSuggestBox nameSuggestBox;
	private final SuggestingComboBox suggestingComboBox;
	@Autowired
	private SuggestingContainer suggestingContainer;
	@Autowired
	private StateService stateService;
	private final ComboBox stateCombobox;
	private int pageSize=20;
	private int currentPage = 0;
	private int totalPages=0;
	Grid grid = new Grid();
	VerticalLayout layout;
	Button start;
	Button previous;
	Button end;
	Button next;
	String searchType;
	String selectedStateName;
	Label pageLabel;

	public VaadinUI() {
		start = new Button("<<");
		previous = new Button("<");
		end = new Button(">>");
		next = new Button(">");
		pageLabel = new Label();
		
		start.setStyleName(ValoTheme.BUTTON_LINK);
		previous.setStyleName(ValoTheme.BUTTON_LINK);
		end.setStyleName(ValoTheme.BUTTON_LINK);
		next.setStyleName(ValoTheme.BUTTON_LINK);
		
		start.setEnabled(false);
		previous.setEnabled(false);
		end.setEnabled(false);
		next.setEnabled(false);
		
		grid.setWidth("800px");
		grid.setHeight("800px");

		//nameSuggestBox = new NameSuggestBox();
		suggestingComboBox = new SuggestingComboBox();
		stateCombobox = new ComboBox();
		stateCombobox.setInvalidAllowed(false);
		stateCombobox.setNullSelectionAllowed(false);
		stateCombobox.setPageLength(0);
		setupButtonListeners();
		
		//suggestingContainer = new SuggestingContainer();
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		List<State> states = stateService.getAllStates();
		BeanItemContainer<State> stateContainer = new BeanItemContainer<State>(State.class);
		stateContainer.addAll(states);
		stateCombobox.setCaption("Search By state");
		stateCombobox.setContainerDataSource(stateContainer);
		stateCombobox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		stateCombobox.setItemCaptionPropertyId("name");
		
		suggestingComboBox.setCaption("Search By Name");
		suggestingComboBox.setImmediate(false);
		suggestingComboBox.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Selected item: " + ((CountryBean) event.getProperty().getValue()).getName(), Type.HUMANIZED_MESSAGE);
                // tell the custom container that a value has been selected. This is necessary to ensure that the
                // selected value is displayed by the ComboBox
				suggestingContainer.setSelectedCountryBean((CountryBean) event.getProperty().getValue());
				suggestingComboBox.setValue(((CountryBean) event.getProperty().getValue()).getName());
				reloadTable();
				enableDisableButtons();
				
			}
		});
		stateCombobox.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Selected item: " + ((State) event.getProperty().getValue()).getName(), Type.HUMANIZED_MESSAGE);
                // tell the custom container that a value has been selected. This is necessary to ensure that the
                // selected value is displayed by the ComboBox
				selectedStateName = ((State) event.getProperty().getValue()).getName();
				stateCombobox.setValue(selectedStateName);
				searchType = "State";
				reloadTable();
				enableDisableButtons();
				
			}
		});
		suggestingComboBox.setContainerDataSource(suggestingContainer);
		//suggestingComboBox.setItemIconPropertyId("profilePic");
		Label label = new Label("----OR-----");

		//VerticalLayout layout = new VerticalLayout(stateCombobox, label, suggestingComboBox, grid);
		VerticalLayout layout = new VerticalLayout(grid);
		HorizontalLayout pageingLayout = new HorizontalLayout(start, previous, pageLabel, next, end);
		layout.addComponent(pageingLayout);
		layout.setWidth("100%");
		
		setContent(layout);
		// Configure layouts and components
		
		layout.setSpacing(true);
		
		reloadTable();
		enableDisableButtons();

	}
	private void reloadTable(){
		try{
			//layout.removeComponent(grid);
			final BeanItemContainer<Member> beans = new BeanItemContainer<Member>(Member.class);
			//&q.options={fields: ['member']}
			String result = null;
			/*
			if(searchType.equalsIgnoreCase("STATE")){
				String searchQuery=selectedStateName.replaceAll(" ", "+");
				result = httpUtil.getResponse("http://"+searchEndPoint+"/2013-01-01/search?q="+selectedStateName+"&return=_all_fields&sort=_score%20desc&size="+pageSize+"&start="+(currentPage*pageSize));
			}else{
				result = httpUtil.getResponse("http://"+searchEndPoint+"/2013-01-01/search?q=yes&return=_all_fields&sort=_score%20desc&size="+pageSize+"&start="+(currentPage*pageSize));
					
			}
			*/
			result = httpUtil.getResponse("http://"+searchEndPoint+"/2013-01-01/search?q=yes&return=_all_fields&sort=_score%20desc&size="+pageSize+"&start="+(currentPage*pageSize));

			//String result = httpUtil.getResponse("http://search-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com/2013-01-01/search?q=yes&q.options={fields: ['member']}&return=_all_fields&sort=_score desc");
			JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
			JsonArray hits = resultObject.get("hits").getAsJsonObject().get("hit").getAsJsonArray();
			int total = resultObject.get("hits").getAsJsonObject().get("found").getAsInt();
			
			totalPages =  total / pageSize;
			if(total % pageSize > 0){
				totalPages++;
			}
			
			List<Member> members = new ArrayList<>(hits.size());
			Gson gson = new Gson();
			for(int i=0;i<hits.size();i++){
				Member oneMember = gson.fromJson(hits.get(i).getAsJsonObject().get("fields").toString(), Member.class);
				members.add(oneMember);
			}
			beans.addAll(members);
			grid.setContainerDataSource(beans);
			grid.setColumns("memberid","name", "state");
			DateRenderer dateRenderer = new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH);
			//Grid.Column dateColumn = grid.getColumn("menddate");
			//dateColumn.setRenderer(dateRenderer);
			Grid.Column memberIdColumn = grid.getColumn("memberid");
			memberIdColumn.setHeaderCaption("Membership ID");
			//Grid.Column bornColumn = grid.getColumn("profilepicResource");
			//bornColumn.setRenderer(new ImageRenderer());

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	private void setupButtonListeners(){
		start.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				currentPage = 0;
				enableDisableButtons();
				reloadTable();
				
			}
		});
		previous.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				currentPage--;
				enableDisableButtons();
				reloadTable();
				
			}
		});
		end.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				currentPage = totalPages - 1;
				enableDisableButtons();
				reloadTable();
				
			}
		});
		next.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				currentPage++;
				enableDisableButtons();
				reloadTable();
				
			}
		});
	}
	private void enableDisableButtons(){
		if(currentPage == 0){
			start.setEnabled(false);
			previous.setEnabled(false);
		}else{
			start.setEnabled(true);
			previous.setEnabled(true);
		}
		if(currentPage == totalPages - 1){
			end.setEnabled(false);
			next.setEnabled(false);
		}else{
			end.setEnabled(true);
			next.setEnabled(true);
		}
		pageLabel.setCaption(currentPage+ "/ "+ totalPages);
	}

}
