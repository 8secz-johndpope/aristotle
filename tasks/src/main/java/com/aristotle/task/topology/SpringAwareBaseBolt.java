package com.aristotle.task.topology;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

/**
 * All bolts should extend this class
 * 
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */

public class SpringAwareBaseBolt extends BaseComponent implements IRichBolt {

	private static final long serialVersionUID = 1L;
    public SpringAwareBaseBolt() {}

    protected OutputCollector outputCollector;
    protected String componentId;
    private String boltProcessor;
    // key - CompnenetId , Value - Stream
    private Map<String, String> sourceComponentStreams;
    private List<String> fields;
    private List<String> outputStreams;

    protected BoltProcessor getBoltProcessor() {
        try {
            logger.debug("Getting Bolt Processor for {}", boltProcessor);
            BoltProcessor boltProcessorObject =  (BoltProcessor)getApplicationContext().getBean(Class.forName(boltProcessor));
            // boltProcessorObject.initBoltProcessorForTuple(getTupleThreadLocal(), this);
            return boltProcessorObject;
        } catch (Exception e) {
            logger.error("Unable to create Bolt Processor " + boltProcessor, e);
        }
        logger.warn("Returning Null Processor");
        return null;
    }
    @Override
    public final void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
        onPrepare(stormConf, context, collector);
    }

    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        getBoltProcessor().declareOutputFields(declarer);
    }

    public enum Result {
        Success, Failed;
    }

    @Override
    public final void execute(Tuple inputTuple) {
        logger.debug("Received Message {} in component {}", inputTuple.getMessageId(), componentId);
        try {
            getBoltProcessor().execute(inputTuple);
        } catch (Throwable t) {
            logger.error("Bolt Processor threw Exception", t);
            failTuple(inputTuple);
        }
    }

    protected String[] getFields() {
        if (CollectionUtils.isEmpty(fields)) {
            return new String[] { "Default" };
        }
        return fields.toArray(new String[fields.size()]);
    }

    /*
     * public void writeToStream(Tuple anchor, List<Object> tuple) { logger.debug("Writing To Stream {}", outputStream); List<Integer> taskIds = outputCollector.emit(outputStream, anchor, tuple);
     * logger.debug("Sent to task {}", taskIds); }
     */

    public void writeToParticularStream(Tuple anchor, List<Object> tuple, String stream) {
        logger.debug("Writing To Stream {}", stream);
        List<Integer> taskIds = outputCollector.emit(stream, anchor, tuple);
        logger.debug("Sent to task {}", taskIds);
    }
    
    /*
     * public void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) { logger.debug("Writing To Stream {}", outputStream); outputCollector.emitDirect(taskId, outputStream, anchor,
     * tuple); }
     */

    private void acknowledgeTuple(Tuple input) {
        logger.debug("acknowledgeTuple : " + printTuple(input));
        outputCollector.ack(input);
    }

    private void failTuple(Tuple input) {
        logger.warn("***Failed acknowledgeTuple : " + printTuple(input));
        outputCollector.fail(input);
    }

    protected String printTuple(Tuple input) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Component : " + componentId + " , ");
        stringBuilder.append("getSourceComponent : " + input.getSourceComponent() + " , ");
        stringBuilder.append("getSourceStreamId : " + input.getSourceStreamId() + " , ");
        stringBuilder.append("getSourceGlobalStreamid : " + input.getSourceGlobalStreamid() + " , ");
        stringBuilder.append("getSourceTask : " + input.getSourceTask() + " , ");
        stringBuilder.append("getMessageId : " + input.getMessageId() + " , ");
        stringBuilder.append("getMessageId.getAnchors : " + input.getMessageId().getAnchors() + " , ");
        stringBuilder.append("getMessageId.getAnchorsToIds : " + input.getMessageId().getAnchorsToIds() + " , ");
        return stringBuilder.toString();
    }

    @Override
    public void cleanup() {
        super.destroy();
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Map<String, String> getSourceComponentStreams() {
        return sourceComponentStreams;
    }

    public void setSourceComponentStreams(Map<String, String> sourceComponentStreams) {
        this.sourceComponentStreams = sourceComponentStreams;
    }

    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public void setBoltProcessor(String boltProcessor) {
        this.boltProcessor = boltProcessor;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getOutputStreams() {
        return outputStreams;
    }

    public void setOutputStreams(List<String> outputStreams) {
        this.outputStreams = outputStreams;
    }

}
