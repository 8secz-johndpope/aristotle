package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "donations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "donation")
public class Donation extends BaseEntity {

    @Column(name = "donor_ip")
    private String donorIp;
    @Column(name = "donation", updatable = false, insertable = false)
    private String donationype;

    @Column(name = "donation_date")
    private Date donationDate;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "utm_source")
    private String utmSource;
    @Column(name = "utm_medium")
    private String utmMedium;
    @Column(name = "utm_term")
    private String utmTerm;
    @Column(name = "utm_content")
    private String utmContent;
    @Column(name = "utm_campaign")
    private String utmCampaign;
    @Column(name = "remark", columnDefinition = "LONGTEXT")
    private String remark;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} , fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;

	//Following fields are for old records and will not be used anywahere, storing only for future auditin purpose
	@Column(name = "donor_name")
	private String donorName;
	@Column(name = "donor_gender")
	private String donorGender;
	@Column(name = "donor_age")
	private String donorAge;
	@Column(name = "donor_mobile")
	private String donorMobile;
	@Column(name = "donor_email")
	private String donorEmail;
	@Column(name = "donor_country_id")
	private String donorCountryId;
	@Column(name = "donor_state_id")
	private String donorStateId;
	@Column(name = "donor_district_id")
	private String donorDistrictId;
	@Column(name = "donor_address", length=512)
	private String donorAddress;
	@Column(name = "donate_to_state")
	private String donateToState;
	@Column(name = "donate_to_district")
	private String donateToDistrict;
	@Column(name = "donate_to_loksabha")
	private String donateToLoksabha;
    @Column(name = "donate_to_vidhansabha")
    private String donateToVidhansabha;

	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Date getDonationDate() {
		return donationDate;
	}
	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}
	public String getDonorIp() {
		return donorIp;
	}
	public void setDonorIp(String donorIp) {
		this.donorIp = donorIp;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUtmSource() {
		return utmSource;
	}
	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}
	public String getUtmMedium() {
		return utmMedium;
	}
	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}
	public String getUtmTerm() {
		return utmTerm;
	}
	public void setUtmTerm(String utmTerm) {
		this.utmTerm = utmTerm;
	}
	public String getUtmContent() {
		return utmContent;
	}
	public void setUtmContent(String utmContent) {
		this.utmContent = utmContent;
	}
	public String getUtmCampaign() {
		return utmCampaign;
	}
	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	public String getDonorGender() {
		return donorGender;
	}
	public void setDonorGender(String donorGender) {
		this.donorGender = donorGender;
	}
	public String getDonorAge() {
		return donorAge;
	}
	public void setDonorAge(String donorAge) {
		this.donorAge = donorAge;
	}
	public String getDonorMobile() {
		return donorMobile;
	}
	public void setDonorMobile(String donorMobile) {
		this.donorMobile = donorMobile;
	}
	public String getDonorEmail() {
		return donorEmail;
	}
	public void setDonorEmail(String donorEmail) {
		this.donorEmail = donorEmail;
	}
	public String getDonorCountryId() {
		return donorCountryId;
	}
	public void setDonorCountryId(String donorCountryId) {
		this.donorCountryId = donorCountryId;
	}
	public String getDonorStateId() {
		return donorStateId;
	}
	public void setDonorStateId(String donorStateId) {
		this.donorStateId = donorStateId;
	}
	public String getDonorDistrictId() {
		return donorDistrictId;
	}
	public void setDonorDistrictId(String donorDistrictId) {
		this.donorDistrictId = donorDistrictId;
	}
	public String getDonorAddress() {
		return donorAddress;
	}
	public void setDonorAddress(String donorAddress) {
		this.donorAddress = donorAddress;
	}
	public String getDonateToState() {
		return donateToState;
	}
	public void setDonateToState(String donateToState) {
		this.donateToState = donateToState;
	}
	public String getDonateToDistrict() {
		return donateToDistrict;
	}
	public void setDonateToDistrict(String donateToDistrict) {
		this.donateToDistrict = donateToDistrict;
	}
	public String getDonateToLoksabha() {
		return donateToLoksabha;
	}
	public void setDonateToLoksabha(String donateToLoksabha) {
		this.donateToLoksabha = donateToLoksabha;
	}

    public String getDonateToVidhansabha() {
        return donateToVidhansabha;
    }

    public void setDonateToVidhansabha(String donateToVidhansabha) {
        this.donateToVidhansabha = donateToVidhansabha;
    }

    public String getDonationype() {
        return donationype;
    }

	
	
}
