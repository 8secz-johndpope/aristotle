package com.aristotle.task.spout;

import java.util.Map;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;

import com.amazonaws.services.sqs.model.Message;
import com.aristotle.core.service.aws.QueueService;
import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;

public class MemberRefreshSpout extends SpringAwareBaseSpout {

    private static final long serialVersionUID = 1L;
    @Autowired(required = false)
    // required false as this will be injected later
    private transient QueueService queueService;

    private Stream outputStream;

    @Override
    public void nextTuple() {
        try {
            Message message = queueService.receiveRefreshUserMessage();
            if (message == null) {
                return;
            }
            String messageBody = message.getBody();
            logInfo("Received Message {}", messageBody);
            emitTuple(outputStream.getStreamId(), new Values(messageBody));
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onAck(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onFail(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onLastFail(Object msgId) {
        // TODO Auto-generated method stub

    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

}
