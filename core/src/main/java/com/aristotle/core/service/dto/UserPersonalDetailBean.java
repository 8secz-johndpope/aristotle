package com.aristotle.core.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserPersonalDetailBean {

    private String name;

    private String gender;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    private boolean nri;

    private Long editUserNriCountryId;

    private Long editUserNriCountryRegionId;

    private Long editUserNriCountryRegionAreaId;

    private Long editUserStateLivingId;

    private Long editUserDistrictLivingId;

    private Long editUserAssemblyConstituencyLivingId;

    private Long editUserParliamentConstituencyLivingId;

    private Long editUserStateVotingId;

    private Long editUserDistrictVotingId;

    private Long editUserAssemblyConstituencyVotingId;

    private Long editUserParliamentConstituencyVotingId;

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

    public Long getEditUserNriCountryId() {
        return editUserNriCountryId;
    }

    public void setEditUserNriCountryId(Long editUserNriCountryId) {
        this.editUserNriCountryId = editUserNriCountryId;
    }

    public Long getEditUserNriCountryRegionId() {
        return editUserNriCountryRegionId;
    }

    public void setEditUserNriCountryRegionId(Long editUserNriCountryRegionId) {
        this.editUserNriCountryRegionId = editUserNriCountryRegionId;
    }

    public Long getEditUserNriCountryRegionAreaId() {
        return editUserNriCountryRegionAreaId;
    }

    public void setEditUserNriCountryRegionAreaId(Long editUserNriCountryRegionAreaId) {
        this.editUserNriCountryRegionAreaId = editUserNriCountryRegionAreaId;
    }

    public Long getEditUserStateLivingId() {
        return editUserStateLivingId;
    }

    public void setEditUserStateLivingId(Long editUserStateLivingId) {
        this.editUserStateLivingId = editUserStateLivingId;
    }

    public Long getEditUserDistrictLivingId() {
        return editUserDistrictLivingId;
    }

    public void setEditUserDistrictLivingId(Long editUserDistrictLivingId) {
        this.editUserDistrictLivingId = editUserDistrictLivingId;
    }

    public Long getEditUserAssemblyConstituencyLivingId() {
        return editUserAssemblyConstituencyLivingId;
    }

    public void setEditUserAssemblyConstituencyLivingId(Long editUserAssemblyConstituencyLivingId) {
        this.editUserAssemblyConstituencyLivingId = editUserAssemblyConstituencyLivingId;
    }

    public Long getEditUserParliamentConstituencyLivingId() {
        return editUserParliamentConstituencyLivingId;
    }

    public void setEditUserParliamentConstituencyLivingId(Long editUserParliamentConstituencyLivingId) {
        this.editUserParliamentConstituencyLivingId = editUserParliamentConstituencyLivingId;
    }

    public Long getEditUserStateVotingId() {
        return editUserStateVotingId;
    }

    public void setEditUserStateVotingId(Long editUserStateVotingId) {
        this.editUserStateVotingId = editUserStateVotingId;
    }

    public Long getEditUserDistrictVotingId() {
        return editUserDistrictVotingId;
    }

    public void setEditUserDistrictVotingId(Long editUserDistrictVotingId) {
        this.editUserDistrictVotingId = editUserDistrictVotingId;
    }

    public Long getEditUserAssemblyConstituencyVotingId() {
        return editUserAssemblyConstituencyVotingId;
    }

    public void setEditUserAssemblyConstituencyVotingId(Long editUserAssemblyConstituencyVotingId) {
        this.editUserAssemblyConstituencyVotingId = editUserAssemblyConstituencyVotingId;
    }

    public Long getEditUserParliamentConstituencyVotingId() {
        return editUserParliamentConstituencyVotingId;
    }

    public void setEditUserParliamentConstituencyVotingId(Long editUserParliamentConstituencyVotingId) {
        this.editUserParliamentConstituencyVotingId = editUserParliamentConstituencyVotingId;
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

    @Override
    public String toString() {
        return "UserPersonalDetailBean [name=" + name + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", nri=" + nri + ", editUserNriCountryId=" + editUserNriCountryId
                + ", editUserNriCountryRegionId=" + editUserNriCountryRegionId + ", editUserNriCountryRegionAreaId=" + editUserNriCountryRegionAreaId + ", editUserStateLivingId="
                + editUserStateLivingId + ", editUserDistrictLivingId=" + editUserDistrictLivingId + ", editUserAssemblyConstituencyLivingId=" + editUserAssemblyConstituencyLivingId
                + ", editUserParliamentConstituencyLivingId=" + editUserParliamentConstituencyLivingId + ", editUserStateVotingId=" + editUserStateVotingId + ", editUserDistrictVotingId="
                + editUserDistrictVotingId + ", editUserAssemblyConstituencyVotingId=" + editUserAssemblyConstituencyVotingId + ", editUserParliamentConstituencyVotingId="
                + editUserParliamentConstituencyVotingId + ", identityNumber=" + identityNumber + ", identityType=" + identityType + ", fatherName=" + fatherName + ", motherName=" + motherName + "]";
    }

}
