package com.medilabosolutions.patient.repository;

import com.medilabosolutions.patient.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByNumberAndStreet(Integer number, String street);
}
