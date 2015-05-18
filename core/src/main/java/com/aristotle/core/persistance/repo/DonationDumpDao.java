package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.persistance.DonationDump;

public interface DonationDumpDao {

	public abstract DonationDump saveDonationDump(DonationDump donationDump);

	public abstract DonationDump getDonationDumpById(Long id);

	public abstract List<DonationDump> getDonationDumpsToImport(int pageSize);

}