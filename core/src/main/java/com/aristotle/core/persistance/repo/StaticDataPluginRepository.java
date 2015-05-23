package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.StaticDataPlugin;

public interface StaticDataPluginRepository extends JpaRepository<StaticDataPlugin, Long> {

}
