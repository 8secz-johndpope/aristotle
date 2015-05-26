package com.aristotle.core.service.dto;

import java.util.Date;

public class UserRegisterBean {
	
	private String name;
	private String fatherName;
	private String motherName;
	private String address;
	private String gender;
	private Date dateOfBirth;
	private boolean nri;
	private Long nriCountryId;
	private Long nriCountryRegionId;
	private Long nriCountryRegionAreaId;
	
	private Long stateLivingId;
	private Long districtLivingId;
	private Long assemblyConstituencyLivingId;
	private Long parliamentConstituencyLivingId;
	private Long stateVotingId;
	private Long districtVotingId;
	private Long assemblyConstituencyVotingId;
	private Long parliamentConstituencyVotingId;
    private String email;

    private Long[] interests;

	private boolean member;
	
	private boolean volunteer;

	private String passportNumber;

    private String identityNumber;

    private String identityType;

    private String education;
    private String professionalBackground;
    private String domainExpertise;
    private String offences;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactNo;

    private boolean pastVolunteer;
    private String pastOrganisation;
    private boolean knowExistingMember;
    private String existingMemberName;
    private String existingMemberEmail;
    private String existingMemberMobile;
    private String existingMemberCountryCode;

    private String mobileNumber;
    private String countryCode;

    private String nriMobileNumber;
    private String nriCountryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isNri() {
        return nri;
    }

    public void setNri(boolean nri) {
        this.nri = nri;
    }

    public Long getNriCountryId() {
        return nriCountryId;
    }

    public void setNriCountryId(Long nriCountryId) {
        this.nriCountryId = nriCountryId;
    }

    public Long getNriCountryRegionId() {
        return nriCountryRegionId;
    }

    public void setNriCountryRegionId(Long nriCountryRegionId) {
        this.nriCountryRegionId = nriCountryRegionId;
    }

    public Long getNriCountryRegionAreaId() {
        return nriCountryRegionAreaId;
    }

    public void setNriCountryRegionAreaId(Long nriCountryRegionAreaId) {
        this.nriCountryRegionAreaId = nriCountryRegionAreaId;
    }

    public Long getStateLivingId() {
        return stateLivingId;
    }

    public void setStateLivingId(Long stateLivingId) {
        this.stateLivingId = stateLivingId;
    }

    public Long getDistrictLivingId() {
        return districtLivingId;
    }

    public void setDistrictLivingId(Long districtLivingId) {
        this.districtLivingId = districtLivingId;
    }

    public Long getAssemblyConstituencyLivingId() {
        return assemblyConstituencyLivingId;
    }

    public void setAssemblyConstituencyLivingId(Long assemblyConstituencyLivingId) {
        this.assemblyConstituencyLivingId = assemblyConstituencyLivingId;
    }

    public Long getParliamentConstituencyLivingId() {
        return parliamentConstituencyLivingId;
    }

    public void setParliamentConstituencyLivingId(Long parliamentConstituencyLivingId) {
        this.parliamentConstituencyLivingId = parliamentConstituencyLivingId;
    }

    public Long getStateVotingId() {
        return stateVotingId;
    }

    public void setStateVotingId(Long stateVotingId) {
        this.stateVotingId = stateVotingId;
    }

    public Long getDistrictVotingId() {
        return districtVotingId;
    }

    public void setDistrictVotingId(Long districtVotingId) {
        this.districtVotingId = districtVotingId;
    }

    public Long getAssemblyConstituencyVotingId() {
        return assemblyConstituencyVotingId;
    }

    public void setAssemblyConstituencyVotingId(Long assemblyConstituencyVotingId) {
        this.assemblyConstituencyVotingId = assemblyConstituencyVotingId;
    }

    public Long getParliamentConstituencyVotingId() {
        return parliamentConstituencyVotingId;
    }

    public void setParliamentConstituencyVotingId(Long parliamentConstituencyVotingId) {
        this.parliamentConstituencyVotingId = parliamentConstituencyVotingId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long[] getInterests() {
        return interests;
    }

    public void setInterests(Long[] interests) {
        this.interests = interests;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public boolean isVolunteer() {
        return volunteer;
    }

    public void setVolunteer(boolean volunteer) {
        this.volunteer = volunteer;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfessionalBackground() {
        return professionalBackground;
    }

    public void setProfessionalBackground(String professionalBackground) {
        this.professionalBackground = professionalBackground;
    }

    public String getDomainExpertise() {
        return domainExpertise;
    }

    public void setDomainExpertise(String domainExpertise) {
        this.domainExpertise = domainExpertise;
    }

    public String getOffences() {
        return offences;
    }

    public void setOffences(String offences) {
        this.offences = offences;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public boolean isPastVolunteer() {
        return pastVolunteer;
    }

    public void setPastVolunteer(boolean pastVolunteer) {
        this.pastVolunteer = pastVolunteer;
    }

    public String getPastOrganisation() {
        return pastOrganisation;
    }

    public void setPastOrganisation(String pastOrganisation) {
        this.pastOrganisation = pastOrganisation;
    }

    public boolean isKnowExistingMember() {
        return knowExistingMember;
    }

    public void setKnowExistingMember(boolean knowExistingMember) {
        this.knowExistingMember = knowExistingMember;
    }

    public String getExistingMemberName() {
        return existingMemberName;
    }

    public void setExistingMemberName(String existingMemberName) {
        this.existingMemberName = existingMemberName;
    }

    public String getExistingMemberEmail() {
        return existingMemberEmail;
    }

    public void setExistingMemberEmail(String existingMemberEmail) {
        this.existingMemberEmail = existingMemberEmail;
    }

    public String getExistingMemberMobile() {
        return existingMemberMobile;
    }

    public void setExistingMemberMobile(String existingMemberMobile) {
        this.existingMemberMobile = existingMemberMobile;
    }

    public String getExistingMemberCountryCode() {
        return existingMemberCountryCode;
    }

    public void setExistingMemberCountryCode(String existingMemberCountryCode) {
        this.existingMemberCountryCode = existingMemberCountryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNriMobileNumber() {
        return nriMobileNumber;
    }

    public void setNriMobileNumber(String nriMobileNumber) {
        this.nriMobileNumber = nriMobileNumber;
    }

    public String getNriCountryCode() {
        return nriCountryCode;
    }

    public void setNriCountryCode(String nriCountryCode) {
        this.nriCountryCode = nriCountryCode;
    }

}
