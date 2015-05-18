package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Version
    @Column(name = "ver")
    protected int ver;
    @Column(name = "date_created")
    @CreatedDate
    protected Date dateCreated;
    @Column(name = "date_modified")
    @LastModifiedDate
    protected Date dateModified;
    @Column(name = "creator_id")
    @CreatedBy
    protected Long creatorId;
    @Column(name = "modifier_id")
    @LastModifiedBy
    protected Long modifierId;

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

    public Date getDateModified() {
        return dateModified;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }
}
