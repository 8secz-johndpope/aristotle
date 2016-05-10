package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Sms;


public interface SmsRepository extends JpaRepository<Sms, Long> {

    @Query("select sms from Sms sms where status='PENDING'")
    public abstract List<Sms> getAllPendingSms();

    @Query("select sms from Sms sms where plannedSmsId=?1 and phoneId =?2")
    public abstract Sms getSmsByPlannedSmsIdAndPhoneId(long plannedSmsId, long phoneId);

    @Query("select sms from Sms sms where status='PENDING'")
    public abstract Page<Sms> getPendingSms(Pageable pageable);

}