package com.aristotle.task.topology;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.CollectionUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.aristotle.task.spring.SpringContext;
import com.aristotle.task.topology.beans.Stream;

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
    protected String componentId;
    // key - CompnenetId , Value - Stream
    private Map<String, String> sourceComponentStreams;
    private List<String> fields;
    private List<String> outputStreams;

    @Override
    public final void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        SpringContext.getContext().getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, false);
        this.outputCollector = collector;
        onPrepare(stormConf, context, collector);
    }

    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public final void execute(Tuple input) {
        Result result;
        try{
            result = onExecute(input);
        }catch(Exception ex){
            result = Result.Failed;
            logError("Error", ex);
        }
        sendResponse(result, input);

    }
    private void sendResponse(Result result, Tuple input){
        if(result == Result.Failed){
            failTuple(input);
        }else{
            acknowledgeTuple(input);
        }
    }
    public abstract Result onExecute(Tuple input) throws Exception;

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
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
        // super.destroy();
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
