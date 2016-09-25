package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "domain_template_file")
public class DomainTemplateFile extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "file_type")
    private String fileType;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.LAZY)
    @JoinColumn(name = "domain_template_id")
    private DomainTemplate domainTemplate;
    @Column(name = "domain_template_id", insertable = false, updatable = false)
    private Long domainTemplateId;

    @Column(name = "template_file_id")
    private Long templateFileId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public Long getTemplateFileId() {
        return templateFileId;
    }

    public void setTemplateFileId(Long templateFileId) {
        this.templateFileId = templateFileId;
    }


}
