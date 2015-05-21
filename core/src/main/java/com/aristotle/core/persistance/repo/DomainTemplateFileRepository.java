package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DomainTemplateFile;

public interface DomainTemplateFileRepository extends JpaRepository<DomainTemplateFile, Long> {

    DomainTemplateFile getDomainTemplateFileByFileName(String fileName);
}
