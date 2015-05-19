package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.LocationCampaign;

public interface LocationCampaignRepository extends JpaRepository<LocationCampaign, Long> {

    /*
	public abstract LocationCampaign getLocationCampaignByLocationCampaign(String locationCampaign);
	
	public abstract LocationCampaign getDefaultLocationCampaignByStateId(Long stateId);
	
	public abstract LocationCampaign getDefaultLocationCampaignByDistrictId(Long districtId);
	
	public abstract LocationCampaign getDefaultLocationCampaignByAcId(Long acId);
	
	public abstract LocationCampaign getDefaultLocationCampaignByPcId(Long pcId);
	
	public abstract List<LocationCampaign> getAllLocationCampaigns();
	    */
}