package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    public abstract Candidate getCandidateByPcIdAndElectionId(Long pcId, Long electionId);
	
    public abstract Candidate getCandidateByAcIdAndElectionId(Long acId, Long electionId);

	public abstract Candidate getCandidateByExtPcId(String pcId);
	
    public abstract Candidate getCandidateByExtAcId(String acId);

	public abstract List<Candidate> getAllCandidates(int totalItems, int pageNumber);
	
	public abstract List<Candidate> getAllCandidates();
	
    public abstract List<Candidate> getAllCandidatesByElectionId(Long electionId);

	public abstract List<Candidate> getCandidateItemsAfterId(long candidateId);

}
