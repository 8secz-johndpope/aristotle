package com.aristotle.task.test.spout;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.aristotle.task.spring.SpringContext;

public class OneBolt implements IRichBolt {

    @Autowired
    private SpringClass springClass;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        SpringContext.getContext().getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true);
    }

    @Override
    public void execute(Tuple input) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String message = input.getString(0);
            System.out.println("message Recieved : " + message);
            springClass.printData("message Recieved : " + message);
        } catch (Exception ex) {
            // logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            // logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
