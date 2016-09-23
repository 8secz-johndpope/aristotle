package com.aristotle.core.service.dto;

import java.util.Map;
import java.util.Map.Entry;

import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.User;

public class UserSearchResultForEdting {

    private User user;
    private Email email;
    private Phone phone;
    private Location livingState = new Location();
    private Location livingDistrict = new Location();
    private Location livingAc = new Location();
    private Location livingPc = new Location();
    private Location votingState = new Location();
    private Location votingDistrict = new Location();
    private Location votingAc = new Location();
    private Location votingPc = new Location();
    private Location country = new Location();
    private Location countryRegion = new Location();
    private Location countryRegionArea = new Location();
    
    public void setLocations(Map<String, Location> locations){
    	for(Entry<String, Location> oneEntry:locations.entrySet()){
    		if("LivingState".equals(oneEntry.getKey())){
    			livingState = oneEntry.getValue();
    		}
    		if("LivingDistrict".equals(oneEntry.getKey())){
    			livingDistrict = oneEntry.getValue();
    		}
    		if("LivingAssemblyConstituency".equals(oneEntry.getKey())){
    			livingAc = oneEntry.getValue();
    		}
    		if("LivingParliamentConstituency".equals(oneEntry.getKey())){
    			livingPc = oneEntry.getValue();
    		}
    		if("VotingState".equals(oneEntry.getKey())){
    			votingState = oneEntry.getValue();
    		}
    		if("VotingDistrict".equals(oneEntry.getKey())){
    			votingDistrict = oneEntry.getValue();
    		}
    		if("VotingAssemblyConstituency".equals(oneEntry.getKey())){
    			votingAc = oneEntry.getValue();
    		}
    		if("VotingParliamentConstituency".equals(oneEntry.getKey())){
    			votingPc = oneEntry.getValue();
    		}
    		if("LivingCountry".equals(oneEntry.getKey())){
    			country = oneEntry.getValue();
    		}
    		if("LivingRegion".equals(oneEntry.getKey())){
    			countryRegion = oneEntry.getValue();
    		}
    		if("LivingCountryRegionArea".equals(oneEntry.getKey())){
    			countryRegionArea = oneEntry.getValue();
    		}
    	}
    	
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

	public Location getLivingState() {
		return livingState;
	}

	public void setLivingState(Location livingState) {
		this.livingState = livingState;
	}

	public Location getLivingDistrict() {
		return livingDistrict;
	}

	public void setLivingDistrict(Location livingDistrict) {
		this.livingDistrict = livingDistrict;
	}

	public Location getLivingAc() {
		return livingAc;
	}

	public void setLivingAc(Location livingAc) {
		this.livingAc = livingAc;
	}

	public Location getLivingPc() {
		return livingPc;
	}

	public void setLivingPc(Location livingPc) {
		this.livingPc = livingPc;
	}

	public Location getVotingState() {
		return votingState;
	}

	public void setVotingState(Location votingState) {
		this.votingState = votingState;
	}

	public Location getVotingDistrict() {
		return votingDistrict;
	}

	public void setVotingDistrict(Location votingDistrict) {
		this.votingDistrict = votingDistrict;
	}

	public Location getVotingAc() {
		return votingAc;
	}

	public void setVotingAc(Location votingAc) {
		this.votingAc = votingAc;
	}

	public Location getVotingPc() {
		return votingPc;
	}

	public void setVotingPc(Location votingPc) {
		this.votingPc = votingPc;
	}

	public Location getCountry() {
		return country;
	}

	public void setCountry(Location country) {
		this.country = country;
	}

	public Location getCountryRegion() {
		return countryRegion;
	}

	public void setCountryRegion(Location countryRegion) {
		this.countryRegion = countryRegion;
	}

	public Location getCountryRegionArea() {
		return countryRegionArea;
	}

	public void setCountryRegionArea(Location countryRegionArea) {
		this.countryRegionArea = countryRegionArea;
	}
}
