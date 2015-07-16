package com.aristotle.task.topology;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.amazonaws.services.sqs.model.Message;
import com.aristotle.task.spring.SpringContext;
import com.aristotle.task.topology.beans.Stream;

public abstract class SpringAwareBaseSpout extends BaseComponent implements IRichSpout {

	private static final long serialVersionUID = 1L;


    protected String componentId;
    private int maxSpoutPending;
    private Map<String, Object> config;
    private SpoutOutputCollector collector;
    private int maxRetry = 0;

    @Override
    public final void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        SpringContext.getContext().getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, false);
        this.config = conf;
        this.collector = collector;
        onOpen(conf, context, collector);
    }

    public abstract void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector);

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        try {
            Field[] classFields = this.getClass().getDeclaredFields();
            for (Field oneClassField : classFields) {
                if (oneClassField.getType().isAssignableFrom(Stream.class)) {
                    oneClassField.setAccessible(true);
                    Fields fields = new Fields("default");
                    Stream stream = (Stream) oneClassField.get(this);
                    if (stream.getFields() != null && !stream.getFields().isEmpty()) {
                        fields = new Fields(stream.getFields());
                    }
                    declarer.declareStream(stream.getStreamId(), fields);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void emitTuple(String streamName, Values values) {
        collector.emit(streamName, values);
    }

    protected MessageId<List<Object>> writeToStream(List<Object> tuple) {
        MessageId<List<Object>> messageId = new MessageId<>();
        messageId.setData(tuple);
        // writeToStream(tuple, messageId);
        return messageId;
    }

    protected void writeToStream(String streamId, List<Object> tuple, Object messageId) {
        logInfo("Writing To Stream " + streamId + " with message id as " + messageId);
        collector.emit(streamId, tuple, messageId);
    }

    protected void writeToStream(List<Object> tuple, String streamId) {
        logInfo("Writing To Stream " + streamId);
        logInfo("collector =  " + collector);
        collector.emit(streamId, tuple);
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public final void close() {
        // super.destroy();
        onClose();
    }

    public void onClose() {
        
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public final void ack(Object msgId) {
        logInfo("********************************");
        if (msgId instanceof MessageId) {
            logInfo("Message {} has been processed", msgId + " , " + msgId.getClass() + ", total time takes is " + ((MessageId) msgId).getTimeSinceStart() + " ms");
        } else {
            logInfo("Message {} has been processed", msgId + " , " + msgId.getClass());
        }
        onAck(msgId);
        logInfo("********************************");
    }

    protected abstract void onAck(Object msgId);

    @Override
    public final void fail(Object msgId) {
        logWarning("********************************");
        logWarning("Message {} has been failed", msgId + " , " + msgId.getClass());
        onFail(msgId);
        if (getMaxRetry() > 0) {
            logInfo("Retry count set to {} so will see if i can retry", getMaxRetry());
            // If retry count set more then 0
            if (msgId instanceof MessageId) {
                MessageId messageId = (MessageId) msgId;
                logInfo("current Retry count is {} " + messageId.getRetryCount());
                if (messageId.getRetryCount() < getMaxRetry()) {
                    logInfo("Retrying {} " + messageId);
                    messageId.setRetryCount(messageId.getRetryCount() + 1);
                    if (messageId.getData() instanceof Message) {
                        Message message = (Message) messageId.getData();
                        writeToStream(messageId.getStreamId(), new Values(message.getBody()), messageId);
                    } else {
                        writeToStream(messageId.getStreamId(), new Values(messageId.getData()), messageId);
                    }
                } else {
                    onFail(msgId);
                }
            } else {
                logWarning("msgId is not of type MessageId so can not retry");
                onFail(msgId);
            }

        } else {
            logWarning("Message Failed and i will not retry it as retry count set to 0 : {}", msgId);
            onFail(msgId);
        }

        logWarning("********************************");
    }

    protected abstract void onFail(Object msgId);

    protected abstract void onLastFail(Object msgId);

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return config;
    }

    public int getMaxSpoutPending() {
        return maxSpoutPending;
    }

    public void setMaxSpoutPending(int maxSpoutPending) {
        this.maxSpoutPending = maxSpoutPending;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }
    
    protected void sleepForMilliSeconds(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
