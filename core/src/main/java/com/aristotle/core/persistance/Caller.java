package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "call_campaing_id")
    private CallCampaign callCampaign;
    @Column(name = "call_campaing_id", insertable = false, updatable = false)
    private Long callCampaignId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "status")
    private String status;

    @Column(name = "approved_by")
    private Long approvedById;

    @Column(name = "join_date")
    private Date joinDate;

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

}
