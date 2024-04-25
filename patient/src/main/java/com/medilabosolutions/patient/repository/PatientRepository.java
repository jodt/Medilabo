package com.medilabosolutions.patient.repository;

import com.medilabosolutions.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByLastNameAndFirstNameAndDateOfBirth(String lastName, String firstName, LocalDate dateOfBirth);

}
