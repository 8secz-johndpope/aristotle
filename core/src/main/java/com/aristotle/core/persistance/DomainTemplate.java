package com.aristotle.core.persistance;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "domain_template")
public class DomainTemplate extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_id")
    private Domain domain;
    @Column(name = "domain_id", insertable = false, updatable = false)
    private Long domainId;

    // No need to keep ManyToOne Mapping
    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "domainTemplate", fetch = FetchType.EAGER)
    private List<DomainPageTemplate> domainPageTemplates;

    @OneToMany(mappedBy = "domainTemplate", fetch = FetchType.EAGER)
    private List<DomainTemplateFile> domainTemplateFiles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<DomainPageTemplate> getDomainPageTemplates() {
        return domainPageTemplates;
    }

    public void setDomainPageTemplates(List<DomainPageTemplate> domainPageTemplates) {
        this.domainPageTemplates = domainPageTemplates;
    }

    public List<DomainTemplateFile> getDomainTemplateFiles() {
        return domainTemplateFiles;
    }

    public void setDomainTemplateFiles(List<DomainTemplateFile> domainTemplateFiles) {
        this.domainTemplateFiles = domainTemplateFiles;
    }

}
