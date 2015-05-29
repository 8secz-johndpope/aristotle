package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.repo.InterestGroupRepository;

@Service
@Transactional
public class AppServiceImpl implements AppService {

    @Autowired
    private InterestGroupRepository interestGroupRepository;
    @Override
    public List<InterestGroup> getAllInterests() {
        return interestGroupRepository.findAll();
    }

}
