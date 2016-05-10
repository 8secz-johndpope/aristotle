package com.aristotle.task.bolt.twitter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class TwitterListener extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterService twitterService;

    @Override
    @Transactional
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved " + new Date());
        List<TwitterAccount> twitterAccounts = twitterService.getAllSourceTwitterAccounts();
        for(TwitterAccount oneTwitterAccount : twitterAccounts){
            logInfo("Getting Tweets From Account :  " + oneTwitterAccount.getScreenName());
            twitterService.processTweetsFromOneAccount(oneTwitterAccount);
        }
        return Result.Success;
    }


}
