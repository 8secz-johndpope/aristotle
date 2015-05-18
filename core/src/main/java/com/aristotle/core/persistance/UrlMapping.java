package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "url_mapping")
public class UrlMapping extends BaseEntity {

    @Column(name = "url_pattern")
    private String urlPattern;

    @Column(name = "active")
    private boolean active;

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
