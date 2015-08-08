package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "call_campaign")
public class CallCampaign extends BaseEntity {

    @Column(name = "welcome_message", columnDefinition = "LONGTEXT")
    private String welcomeMessage;

    @Column(name = "call_script", columnDefinition = "LONGTEXT")
    private String call_script;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCall_script() {
        return call_script;
    }

    public void setCall_script(String call_script) {
        this.call_script = call_script;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
