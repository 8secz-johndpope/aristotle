package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "domain_page_template")
public class DomainPageTemplate extends BaseEntity {

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @Column(name = "html_content_draft", columnDefinition = "LONGTEXT")
    private String htmlContentDraft;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_template_id")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "url_mapping_id", nullable = true)
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;

    // No need to keep Many to One Mapping
    @Column(name = "page_template_id")
    private Long pageTemplateId;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getHtmlContentDraft() {
        return htmlContentDraft;
    }

    public void setHtmlContentDraft(String htmlContentDraft) {
        this.htmlContentDraft = htmlContentDraft;
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

    public Long getPageTemplateId() {
        return pageTemplateId;
    }

    public void setPageTemplateId(Long pageTemplateId) {
        this.pageTemplateId = pageTemplateId;
    }

    @Override
    public String toString() {
        return "DomainPageTemplate [htmlContent=" + htmlContent + ", domainTemplateId=" + domainTemplateId + ", urlMappingId=" + urlMappingId + ", pageTemplateId=" + pageTemplateId + ", id=" + id
                + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId + "]";
    }

}
