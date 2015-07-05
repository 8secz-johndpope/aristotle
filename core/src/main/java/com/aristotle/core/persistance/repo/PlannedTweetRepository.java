package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.PlannedTweet;


public interface PlannedTweetRepository extends JpaRepository<PlannedTweet, Long> {

    public PlannedTweet getPlannedTweetByTweetId(Long id);

    @Query("select pt from PlannedTweet pt where pt.status = 'PENDING' and pt.postingTime <= now() order by pt.postingTime")
    public List<PlannedTweet> getPlannedTweetByLocationTypeAndLocationId();

    // public abstract List<PlannedTweet> getPlannedTweetByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
    // public abstract List<PlannedTweet> getExecutedTweetByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);

}