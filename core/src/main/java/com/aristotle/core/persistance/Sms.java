package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sms")
public class Sms extends BaseEntity {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "planned_sms_id")
    private PlannedSms plannedSms;
    @Column(name = "planned_sms_id", insertable = false, updatable = false)
    private Long plannedSmsId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "phone_id")
    private Phone phone;
    @Column(name = "phone_id", insertable = false, updatable = false)
    private Long phoneId;

    @Column(name = "status")
    private String status;

    @Column(name = "error_message")
    private String errorMessage;

    public PlannedSms getPlannedSms() {
        return plannedSms;
    }

    public void setPlannedSms(PlannedSms plannedSms) {
        this.plannedSms = plannedSms;
    }

    public Long getPlannedSmsId() {
        return plannedSmsId;
    }

    public void setPlannedSmsId(Long plannedSmsId) {
        this.plannedSmsId = plannedSmsId;
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

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
