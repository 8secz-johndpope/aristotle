package com.aristotle.task.bolt.sms;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.aristotle.core.service.SmsService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.aristotle.task.topology.beans.Stream;

public class SmsListener extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    private Stream outputStream;
    
    @Autowired(required = false)
    // required false as this will be injected later
    private transient SmsService smsService;

    @Override
    @Transactional
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved " + new Date());
        while (smsService.processNextSms()) {
            // Keep processing
            logInfo("More messages to process");
            Calendar calendar = Calendar.getInstance();
            writeToParticularStream(input, new Values(calendar.getTime()), outputStream.getStreamId());
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
