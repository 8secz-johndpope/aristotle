package com.aristotle.core.persistance;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "team")
public class Team extends BaseEntity{

	@Column(name = "name")
	private String name;
	
    @Column(name = "url")
    private String url;

    @Column(name = "description")
	private String description;

    @Column(name = "display_order")
    private int displayOrder;
    
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "team_location",
    joinColumns = {
    @JoinColumn(name="team_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name = "location_id")
    })
    private Set<Location> locations;

    @Column(name = "global")
    private boolean global;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

}
