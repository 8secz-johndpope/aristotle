package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookAccount;

public interface FacebookAccountRepository extends JpaRepository<FacebookAccount, Long> {

	public abstract FacebookAccount getFacebookAccountById(Long id);

	public abstract FacebookAccount getFacebookAccountByUserId(Long userId);
	
	public abstract FacebookAccount getFacebookAccountByFacebookUserId(String userId);

	public abstract FacebookAccount getFacebookAccountByUserName(String userName);

	/*
	public abstract List<FacebookAccount> getFacebookAccountsAfterId(Long lastId, int pageSize);
	
	public abstract List<FacebookAccount> getAllFacebookAccountsForVoiceOfAapToPublishOnTimeLine();
	
	public abstract List<FacebookAccount> getStateFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long stateId);
	
	public abstract List<FacebookAccount> getDistrictFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long district);
	
	public abstract List<FacebookAccount> getAcFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long acId);
	
	public abstract List<FacebookAccount> getPcFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long pcId);
	
	public abstract List<FacebookAccount> getCountryFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long countryId);
	
	public abstract List<FacebookAccount> getCountryRegionFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long countryRegionId);
	
	public abstract List<FacebookAccount> getCountryRegionAreaFacebookAccountsForVoiceOfAapToPublishOnTimeLine(Long countryRegionAreaId);
    */
}