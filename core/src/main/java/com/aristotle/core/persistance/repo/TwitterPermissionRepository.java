package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.TwitterPermission;

public interface TwitterPermissionRepository extends JpaRepository<TwitterPermission, Long> {

    List<TwitterPermission> getTwitterPermissionByTwitterAccountId(Long twitterAccountId);
}