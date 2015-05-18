package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // public abstract Tweet getTweetByPlannedPostIdAndTwitterAccountId(Long plannedTweetId, Long twitterAccountId);
	
    // public abstract List<Tweet> getTweetByUserId(Long userId);

}