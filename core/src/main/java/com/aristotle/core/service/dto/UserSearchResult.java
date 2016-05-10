package com.aristotle.core.service.dto;

import java.util.Date;
import java.util.Set;

import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Volunteer;

public class UserSearchResult {

    private Long id;
    private String membershipNumber;

    private String name;

    private String fatherName;

    private String motherName;

    private String address;

    private String gender;

    private Date dateOfBirth;

    private boolean nri;

    private String profilePic;

    private String passportNumber;

    private String identityNumber;
    
    private String referenceMobileNumber;

    private String identityType;
    private String mobileNumber;
    private String email;

    private Set<Interest> volunteerInterests;
    private Volunteer volunteerRecord;

    private Location livingState;
    private Location livingDistrict;
    private Location livingAssemblyConstituency;
    private Location livingParliamentConstituency;

    private Location votingState;
    private Location votingDistrict;
    private Location votingAssemblyConstituency;
    private Location votingParliamentConstituency;

    private Location nriCountry;
    private Location nriCountryRegion;
    private Location nriCountryRegionArea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Interest> getVolunteerInterests() {
        return volunteerInterests;
    }

    public void setVolunteerInterests(Set<Interest> volunteerInterests) {
        this.volunteerInterests = volunteerInterests;
    }

    public Volunteer getVolunteerRecord() {
        return volunteerRecord;
    }

    public void setVolunteerRecord(Volunteer volunteerRecord) {
        this.volunteerRecord = volunteerRecord;
    }

    public Location getLivingState() {
        return livingState;
    }

    public void setLivingState(Location livingState) {
        this.livingState = livingState;
    }

    public Location getLivingDistrict() {
        return livingDistrict;
    }

    public void setLivingDistrict(Location livingDistrict) {
        this.livingDistrict = livingDistrict;
    }

    public Location getLivingAssemblyConstituency() {
        return livingAssemblyConstituency;
    }

    public void setLivingAssemblyConstituency(Location livingAssemblyConstituency) {
        this.livingAssemblyConstituency = livingAssemblyConstituency;
    }

    public Location getLivingParliamentConstituency() {
        return livingParliamentConstituency;
    }

    public void setLivingParliamentConstituency(Location livingParliamentConstituency) {
        this.livingParliamentConstituency = livingParliamentConstituency;
    }

    public Location getVotingState() {
        return votingState;
    }

    public void setVotingState(Location votingState) {
        this.votingState = votingState;
    }

    public Location getVotingDistrict() {
        return votingDistrict;
    }

    public void setVotingDistrict(Location votingDistrict) {
        this.votingDistrict = votingDistrict;
    }

    public Location getVotingAssemblyConstituency() {
        return votingAssemblyConstituency;
    }

    public void setVotingAssemblyConstituency(Location votingAssemblyConstituency) {
        this.votingAssemblyConstituency = votingAssemblyConstituency;
    }

    public Location getVotingParliamentConstituency() {
        return votingParliamentConstituency;
    }

    public void setVotingParliamentConstituency(Location votingParliamentConstituency) {
        this.votingParliamentConstituency = votingParliamentConstituency;
    }

    public Location getNriCountry() {
        return nriCountry;
    }

    public void setNriCountry(Location nriCountry) {
        this.nriCountry = nriCountry;
    }

    public Location getNriCountryRegion() {
        return nriCountryRegion;
    }

    public void setNriCountryRegion(Location nriCountryRegion) {
        this.nriCountryRegion = nriCountryRegion;
    }

    public Location getNriCountryRegionArea() {
        return nriCountryRegionArea;
    }

    public void setNriCountryRegionArea(Location nriCountryRegionArea) {
        this.nriCountryRegionArea = nriCountryRegionArea;
    }

	public String getReferenceMobileNumber() {
		return referenceMobileNumber;
	}

	public void setReferenceMobileNumber(String referenceMobileNumber) {
		this.referenceMobileNumber = referenceMobileNumber;
	}

}
