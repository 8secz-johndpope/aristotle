package com.aristotle.core.persistance.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.PlannedTweet;


public interface PlannedTweetRepository extends JpaRepository<PlannedTweet, Long> {

    public PlannedTweet getPlannedTweetByTweetId(Long id);

    @Query("select pt from PlannedTweet pt where pt.status = 'PENDING' and pt.postingTime <= ? order by pt.postingTime")
    public List<PlannedTweet> getPlannedTweetByLocationTypeAndLocationId(Date currentTime);

    @Query("select count(pt) from PlannedTweet pt where pt.postingTime >= ? and pt.postingTime <= ?")
    public int getPlannedTweetCount(Date startDateTime, Date endDateTime);

    @Query("select pt from PlannedTweet pt where pt.postingTime >= ? and pt.postingTime <= ? order by pt.postingTime")
    public List<PlannedTweet> getPlannedTweets(Date startDateTime, Date endDateTime);


}