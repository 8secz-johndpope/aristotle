package com.aristotle.task.bolt.twitter;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.aristotle.core.service.TwitterReportService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class GenerateDailyTwitterReportBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterReportService twitterReportService;

    @Override
    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    }

    @Override
    public Result onExecute(Tuple input) throws Exception{
        Date reportDate = (Date) input.getValue(0);
        logInfo("Message Recieved " + reportDate);
        twitterReportService.genrateDailyTwitterReport(reportDate);
        return Result.Success;
    }

}
