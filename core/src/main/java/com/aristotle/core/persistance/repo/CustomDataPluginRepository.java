package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CustomDataPlugin;

public interface CustomDataPluginRepository extends JpaRepository<CustomDataPlugin, Long> {

}
