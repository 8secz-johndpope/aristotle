package com.aristotle.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.MobileGroupMobile;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.GroupPlannedSmsRepository;
import com.aristotle.core.persistance.repo.LocationPlannedSmsRepository;
import com.aristotle.core.persistance.repo.MobileGroupMobileRepository;
import com.aristotle.core.persistance.repo.MobileGroupRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.PlannedSmsRepository;
import com.aristotle.core.persistance.repo.TeamPlannedSmsRepository;
import com.aristotle.core.persistance.repo.TeamRepository;

@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    @Autowired
    private GroupPlannedSmsRepository groupPlannedSmsRepository;
    @Autowired
    private TeamPlannedSmsRepository teamPlannedSmsRepository;
    @Autowired
    private LocationPlannedSmsRepository locationPlannedSmsRepository;
    @Autowired
    private PlannedSmsRepository plannedSmsRepository;
    @Autowired
    private MobileGroupRepository mobileGroupRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private MobileGroupMobileRepository mobileGroupMobileRepository;


    @Override
    public GroupPlannedSms saveGroupPlannedSms(GroupPlannedSms groupPlannedSms) throws AppException {
        if (groupPlannedSms.getMobileGroup() == null) {
            throw new AppException("Please select a Group");
        }
        MobileGroup mobileGroup = mobileGroupRepository.findOne(groupPlannedSms.getMobileGroup().getId());
        if (mobileGroup == null) {
            throw new AppException("Please select a Group");
        }
        groupPlannedSms.setMobileGroup(mobileGroup);
        groupPlannedSms = groupPlannedSmsRepository.save(groupPlannedSms);
        return groupPlannedSms;
    }

    @Override
    public TeamPlannedSms saveTeamPlannedSms(TeamPlannedSms teamPlannedSms) throws AppException {
        if (teamPlannedSms.getTeam() == null) {
            throw new AppException("Please select a team");
        }
        Team team = teamRepository.findOne(teamPlannedSms.getTeam().getId());
        if (team == null) {
            throw new AppException("Please select a team");
        }
        teamPlannedSms.setTeam(team);
        teamPlannedSms = teamPlannedSmsRepository.save(teamPlannedSms);
        return teamPlannedSms;
    }

    @Override
    public LocationPlannedSms saveLocationPlannedSms(LocationPlannedSms locationPlannedSms) throws AppException {
        locationPlannedSms = locationPlannedSmsRepository.save(locationPlannedSms);
        return locationPlannedSms;
    }

    @Override
    public List<PlannedSms> getAllPlannedSms() throws AppException {
        return plannedSmsRepository.getAllPlannedSms();
    }

    @Override
    public List<MobileGroup> getAllMobileGroups() throws AppException {
        return mobileGroupRepository.getMobileGroups();
    }

    @Override
    public MobileGroup saveMobileGroup(MobileGroup mobileGroup) throws Exception {
        mobileGroup = mobileGroupRepository.save(mobileGroup);
        return mobileGroup;
    }

    @Override
    public void addMemberToMobileGroup(MobileGroup mobileGroup, String emailIdOrPhoneNumber) throws Exception {
        Phone phone = null;
        if (isNumber(emailIdOrPhoneNumber)) {
            phone = phoneRepository.getPhoneByPhoneNumber(emailIdOrPhoneNumber);
            if (phone == null) {
                phone = new Phone();
                phone.setConfirmed(false);
                phone.setCountryCode("91");
                phone.setPhoneNumber(emailIdOrPhoneNumber);
                phone.setPhoneType(PhoneType.MOBILE);
                phone = phoneRepository.save(phone);
            }
        } else {
            Email email = emailRepository.getEmailByEmailUp(emailIdOrPhoneNumber.toUpperCase());
            if (email == null) {
                throw new AppException("Either Invalid Mobile Number or This Email doesnot exists in our system");
            }
            User user = email.getUser();
            List<Phone> phoneList = phoneRepository.getPhonesByUserId(user.getId());
            if (phoneList != null && !phoneList.isEmpty()) {
                phone = phoneList.get(0);
            } else {
                throw new AppException("No mobile number exists for this user");
            }
        }


        if (phone == null) {
            throw new AppException("No Registered user found with email/mobile [" + emailIdOrPhoneNumber + "]");
        }

        mobileGroup = mobileGroupRepository.findOne(mobileGroup.getId());
        MobileGroupMobile mobileGroupMobile = new MobileGroupMobile();
        mobileGroupMobile.setPhone(phone);
        mobileGroupMobile.setMobileGroup(mobileGroup);
        
        mobileGroupMobile = mobileGroupMobileRepository.save(mobileGroupMobile);

    }

    private boolean isNumber(String mobile) {
        try {
            Integer.parseInt(mobile);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    @Override
    public void removeMemberFromMobileGroup(MobileGroupMobile mobileGroupMobile) throws Exception {
        mobileGroupMobileRepository.delete(mobileGroupMobile.getId());

    }

    @Override
    public List<MobileGroupMobile> getMembersOfMobileGroup(Long mobileGroupId) throws Exception {
        return mobileGroupMobileRepository.getMobileGroupMobileByMobileGroupId(mobileGroupId);
    }

}
