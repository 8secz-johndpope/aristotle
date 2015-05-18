package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.persistance.Tweet;

public interface TweetDao {

	public abstract Tweet saveTweet(Tweet tweet);

	public abstract Tweet getTweetById(Long id);

	public abstract Tweet getTweetByPlannedPostIdAndTwitterAccountId(Long plannedTweetId, Long twitterAccountId);
	
	public abstract List<Tweet> getTweetByUserId(Long userId);

}