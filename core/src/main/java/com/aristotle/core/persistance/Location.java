package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location extends BaseEntity {

    @Column(name="name", nullable = false)
    private String name;
    
    @Column(name = "name_up", nullable = false)
    private String nameUp;

    @Column(name = "isd_code")
    private String isdCode;
    
    @Column(name = "state_code")
    private String stateCode;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name="location_type_id")
    private LocationType locationType;
    @Column(name = "location_type_id", insertable = false, updatable = false)
    private Long locationTypeId;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="parent_id")
    private Location parentLocation;
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentLocationId;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNameUp() {
        return nameUp;
    }

    public void setNameUp(String nameUp) {
        this.nameUp = nameUp;
    }
    public LocationType getLocationType() {
        return locationType;
    }
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Long getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(Long locationTypeId) {
        this.locationTypeId = locationTypeId;
    }
    public Location getParentLocation() {
        return parentLocation;
    }
    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }
    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }
    public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	@Override
    public String toString() {
        return "Location [name=" + name + ", nameUp=" + nameUp + ", isdCode=" + isdCode + ", locationTypeId=" + locationTypeId + ", parentLocation=" + parentLocation + ", parentLocationId="
                + parentLocationId
                + ", id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId + "]";
    }
    
    
    
}
