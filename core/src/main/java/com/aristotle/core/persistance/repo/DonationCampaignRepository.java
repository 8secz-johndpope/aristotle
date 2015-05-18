package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.enums.CampaignType;
import com.aristotle.core.persistance.DonationCampaign;

public interface DonationCampaignRepository extends JpaRepository<DonationCampaign, Long> {

	public abstract List<DonationCampaign> getDonationCampaignsByUserId(Long userId);
	
    public abstract DonationCampaign getDonationCampaignByCampaignTypeAndUserId(CampaignType campaigntype, Long userId);
	
}