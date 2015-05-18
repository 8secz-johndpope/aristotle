package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FinancialPlanning;

public interface FinancialPlanningRepository extends JpaRepository<FinancialPlanning, Long> {

}