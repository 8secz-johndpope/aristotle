package com.aristotle.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CallCampaign;
import com.aristotle.core.persistance.repo.CallCampaignRepository;
import com.aristotle.core.persistance.repo.CallTaskRepository;
import com.aristotle.core.persistance.repo.CallerRepository;

@Service
@Transactional
public class CallCampaignServiceImpl implements CallCampaignService {

    @Autowired
    private CallCampaignRepository callCampaignRepository;
    @Autowired
    private CallerRepository callerRepository;
    @Autowired
    private CallTaskRepository callTaskRepository;

    @Override
    public List<CallCampaign> getCallCampigns() throws AppException {
        return callCampaignRepository.findAll();
    }

    @Override
    public CallCampaign saveCallCampign(CallCampaign callCampaign) throws AppException {
        return callCampaignRepository.save(callCampaign);
    }

}
