package com.aristotle.member;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aristotle.core.persistance.Location;
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

@SpringUI(path = "/test.html")
@Theme("valo")
public class VaadinUI extends UI {

	@Value("${user_search_end_point}")
	private String searchEndPoint;
	private HttpUtil httpUtil = new HttpUtil();
	private JsonParser jsonParser = new JsonParser();

	// private final NameSuggestBox nameSuggestBox;
	private final SuggestingComboBox suggestingComboBox;
	@Autowired
	private SuggestingContainer suggestingContainer;
	@Autowired
	private LocationService locationService;
	private final ComboBox stateCombobox;
	private final ComboBox districtCombobox;
	private final ComboBox sortByComboBox;
	private int pageSize = 20;
	private int currentPage = 0;
	private int totalPages = 0;
	private int totalRecords = 0;
	Grid grid = new Grid();
	VerticalLayout layout;
	Button start;
	Button previous;
	Button end;
	Button next;
	String searchType = "GLOBAl";
	String selectedLocationName;
	Long selectedLocationId;
	Label pageLabel;
	Label totalMemberLabel;
	String sortBy;

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

		// nameSuggestBox = new NameSuggestBox();
		suggestingComboBox = new SuggestingComboBox();
		stateCombobox = new ComboBox();
		stateCombobox.setInvalidAllowed(false);
		stateCombobox.setNullSelectionAllowed(false);
		stateCombobox.setPageLength(0);
		
		districtCombobox = new ComboBox();
		districtCombobox.setInvalidAllowed(false);
		districtCombobox.setNullSelectionAllowed(false);
		districtCombobox.setPageLength(0);
		setupButtonListeners();
		
		sortByComboBox = new ComboBox("Sort By");
		sortByComboBox.setInvalidAllowed(false);
		sortByComboBox.setNullSelectionAllowed(false);
		sortByComboBox.setPageLength(0);
		
		totalMemberLabel = new Label();
		// suggestingContainer = new SuggestingContainer();
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout

		suggestingComboBox.setCaption("Search By Name");
		suggestingComboBox.setImmediate(false);
		suggestingComboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Selected item: " + ((CountryBean) event.getProperty().getValue()).getName(),
						Type.HUMANIZED_MESSAGE);
				// tell the custom container that a value has been selected.
				// This is necessary to ensure that the
				// selected value is displayed by the ComboBox
				suggestingContainer.setSelectedCountryBean((CountryBean) event.getProperty().getValue());
				suggestingComboBox.setValue(((CountryBean) event.getProperty().getValue()).getName());
				reloadTable();
				enableDisableButtons();

			}
		});
		buildStateComoboBox();
		buildDistrictComoboBox(0L);
		buildSortByComoboBox();
		suggestingComboBox.setContainerDataSource(suggestingContainer);
		// suggestingComboBox.setItemIconPropertyId("profilePic");
		//Label label = new Label("----OR-----");

		HorizontalLayout locationLayout = new HorizontalLayout(stateCombobox, districtCombobox, sortByComboBox);
		HorizontalLayout pagingLayout = new HorizontalLayout(start, previous, pageLabel, next, end);

		VerticalLayout layout = new VerticalLayout(locationLayout, totalMemberLabel, grid, pagingLayout);
		layout.setWidth("100%");

		setContent(layout);
		// Configure layouts and components

		layout.setSpacing(true);

		reloadTable();
		enableDisableButtons();

	}

	private void buildStateComoboBox() {
		List<Location> states = locationService.getAllStates();
		BeanItemContainer<Location> stateContainer = new BeanItemContainer<Location>(Location.class);
		stateContainer.addAll(states);
		stateCombobox.setCaption("Search By Location");
		stateCombobox.setContainerDataSource(stateContainer);
		stateCombobox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		stateCombobox.setItemCaptionPropertyId("name");
		stateCombobox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Notification.show("Selected item: " + ((Location) event.getProperty().getValue()).getName(),
						//Type.HUMANIZED_MESSAGE);
				// tell the custom container that a value has been selected.
				// This is necessary to ensure that the
				// selected value is displayed by the ComboBox
				Location selecteState = (Location) event.getProperty().getValue();
				selectedLocationName = selecteState.getName();
				selectedLocationId = selecteState.getId();
				stateCombobox.setValue(selectedLocationName);
				searchType = "State";
				currentPage = 0;
				reloadTable();
				enableDisableButtons();
				buildDistrictComoboBox(selecteState.getId());

			}
		});
	}
	private void buildDistrictComoboBox(Long selectedStateId) {
		List<Location> districts = new ArrayList<>();
		if(selectedStateId == null || selectedStateId <= 0){
			districts = new ArrayList<>();
			Location location = new Location();
			location.setName("--Select State--");
			location.setId(0L);
			districts.add(location);
			districtCombobox.setEnabled(false);
		}else{
			districts = locationService.getDistrictOfState(selectedStateId);
			districtCombobox.setEnabled(true);
		}
		
		BeanItemContainer<Location> districtContainer = new BeanItemContainer<Location>(Location.class);
		districtContainer.addAll(districts);
		districtCombobox.setCaption("District");
		districtCombobox.setContainerDataSource(districtContainer);
		districtCombobox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		districtCombobox.setItemCaptionPropertyId("name");
		districtCombobox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Notification.show("Selected item: " + ((Location) event.getProperty().getValue()).getName(),
						//Type.HUMANIZED_MESSAGE);
				// tell the custom container that a value has been selected.
				// This is necessary to ensure that the
				// selected value is displayed by the ComboBox
				Location selecteDistrict = (Location) event.getProperty().getValue();
				selectedLocationName = selecteDistrict.getName();
				selectedLocationId = selecteDistrict.getId();
				districtCombobox.setValue(selectedLocationName);
				searchType = "District";
				currentPage = 0;
				reloadTable();
				enableDisableButtons();

			}
		});
	}
	
	private void buildSortByComoboBox() {
		sortByComboBox.addItem("Name");
		sortByComboBox.addItem("MemberId");

		sortByComboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				sortBy = (String) event.getProperty().getValue();
				sortByComboBox.setValue(sortBy);
				currentPage = 0;
				reloadTable();
				enableDisableButtons();
			}
		});
	}

	private void reloadTable() {
		try {
			// layout.removeComponent(grid);
			final BeanItemContainer<Member> beans = new BeanItemContainer<Member>(Member.class);
			
			List<Member> members = getMembers();
			beans.addAll(members);
			grid.setContainerDataSource(beans);
			grid.setColumns("memberid", "name", "state", "district");
			DateRenderer dateRenderer = new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH);
			// Grid.Column dateColumn = grid.getColumn("menddate");
			// dateColumn.setRenderer(dateRenderer);
			Grid.Column memberIdColumn = grid.getColumn("memberid");
			memberIdColumn.setHeaderCaption("Membership ID");
			// Grid.Column bornColumn = grid.getColumn("profilepicResource");
			// bornColumn.setRenderer(new ImageRenderer());
			totalMemberLabel.setCaption("Total Members : "+totalRecords);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void setupButtonListeners() {
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

	private void enableDisableButtons() {
		if (currentPage == 0) {
			start.setEnabled(false);
			previous.setEnabled(false);
		} else {
			start.setEnabled(true);
			previous.setEnabled(true);
		}
		if (currentPage == totalPages - 1) {
			end.setEnabled(false);
			next.setEnabled(false);
		} else {
			end.setEnabled(true);
			next.setEnabled(true);
		}
		pageLabel.setCaption(currentPage + "/ " + totalPages);
	}
	
	public List<Member> getMembers() throws Exception{
		List<Member> members = new ArrayList<>(pageSize);
		String result = null;
		String query = "q=yes&q.parser=simple&q.options=%7BdefaultOperator:%22and%22,fields:[%22member%22]%7D&return=_all_fields&size=" + pageSize + "&start=" + (currentPage * pageSize);

		if ("STATE".equalsIgnoreCase(searchType)) {
			query = "q="+selectedLocationId+"&q.parser=simple&q.options=%7BdefaultOperator:%22and%22,fields:[%22votingstateid%22,%22livingstateid%22]%7D&return=_all_fields&size=" + pageSize + "&start=" + (currentPage * pageSize);
		}
		if ("DISTRICT".equalsIgnoreCase(searchType)) {
			query = "q="+selectedLocationId+"&q.parser=simple&q.options=%7BdefaultOperator:%22and%22,fields:[%22livingdistrictid%22,%22votingdistrictid%22]%7D&return=_all_fields&size=" + pageSize + "&start=" + (currentPage * pageSize);
		}
		if(sortBy == null){
			query = query +"&sort=_score%20desc";
		}else{
			query = query +"&sort="+sortBy.toLowerCase()+"%20asc";
		}
		String url = "http://" + searchEndPoint + "/2013-01-01/search?" + query;
		//url = "https://search-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com/2013-01-01/search?q=yes&q.parser=simple&q.options=%7BdefaultOperator:%22and%22,fields:[%22member%22]%7D&return=_all_fields,_score&sort=_score%20desc";
		result = httpUtil.getResponse(url);

		JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
		JsonArray hits = resultObject.get("hits").getAsJsonObject().get("hit").getAsJsonArray();
		totalRecords = resultObject.get("hits").getAsJsonObject().get("found").getAsInt();

		totalPages = totalRecords / pageSize;
		if (totalRecords % pageSize > 0) {
			totalPages++;
		}

		Gson gson = new Gson();
		for (int i = 0; i < hits.size(); i++) {
			Member oneMember = gson.fromJson(hits.get(i).getAsJsonObject().get("fields").toString(), Member.class);
			members.add(oneMember);
		}
		return members;
	}
}
