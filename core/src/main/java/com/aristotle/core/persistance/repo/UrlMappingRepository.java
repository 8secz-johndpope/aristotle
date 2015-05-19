package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UrlMapping;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

}
