package com.aristotle.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.service.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;

@Component
public class SuggestingContainer extends BeanItemContainer<CountryBean> {

	@Value("${user_search_end_point}")
	private String searchEndPoint;
	private CountryBean defaultCountry;
	private HttpUtil httpUtil = new HttpUtil();
	private JsonParser jsonParser = new JsonParser();

	public SuggestingContainer() throws IllegalArgumentException {
		super(CountryBean.class);
	}

	public SuggestingContainer(CountryBean defaultCountry)
			throws IllegalArgumentException {
		this();
		addBean(defaultCountry);
		this.defaultCountry = defaultCountry;
	}

	/**
	 * This method will be called by ComboBox each time the user has entered a
	 * new value into the text field of the ComboBox. For our custom ComboBox
	 * class {@link SuggestingComboBox} it is assured by
	 * {@link SuggestingComboBox#buildFilter(String, com.vaadin.shared.ui.combobox.FilteringMode)}
	 * that only instances of {@link SuggestionFilter} are passed into this
	 * method. We can therefore safely cast the filter to this class. Then we
	 * simply get the filterString from this filter and call the database
	 * service with this filterString. The database then returns a list of
	 * country objects whose country names begin with the filterString. After
	 * having removed all existing items from the container we add the new list
	 * of freshly filtered country objects.
	 */
	@Override
	protected void addFilter(Filter filter) throws UnsupportedFilterException {
		SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
		filterItems(suggestionFilter.getFilterString());
	}

	private void filterItems(String filterString) {
		removeAllItems();
		if(StringUtils.isEmpty(filterString)){
			return;
		}
		filterString = filterString.replaceAll(" ", "+");
		try {
			String data =httpUtil.getResponse("http://"+searchEndPoint+"/2013-01-01/suggest?q="+filterString+"&suggester=full_name_suggester");
			System.out.println("data="+data);
			JsonObject resultJson = (JsonObject)jsonParser.parse(data);
			JsonArray suggestions = resultJson.get("suggest").getAsJsonObject().get("suggestions").getAsJsonArray();
			List<CountryBean> countries = new ArrayList<>(suggestions.size());
			CountryBean oneCountryBean;
			for (int i = 0; i < suggestions.size(); i++) {
				oneCountryBean = new CountryBean(suggestions.get(i).getAsJsonObject().get("id").getAsLong(), suggestions.get(i).getAsJsonObject().get("suggestion").getAsString());
				if(suggestions.get(i).getAsJsonObject().get("profilePic") != null && !suggestions.get(i).getAsJsonObject().get("profilePic").isJsonNull()){
					oneCountryBean.setProfilePic("http://static.swarajabhiyan.org/"+suggestions.get(i).getAsJsonObject().get("profilePic").getAsString());
				}
				countries.add(oneCountryBean);
			}
			addAll(countries);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * This method makes sure that the selected value is the only value shown in
	 * the dropdown list of the ComboBox when this is explicitly opened with the
	 * arrow icon. If such a method is omitted, the dropdown list will contain
	 * the most recently suggested items.
	 */
	public void setSelectedCountryBean(CountryBean country) {
		removeAllItems();
		addBean(country);
	}

}
