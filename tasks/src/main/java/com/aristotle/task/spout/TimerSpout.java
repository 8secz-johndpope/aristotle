package com.aristotle.task.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import com.aristotle.task.topology.SpringAwareBaseSpout;
import com.aristotle.task.topology.beans.Stream;

import java.util.Map;

/**
 * Created by Ravi Sharma on 01/07/2015.
 */
public class TimerSpout extends SpringAwareBaseSpout {

    private long lastTime = 0;
    private long durationMs;
    private boolean onStart;
    private Stream outputStream;
    @Override
    public void onOpen(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        if(!onStart){
            lastTime = System.currentTimeMillis();
        }
    }

    @Override
    public void nextTuple() {
        Long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime >= durationMs){

        }
    }


}
