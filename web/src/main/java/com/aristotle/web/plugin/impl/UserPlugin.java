package com.aristotle.web.plugin.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.NewsService;
import com.aristotle.core.service.UserService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserPlugin extends LocationAwareDataPlugin {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    private Gson gson;
    private JsonParser jsonParser = new JsonParser();

    @PostConstruct
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        UserFieldExclusionStrategy exclusionStrategy = new UserFieldExclusionStrategy();
        gsonBuilder.addDeserializationExclusionStrategy(exclusionStrategy);
        gson = gsonBuilder.create();
    }

    public class UserFieldExclusionStrategy implements ExclusionStrategy {
        Set<String> fieldsToIgnore;

        public UserFieldExclusionStrategy() {
            fieldsToIgnore = new HashSet<String>();
            fieldsToIgnore.add("nriCountry");
            fieldsToIgnore.add("nriCountryRegion");
            fieldsToIgnore.add("nriCountryRegionArea");
            fieldsToIgnore.add("stateLiving");
            fieldsToIgnore.add("districtLiving");
            fieldsToIgnore.add("assemblyConstituencyLiving");
            fieldsToIgnore.add("parliamentConstituencyLiving");
            fieldsToIgnore.add("stateVoting");
            fieldsToIgnore.add("districtVoting");
            fieldsToIgnore.add("assemblyConstituencyVoting");
            fieldsToIgnore.add("parliamentConstituencyVoting");
            fieldsToIgnore.add("donations");
            fieldsToIgnore.add("phones");
            fieldsToIgnore.add("emails");
            fieldsToIgnore.add("allRoles");
            fieldsToIgnore.add("stateRoles");
            fieldsToIgnore.add("locationRoles");
            fieldsToIgnore.add("districtRoles");
            fieldsToIgnore.add("acRoles");
            fieldsToIgnore.add("pcRoles");
            fieldsToIgnore.add("countryRoles");
            fieldsToIgnore.add("countryRegionRoles");
            fieldsToIgnore.add("countryRegionAreaRoles");
            fieldsToIgnore.add("superAdmin");
            fieldsToIgnore.add("dateOfBirth");
            fieldsToIgnore.add("dateCreated");
            fieldsToIgnore.add("dateModified");

        }
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            System.out.println("Field : " + f.getName());
            if (fieldsToIgnore.contains(f.getName())) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    public UserPlugin() {
    }

    public UserPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            if (user != null) {
                User dbUser = userService.getUserById(user.getId());
                JsonObject userJsonObject = convertUserFull(dbUser);
                context.add(name, userJsonObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JsonObject convertUserFull(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("address", user.getAddress());
        jsonObject.addProperty("fatherName", user.getFatherName());
        jsonObject.addProperty("gender", user.getGender());
        jsonObject.addProperty("identityNumber", user.getIdentityNumber());
        jsonObject.addProperty("identityType", user.getIdentityType());
        jsonObject.addProperty("motherName", user.getMotherName());
        jsonObject.addProperty("passportNumber", user.getPassportNumber());
        jsonObject.addProperty("profilePic", user.getProfilePic());
        if (user.getDateOfBirth() != null) {
            jsonObject.addProperty("dateOfBirth", ddMMyyyyFormat.format(user.getDateOfBirth()));
        }

        addGenderOptions(jsonObject, user);
        addIdentityTypeOptions(jsonObject, user);
        return jsonObject;
    }

    private void addLocations(JsonObject jsonObject, User user) throws AppException {
        List<UserLocation> userLocations = userService.getUserLocations(user.getId());
        if (userLocations == null || userLocations.isEmpty()) {
            // then just load State or Country(if NRI)
            return;
        }

        Long selectedCountry = getSelectedLocation(userLocations, "Country", "Living");
        Long selectedCountryRegion = getSelectedLocation(userLocations, "CountryRegion", "Living");
        Long selectedCountryRegionArea = getSelectedLocation(userLocations, "CountryRegionArea", "Living");
        Long selectedLivingState = getSelectedLocation(userLocations, "State", "Living");
        Long selectedLivingPc = getSelectedLocation(userLocations, "ParliamentConstituency", "Living");
        Long selectedLivingAc = getSelectedLocation(userLocations, "AssemblyConstituency", "Living");
        Long selectedLivingDistrict = getSelectedLocation(userLocations, "District", "Living");
        Long selectedVotingState = getSelectedLocation(userLocations, "State", "Voting");
        Long selectedVotingPc = getSelectedLocation(userLocations, "ParliamentConstituency", "Voting");
        Long selectedVotingAc = getSelectedLocation(userLocations, "AssemblyConstituency", "Voting");
        Long selectedVotingDistrict = getSelectedLocation(userLocations, "District", "Voting");

        addCountries(jsonObject, selectedCountry);
        addCountryRegions(jsonObject, "CountryRegions", selectedCountry, selectedCountryRegion);
        addCountryRegionAreas(jsonObject, "CountryRegionAreas", selectedCountryRegion, selectedCountryRegionArea);
        addStates(jsonObject, selectedLivingState, selectedVotingState);
        addDistricts(jsonObject, "LivingDistricts", selectedLivingState, selectedLivingDistrict);
        addDistricts(jsonObject, "VotingDistricts", selectedVotingState, selectedVotingDistrict);
        addAcs(jsonObject, "LivingAcs", selectedLivingDistrict, selectedLivingAc);
        addAcs(jsonObject, "VotingAcs", selectedVotingDistrict, selectedVotingAc);
        addPcs(jsonObject, "LivingPcs", selectedLivingState, selectedLivingPc);
        addPcs(jsonObject, "VotingPcs", selectedVotingState, selectedVotingPc);

    }


    private Long getSelectedLocation(List<UserLocation> userLocations, String locationTypeName, String userLocationType) {
        if (userLocations == null) {
            return null;
        }
        for (UserLocation oneUserLocation : userLocations) {
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase(locationTypeName) && oneUserLocation.getUserLocationType().equalsIgnoreCase(userLocationType)) {
                return oneUserLocation.getLocationId();
            }
        }
        return null;
    }

    private void addCountries(JsonObject jsonObject, Long selectedCountry) throws AppException {
        List<Location> countries = locationService.getAllCountries();
        addLocationOptions(jsonObject, "Countries", countries, selectedCountry);
    }

    private void addStates(JsonObject jsonObject, Long selectedLivingState, Long selectedVotingState) throws AppException {
        List<Location> states = locationService.getAllStates();
        addLocationOptions(jsonObject, "LivingStates", states, selectedLivingState);
        addLocationOptions(jsonObject, "VotingStates", states, selectedVotingState);
    }

    private void addCountryRegions(JsonObject jsonObject, String fieldName, Long selectedCountry, Long selectedCountryRegion) throws AppException {
        if (selectedCountry == null) {
            return;
        }
        List<Location> countryRegions = locationService.getAllChildLocations(selectedCountry);
        addLocationOptions(jsonObject, fieldName, countryRegions, selectedCountryRegion);
    }

    private void addCountryRegionAreas(JsonObject jsonObject, String fieldName, Long selectedCountryRegion, Long selectedCountryRegionArea) throws AppException {
        if (selectedCountryRegion == null) {
            return;
        }
        List<Location> countryRegions = locationService.getAllChildLocations(selectedCountryRegion);
        addLocationOptions(jsonObject, fieldName, countryRegions, selectedCountryRegionArea);
    }

    private void addDistricts(JsonObject jsonObject, String fieldName, Long selectedState, Long selectedDistrict) throws AppException {
        if (selectedState == null) {
            return;
        }
        List<Location> districts = locationService.getAllDistrictOfState(selectedState);
        addLocationOptions(jsonObject, fieldName, districts, selectedDistrict);
    }

    private void addPcs(JsonObject jsonObject, String fieldName, Long selectedState, Long selectedPc) throws AppException {
        if (selectedState == null) {
            return;
        }
        List<Location> pcs = locationService.getAllParliamentConstituenciesOfState(selectedState);
        addLocationOptions(jsonObject, fieldName, pcs, selectedPc);
    }

    private void addAcs(JsonObject jsonObject, String fieldName, Long selectedDistrict, Long selectedAc) throws AppException {
        if (selectedDistrict == null) {
            return;
        }
        List<Location> acs = locationService.getAllAssemblyConstituenciesOfDistrict(selectedDistrict);
        addLocationOptions(jsonObject, fieldName, acs, selectedAc);
    }

    private void addLocationOptions(JsonObject jsonObject, String name, List<Location> locations, Long selectedLocation) {
        StringBuilder sb = new StringBuilder("<option value=\"0\">Select Country</option>");
        for (Location oneLocation : locations) {
            if (selectedLocation != null && oneLocation.getId().equals(selectedLocation)) {
                sb.append("<option selected=\"selected\" value=\"" + oneLocation.getId() + "\">" + oneLocation.getName() + "</option>");
            } else {
                sb.append("<option value=\"" + oneLocation.getId() + "\">" + oneLocation.getName() + "</option>");
            }
        }
        jsonObject.addProperty(name, sb.toString());
    }

    private void addGenderOptions(JsonObject jsonObject, User user) {
        StringBuilder sb = new StringBuilder();

        if ("Male".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Male</option>");
        } else {
            sb.append("<option>Male</option>");
        }
        if ("Female".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Female</option>");
        } else {
            sb.append("<option>Female</option>");
        }
        if ("Other".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Other</option>");
        } else {
            sb.append("<option>Other</option>");
        }
        jsonObject.addProperty("genderChoices", sb.toString());
    }

    private void addIdentityTypeOptions(JsonObject jsonObject, User user) {
        StringBuilder sb = new StringBuilder();

        if ("Ration Card".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Ration Card</option>");
        } else {
            sb.append("<option>Ration Card</option>");
        }
        if ("Driving License".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Driving License</option>");
        } else {
            sb.append("<option>Driving License</option>");
        }
        if ("Voter Card".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Voter Card</option>");
        } else {
            sb.append("<option>Voter Card</option>");
        }
        if ("Passport".equalsIgnoreCase(user.getGender())) {
            sb.append("<option selected=\"selected\">Passport</option>");
        } else {
            sb.append("<option>Passport</option>");
        }

        jsonObject.addProperty("identityChoices", sb.toString());
    }

}
