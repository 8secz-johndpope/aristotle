package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="phone")
public class Phone extends BaseEntity {

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "country_code", nullable = false)
	private String countryCode;

	@Column(name = "phone_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PhoneType phoneType;
	
    @Column(name = "confirmed", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean confirmed;

	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;
	
	public enum PhoneType{
		MOBILE,
		LANDLINE,
		NRI_MOBILE
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public PhoneType getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public String toString() {
        return "Phone [phoneNumber=" + phoneNumber + ", countryCode=" + countryCode + ", phoneType=" + phoneType + ", confirmed=" + confirmed + ", userId=" + userId + ", id=" + id + "]";
    }



}
