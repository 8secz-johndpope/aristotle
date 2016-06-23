package com.aristotle.member.test.stepdef;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.service.LocationService;
import com.aristotle.member.test.TestContext;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;

public class LocationStepDef extends BaseStepDef{
	
	@Autowired
	private LocationService locationService;
	
	@Given("Create Location Type as \"([^\"]*)\"")
    public void createNewLocationType(String locationTypeTestId, List<LocationType> list) throws FieldDoNotExistsException, AppException {
		TestContext testContext = TestContext.getCurrentContext();
		for(LocationType oneLocationType : list){
			oneLocationType = locationService.saveLocationType(oneLocationType);
			testContext.setData(locationTypeTestId, oneLocationType);
		}
    }
	@Given("Create Location as \"([^\"]*)\" with locationType \"([^\"]*)\"")
    public void createNewLocationAs(String locationTestId, String locationTypeTestId, List<Location> list) throws FieldDoNotExistsException, AppException {
		TestContext testContext = TestContext.getCurrentContext();
		LocationType locationType = testContext.getData(locationTypeTestId, LocationType.class);
		for(Location oneLocation : list){
			oneLocation.setLocationType(locationType);
			oneLocation = locationService.saveLocation(oneLocation);
			if(locationTestId != null){
				testContext.setData(locationTestId, oneLocation);
			}
		}
    }
	@Given("Create Location with locationType \"([^\"]*)\"")
    public void createNewLocation(String locationTypeTestId, List<Location> list) throws FieldDoNotExistsException, AppException {
		createNewLocationAs(null, locationTypeTestId, list);
    }
}
