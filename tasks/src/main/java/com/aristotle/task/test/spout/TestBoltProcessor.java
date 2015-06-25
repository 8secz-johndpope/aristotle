package com.aristotle.task.test.spout;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.aristotle.task.topology.BoltProcessor;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.aristotle.task.topology.SpringAwareBaseBolt.Result;

@Component
public class TestBoltProcessor implements BoltProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result processTuple(Tuple inputTuple) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String message = inputTuple.getString(0);
            System.out.println("message Recieved : " + message);
            return Result.Success;
        } catch (Exception ex) {
            // logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            // logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
        return Result.Failed;

    }

    @Override
    public void initBoltProcessorForTuple(ThreadLocal<Tuple> tuple, SpringAwareBaseBolt springAwareBaseBolt) {
        System.out.println("initBoltProcessorForTuple ");

    }

}
