package com.aristotle.core.persistance.repo;

import com.next.aap.core.persistance.FinancialPlanning;

public interface FinancialPlanningDao {

	public abstract FinancialPlanning saveFinancialPlanning(FinancialPlanning financialPlanning);

	public abstract FinancialPlanning getFinancialPlanningById(Long id);

}