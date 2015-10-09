package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.Sms;


public interface SmsRepository extends JpaRepository<Sms, Long> {

    @Query("select sms from Sms sms where status='PENDING'")
    public abstract List<PlannedSms> getAllPendingSms();

}