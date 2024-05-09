package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {

    List<PatientDto> findAllPatients();

    List<PatientDto> findPatients(String lastName, String firstName, LocalDate dateOfBirth, boolean matchAll);

    Patient addPatient(PatientDto patient);

    PatientDto getPatient(PatientDto patientDto) throws ResouceNotFoundException;

    Patient updatePatient(PatientDto patientDto) throws ResouceNotFoundException;


}
