package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "domain_template_part")
public class DomainTemplatePart extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_template_id")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

}
