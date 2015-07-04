package com.aristotle.core.service;

import java.util.List;

import org.springframework.social.twitter.api.Tweet;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.TwitterAccount;

/**
 * Created by Ravi Sharma on 02/07/2015.
 */
public interface TwitterService {

    List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException;
    
    PlannedTweet planRetweet(Tweet tweet, TwitterAccount twitterAccount) throws AppException;

    List<PlannedTweet> getAllPlannedTweetReadyToProcess() throws AppException;

}
