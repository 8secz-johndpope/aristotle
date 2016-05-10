package com.aristotle.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CallCampaign;
import com.aristotle.core.persistance.UploadedFile;
import com.aristotle.core.persistance.repo.CallCampaignRepository;
import com.aristotle.core.persistance.repo.CallTaskRepository;
import com.aristotle.core.persistance.repo.CallerRepository;
import com.aristotle.core.persistance.repo.UploadedFileRepository;

@Service
@Transactional
public class CallCampaignServiceImpl implements CallCampaignService {

    @Autowired
    private CallCampaignRepository callCampaignRepository;
    @Autowired
    private CallerRepository callerRepository;
    @Autowired
    private CallTaskRepository callTaskRepository;
    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Override
    public List<CallCampaign> getCallCampigns() throws AppException {
        return callCampaignRepository.findAll();
    }

    @Override
    public CallCampaign saveCallCampign(CallCampaign callCampaign) throws AppException {
        return callCampaignRepository.save(callCampaign);
    }

    @Override
    public List<UploadedFile> getCallCampaignUploadedFiles(Long callCampignId) throws AppException {
        CallCampaign callCampaign = callCampaignRepository.findOne(callCampignId);
        if (callCampaign == null || callCampaign.getFiles() == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(callCampaign.getFiles());
    }

    @Override
    public UploadedFile saveCallCampaignUploadedFile(Long callCampaignId, String filePathAndName, long fileSize, String type) throws AppException {
        CallCampaign callCampaign = callCampaignRepository.findOne(callCampaignId);
        filePathAndName = filePathAndName.toLowerCase();
        UploadedFile uploadedFile = uploadedFileRepository.getUploadedFileByFileName(filePathAndName);
        if (uploadedFile == null) {
            uploadedFile = new UploadedFile();
            uploadedFile.setFileName(filePathAndName);
        }
        uploadedFile.setSize(fileSize);
        uploadedFile.setFileType(type);
        uploadedFile = uploadedFileRepository.save(uploadedFile);
        if (callCampaign.getFiles() == null) {
            callCampaign.setFiles(new HashSet<UploadedFile>());
        }
        callCampaign.getFiles().add(uploadedFile);
        return uploadedFile;
    }

}
