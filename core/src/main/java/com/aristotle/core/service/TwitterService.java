package com.aristotle.core.service;

import java.util.List;

import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.Twitter;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.persistance.User;

/**
 * Created by Ravi Sharma on 02/07/2015.
 */
public interface TwitterService {

    List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException;
    
    PlannedTweet planRetweet(org.springframework.social.twitter.api.Tweet tweet, TwitterAccount twitterAccount) throws AppException;

    PlannedTweet getPlannedTweetById(Long id) throws AppException;

    List<PlannedTweet> getAllPlannedTweetReadyToProcess() throws AppException;

    void updatePlannedTweetStatusToProcessing(Long plannedTweetId) throws AppException;

    List<org.springframework.social.twitter.api.Tweet> processTweetsFromOneAccount(TwitterAccount twitterAccount) throws AppException;

    void processPlannedTweet(Long plannedTweetId) throws AppException;

    Tweet getNextPendingTweet() throws AppException;

    void updateTweetStatusToProcessing(Long tweetId) throws AppException;

    void updateTweetStatusToFailed(Long tweetId) throws AppException;

    void updateTweetStatusToRetrying(Long tweetId) throws AppException;

    void updateTweetStatusToDone(Long tweetId) throws AppException;

    void tweetIt(Long tweetId) throws AppException;

    TwitterTeam getTwitterTeamByUrl(String url) throws AppException;
    
    TwitterAccount saveTwitterAccount(Connection<Twitter> twitterConnection, Long twitterAppId, Long twitterTeamId, User user) throws AppException;

    boolean isUserPartOfTwitterTeam(long userId, Long twitterTeamId) throws AppException;

    List<TwitterTeam> getAllTwitterTeams() throws AppException;

    void updateFollowerCounts() throws AppException;

    void updatePlannedTweetRetweetCounts() throws AppException;
}
