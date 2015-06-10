package com.aristotle.core.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserPersonalDetailBean {

    private String name;

    private String gender;
    @JsonFormat(pattern = "dd-MM-yyyy")
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

    private String countryCode;
    private String mobileNumber;

    private String passportNumber;

    private String identityNumber;

    private String identityType;

    private String fatherName;

    private String motherName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

}
