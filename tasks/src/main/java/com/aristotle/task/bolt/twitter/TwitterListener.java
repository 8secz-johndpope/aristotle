package com.aristotle.task.bolt.twitter;

import java.util.Calendar;
import java.util.List;

import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import backtype.storm.tuple.Tuple;

import com.aristotle.task.test.spout.SpringClass;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class TwitterListener extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;
    @Value("${consumer_key}")
    private String consumerKey;
    @Value("${consumer_secret}")
    private String consumerSecret;

    @Autowired
    private transient SpringClass springClass;

    @Autowired(required = false)//required false as this will ce injected later
    private transient TwitterService twitterService;

    @Override
    public Result onExecute(Tuple input) throws Exception{
        List<TwitterAccount> twitterAccounts = twitterService.getAllSourceTwitterAccounts();
        for(TwitterAccount oneTwitterAccount : twitterAccounts){

        }
        return Result.Success;
    }
    private void processTweetsFromOneAccount(TwitterAccount twitterAccount){
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret);
        List<Tweet> tweets = twitter.timelineOperations().getUserTimeline(Integer.parseInt(twitterAccount.getTwitterId()), 5);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -5);
        Calendar postingTime = Calendar.getInstance();
        postingTime.add(Calendar.MINUTE, 5);
        for (Tweet oneTweet : tweets) {
            logger.info("Tweet : " + oneTweet.getCreatedAt() + " : " + oneTweet.getText());
/*
            if (oneTweet.getCreatedAt().after(now.getTime())) {

                PlannedTweetDto plannedTweetDto;
                try {
                    plannedTweetDto = aapService.getPlannedTweetByTweetId(oneTweet.getId());
                    logger.info("plannedTweetDto : " + plannedTweetDto);
                    if (plannedTweetDto != null) {
                        logger.info("Ignoring as already have planned Tweet");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } // Create New Planned tweet only if it happens in last 5 minutes

                logger.info("Creating a planned Tweet");
                plannedTweetDto = new PlannedTweetDto();
                plannedTweetDto.setLocationType(PostLocationType.Global);
                plannedTweetDto.setStatus(PlannedPostStatus.PENDING);
                plannedTweetDto.setTweetType("Retweet");
                plannedTweetDto.setTotalRequired(200);
                logger.info("Setting total Required Tweets to 200");
                plannedTweetDto.setTweetId(oneTweet.getId());
                plannedTweetDto.setPostingTime(postingTime.getTime());
                plannedTweetDto =
                        aapService.savePlannedTweet(plannedTweetDto);
                logger.info("Creating a planned Tweet Success");
            } else {
                logger.info("Ignoring as not in last 5 minutes");
            }
            */

        }
    }

}
