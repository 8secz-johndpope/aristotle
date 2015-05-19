package com.aristotle.core.persistance;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "url_mapping")
public class UrlMapping extends BaseEntity {

    @Column(name = "url_pattern")
    private String urlPattern;

    @Column(name = "active")
    private boolean active;

    @Column(name = "secured")
    private boolean secured;

    @OneToMany(mappedBy = "urlMapping", fetch = FetchType.LAZY)
    private List<UrlMappingPlugin> urlMappingPlugins;

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

    public List<UrlMappingPlugin> getUrlMappingPlugins() {
        return urlMappingPlugins;
    }

    public void setUrlMappingPlugins(List<UrlMappingPlugin> urlMappingPlugins) {
        this.urlMappingPlugins = urlMappingPlugins;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    @Override
    public String toString() {
        return "UrlMapping [urlPattern=" + urlPattern + ", active=" + active + ", id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId="
                + creatorId + ", modifierId=" + modifierId + "]";
    }

}
