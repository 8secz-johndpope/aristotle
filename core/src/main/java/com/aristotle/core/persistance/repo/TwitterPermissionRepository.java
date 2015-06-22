package com.aristotle.core.persistance.repo;

import com.aristotle.core.persistance.TwitterApp;
import com.aristotle.core.persistance.TwitterPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterPermissionRepository extends JpaRepository<TwitterPermission, Long> {

}