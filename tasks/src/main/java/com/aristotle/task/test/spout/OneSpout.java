package com.aristotle.task.test.spout;

import java.util.Map;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.aristotle.task.spring.SpringContext;

public class OneSpout implements IRichSpout {

    private SpoutOutputCollector collector;
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        SpringContext.getContext().getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true);
        this.collector = collector;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void nextTuple() {
        try {
            System.out.println("Getting Next message");
            collector.emit("TestStream", new Values("Hello World " + System.currentTimeMillis()));
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
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        Fields fields = new Fields("Test");
        declarer.declareStream("TestStream", fields);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
