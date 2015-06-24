package com.aristotle.task.topology;

import backtype.storm.tuple.Tuple;

import com.aristotle.task.topology.SpringAwareBaseBolt.Result;


public interface BoltProcessor {

    public Result processTuple(Tuple input) throws Exception;
    
    public void initBoltProcessorForTuple(ThreadLocal<Tuple> tuple, SpringAwareBaseBolt springAwareBaseBolt);
}
