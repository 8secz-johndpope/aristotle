package com.aristotle.core.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.TwitterApp;
import com.aristotle.core.persistance.TwitterPermission;
import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.PlannedTweetRepository;
import com.aristotle.core.persistance.repo.TweetRepository;
import com.aristotle.core.persistance.repo.TwitterAccountRepository;
import com.aristotle.core.persistance.repo.TwitterAppRepository;
import com.aristotle.core.persistance.repo.TwitterPermissionRepository;
import com.aristotle.core.persistance.repo.TwitterTeamRepository;
import com.aristotle.core.persistance.repo.UserRepository;

/**
 * Created by Ravi Sharma on 02/07/2015.
 */
@Service
@Transactional
public class TwitterServiceImpl implements TwitterService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${twitter_consumer_key}")
    private String consumerKey;
    @Value("${twitter_consumer_secret}")
    private String consumerSecret;

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;
    @Autowired
    private PlannedTweetRepository plannedTweetRepository;
    @Autowired
    private TwitterTeamRepository twitterTeamRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private TwitterPermissionRepository twitterPermissionRepository;
    @Autowired
    private TwitterAppRepository twitterAppRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException {
        return twitterAccountRepository.getAllSourceTwitterAccounts();
    }

    @Override
    public PlannedTweet planRetweet(org.springframework.social.twitter.api.Tweet tweet, TwitterAccount twitterAccount) throws AppException {
        PlannedTweet plannedTweet = plannedTweetRepository.getPlannedTweetByTweetId(tweet.getId());
        logger.info("plannedTweetDto : " + plannedTweet);
        if (plannedTweet != null) {
            logger.info("Ignoring as already have planned Tweet");
            return null;
        }

        logger.info("Creating a planned Tweet");
        plannedTweet = new PlannedTweet();
        plannedTweet.setLocationType(PostLocationType.Global);
        plannedTweet.setStatus(PlannedPostStatus.PENDING);
        plannedTweet.setTweetType("Retweet");
        plannedTweet.setTotalRequired(2000);
        logger.info("Setting total Required Tweets to 2000");
        plannedTweet.setTweetId(tweet.getId());
        plannedTweet.setPostingTime(new Date());
        plannedTweet.setFromTwitterUserId(tweet.getFromUserId());
        plannedTweet.setTwitterAccount(twitterAccount);
        plannedTweet = plannedTweetRepository.save(plannedTweet);
        logger.info("Creating a planned Tweet Success");
        return plannedTweet;
    }

    @Override
    public List<PlannedTweet> getAllPlannedTweetReadyToProcess() throws AppException {
        return plannedTweetRepository.getPlannedTweetByLocationTypeAndLocationId(new Date());
    }

    @Override
    public List<org.springframework.social.twitter.api.Tweet> processTweetsFromOneAccount(TwitterAccount twitterAccount) throws AppException {
        twitterAccount = twitterAccountRepository.findOne(twitterAccount.getId());
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret);
        List<org.springframework.social.twitter.api.Tweet> tweets = twitter.timelineOperations().getUserTimeline(Long.parseLong(twitterAccount.getTwitterId()), 5);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -5);
        for (org.springframework.social.twitter.api.Tweet oneTweet : tweets) {
            logger.info("Tweet : " + oneTweet.getCreatedAt() + " : " + oneTweet.getText());
            if (oneTweet.getCreatedAt().after(now.getTime())) {
                try {
                    planRetweet(oneTweet, twitterAccount);
                } catch (AppException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("Ignoring as not in last 5 minutes");
            }
        }
        return tweets;
    }

    @Override
    public PlannedTweet getPlannedTweetById(Long id) throws AppException {
        return plannedTweetRepository.findOne(id);
    }

    @Override
    public void processPlannedTweet(Long plannedTweetId) throws AppException {
        PlannedTweet plannedTweet = plannedTweetRepository.findOne(plannedTweetId);
        /*
        if (!plannedTweet.getStatus().equals(PlannedPostStatus.PROCESSING)) {
            logger.info("Status is not processing so Ignoring it");
            return;
        }
        */
        TwitterAccount tweetOwnerAccount = plannedTweet.getTwitterAccount();
        List<TwitterTeam> twitterTeams = twitterTeamRepository.getTwitterTeamsOfSourceTwitterAccount(tweetOwnerAccount.getId());
        TwitterTeam globalTeam = getGlobalTeam(twitterTeams);
        if (globalTeam == null) {
            // process all teams
            for (TwitterTeam oneTwitterTeam : twitterTeams) {
                processOneTwitterTeam(plannedTweet, oneTwitterTeam);
            }
        } else {
            // just process global team
            processOneTwitterTeam(plannedTweet, globalTeam);
        }
        plannedTweet.setStatus(PlannedPostStatus.DONE);
        plannedTweet = plannedTweetRepository.save(plannedTweet);
    }

    private void processOneTwitterTeam(PlannedTweet plannedTweet, TwitterTeam oneTwitterTeam) {
        Set<TwitterAccount> allTwitterAccountsOfTeam = oneTwitterTeam.getTweetTweeters();
        if (allTwitterAccountsOfTeam == null || allTwitterAccountsOfTeam.isEmpty()) {
            return;
        }
        Tweet tweet;
        for (TwitterAccount oneTwitterAccount : allTwitterAccountsOfTeam) {
            tweet = tweetRepository.getTweetByPlannedTweetIdAndTwitterAccountId(plannedTweet.getId(), oneTwitterAccount.getId());
            if (tweet != null) {
                continue;
            }
            tweet = new Tweet();
            tweet.setPlannedTweet(plannedTweet);
            tweet.setStatus("Pending");
            tweet.setTwitterAccount(oneTwitterAccount);
            tweet = tweetRepository.save(tweet);
        }

    }
    private TwitterTeam getGlobalTeam(List<TwitterTeam> twitterTeams) {
        for (TwitterTeam oneTwitterTeam : twitterTeams) {
            if (oneTwitterTeam.isGlobal()) {
                return oneTwitterTeam;
            }
        }
        return null;
    }

    @Override
    public void updatePlannedTweetStatusToProcessing(Long plannedTweetId) throws AppException {
        PlannedTweet plannedTweet = plannedTweetRepository.findOne(plannedTweetId);
        plannedTweet.setStatus(PlannedPostStatus.PROCESSING);
        plannedTweet = plannedTweetRepository.save(plannedTweet);

    }

    Random random = new Random(System.currentTimeMillis());
    @Override
    public Tweet getNextPendingTweet() throws AppException {
        int delayInSeconds = 60 + random.nextInt(16);// minimum 60 seconds max 75 seconds as per twitter rate limit
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 0 - delayInSeconds);
        PageRequest pageRequest = new PageRequest(0, 1);
        List<Tweet> tweets = tweetRepository.getNextPendingTweet(now.getTime(), pageRequest);
        if (tweets.isEmpty()) {
            return null;
        }
        return tweets.get(0);
    }

    private void updateTweetStatus(Long tweetId, String status) {
        Tweet tweet = tweetRepository.findOne(tweetId);
        tweet.setStatus(status);
        tweet = tweetRepository.save(tweet);
    }
    @Override
    public void updateTweetStatusToProcessing(Long tweetId) throws AppException {
        updateTweetStatus(tweetId, "Processing");
    }

    @Override
    public void updateTweetStatusToFailed(Long tweetId) throws AppException {
        updateTweetStatus(tweetId, "Failed");
    }

    @Override
    public void updateTweetStatusToRetrying(Long tweetId) throws AppException {
        updateTweetStatus(tweetId, "Retrying");
    }

    @Override
    public void updateTweetStatusToDone(Long tweetId) throws AppException {
        updateTweetStatus(tweetId, "Done");
    }

    @Override
    public void tweetIt(Long tweetId) throws AppException {
        Tweet tweet = tweetRepository.findOne(tweetId);
        List<TwitterPermission> twitterPermissions = twitterPermissionRepository.getTwitterPermissionByTwitterAccountId(tweet.getTwitterAccountId());
        if (twitterPermissions.isEmpty()) {
            tweet.setStatus("Error");
            tweet.setErrorMessage("No Permission exists for twitter Account [" + tweet.getTwitterAccountId() + "]");
            tweet = tweetRepository.save(tweet);
            return;
        }
        TwitterPermission twitterPermission = getPrivateAppPermission(twitterPermissions);
        TwitterTemplate twitterTemplate = new TwitterTemplate(twitterPermission.getTwitterApp().getConsumerKey(), twitterPermission.getTwitterApp().getConsumerSecret(), twitterPermission.getToken(),
                twitterPermission.getTokenSecret());

        if (tweet.getPlannedTweet().getTweetType().equalsIgnoreCase("Retweet")) {
            org.springframework.social.twitter.api.Tweet remoteTweet = twitterTemplate.timelineOperations().retweet(tweet.getPlannedTweet().getTweetId());
            tweet.setTweetExternalId(remoteTweet.getId());
            tweet.setTweetContent(remoteTweet.getUnmodifiedText());
            tweet.getTwitterAccount().setLastTweetSentTime(new Date());
            tweet = tweetRepository.save(tweet);
        }
    }

    private TwitterPermission getPrivateAppPermission(List<TwitterPermission> twitterPermissions) {
        for (TwitterPermission oneTwitterPermission : twitterPermissions) {
            if (oneTwitterPermission.getTwitterApp().isPrivateApp()) {
                return oneTwitterPermission;
            }
        }
        return twitterPermissions.get(0);
    }

    @Override
    public TwitterTeam getTwitterTeamByUrl(String url) throws AppException {
        return twitterTeamRepository.getTwitterTeamByUrl(url);
    }

    @Override
    public TwitterAccount saveTwitterAccount(Connection<Twitter> twitterConnection, Long twitterAppId, Long twitterTeamId, User user) throws AppException {
        TwitterApp twitterApp = twitterAppRepository.findOne(twitterAppId);
        TwitterTeam twitterTeam = twitterTeamRepository.findOne(twitterTeamId);
        ConnectionData twitterConnectionData = twitterConnection.createData();
        
        TwitterAccount twitterAccount = twitterAccountRepository.getTwitterAccountByTwitterId(twitterConnectionData.getProviderUserId());
        if (twitterAccount == null) {
            twitterAccount = new TwitterAccount();
            twitterAccount.setDateCreated(new Date());
            twitterAccount.setTwitterId(twitterConnectionData.getProviderUserId());
            twitterAccount.setScreenName(twitterConnectionData.getDisplayName());
            twitterAccount.setScreenNameCap(twitterConnectionData.getDisplayName().toUpperCase());
            twitterAccount.setImageUrl(twitterConnectionData.getImageUrl());
            twitterAccount = twitterAccountRepository.save(twitterAccount);
        }
        if(user != null){
            User dbUser = userRepository.findOne(user.getId());
            twitterAccount.setUser(dbUser);
        }

        twitterAccount.setDateModified(new Date());
        twitterAccount.setImageUrl(twitterConnectionData.getImageUrl());

        TwitterPermission twitterPermission = twitterPermissionRepository.getTwitterPermissionByTwitterAccountIdAndTwitterAppId(twitterAccount.getId(), twitterAppId);
        if (twitterPermission == null) {
            twitterPermission = new TwitterPermission();
            twitterPermission.setTwitterApp(twitterApp);
            twitterPermission.setTwitterAccount(twitterAccount);
            twitterPermission.setToken(twitterConnectionData.getAccessToken());
            twitterPermission.setTokenSecret(twitterConnectionData.getSecret());
            twitterPermission = twitterPermissionRepository.save(twitterPermission);
        }

        twitterPermission.setToken(twitterConnectionData.getAccessToken());
        twitterPermission.setTokenSecret(twitterConnectionData.getSecret());
        
        if (twitterAccount.getTwitterTeams() == null) {
            twitterAccount.setTwitterTeams(new HashSet<TwitterTeam>());
        }
        twitterAccount.getTwitterTeams().add(twitterTeam);

        return twitterAccount;
    }

    @Override
    public boolean isUserPartOfTwitterTeam(long userId, Long twitterTeamId) throws AppException {
        TwitterAccount twitterAccount = twitterAccountRepository.getTwitterAccountByUserId(userId);
        if (twitterAccount == null) {
            return false;
        }
        TwitterTeam twitterTeam = twitterTeamRepository.findOne(twitterTeamId);
        TwitterPermission twitterPermission = twitterPermissionRepository.getTwitterPermissionByTwitterAccountIdAndTwitterAppId(twitterAccount.getId(), twitterTeam.getTwitterAppId());
        if (twitterPermission == null) {
            return false;
        }
        return true;
    }
}
