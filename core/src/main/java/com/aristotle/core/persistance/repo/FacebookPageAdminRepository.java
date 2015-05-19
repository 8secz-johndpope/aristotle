package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookPageAdmins;

public interface FacebookPageAdminRepository extends JpaRepository<FacebookPageAdmins, Long> {

    /*
	public abstract FacebookPageAdmins getFacebookPageAdminsByFacebookPageIdForPosting(Long facebookGroupId) ;

	public abstract FacebookPageAdmins getFacebookPageAdminsByFacebookGroupIdForReading(Long facebookGroupId) ;

	public abstract FacebookPageAdmins getFacebookPageAdminsByFacebookUserIdAndPageId(Long facebookAccountId, Long facebookPageId) ;

	public abstract List<FacebookPageAdmins> getFacebookPageAdminssAfterId(Long lastId, int pageSize) ;
    */
}