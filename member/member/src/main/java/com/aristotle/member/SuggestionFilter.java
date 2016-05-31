package com.aristotle.member;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class SuggestionFilter implements Container.Filter {
	 
    private String filterString;
 
    public SuggestionFilter(String filterString) {
      this.filterString = filterString;
    }
 
    public String getFilterString() {
      return filterString;
    }
 
    @Override
    public boolean passesFilter(Object itemId, Item item) 
        throws UnsupportedOperationException {
      return false;
    }
 
    @Override
    public boolean appliesToProperty(Object propertyId) {
      return false;
    }
  }