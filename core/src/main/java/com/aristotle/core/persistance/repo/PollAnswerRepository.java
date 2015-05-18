package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.PollAnswer;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {

    public abstract List<PollAnswer> getPollAnswersByPollQuestionId(Long pollQuestionId);

}