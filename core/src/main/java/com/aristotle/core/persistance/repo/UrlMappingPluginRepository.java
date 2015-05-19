package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UrlMappingPlugin;

public interface UrlMappingPluginRepository extends JpaRepository<UrlMappingPlugin, Long> {

}
