package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DataPlugin;

public interface DataPluginRepository extends JpaRepository<DataPlugin, Long> {

}
