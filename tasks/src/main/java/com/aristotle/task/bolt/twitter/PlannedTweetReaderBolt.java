package com.aristotle.task.bolt.twitter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.service.TwitterService;
import com.aristotle.core.service.aws.QueueService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.aristotle.task.topology.beans.Stream;
import com.google.gson.JsonObject;

public class PlannedTweetReaderBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterService twitterService;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient QueueService queueService;

    private Stream outputStream;

    @Override
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved " + new Date());
        List<PlannedTweet> plannedTweets = twitterService.getAllPlannedTweetReadyToProcess();
        logInfo("plannedTweets" + plannedTweets);
        String plannedTweetMessage;
        for (PlannedTweet onePlannedTweet : plannedTweets) {
            //Send a Processing message to Queue to processed by Another Spout
            plannedTweetMessage = createPlannedTweetMessage(onePlannedTweet);
            queueService.sendPlannedTweetMessage(plannedTweetMessage);
            twitterService.updatePlannedTweetStatusToProcessing(onePlannedTweet.getId());
        }
        return Result.Success;
    }

    private String createPlannedTweetMessage(PlannedTweet onePlannedTweet){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", onePlannedTweet.getId());
        jsonObject.addProperty("tweetType", onePlannedTweet.getTweetType());
        return jsonObject.toString();
    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

}
