package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.persistance.Election;

public interface ElectionDao {

    public abstract Election saveElection(Election Election);

    public abstract Election getElectionById(Long id);

    public abstract List<Election> getAllElections();

}