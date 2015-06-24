package com.aristotle.task.topology;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backtype.storm.tuple.Tuple;

/**
 * This class gives Basic Support to bolts and Spouts
 * 
 * @author Ravi
 *
 */
public abstract class BaseComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private int paralellism = 1;
    private static ClassPathXmlApplicationContext applicationContext;
    private transient ThreadLocal<Tuple> tupleThreadLocal;


    protected ClassPathXmlApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            synchronized (this) {
                if (applicationContext == null) {
                    applicationContext = new ClassPathXmlApplicationContext("task-spring-context.xml");
                }
            }
        }
        return applicationContext;
    }
    protected void init() {
        tupleThreadLocal = new ThreadLocal<>();
    }

    protected void destroy() {
        if (applicationContext != null) {
            try {
                applicationContext.close();
            } catch (Exception ex) {
                logError("Unable to close application context", ex);
            }
        }
    }

    public int getParalellism() {
        return paralellism;
    }

    public void setParalellism(int paralellism) {
        this.paralellism = paralellism;
    }

    protected void setCurrentTuple(Tuple tuple) {
        getTupleThreadLocal().set(tuple);
    }

    protected void clearCurrentTuple() {
        getTupleThreadLocal().remove();
    }

    protected String getCurremtTupleAnchor() {
        ThreadLocal<Tuple> threadLocal = getTupleThreadLocal();
        if (threadLocal == null) {
            return "NI";
        }
        Tuple tuple = threadLocal.get();
        if (tuple == null) {
            return "NI";
        }
        return tuple.getMessageId().getAnchors().toString();
    }
    // Log related functions
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

    public ThreadLocal<Tuple> getTupleThreadLocal() {
        return tupleThreadLocal;
    }

}
