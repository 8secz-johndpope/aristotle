package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookAppPermission;

public interface FacebookAppPermissionRepository extends JpaRepository<FacebookAppPermission, Long> {

    public abstract FacebookAppPermission getFacebookAppPermissionByFacebookAppIdAndFacebookAccountId(Long appId, Long facebookAccountId);
	
	public abstract FacebookAppPermission getFacebookAppPermissionByFacebookAppIdAndFacebookAccountId(String facebookAppId, Long facebookAccountId);
	
	public abstract List<FacebookAppPermission> getFacebookAppPermissionByFacebookAccountId(Long facebookAccountId);
	
    // public abstract List<FacebookAppPermission> getFacebookAppPermissionAfterId(Long facebookAppId, int pageSize);

}