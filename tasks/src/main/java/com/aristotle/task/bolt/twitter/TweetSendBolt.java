package com.aristotle.task.bolt.twitter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TweetSendBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;
    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterService twitterService;

    private transient JsonParser JsonParser;

    @Override
    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        JsonParser = new JsonParser();
    }

    @Override
    public Result onExecute(Tuple input) throws Exception{
        String messageRecived = input.getString(0);
        logInfo("Message Recieved " + messageRecived);

        JsonObject plannedTweetJsonObject = JsonParser.parse(messageRecived).getAsJsonObject();
        Long tweetId = plannedTweetJsonObject.get("id").getAsLong();
        twitterService.tweetIt(tweetId);
        return Result.Success;
    }
}
