package com.medilabosolutions.patient.controller;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("v1/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/findBySearchCriteria")
    public List<PatientDto> getBySerachCriteria(@RequestParam(required = false) String lastName, @RequestParam(required = false) String firstName, @RequestParam(required = false) LocalDate dateOfBirth) {
        log.info("GET /findBySearchCriteria called -> start process to search patient with lastName : {}, firstName : {}, date of birth : {}", lastName.isEmpty() ? null : lastName , firstName.isEmpty() ? null : firstName, dateOfBirth);
        boolean matchAll = !lastName.isEmpty() && !firstName.isEmpty() && !Objects.isNull(dateOfBirth);
        log.info("The three criteria have been completed : {}", matchAll);
        List<PatientDto> patientFound = this.patientService.findPatients(lastName, firstName, dateOfBirth, matchAll);
        log.info("{} patient(s) found with these criteria", patientFound.size());
        return patientFound;
    }

    @GetMapping("/findPatientById/{id}")
    public PatientDto getPatientById(@PathVariable Integer id) throws ResouceNotFoundException {
        log.info("GET /findPatientById/{} called -> start process to find patient with id {}", id, id);
        return this.patientService.getPatientById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody PatientDto patient) throws PatientAlreadyRegisteredException {
        log.info("POST /add called -> start process to add a patient");
        Patient patientAdded = this.patientService.addPatient(patient);
        log.info("Patient added successfully");
        return new ResponseEntity<>(patientAdded, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updatePatient(@RequestBody PatientDto patientDto) throws ResouceNotFoundException {
        log.info("PUT /update called -> start process to update a patient");
        return new ResponseEntity<>(this.patientService.updatePatient(patientDto), HttpStatus.OK);
    }

}
