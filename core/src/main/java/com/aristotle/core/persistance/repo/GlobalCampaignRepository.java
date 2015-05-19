package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.GlobalCampaign;

public interface GlobalCampaignRepository extends JpaRepository<GlobalCampaign, Long> {

    public abstract GlobalCampaign getGlobalCampaignByCampaignIdUp(String globalCampaign);
	
}