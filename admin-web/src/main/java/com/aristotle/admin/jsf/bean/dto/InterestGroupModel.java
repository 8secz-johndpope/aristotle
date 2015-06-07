package com.aristotle.admin.jsf.bean.dto;

import com.aristotle.core.persistance.InterestGroup;

public class InterestGroupModel extends InterestGroup {

	private static final long serialVersionUID = 1L;
	private InterestModel interestDtoModels;

    public InterestGroupModel(InterestGroup interestGroupDto) {
		this.setDescription(interestGroupDto.getDescription());
		this.setId(interestGroupDto.getId());
        interestDtoModels = new InterestModel(interestGroupDto.getInterests());
	}
	public InterestModel getInterestDtoModels() {
		return interestDtoModels;
	}
	public void setInterestDtoModels(InterestModel interestDtoModels) {
		this.interestDtoModels = interestDtoModels;
	}
    
}
