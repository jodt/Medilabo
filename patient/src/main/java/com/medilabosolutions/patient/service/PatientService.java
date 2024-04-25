package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.model.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientService {

    public List<PatientDto> findAllPatients();

    public Patient addPatient (PatientDto patient);

    public Optional<Patient> getPatient(PatientDto patientDto);

}
