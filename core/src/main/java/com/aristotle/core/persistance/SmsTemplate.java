package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sms_template")
public class SmsTemplate extends BaseEntity {

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

    @Column(name = "automated")
    private boolean automated;

    @Column(name = "system_name", unique = true)
    private String systemName;

    @Column(name = "message", length = 1024)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isAutomated() {
        return automated;
    }

    public void setAutomated(boolean automated) {
        this.automated = automated;
    }

}
