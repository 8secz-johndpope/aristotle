package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {

}