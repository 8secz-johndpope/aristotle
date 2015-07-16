package com.aristotle.task.spout;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;

import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;

/**
 * Created by Ravi Sharma on 01/07/2015.
 */
public class ScheduleSpout extends SpringAwareBaseSpout {

    private static final long serialVersionUID = 1L;
    private Stream outputStream;
    private String cronTime;

    private transient LinkedBlockingQueue<String> queue;
    private transient ScheduledFuture<?> scheduledFuture;

    @Autowired(required = false)
    // required false as this will be injected later
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;


    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        queue = new LinkedBlockingQueue<>();
        ScheduleTask scheduleTask = new ScheduleTask();
        logInfo("cronTime = {}", cronTime);
        scheduledFuture = threadPoolTaskScheduler.schedule(scheduleTask, new CronTrigger(cronTime));
        logInfo("scheduledFuture = {}", scheduledFuture);
    }

    @Override
    public void onClose() {
        scheduledFuture.cancel(false);
    }

    @Override
    public void nextTuple() {
        try {
            logInfo("Get Next Tuple");
            String tick = queue.poll(1000, TimeUnit.MILLISECONDS);
            if (tick != null && tick.equals("Tick")) {
                emitTuple(outputStream.getStreamId(), new Values("default"));
            }
        } catch (Exception ex) {
            logError("Unable to tick", ex);
        }
    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    protected void onAck(Object msgId) {
    }

    @Override
    protected void onFail(Object msgId) {
    }

    @Override
    public void onLastFail(Object msgId) {

    }

    public String getCronTime() {
        return cronTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
    }

    private class ScheduleTask implements Runnable {

        @Override
        public void run() {
            logInfo("Ticking From task");
            queue.offer("Tick");
        }
    }

}
