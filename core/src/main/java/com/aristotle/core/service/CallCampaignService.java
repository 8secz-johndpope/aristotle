package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CallCampaign;

public interface CallCampaignService {

    List<CallCampaign> getCallCampigns() throws AppException;

    CallCampaign saveCallCampign(CallCampaign callCampaign) throws AppException;
}
