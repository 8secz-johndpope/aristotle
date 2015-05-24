package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

}