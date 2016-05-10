package com.aristotle.task.bolt.donation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.aristotle.core.service.aws.DonationSearchService;
import com.aristotle.task.topology.Result;
import com.aristotle.task.topology.SpringAwareBaseBolt;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DonationRefreshBolt extends SpringAwareBaseBolt {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    // required false as this will be injected later
    private transient DonationSearchService donationSearchService;

    private transient JsonParser jsonParser;

    @Override
    protected void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        jsonParser = new JsonParser();

    }

    @Override
    @Transactional
    public Result onExecute(Tuple input) throws Exception{
        String message = input.getString(0);
        logInfo("Message Recieved " + message);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(message);
        Long donationId = jsonObject.get("donationId").getAsLong();
        donationSearchService.indexDonation(donationId);
        return Result.Success;
    }


}
