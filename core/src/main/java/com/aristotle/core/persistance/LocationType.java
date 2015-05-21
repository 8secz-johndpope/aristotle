package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location_type")
public class LocationType extends BaseEntity {


    @Column(name="name", nullable = false)
    private String name;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="parent_type_id")
    private LocationType parentLocationType;
    @Column(name = "parent_type_id", insertable = false, updatable = false)
    private Long parentTypeId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocationType getParentLocationType() {
        return parentLocationType;
    }
    public void setParentLocationType(LocationType parentLocationType) {
        this.parentLocationType = parentLocationType;
    }
    public Long getParentTypeId() {
        return parentTypeId;
    }
    public void setParentTypeId(Long parentTypeId) {
        this.parentTypeId = parentTypeId;
    }
    @Override
    public String toString() {
        return "LocationType [id=" + id + ", name=" + name
                + ", parentLocationType=" + parentLocationType
                + ", parentTypeId=" + parentTypeId + "]";
    }
    
    
}
