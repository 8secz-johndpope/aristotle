package com.aristotle.core.persistance;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="volunteer")
public class Volunteer extends BaseEntity {

	@Column(name = "education")
	private String education;
	@Column(name = "professional_background", length=512)
	private String professionalBackground;
	@Column(name = "domain_expertise")
	private String domainExpertise;
	@Column(name = "offences", length=512)
	private String offences;
	@Column(name = "emergency_contact_name")
	private String emergencyContactName;
	@Column(name = "emergency_contact_relation")
	private String emergencyContactRelation;
	@Column(name = "emergency_contact_no")
	private String emergencyContactNo;
    @Column(name = "emergency_contact_no_country_code")
    private String emergencyContactCountryCode;
    @Column(name = "emergency_contact_no_country_iso2")
    private String emergencyContactCountryIso2;
	@Column(name = "info_recorded_by")
	private String infoRecordedBy;
	@Column(name = "info_recorded_at")
	private String infoRecordedAt;
    @Column(name = "past_volunteer")
    private boolean pastVolunteer;
    @Column(name = "past_organisation", length = 128)
    private String pastOrganisation;
    @Column(name = "know_existing_member")
    private boolean knowExistingMember;
    @Column(name = "existing_member_name", length = 64)
    private String existingMemberName;
    @Column(name = "existing_member_email", length = 64)
    private String existingMemberEmail;
    @Column(name = "existing_member_mobile", length = 16)
    private String existingMemberMobile;
    @Column(name = "existing_member_country_code", length = 8)
    private String existingMemberCountryCode;
    @Column(name = "existing_member_country_iso2", length = 8)
    private String existingMemberCountryIso2;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name = "volunteer_interests",
	joinColumns = {
	@JoinColumn(name="volunteer_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="interest_id")
	})
	Set<Interest> interests;
	
	
	@Override
    public Long getId() {
		return id;
	}
	@Override
    public void setId(Long id) {
		this.id = id;
	}
	@Override
    public int getVer() {
		return ver;
	}
	@Override
    public void setVer(int ver) {
		this.ver = ver;
	}
	@Override
    public Date getDateCreated() {
		return dateCreated;
	}
	@Override
    public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@Override
    public Date getDateModified() {
		return dateModified;
	}
	@Override
    public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	@Override
    public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	@Override
    public Long getModifierId() {
		return modifierId;
	}
	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
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
	public String getInfoRecordedBy() {
		return infoRecordedBy;
	}
	public void setInfoRecordedBy(String infoRecordedBy) {
		this.infoRecordedBy = infoRecordedBy;
	}
	public String getInfoRecordedAt() {
		return infoRecordedAt;
	}
	public void setInfoRecordedAt(String infoRecordedAt) {
		this.infoRecordedAt = infoRecordedAt;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Set<Interest> getInterests() {
		return interests;
	}
	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
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

    public String getEmergencyContactCountryCode() {
        return emergencyContactCountryCode;
    }

    public void setEmergencyContactCountryCode(String emergencyContactCountryCode) {
        this.emergencyContactCountryCode = emergencyContactCountryCode;
    }

    public String getEmergencyContactCountryIso2() {
        return emergencyContactCountryIso2;
    }

    public void setEmergencyContactCountryIso2(String emergencyContactCountryIso2) {
        this.emergencyContactCountryIso2 = emergencyContactCountryIso2;
    }

    public String getExistingMemberCountryIso2() {
        return existingMemberCountryIso2;
    }

    public void setExistingMemberCountryIso2(String existingMemberCountryIso2) {
        this.existingMemberCountryIso2 = existingMemberCountryIso2;
    }
    @Override
    public String toString() {
        return "Volunteer [id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId + ", education="
                + education + ", professionalBackground=" + professionalBackground + ", domainExpertise=" + domainExpertise + ", offences=" + offences + ", emergencyContactName="
                + emergencyContactName + ", emergencyContactRelation=" + emergencyContactRelation + ", emergencyContactNo=" + emergencyContactNo + ", infoRecordedBy=" + infoRecordedBy
                + ", infoRecordedAt=" + infoRecordedAt + ", user=" + user + ", userId=" + userId + ", interests=" + interests + "]";
    }

	
}
