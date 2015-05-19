package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DomainTemplateFile;

public interface TemplateFileRepository extends JpaRepository<DomainTemplateFile, Long> {

}
