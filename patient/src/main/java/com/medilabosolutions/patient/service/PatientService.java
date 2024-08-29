package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientAgeGenderDto;
import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientService {

    List<PatientDto> findPatients(String lastName, String firstName, LocalDate dateOfBirth);

    Patient addPatient(PatientDto patient) throws PatientAlreadyRegisteredException;

    Optional<Patient> getPatientByLastNameAndFirstNameAndDateOfBirth(PatientDto patientDto);

    Patient updatePatient(PatientDto patientDto) throws ResouceNotFoundException;

    PatientDto getPatientById(Integer id) throws ResouceNotFoundException;

    PatientAgeGenderDto getPatientWithAgeAndGender (Integer id) throws ResouceNotFoundException;

}
