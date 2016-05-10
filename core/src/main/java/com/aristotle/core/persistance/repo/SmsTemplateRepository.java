package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.SmsTemplate;


public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, Long> {

    @Query("select smsTemplate from SmsTemplate smsTemplate where automated=false order by name desc")
    public abstract List<SmsTemplate> getAllSmsTemplate();

    public abstract SmsTemplate getSmsTemplateBySystemName(String systemName);

}