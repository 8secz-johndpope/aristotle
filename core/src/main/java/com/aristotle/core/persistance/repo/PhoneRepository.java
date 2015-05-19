package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    public Phone getPhoneByPhoneNumberAndCountryCode(String phone, String countryCode);
	
    public List<Phone> getPhonesByUserId(Long userId);

}