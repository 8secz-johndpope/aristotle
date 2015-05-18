package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CountryRegionArea;

public interface CountryRegionAreaRepository extends JpaRepository<CountryRegionArea, Long> {

    public abstract CountryRegionArea getCountryRegionAreaByNameAndCountryRegionId(Long countryRegionId, String countryRegionArea);

    public abstract List<CountryRegionArea> getCountryRegionAreasByCountryRegionId(Long countryRegionId);

}