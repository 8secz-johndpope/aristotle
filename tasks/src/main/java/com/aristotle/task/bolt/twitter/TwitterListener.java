package com.aristotle.task.bolt.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class TwitterListener extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;
    @Value("${consumer_key}")
    private String consumerKey;
    @Value("${consumer_secret}")
    private String consumerSecret;

    @Autowired(required = false)//required false as this will ce injected later
    private transient TwitterService twitterService;

    @Override
    @Transactional
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved " + new Date());
        List<TwitterAccount> twitterAccounts = twitterService.getAllSourceTwitterAccounts();
        for(TwitterAccount oneTwitterAccount : twitterAccounts){
            processTweetsFromOneAccount(oneTwitterAccount);
        }
        return Result.Success;
    }

    private void processTweetsFromOneAccount(TwitterAccount twitterAccount) {
        System.out.println(twitterAccount.getUser().getName());
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret);
        List<Tweet> tweets = twitter.timelineOperations().getUserTimeline(Integer.parseInt(twitterAccount.getTwitterId()), 5);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -5);
        for (Tweet oneTweet : tweets) {
            logger.info("Tweet : " + oneTweet.getCreatedAt() + " : " + oneTweet.getText());
            if (oneTweet.getCreatedAt().after(now.getTime())) {
                try {
                    PlannedTweet plannedTweet = twitterService.planRetweet(oneTweet);
                    if (plannedTweet != null) {
                    }
                } catch (AppException e) {
                    e.printStackTrace();
                }
                
            } else {
                logger.info("Ignoring as not in last 5 minutes");
            }

        }
    }

}
