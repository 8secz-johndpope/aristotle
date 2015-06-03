package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    List<UserLocation> getUserLocationByUserId(Long userId);
}