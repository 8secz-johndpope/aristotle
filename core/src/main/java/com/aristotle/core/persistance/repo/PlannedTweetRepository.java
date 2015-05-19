package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.PlannedTweet;


public interface PlannedTweetRepository extends JpaRepository<PlannedTweet, Long> {

    public abstract PlannedTweet getPlannedTweetByTweetId(Long id);

    // public abstract List<PlannedTweet> getPlannedTweetByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
    // public abstract List<PlannedTweet> getExecutedTweetByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);

}