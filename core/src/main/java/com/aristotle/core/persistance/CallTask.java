package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "call_task")
public class CallTask extends BaseEntity {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "call_campaing_id")
    private CallCampaign callCampaign;
    @Column(name = "call_campaing_id", insertable = false, updatable = false)
    private Long callCampaignId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "caller_id")
    private Caller caller;
    @Column(name = "caller_id", insertable = false, updatable = false)
    private Long callerId;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "call_start_time")
    private Date callStartTime;

    @Column(name = "call_end_time")
    private Date callEndTime;

    public CallCampaign getCallCampaign() {
        return callCampaign;
    }

    public void setCallCampaign(CallCampaign callCampaign) {
        this.callCampaign = callCampaign;
    }

    public Long getCallCampaignId() {
        return callCampaignId;
    }

    public void setCallCampaignId(Long callCampaignId) {
        this.callCampaignId = callCampaignId;
    }

    public Caller getCaller() {
        return caller;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }

    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Date getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Date callEndTime) {
        this.callEndTime = callEndTime;
    }

}
