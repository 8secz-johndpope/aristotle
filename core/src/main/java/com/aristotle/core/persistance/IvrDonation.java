package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "IvrDonation")
public class IvrDonation extends Donation {

    @Column(name = "upid")
    private String upid;
    @Column(name = "admin_upid")
    private String adminUpid;
    @Column(name = "admin_mobile_number")
    private String adminMobileNumber;
    @Column(name = "sms_message")
    private String smsMessage;

    public String getUpid() {
        return upid;
    }

    public void setUpid(String upid) {
        this.upid = upid;
    }

    public String getAdminUpid() {
        return adminUpid;
    }

    public void setAdminUpid(String adminUpid) {
        this.adminUpid = adminUpid;
    }

    public String getAdminMobileNumber() {
        return adminMobileNumber;
    }

    public void setAdminMobileNumber(String adminMobileNumber) {
        this.adminMobileNumber = adminMobileNumber;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }
}
