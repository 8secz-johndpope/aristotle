package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.MobileGroup;


public interface MobileGroupRepository extends JpaRepository<MobileGroup, Long> {

    @Query("select mobileGroup from MobileGroup mobileGroup order by name desc")
    public abstract List<MobileGroup> getMobileGroups();

}