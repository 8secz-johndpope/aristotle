package com.aristotle.task.spout.twitter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.MessageId;
import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;
import com.google.gson.JsonObject;

public class SendTweetSpout extends SpringAwareBaseSpout {

    private static final long serialVersionUID = 1L;
    private Stream outputStream;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterService twitterService;



    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    }


    @Override
    public void nextTuple() {
        try {
            Tweet tweet = twitterService.getNextPendingTweet();
            if (tweet == null) {
                sleepForMilliSeconds(5000);// sleep for 5 seconds
            } else {
                sendProcessTweetMessage(tweet);
            }
            logInfo("Getting Next message");
        } catch (Exception e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }

    private void sendProcessTweetMessage(Tweet tweet) throws AppException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", tweet.getId());
        MessageId<Tweet> messageId = new MessageId<>();
        messageId.setData(tweet);
        messageId.setStreamId(outputStream.getStreamId());

        twitterService.updateTweetStatusToProcessing(tweet.getId());
        writeToStream(outputStream.getStreamId(), new Values(jsonObject.toString()), messageId);
    }

    @Override
    public void onAck(Object msgId) {
        MessageId<Tweet> messageId = (MessageId<Tweet>) msgId;
        Tweet tweet = messageId.getData();
        try {
            twitterService.updateTweetStatusToDone(tweet.getId());
        } catch (AppException e) {
            logError("Unable to update Tweet Status to Done", e);
        }
    }

    @Override
    public void onFail(Object msgId) {
        MessageId<Tweet> messageId = (MessageId<Tweet>) msgId;
        Tweet tweet = messageId.getData();
        try {
            twitterService.updateTweetStatusToRetrying(tweet.getId());
        } catch (AppException e) {
            logError("Unable to update Tweet Status to Done", e);
        }
    }

    @Override
    public void onLastFail(Object msgId) {
        MessageId<Tweet> messageId = (MessageId<Tweet>) msgId;
        Tweet tweet = messageId.getData();
        try {
            twitterService.updateTweetStatusToFailed(tweet.getId());
        } catch (AppException e) {
            logError("Unable to update Tweet Status to Done", e);
        }
    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

}
