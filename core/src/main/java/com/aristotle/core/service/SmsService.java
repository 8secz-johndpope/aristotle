package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.MobileGroupMobile;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.SmsTemplate;
import com.aristotle.core.persistance.TeamPlannedSms;

public interface SmsService {

    GroupPlannedSms saveGroupPlannedSms(GroupPlannedSms groupPlannedSms) throws AppException;

    TeamPlannedSms saveTeamPlannedSms(TeamPlannedSms teamPlannedSms) throws AppException;

    LocationPlannedSms saveLocationPlannedSms(LocationPlannedSms locationPlannedSms) throws AppException;

    List<PlannedSms> getAllPlannedSms() throws AppException;

    List<MobileGroup> getAllMobileGroups() throws AppException;

    MobileGroup saveMobileGroup(MobileGroup mobileGroup) throws Exception;

    void addMemberToMobileGroup(MobileGroup mobileGroup, String emailPhone) throws Exception;

    void removeMemberFromMobileGroup(MobileGroupMobile mobileGroupMobile) throws Exception;

    List<MobileGroupMobile> getMembersOfMobileGroup(Long mobileGroupId) throws Exception;

    boolean processNextSms() throws AppException;

    boolean sendNextSms() throws AppException;

    List<SmsTemplate> getAllSmsTemplates() throws AppException;

    void sendUsernamePasswordSms(Phone phone, String userName, String password) throws Exception;
}
