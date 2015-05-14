package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

    public abstract Country getCountryByName(String name);

}