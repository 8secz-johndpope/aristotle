package com.aristotle.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.MobileGroupMobile;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.Sms;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamMember;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.GroupPlannedSmsRepository;
import com.aristotle.core.persistance.repo.LocationPlannedSmsRepository;
import com.aristotle.core.persistance.repo.MobileGroupMobileRepository;
import com.aristotle.core.persistance.repo.MobileGroupRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.PlannedSmsRepository;
import com.aristotle.core.persistance.repo.SmsRepository;
import com.aristotle.core.persistance.repo.TeamMemberRepository;
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
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private SmsRepository smsRepository;


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
            phone = getUserPhone(user.getId());
            if (phone == null) {
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

    private Phone getUserPhone(Long userId) {
        List<Phone> phoneList = phoneRepository.getPhonesByUserId(userId);
        if (phoneList != null && !phoneList.isEmpty()) {
            return phoneList.get(0);
        }
        return null;

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

    @Override
    public boolean processNextSms() throws AppException {
        List<PlannedSms> plannedSms = plannedSmsRepository.getAllPendingPlannedSms();
        if (plannedSms == null || plannedSms.isEmpty()) {
            return false;
        }
        PlannedSms onePlannedSms = plannedSms.get(0);
        switch (onePlannedSms.getTargetType()) {
        case "TeamSms":
            processTeamSms(onePlannedSms);
            break;
        case "GroupSms":
            processMobileGroupSms(onePlannedSms);
            break;
        case "LocationSms":
            System.out.println("Not implemented yet");
            break;

        default:
            System.out.println("Invalid SMS Type");
            break;
        }
        onePlannedSms.setStatus(PlannedPostStatus.PROCESSING);
        onePlannedSms = plannedSmsRepository.save(onePlannedSms);
        return true;
    }

    private void processTeamSms(PlannedSms plannedSms) {
        TeamPlannedSms teamPlannedSms = (TeamPlannedSms) plannedSms;
        Team team = teamPlannedSms.getTeam();
        List<TeamMember> teamMembers = teamMemberRepository.getTeamMembersByTeamId(team.getId());
        if (teamMembers == null || teamMembers.isEmpty()) {
            plannedSms.setTotalMembers(0);
            plannedSms.setTotalScheduled(0);
            plannedSms.setTotalSuccess(0);
        }
        Phone phone;
        plannedSms.setTotalMembers(teamMembers.size());
        int totalSchedule = 0;
        for (TeamMember oneTeamMember : teamMembers) {
            phone = getUserPhone(oneTeamMember.getUser().getId());
            if (phone == null) {
                System.out.println("User " + oneTeamMember.getUser().getName() + " do not have any phone");
                continue;
            }
            Sms sms = new Sms();
            sms.setPlannedSms(teamPlannedSms);
            sms.setUser(oneTeamMember.getUser());
            sms.setPhone(phone);
            sms.setStatus("PENDING");
            sms = smsRepository.save(sms);
            totalSchedule++;
        }
        plannedSms.setTotalScheduled(totalSchedule);
    }

    private void processMobileGroupSms(PlannedSms plannedSms) {
        GroupPlannedSms groupPlannedSms = (GroupPlannedSms) plannedSms;
        MobileGroup mobileGroup = groupPlannedSms.getMobileGroup();
        List<MobileGroupMobile> teamMembers = mobileGroupMobileRepository.getMobileGroupMobileByMobileGroupId(mobileGroup.getId());
        if (teamMembers == null || teamMembers.isEmpty()) {
            plannedSms.setTotalMembers(0);
            plannedSms.setTotalScheduled(0);
            plannedSms.setTotalSuccess(0);
        }
        Phone phone;
        plannedSms.setTotalMembers(teamMembers.size());
        int totalSchedule = 0;
        for (MobileGroupMobile oneTeamMember : teamMembers) {
            phone = oneTeamMember.getPhone();
            Sms sms = new Sms();
            sms.setPlannedSms(groupPlannedSms);
            sms.setUser(phone.getUser());
            sms.setPhone(phone);
            sms.setStatus("PENDING");
            sms = smsRepository.save(sms);
            totalSchedule++;
        }
        plannedSms.setTotalScheduled(totalSchedule);
    }

}
