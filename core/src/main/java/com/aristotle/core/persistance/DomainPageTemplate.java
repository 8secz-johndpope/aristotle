package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "domain_page_template")
public class DomainPageTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	@Column(name = "ver")
	private int ver;
	@Column(name = "date_created")
	private Date dateCreated;
	@Column(name = "date_modified")
	private Date dateModified;
	@Column(name = "creator_id")
	private Long creatorId;
	@Column(name = "modifier_id")
	private Long modifierId;

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "domain_template_id")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;

    // No need to keep Many to One Mapping
    @Column(name = "page_template_id")
    private Long pageTemplateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public DomainTemplate getDomainTemplate() {
        return domainTemplate;
    }

    public void setDomainTemplate(DomainTemplate domainTemplate) {
        this.domainTemplate = domainTemplate;
    }

    public Long getDomainTemplateId() {
        return domainTemplateId;
    }

    public void setDomainTemplateId(Long domainTemplateId) {
        this.domainTemplateId = domainTemplateId;
    }

    public Long getPageTemplateId() {
        return pageTemplateId;
    }

    public void setPageTemplateId(Long pageTemplateId) {
        this.pageTemplateId = pageTemplateId;
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(UrlMapping urlMapping) {
        this.urlMapping = urlMapping;
    }

    public Long getUrlMappingId() {
        return urlMappingId;
    }

    public void setUrlMappingId(Long urlMappingId) {
        this.urlMappingId = urlMappingId;
    }

	
}
