package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookPage;

public interface FacebookPageRepository extends JpaRepository<FacebookPage, Long> {

	public abstract FacebookPage getFacebookPageByFacebookPageExternalId(String facebookPageExternalId);
	/*
	public abstract List<FacebookPage> getFacebookPagesForPostingAfterId(Long lastId, int pageSize);

	public abstract List<FacebookPage> getFacebookPagesForRePostingAfterId(Long lastId, int pageSize);
    */
}