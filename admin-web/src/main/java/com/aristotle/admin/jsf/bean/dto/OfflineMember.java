package com.aristotle.admin.jsf.bean.dto;

import com.aristotle.core.persistance.Location;

public class OfflineMember {

	private String name;
	private Location selectedState;
	private Location selectedDistrict;
	private Location selectedAc;
	private Location selectedPc;
	private String referenceMobile;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Location getSelectedState() {
		return selectedState;
	}
	public void setSelectedState(Location selectedState) {
		this.selectedState = selectedState;
	}
	public Location getSelectedDistrict() {
		return selectedDistrict;
	}
	public void setSelectedDistrict(Location selectedDistrict) {
		this.selectedDistrict = selectedDistrict;
	}
	public Location getSelectedAc() {
		return selectedAc;
	}
	public void setSelectedAc(Location selectedAc) {
		this.selectedAc = selectedAc;
	}
	public Location getSelectedPc() {
		return selectedPc;
	}
	public void setSelectedPc(Location selectedPc) {
		this.selectedPc = selectedPc;
	}
	public String getReferenceMobile() {
		return referenceMobile;
	}
	public void setReferenceMobile(String referenceMobile) {
		this.referenceMobile = referenceMobile;
	}
}
