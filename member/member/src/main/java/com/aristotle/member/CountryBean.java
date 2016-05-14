package com.aristotle.member;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

public class CountryBean {
	private Long id;
	private String name;
	
	public CountryBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CountryBean(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
