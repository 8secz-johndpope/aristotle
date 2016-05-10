package com.aristotle.task.bolt.sms;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.service.SmsService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class SendSms extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient SmsService smsService;

    @Override
    @Transactional
    public Result onExecute(Tuple input) throws Exception{
        logInfo("Message Recieved to send Sms");
        while (smsService.sendNextSms()) {
            // Keep processing
            logInfo("More Sms to process");
        }
        return Result.Success;
    }


}
