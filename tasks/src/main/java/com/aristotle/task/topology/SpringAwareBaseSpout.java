package com.aristotle.task.topology;

import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;

public class SpringAwareBaseSpout extends BaseComponent implements IRichSpout {

	private static final long serialVersionUID = 1L;


    protected String componentId;
    private String spoutProcessor;
    private int maxSpoutPending;
    private List<String> outputStreams;

    protected SpoutProcessor getSpoutProcessor() {
        try {
            logger.debug("Getting Spout Processor for {}", spoutProcessor);
            SpoutProcessor boltProcessorObject = (SpoutProcessor) getApplicationContext().getBean(Class.forName(spoutProcessor));
            return boltProcessorObject;
        } catch (Exception e) {
            logger.error("Unable to create Spout Processor " + spoutProcessor, e);
        }
        logger.warn("Returning Null Processor");
        return null;
    }
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        getSpoutProcessor().open(conf, context, collector);
    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        try {
            getSpoutProcessor().declareOutputFields(declarer);
        } catch (Exception e) {
            logger.error("Unable to declare output Fields", e);
        }

    }

    protected String[] getFields() {
        return getSpoutProcessor().getFields();
    }

    @Override
    public final void nextTuple() {
        try {
            getSpoutProcessor().nextTuple();
        } catch (Exception e) {
            logger.error("Unable to get Next Tuple", e);
        }

    }

    protected MessageId<List<Object>> writeToStream(List<Object> tuple) {
        MessageId<List<Object>> messageId = new MessageId<>();
        messageId.setData(tuple);
        // writeToStream(tuple, messageId);
        return messageId;
    }


    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public void close() {
        super.destroy();
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public final void ack(Object msgId) {
        getSpoutProcessor().ack(msgId);
    }

    @Override
    public final void fail(Object msgId) {
        getSpoutProcessor().fail(msgId);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return getSpoutProcessor().getComponentConfiguration();
    }

    public int getMaxSpoutPending() {
        return maxSpoutPending;
    }

    public void setMaxSpoutPending(int maxSpoutPending) {
        this.maxSpoutPending = maxSpoutPending;
    }

    public void setSpoutProcessor(String spoutProcessor) {
        this.spoutProcessor = spoutProcessor;
    }

    public List<String> getOutputStreams() {
        return outputStreams;
    }

    public void setOutputStreams(List<String> outputStreams) {
        this.outputStreams = outputStreams;
    }

}
