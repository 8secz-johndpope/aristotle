package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedEmail;


public interface PlannedEmailDao {

	public abstract PlannedEmail savePlannedEmail(PlannedEmail plannedEmail);

	public abstract PlannedEmail getPlannedEmailById(Long id);

	public abstract PlannedEmail getPlannedEmailByAppId(String appId);
	
	public abstract List<PlannedEmail> getPlannedEmailByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
	public abstract PlannedEmail getNextPlannedEmailToPublish();

}