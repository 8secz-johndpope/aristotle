package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Election;

public interface ElectionRepository extends JpaRepository<Election, Long> {

}