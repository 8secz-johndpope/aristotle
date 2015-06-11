package com.aristotle.web.plugin.impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.User;
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
