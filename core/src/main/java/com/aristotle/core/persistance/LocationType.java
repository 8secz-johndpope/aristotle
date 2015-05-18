package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location_type")
public class LocationType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="name", nullable = false)
    private String name;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="parent_type_id")
    private LocationType parentLocationType;
    @Column(name="parent_type_id", nullable = true)
    private Long parentTypeId;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
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
