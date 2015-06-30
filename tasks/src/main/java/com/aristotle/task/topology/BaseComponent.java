package com.aristotle.task.topology;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import backtype.storm.spout.SpoutOutputCollector;

import com.aristotle.task.spring.SpringContext;

/**
 * This class gives Basic Support to bolts and Spouts
 * 
 * @author Ravi
 *
 */
public abstract class BaseComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ConfigurableApplicationContext applicationContext;
    Map<String, Object> configuration;
    private SpoutOutputCollector collector;
    private int retry;
    private List<String> outputStreams;
    private String componentId;
    private int paralellism = 1;
    private int maxSpoutPending;


    protected ConfigurableApplicationContext getApplicationContext() {
        applicationContext = SpringContext.getContext();
        return applicationContext;
    }
    protected void destroy() {
        if (applicationContext != null) {
            try {
                applicationContext.close();
            } catch (Exception ex) {
                logger.error("Unable to close application context", ex);
            }
        }
    }

    public int getParalellism() {
        return paralellism;
    }

    public void setParalellism(int paralellism) {
        this.paralellism = paralellism;
    }

    protected void writeToStream(List<Object> tuple, Object messageId, String streamId) {
        logInfo("Writing To Stream " + streamId + " with message id as " + messageId);
        collector.emit(streamId, tuple, messageId);
    }

    protected void writeToStream(List<Object> tuple, String streamId) {
        logInfo("Writing To Stream " + streamId);
        collector.emit(streamId, tuple);
    }

    protected void logInfo(String message) {
        logger.info(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logInfo(String message, Object... objects) {
        logger.info(getCurremtTupleAnchor() + " : " + message, objects);
    }

    protected void logDebug(String message) {
        logger.debug(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logDebug(String message, Object... obj1) {
        logger.debug(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logWarning(String message) {
        logger.warn(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logWarning(String message, Object... obj1) {
        logger.warn(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logError(String message) {
        logger.error(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logError(String message, Throwable ex) {
        logger.error(getCurremtTupleAnchor() + " : " + message, ex);
    }

    protected String getCurremtTupleAnchor() {
        /*
         * ThreadLocal<Tuple> threadLocal = getTupleThreadLocal(); if (threadLocal == null) { return "NI"; } Tuple tuple = threadLocal.get(); if (tuple == null) { return "NI"; } return
         * tuple.getMessageId().getAnchors().toString();
         */
        return "NI";
    }

}
