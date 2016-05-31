package com.aristotle.core.persistance;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.aristotle.core.enums.CreationType;

@Entity
@Table(name="users")
public class User extends BaseEntity {
	
    @Column(name = "ivr_state")
    private String ivrState;

    @Column(name = "ivr_district")
    private String ivrDistrict;

    @Column(name = "sms_msg")
    private String smsMessage;

    @Column(name = "membership_no")
	private String membershipNumber;

	@Column(name = "name", nullable = false, length=256)
	private String name;
	
	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "mother_name")
	private String motherName;
	
	@Column(name = "reference_mobile_number")
	private String referenceMobileNumber;

	@Column(name = "address", length=512)
	private String address;

	@Column(name = "gender")
	private String gender;

	@Column(name = "creation_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private CreationType creationType;

	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "nri")
	private boolean nri;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="reference_user_id")
    private User referenceUser;
	@Column(name="reference_user_id", insertable=false,updatable=false)
	private Long referenceUserId;

    @Column(name = "profile", columnDefinition = "LONGTEXT")
    private String profile;

	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	private Set<FacebookAccount> facebookAccounts;

	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	private Set<TwitterAccount> twitterAccounts; 
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	private Set<Donation> donations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Email> emails;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Phone> phones;

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_all_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="role_id")
	})
	Set<Role> allRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_state_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="state_role_id")
	})
	Set<StateRole> stateRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinTable(name = "user_location_roles",
    joinColumns = {
    @JoinColumn(name="user_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name="location_role_id")
    })
    Set<LocationRole> locationRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_district_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="district_role_id")
	})
	Set<DistrictRole> districtRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_ac_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="ac_role_id")
	})
	Set<AcRole> acRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_pc_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="pc_role_id")
	})
	Set<PcRole> pcRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_country_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_role_id")
	})
	Set<CountryRole> countryRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_country_region_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_region_role_id")
	})
	Set<CountryRegionRole> countryRegionRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_country_region_area_roles",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_region_area_role_id")
	})
	Set<CountryRegionAreaRole> countryRegionAreaRoles;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "user_interests",
	joinColumns = {
	@JoinColumn(name="user_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="interest_id")
	})
	Set<Interest> interests;

	@Column(name = "allow_tweets", nullable = false)
	private boolean allowTweets;

	@Column(name = "profile_pic")
	private String profilePic;

	@Column(name = "super_admin", nullable = false)
	private boolean superAdmin;

	@Column(name = "member", nullable = false)
	private boolean member;
	
	@Column(name = "donor", nullable = false)
	private boolean donor;

	@Column(name = "volunteer", nullable = false)
	private boolean volunteer;

    @Column(name = "reindex", nullable = true)
    private boolean reindex;

    @Column(name = "membership_status")
	private String membershipStatus;
	
	@Column(name = "passport_number")
	private String passportNumber;

    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "identity_type")
    private String identityType;

	@Column(name = "voter_id")
	private String voterId;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="membership_confirmed_by")
    private User membershipConfirmedBy;
	@Column(name="membership_confirmed_by", insertable=false,updatable=false)
	private Long membershipConfirmedById;
	
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

	public Set<FacebookAccount> getFacebookAccounts() {
		return facebookAccounts;
	}

	public void setFacebookAccounts(Set<FacebookAccount> facebookAccounts) {
		this.facebookAccounts = facebookAccounts;
	}

	public Set<TwitterAccount> getTwitterAccounts() {
		return twitterAccounts;
	}

	public void setTwitterAccounts(Set<TwitterAccount> twitterAccounts) {
		this.twitterAccounts = twitterAccounts;
	}

	public Set<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(Set<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public Set<StateRole> getStateRoles() {
		return stateRoles;
	}

	public void setStateRoles(Set<StateRole> stateRoles) {
		this.stateRoles = stateRoles;
	}

	public Set<DistrictRole> getDistrictRoles() {
		return districtRoles;
	}

	public void setDistrictRoles(Set<DistrictRole> districtRoles) {
		this.districtRoles = districtRoles;
	}

	public Set<AcRole> getAcRoles() {
		return acRoles;
	}

	public void setAcRoles(Set<AcRole> acRoles) {
		this.acRoles = acRoles;
	}

	public Set<PcRole> getPcRoles() {
		return pcRoles;
	}

	public void setPcRoles(Set<PcRole> pcRoles) {
		this.pcRoles = pcRoles;
	}

	public boolean isAllowTweets() {
		return allowTweets;
	}

	public void setAllowTweets(boolean allowTweets) {
		this.allowTweets = allowTweets;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public String getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(String membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public User getMembershipConfirmedBy() {
		return membershipConfirmedBy;
	}

	public void setMembershipConfirmedBy(User membershipConfirmedBy) {
		this.membershipConfirmedBy = membershipConfirmedBy;
	}

	public Long getMembershipConfirmedById() {
		return membershipConfirmedById;
	}

	public void setMembershipConfirmedById(Long membershipConfirmedById) {
		this.membershipConfirmedById = membershipConfirmedById;
	}

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}

	public Set<CountryRole> getCountryRoles() {
		return countryRoles;
	}

	public void setCountryRoles(Set<CountryRole> countryRoles) {
		this.countryRoles = countryRoles;
	}

	public Set<CountryRegionRole> getCountryRegionRoles() {
		return countryRegionRoles;
	}

	public void setCountryRegionRoles(Set<CountryRegionRole> countryRegionRoles) {
		this.countryRegionRoles = countryRegionRoles;
	}

	public Set<CountryRegionAreaRole> getCountryRegionAreaRoles() {
		return countryRegionAreaRoles;
	}

	public void setCountryRegionAreaRoles(Set<CountryRegionAreaRole> countryRegionAreaRoles) {
		this.countryRegionAreaRoles = countryRegionAreaRoles;
	}

	public boolean isVolunteer() {
		return volunteer;
	}

	public void setVolunteer(boolean volunteer) {
		this.volunteer = volunteer;
	}

	public boolean isDonor() {
		return donor;
	}

	public void setDonor(boolean donor) {
		this.donor = donor;
	}

	public CreationType getCreationType() {
		return creationType;
	}

	public void setCreationType(CreationType creationType) {
		this.creationType = creationType;
	}

	public Set<Donation> getDonations() {
		return donations;
	}

	public void setDonations(Set<Donation> donations) {
		this.donations = donations;
	}

	public Set<Email> getEmails() {
		return emails;
	}

	public void setEmails(Set<Email> emails) {
		this.emails = emails;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
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

    public Set<LocationRole> getLocationRoles() {
        return locationRoles;
    }

    public void setLocationRoles(Set<LocationRole> locationRoles) {
        this.locationRoles = locationRoles;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getIvrState() {
        return ivrState;
    }

    public void setIvrState(String ivrState) {
        this.ivrState = ivrState;
    }

    public String getIvrDistrict() {
        return ivrDistrict;
    }

    public void setIvrDistrict(String ivrDistrict) {
        this.ivrDistrict = ivrDistrict;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public boolean isReindex() {
        return reindex;
    }

    public void setReindex(boolean reindex) {
        this.reindex = reindex;
    }

    public String getReferenceMobileNumber() {
		return referenceMobileNumber;
	}

	public void setReferenceMobileNumber(String referenceMobileNumber) {
		this.referenceMobileNumber = referenceMobileNumber;
	}

	public Long getReferenceUserId() {
		return referenceUserId;
	}

	public void setReferenceUserId(Long referenceUserId) {
		this.referenceUserId = referenceUserId;
	}

	public User getReferenceUser() {
		return referenceUser;
	}

	public void setReferenceUser(User referenceUser) {
		this.referenceUser = referenceUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
        return "User [id=" + id + ", ver=" + ver
				+ ", name=" + name + ", dateOfBith=" + dateOfBirth + "]";
	}

}
