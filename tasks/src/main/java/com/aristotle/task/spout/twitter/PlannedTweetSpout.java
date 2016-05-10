package com.aristotle.task.spout.twitter;

import java.util.Map;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;

import com.amazonaws.services.sqs.model.Message;
import com.aristotle.core.service.aws.QueueService;
import com.aristotle.task.topology.MessageId;
import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;

public class PlannedTweetSpout extends SpringAwareBaseSpout {

    private static final long serialVersionUID = 1L;
    private Stream outputStream;
    @Autowired(required = false)
    // required false as this will be injected later
    private transient QueueService queueService;


    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    }


    @Override
    public void nextTuple() {
        try {
            logInfo("Getting Next message");
            Message message = queueService.receivePlannedTweetMessage();
            if (message == null) {
                Thread.sleep(10000);
            } else {
                MessageId<Message> messageId = new MessageId<>();
                messageId.setData(message);
                messageId.setRetryCount(getMaxRetry());
                logInfo("Message from AWS : " + message.getBody() + ", " + message.getMessageId());
                emitTuple(outputStream.getStreamId(), new Values(message.getBody()));
            }
        } catch (Exception e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }

    @Override
    public void onAck(Object msgId) {
        logInfo("Ack " + msgId);
        MessageId<Message> messageId = (MessageId<Message>) msgId;
        Message message = messageId.getData();
        try {
            queueService.deletePlannedTweetMessage(message);
        } catch (ApplicationException e) {
            logError("Unable to delete Message " + message, e);
        }
    }

    @Override
    public void onFail(Object msgId) {

    }

    @Override
    public void onLastFail(Object msgId) {

    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

}
