package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "GroupSms")
public class GroupPlannedSms extends PlannedSms {

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "mobile_group_id")
    private MobileGroup mobileGroup;
    @Column(name = "mobile_group_id", insertable = false, updatable = false)
    private Long mobileGroupId;

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

}
