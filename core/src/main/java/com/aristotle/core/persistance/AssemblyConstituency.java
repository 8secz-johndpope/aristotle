package com.aristotle.core.persistance;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "assembly_constituency")
public class AssemblyConstituency extends BaseEntity {

	@Column(name = "name", nullable = false, length=256)
	private String name;
	@Column(name = "name_up", nullable = false, length=256)
	private String nameUp;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="district_id")
    private District district;
	@Column(name="district_id", insertable=false,updatable=false)
	private Long districtId;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "ac_campaigns",
	joinColumns = {
	@JoinColumn(name="ac_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="location_campaign_id")
	})
	private Set<LocationCampaign> campaigns;
	
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "location_id", insertable = false, updatable = false)
    private Long locationId;

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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Set<LocationCampaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(Set<LocationCampaign> campaigns) {
		this.campaigns = campaigns;
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

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssemblyConstituency other = (AssemblyConstituency) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
