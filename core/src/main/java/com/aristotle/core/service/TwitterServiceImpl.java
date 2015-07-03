package com.aristotle.core.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.repo.PlannedTweetRepository;
import com.aristotle.core.persistance.repo.TwitterAccountRepository;

/**
 * Created by sharmar2 on 02/07/2015.
 */
@Service
@Transactional
public class TwitterServiceImpl implements TwitterService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @Autowired
    private PlannedTweetRepository plannedTweetRepository;

    @Override
    public List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException {
        return twitterAccountRepository.getAllSourceTwitterAccounts();
    }

    @Override
    public PlannedTweet planRetweet(Tweet tweet) throws AppException {
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
        plannedTweet = plannedTweetRepository.save(plannedTweet);
        logger.info("Creating a planned Tweet Success");
        return plannedTweet;
    }

    @Override
    public List<PlannedTweet> getAllPlannedTweetReadyToProcess() throws AppException {
        // TODO Auto-generated method stub
        return null;
    }
}
