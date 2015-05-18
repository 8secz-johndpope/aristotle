package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedFacebookPost;


public interface PlannedFacebookPostDao {

	public abstract PlannedFacebookPost savePlannedFacebookPost(PlannedFacebookPost plannedFacebookPost);

	public abstract PlannedFacebookPost getPlannedFacebookPostById(Long id);

	public abstract PlannedFacebookPost getPlannedFacebookPostByAppId(String appId);
	
	public abstract List<PlannedFacebookPost> getPlannedFacebookPostByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
	public abstract List<PlannedFacebookPost> getExecutedFacebookPostByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
	
	public abstract PlannedFacebookPost getNextPlannedFacebookPostToPublish();

}