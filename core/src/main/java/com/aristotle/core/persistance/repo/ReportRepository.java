package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report getReportByReportDateTimeIdAndReportType(String reportDateTimeId, String reportType);
}