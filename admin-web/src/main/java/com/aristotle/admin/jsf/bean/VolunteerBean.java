package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.admin.jsf.bean.dto.InterestGroupModel;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.service.AppService;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLBeanName("volunteerBean")
public class VolunteerBean extends BaseJsfBean {
	private static final long serialVersionUID = 1L;

    private Volunteer selectedVolunteer;

	private List<InterestGroupModel> interestGroupDtoModels;
	
	private Map<Long, Boolean> selectedInterestMap = new HashMap<Long, Boolean>();

	@Autowired
    private AppService appService;

    public void init(User user) throws Exception {
        List<InterestGroup> interestGroups = appService.getAllInterests();
		selectedInterestMap.clear();
		interestGroupDtoModels = new ArrayList<>();
		if(interestGroups != null && !interestGroups.isEmpty()){
            for (InterestGroup oneInterestGroupDto : interestGroups) {
				interestGroupDtoModels.add(new InterestGroupModel(oneInterestGroupDto));
			}
		}
		if(user != null){
            selectedVolunteer = appService.getVolunteerDataForUser(user.getId());
            Set<Interest> userInterests = selectedVolunteer.getInterests();
			if(userInterests != null && userInterests.size() > 0){
                for (Interest oneInterestDto : userInterests) {
					selectedInterestMap.put(oneInterestDto.getId(), true);
				}
			}
		}
		if(selectedVolunteer == null){
            selectedVolunteer = new Volunteer();
		}
		
	}

    public void onTabChange() {
        System.out.println("Tab Changed");
    }

    public void onTabClose() {
        System.out.println("Tab Closed");
    }

	public List<InterestGroupModel> getInterestGroupDtoModels() {
		return interestGroupDtoModels;
	}

	public void setInterestGroupDtoModels(List<InterestGroupModel> interestGroupDtoModels) {
		this.interestGroupDtoModels = interestGroupDtoModels;
	}

	public Map<Long, Boolean> getSelectedInterestMap() {
		return selectedInterestMap;
	}

	public void setSelectedInterestMap(Map<Long, Boolean> selectedInterestMap) {
		this.selectedInterestMap = selectedInterestMap;
	}

	public List<Long> getSelectedInterestIds(){
		List<Long> selectedInterests = new ArrayList<>();
		for(Entry<Long, Boolean> oneInterest:selectedInterestMap.entrySet()){
			if(oneInterest.getValue()){
				selectedInterests.add(oneInterest.getKey());
			}
		}
		return selectedInterests;
	}

    public Volunteer getSelectedVolunteer() {
        return selectedVolunteer;
    }

    public void setSelectedVolunteer(Volunteer selectedVolunteer) {
        this.selectedVolunteer = selectedVolunteer;
    }
}
