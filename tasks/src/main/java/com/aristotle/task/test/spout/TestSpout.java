package com.aristotle.task.test.spout;

import org.springframework.stereotype.Component;

import backtype.storm.tuple.Values;

import com.aristotle.task.topology.AbstractBaseSpoutProcessor;

@Component
public class TestSpout extends AbstractBaseSpoutProcessor {

    private static final long serialVersionUID = 1L;
    

    @Override
    public void nextTuple() {
        try {
            System.out.println("Getting Next message");
            writeToStream(new Values("Hello World " + System.currentTimeMillis()), "TestStream");
            Thread.sleep(5000);
        } catch (Exception e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }
    
    @Override
    public void ack(Object msgId) {
        System.out.println("Message Ack");

    }

    @Override
    public void fail(Object msgId) {
        System.out.println("Message Fail");
    }
}
