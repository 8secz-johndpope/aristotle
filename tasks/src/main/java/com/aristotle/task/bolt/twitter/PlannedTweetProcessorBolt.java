package com.aristotle.task.bolt.twitter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class PlannedTweetProcessorBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;
    @Value("${consumer_key}")
    private String consumerKey;
    @Value("${consumer_secret}")
    private String consumerSecret;

    @Autowired(required = false)//required false as this will ce injected later
    private transient TwitterService twitterService;

    @Override
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved " + new Date());
        List<PlannedTweet> twitterAccounts = twitterService.getAllPlannedTweetReadyToProcess();
        for (PlannedTweet oneTwitterAccount : twitterAccounts) {
            // processTweetsFromOneAccount(oneTwitterAccount);
        }
        return Result.Success;
    }

}
