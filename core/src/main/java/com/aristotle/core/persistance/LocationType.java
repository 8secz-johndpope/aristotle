package com.aristotle.core.persistance;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "location_type")
public class LocationType extends BaseEntity {


    @Column(name="name", nullable = false, unique=true)
    private String name;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="parent_type_id")
    private LocationType parentLocationType;
    @Column(name = "parent_type_id", insertable = false, updatable = false)
    private Long parentTypeId;
    
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "location_type_role",
    joinColumns = {
    @JoinColumn(name="location_type_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name="role_id")
    })
    private Set<Role> roles;

    @OneToMany(mappedBy = "parentLocationType", fetch = FetchType.EAGER)
    private List<LocationType> childLocationTypes;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<LocationType> getChildLocationTypes() {
        return childLocationTypes;
    }

    public void setChildLocationTypes(List<LocationType> childLocationTypes) {
        this.childLocationTypes = childLocationTypes;
    }
    @Override
    public String toString() {
        return "LocationType [id=" + id + ", name=" + name
                + ", parentLocationType=" + parentLocationType
                + ", parentTypeId=" + parentTypeId + "]";
    }
    
    
}
