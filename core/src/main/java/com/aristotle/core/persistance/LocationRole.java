package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location_role")
public class LocationRole extends BaseEntity {

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} , fetch=FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "location_id", insertable = false, updatable = false)
    private Long locationId;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} , fetch=FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Role role;
	@Column(name="role_id", insertable=false,updatable=false)
	private Long roleId;
	
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
