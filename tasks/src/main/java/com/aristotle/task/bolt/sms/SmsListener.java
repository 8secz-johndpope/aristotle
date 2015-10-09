package com.aristotle.task.bolt.sms;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.tuple.Tuple;

import com.aristotle.core.service.SmsService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;

public class SmsListener extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

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
        }
        return Result.Success;
    }


}
