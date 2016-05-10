package com.aristotle.core.persistance;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "call_campaign")
public class CallCampaign extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "welcome_message", columnDefinition = "LONGTEXT")
    private String welcomeMessage;

    @Column(name = "call_script", columnDefinition = "LONGTEXT")
    private String callScript;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
    
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "call_campaign_uploaded_files",
    joinColumns = {
    @JoinColumn(name="call_campaign_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name = "uploaded_file_id")
    })
    private Set<UploadedFile> files;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCallScript() {
        return callScript;
    }

    public void setCallScript(String callScript) {
        this.callScript = callScript;
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

    public Set<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(Set<UploadedFile> files) {
        this.files = files;
    }

}
