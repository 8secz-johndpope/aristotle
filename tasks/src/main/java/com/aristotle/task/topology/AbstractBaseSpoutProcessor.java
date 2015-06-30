package com.aristotle.task.topology;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

public abstract class AbstractBaseSpoutProcessor implements SpoutProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    Map<String, Object> configuration;
    private SpoutOutputCollector collector;
    private int retry;
    private List<String> outputStreams;
    private String componentId;
    private int paralellism = 1;
    private int maxSpoutPending;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        configuration = conf;
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

    @Override
    public String[] getFields() {
        return new String[] { "Test" };
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        Fields fields = new Fields(getFields());
        if (outputStreams != null && !outputStreams.isEmpty()) {
            for (String oneOutpurStream : outputStreams) {
                declarer.declareStream(oneOutpurStream, fields);
            }
        }
    }

    protected MessageId<List<Object>> writeToParticularStream(List<Object> tuple, String streamId) {
        MessageId<List<Object>> messageId = new MessageId<>();
        messageId.setData(tuple);
        writeToStream(tuple, messageId, streamId);
        return messageId;
    }


    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }

    public SpoutOutputCollector getCollector() {
        return collector;
    }

    public void setCollector(SpoutOutputCollector collector) {
        this.collector = collector;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public List<String> getOutputStreams() {
        return outputStreams;
    }

    public void setOutputStreams(List<String> outputStreams) {
        this.outputStreams = outputStreams;
    }

    public int getParalellism() {
        return paralellism;
    }

    public void setParalellism(int paralellism) {
        this.paralellism = paralellism;
    }

    public int getMaxSpoutPending() {
        return maxSpoutPending;
    }

    public void setMaxSpoutPending(int maxSpoutPending) {
        this.maxSpoutPending = maxSpoutPending;
    }

}
