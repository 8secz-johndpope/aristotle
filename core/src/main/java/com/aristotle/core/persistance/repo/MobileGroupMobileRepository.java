package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.MobileGroupMobile;
import com.aristotle.core.persistance.Phone;


public interface MobileGroupMobileRepository extends JpaRepository<MobileGroupMobile, Long> {


    List<MobileGroupMobile> getMobileGroupMobileByMobileGroupId(Long mobileGroupId);

    @Query("select mobileGroupMobile.phone from MobileGroupMobile mobileGroupMobile where mobileGroupMobile.id=?1")
    List<Phone> getPhoneByMobileGroupId(Long mobileGroupId);
}