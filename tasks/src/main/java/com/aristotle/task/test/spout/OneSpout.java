package com.aristotle.task.test.spout;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;

import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;

public class OneSpout extends SpringAwareBaseSpout {

    private Stream outputStream;
    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    }


    @Override
    public void nextTuple() {
        try {
            System.out.println("Getting Next message");
            emitTuple("TestStream", new Values("Hello World " + System.currentTimeMillis()));
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Unable to receive Location File message from AWS Quque");
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fail(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

}
