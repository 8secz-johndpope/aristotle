package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.PlannedFacebookPost;


public interface PlannedFacebookPostRepository extends JpaRepository<PlannedFacebookPost, Long> {

    // public abstract List<PlannedFacebookPost> getPlannedFacebookPostByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
    // public abstract List<PlannedFacebookPost> getExecutedFacebookPostByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
}