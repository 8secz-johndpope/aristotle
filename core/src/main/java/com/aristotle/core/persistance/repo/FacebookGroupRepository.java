package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookGroup;

public interface FacebookGroupRepository extends JpaRepository<FacebookGroup, Long> {

    public FacebookGroup getFacebookGroupByFacebookGroupExternalId(String facebookGroupExternalId);

    // public abstract List<FacebookGroup> getFacebookGroupsForPostingAfterId(Long lastId, int pageSize);

    // public abstract List<FacebookGroup> getFacebookGroups(Long lastId, int pageSize);

}