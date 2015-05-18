package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.State;

public interface StateRepository extends JpaRepository<State, Long> {

    public State getStateByNameUp(String name);

}