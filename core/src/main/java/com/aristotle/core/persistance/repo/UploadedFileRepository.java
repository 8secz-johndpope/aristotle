package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UploadedFile;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    UploadedFile getUploadedFileByFileName(String fileName);
}
