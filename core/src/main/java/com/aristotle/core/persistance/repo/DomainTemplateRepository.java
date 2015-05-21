package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.DomainTemplate;

public interface DomainTemplateRepository extends JpaRepository<DomainTemplate, Long> {

    @Query("select DT from DomainTemplate DT, Domain D where D.locationId is null and D.id=DT.domainId")
    List<DomainTemplate> getGlobalDomainTemplates();

    @Query("select DT from DomainTemplate DT, Domain D where D.locationId=?1 and D.id=DT.domainId")
    List<DomainTemplate> getLocationDomainTemplates(Long locationId);

}
