package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedSms;


public interface PlannedSmsDao {

	public abstract PlannedSms savePlannedSms(PlannedSms plannedSms);

	public abstract PlannedSms getPlannedSmsById(Long id);

	public abstract PlannedSms getPlannedSmsByAppId(String appId);
	
	public abstract List<PlannedSms> getPlannedSmsByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
	public abstract PlannedSms getNextPlannedSmsToPublish();

}