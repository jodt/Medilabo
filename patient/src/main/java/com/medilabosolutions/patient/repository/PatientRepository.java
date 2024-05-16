package com.medilabosolutions.patient.repository;

import com.medilabosolutions.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByLastNameAndFirstNameAndDateOfBirth(String lastName, String firstName, LocalDate dateOfBirth);

}
