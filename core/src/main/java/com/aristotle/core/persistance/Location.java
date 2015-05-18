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
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name", nullable = false)
    private String name;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="location_type_id")
    private LocationType locationType;
    @Column(name="location_type_id")
    private Long location_type_id;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="parent_id")
    private Location parentLocation;
    @Column(name="parent_id", nullable = true)
    private Long parentId;
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
    public LocationType getLocationType() {
        return locationType;
    }
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
    public Long getLocation_type_id() {
        return location_type_id;
    }
    public void setLocation_type_id(Long location_type_id) {
        this.location_type_id = location_type_id;
    }
    public Location getParentLocation() {
        return parentLocation;
    }
    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    @Override
    public String toString() {
        return "Location [id=" + id + ", name=" + name + ", locationType="
                + locationType + ", location_type_id=" + location_type_id
                + ", parentLocation=" + parentLocation + ", parentId="
                + parentId + "]";
    }
    
    
    
}
