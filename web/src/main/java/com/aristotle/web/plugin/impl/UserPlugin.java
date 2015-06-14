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
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.service.AppService;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.NewsService;
import com.aristotle.core.service.UserService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
    private AppService appService;

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
                addVolunteerOptions(userJsonObject, dbUser);
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
        jsonObject.addProperty("nri", user.isNri());
        jsonObject.addProperty("volunteer", user.isVolunteer());

        if (user.getDateOfBirth() != null) {
            jsonObject.addProperty("dateOfBirth", ddMMyyyyFormat.format(user.getDateOfBirth()));
        }

        addGenderOptions(jsonObject, user);
        addIdentityTypeOptions(jsonObject, user);
        try {
            addLocations(jsonObject, user);
        } catch (AppException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void addVolunteerOptions(JsonObject context, User user) throws AppException {
        List<InterestGroup> interestGroups = appService.getAllInterests();
        Volunteer volunteer = appService.getVolunteerDataForUser(user.getId());
        if (volunteer == null) {
            return;
        }
        context.addProperty("knowExistingMember", volunteer.isKnowExistingMember());
        context.addProperty("pastVolunteer", volunteer.isPastVolunteer());
        context.addProperty("domainExpertise", volunteer.getDomainExpertise());
        context.addProperty("education", volunteer.getEducation());
        context.addProperty("emergencyContactName", volunteer.getEmergencyContactName());
        context.addProperty("emergencyContactNo", volunteer.getEmergencyContactNo());
        context.addProperty("emergencyContactCountryCode", volunteer.getEmergencyContactCountryCode());
        context.addProperty("emergencyContactCountryIso2", volunteer.getEmergencyContactCountryIso2());
        context.addProperty("emergencyContactRelation", volunteer.getEmergencyContactRelation());
        context.addProperty("existingMemberCountryCode", volunteer.getExistingMemberCountryCode());
        context.addProperty("existingMemberCountryIso2", volunteer.getExistingMemberCountryIso2());
        context.addProperty("existingMemberEmail", volunteer.getExistingMemberEmail());
        context.addProperty("existingMemberMobile", volunteer.getExistingMemberMobile());
        context.addProperty("existingMemberName", volunteer.getExistingMemberName());
        context.addProperty("offences", volunteer.getOffences());
        context.addProperty("pastOrganisation", volunteer.getPastOrganisation());
        context.addProperty("professionalBackground", volunteer.getProfessionalBackground());
        Set<Long> userIntrests = new HashSet<Long>();
        if (volunteer.getInterests() != null) {
            for (Interest oneInterest : volunteer.getInterests()) {
                userIntrests.add(oneInterest.getId());
            }
        }

        int rowSize = 3;
        JsonArray interestGroupJsonArray = new JsonArray();
        for (InterestGroup oneInterestGroup : interestGroups) {
            JsonObject oneInterestGroupJsonObject = new JsonObject();
            oneInterestGroupJsonObject.addProperty("description", oneInterestGroup.getDescription());
            JsonArray interestJsonArray = new JsonArray();
            JsonArray interestRowJsonArray = new JsonArray();
            int count = 0;
            if ("Can volunteer for: (Click all that apply)".equals(oneInterestGroup.getDescription())) {
                rowSize = 2;
            } else {
                rowSize = 3;
            }
            for (Interest oneInterest : oneInterestGroup.getInterests()) {
                JsonObject oneInterestJsonObject = new JsonObject();
                oneInterestJsonObject.addProperty("id", oneInterest.getId());
                oneInterestJsonObject.addProperty("description", oneInterest.getDescription());
                if (userIntrests.contains(oneInterest.getId())) {
                    oneInterestJsonObject.addProperty("selected", true);
                }

                interestRowJsonArray.add(oneInterestJsonObject);
                count++;
                if (count % rowSize == 0) {
                    interestJsonArray.add(interestRowJsonArray);
                    interestRowJsonArray = new JsonArray();
                }
            }
            oneInterestGroupJsonObject.add("interests", interestJsonArray);
            interestGroupJsonArray.add(oneInterestGroupJsonObject);
        }
        context.add("userinterests", interestGroupJsonArray);

    }

    private void addLocations(JsonObject jsonObject, User user) throws AppException {
        System.out.println("Adding Locations");
        List<UserLocation> userLocations = userService.getUserLocations(user.getId());

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

        System.out.println("selectedCountry=" + selectedCountry);
        System.out.println("selectedCountryRegion=" + selectedCountryRegion);
        System.out.println("selectedCountryRegionArea=" + selectedCountryRegionArea);
        System.out.println("selectedLivingState=" + selectedLivingState);
        System.out.println("selectedLivingPc=" + selectedLivingPc);
        System.out.println("selectedLivingAc=" + selectedLivingAc);
        System.out.println("selectedLivingDistrict=" + selectedLivingDistrict);
        System.out.println("selectedVotingState=" + selectedVotingState);
        System.out.println("selectedVotingPc=" + selectedVotingPc);
        System.out.println("selectedVotingAc=" + selectedVotingAc);
        System.out.println("selectedVotingDistrict=" + selectedVotingDistrict);

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
        StringBuilder sb = new StringBuilder("");
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
