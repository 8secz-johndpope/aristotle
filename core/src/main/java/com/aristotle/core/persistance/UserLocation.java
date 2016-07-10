package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_locations")
public class UserLocation extends BaseEntity {

	public static final String VOTING = "Voting";
	public static final String LIVING = "Living";
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "location_id", insertable = false, updatable = false)
    private Long locationId;
	
    @Column(name = "user_location_type")
    private String userLocationType;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getUserLocationType() {
        return userLocationType;
    }

    public void setUserLocationType(String userLocationType) {
        this.userLocationType = userLocationType;
    }
	
	
	
}
