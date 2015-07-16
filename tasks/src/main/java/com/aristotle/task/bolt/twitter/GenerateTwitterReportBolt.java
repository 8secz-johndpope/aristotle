package com.aristotle.task.bolt.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.aristotle.core.service.TwitterService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.aristotle.task.topology.beans.Stream;

public class GenerateTwitterReportBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    private Stream outputStream;
    @Autowired(required = false)
    // required false as this will be injected later
    private transient TwitterService twitterService;


    @Override
    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    }

    @Override
    public Result onExecute(Tuple input) throws Exception{
        String messageRecived = input.getString(0);
        logInfo("Message Recieved " + messageRecived);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 5);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.YEAR, 2015);
        Date today = new Date();
        while (calendar.getTime().before(today)) {
            writeToParticularStream(input, new Values(calendar.getTime()), outputStream.getStreamId());
            calendar.add(Calendar.DATE, 1);
        }
        return Result.Success;
    }

    public Stream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(Stream outputStream) {
        this.outputStream = outputStream;
    }
}
