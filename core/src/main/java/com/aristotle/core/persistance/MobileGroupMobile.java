package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_group_mobile")
public class MobileGroupMobile extends BaseEntity {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "mobile_group_id")
    private MobileGroup mobileGroup;
    @Column(name = "mobile_group_id", insertable = false, updatable = false)
    private Long mobileGroupId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "phone_id")
    private Phone phone;
    @Column(name = "phone_id", insertable = false, updatable = false)
    private Long phoneId;

    public MobileGroup getMobileGroup() {
        return mobileGroup;
    }

    public void setMobileGroup(MobileGroup mobileGroup) {
        this.mobileGroup = mobileGroup;
    }

    public Long getMobileGroupId() {
        return mobileGroupId;
    }

    public void setMobileGroupId(Long mobileGroupId) {
        this.mobileGroupId = mobileGroupId;
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

}
