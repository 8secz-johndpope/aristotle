package com.aristotle.task.test.spout;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.aristotle.task.topology.SpringAwareBaseBolt;

public class OneBolt extends SpringAwareBaseBolt {

    @Autowired
    private transient SpringClass springClass;

    @Override
    public void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public void execute(Tuple input) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String message = input.getString(0);
            springClass.printData("message Recieved : " + message + " , " + new Date());
        } catch (Exception ex) {
            // logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            // logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
    }

}
