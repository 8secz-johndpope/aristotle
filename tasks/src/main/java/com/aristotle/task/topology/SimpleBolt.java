package com.aristotle.task.topology;

import backtype.storm.tuple.Tuple;

public class SimpleBolt extends SpringAwareBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    public Result processTuple(Tuple input) {
        BoltProcessor boltprocessor = getBoltProcessor();
        System.out.println("boltprocessor=" + boltprocessor);
        Result result;
        try {
            result = boltprocessor.processTuple(input);
        } catch (Exception ex) {
            logError("Unable to process : ", ex);
            result = Result.Failed;
        }
        return result;
    }

}
