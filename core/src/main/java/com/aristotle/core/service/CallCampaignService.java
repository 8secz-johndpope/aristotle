package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CallCampaign;
import com.aristotle.core.persistance.UploadedFile;

public interface CallCampaignService {

    List<CallCampaign> getCallCampigns() throws AppException;

    CallCampaign saveCallCampign(CallCampaign callCampaign) throws AppException;
    
    List<UploadedFile> getCallCampaignUploadedFiles(Long callCampignId) throws AppException;

    UploadedFile saveCallCampaignUploadedFile(Long callCampaignId, String filePathAndName, long fileSize, String type) throws AppException;
}
