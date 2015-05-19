package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookGroupMembership;

public interface FacebookGroupMembershipRepository extends JpaRepository<FacebookGroupMembership, Long> {

	public abstract List<FacebookGroupMembership> getFacebookGroupMembershipByFacebookAccountId(Long facebookAccountId);

	/*
	public abstract FacebookGroupMembership getFacebookGroupMembershipByFacebookGroupIdForPosting(Long facebookGroupId);

	public abstract FacebookGroupMembership getFacebookGroupMembershipByFacebookGroupIdForReading(Long facebookGroupId);
	
	public abstract FacebookGroupMembership getFacebookGroupMembershipByFacebookUserIdAndGroupId(Long facebookAccountId, Long facebookGroupId);

	public abstract List<FacebookGroupMembership> getFacebookGroupMembershipsAfterId(Long lastId, int pageSize);

	public abstract List<FacebookGroupMembership> getFacebookGroupMembershipsAfterIdForFacebookGroup(Long facebookGroupId, Long lastId, int pageSize);

	public List<FacebookGroupMembership> getAllFacebookGroupMembershipByFacebookGroupIdForPosting(Long facebookGroupId);
	*/
	

}