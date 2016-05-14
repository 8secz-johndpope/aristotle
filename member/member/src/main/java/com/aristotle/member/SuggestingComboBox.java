package com.aristotle.member;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
 
public class SuggestingComboBox extends ComboBox {
 
  public SuggestingComboBox() {
    setItemCaptionMode(ItemCaptionMode.PROPERTY);
    setItemCaptionPropertyId("name");
    setFilteringMode(FilteringMode.STARTSWITH);
    setItemCaptionMode(ItemCaptionMode.PROPERTY);
    setItemIconPropertyId("profilePic");
  }
 
  @Override
  protected Filter buildFilter(String filterString, 
                               FilteringMode filteringMode) {
    return new SuggestionFilter(filterString);
  }
}