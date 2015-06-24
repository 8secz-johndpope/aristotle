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
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * All bolts should extend this class
 * 
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */

public abstract class SpringAwareBaseBolt extends BaseComponent implements IRichBolt {

	private static final long serialVersionUID = 1L;
    public SpringAwareBaseBolt() {}

    protected OutputCollector outputCollector;
    protected String outputStream;
    protected String componentId;
    private String boltProcessor;
    // key - CompnenetId , Value - Stream
    private Map<String, String> sourceComponentStreams;
    private List<String> fields;
    private List<String> outputStreams;

    protected BoltProcessor getBoltProcessor() {
        try {
            logDebug("Getting Bolt Processor for {}", boltProcessor);
            BoltProcessor boltProcessorObject =  (BoltProcessor)getApplicationContext().getBean(Class.forName(boltProcessor));
            boltProcessorObject.initBoltProcessorForTuple(getTupleThreadLocal(), this);
            return boltProcessorObject;
        } catch (Exception e) {
            logError("Unable to create Bolt Processor " + boltProcessor, e);
        }
        logWarning("Returning Null Processor");
        return null;
    }
    @Override
    public final void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.init();
        this.outputCollector = collector;
        onPrepare(stormConf, context, collector);
    }

    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        logInfo("outputStream = " + outputStream);
        if (outputStream != null) {
            declarer.declareStream(outputStream, new Fields(getFields()));
        }
        if (outputStreams != null) {
            for (String oneOutpurStream : outputStreams) {
                declarer.declareStream(oneOutpurStream, new Fields(getFields()));
            }
        }
    }

    public enum Result {
        Success, Failed;
    }

    protected abstract Result processTuple(Tuple inputTuple);

    @Override
    public final void execute(Tuple inputTuple) {
        logDebug("Received Message {} in component {}", inputTuple.getMessageId(), componentId);
        getTupleThreadLocal().set(inputTuple);
        try {
            Result result = processTuple(inputTuple);
            if (Result.Success.equals(result)) {
                acknowledgeTuple(inputTuple);
            } else {
                failTuple(inputTuple);
            }
        } catch (Throwable t) {
            logError("Bolt Processor threw Exception", t);
            failTuple(inputTuple);
        } finally {
            getTupleThreadLocal().remove();
        }
    }

    protected String[] getFields() {
        if (CollectionUtils.isEmpty(fields)) {
            return new String[] { "Default" };
        }
        return fields.toArray(new String[fields.size()]);
    }

    public void writeToStream(Tuple anchor, List<Object> tuple) {
        if (outputStream == null) {
            logDebug("no output stream defined so wont be writing anything");
        } else {
            logDebug("Writing To Stream {}", outputStream);
            List<Integer> taskIds = outputCollector.emit(outputStream, anchor, tuple);
            logDebug("Sent to task {}", taskIds);
        }
    }

    public void writeToParticularStream(Tuple anchor, List<Object> tuple, String stream) {
        logDebug("Writing To Stream {}", stream);
        List<Integer> taskIds = outputCollector.emit(stream, anchor, tuple);
        logDebug("Sent to task {}", taskIds);
    }

    public void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) {
        if (outputStream == null) {
            logDebug("no output stream defined so wont be writing anything");
        } else {
            logDebug("Writing To Stream {}", outputStream);
            outputCollector.emitDirect(taskId, outputStream, anchor, tuple);
        }

    }

    private void acknowledgeTuple(Tuple input) {
        logDebug("acknowledgeTuple : " + printTuple(input));
        outputCollector.ack(input);
    }

    private void failTuple(Tuple input) {
        logWarning("***Failed acknowledgeTuple : " + printTuple(input));
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

    public String getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(String outputStream) {
        this.outputStream = outputStream;
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
