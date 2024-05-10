package com.medilabosolutions.patient.controller;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.service.PatientService;
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

@RestController
@RequestMapping("v1/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/all")
    public List<PatientDto> getAllPatients () {
        return this.patientService.findAllPatients();
    }

    @GetMapping("/findBySearchCriteria")
    public List<PatientDto> getBySerachCriteria(@RequestParam(required = false) String lastName,@RequestParam(required = false) String firstName, @RequestParam(required = false) LocalDate dateOfBirth){
        boolean matchAll = !lastName.isEmpty() && !firstName.isEmpty() && !Objects.isNull(dateOfBirth);
        return this.patientService.findPatients(lastName, firstName, dateOfBirth, matchAll);
    }

    @GetMapping("/findPatientById/{id}")
    public PatientDto getPatientById(@PathVariable Integer id) throws ResouceNotFoundException {
        return this.patientService.getPatientById(id);
    }

    @PostMapping ("/add")
    public ResponseEntity<Patient> addPatient (@RequestBody PatientDto patient) throws PatientAlreadyRegisteredException {
        Patient patientAdded = this.patientService.addPatient(patient);
        return new ResponseEntity<>(patientAdded,HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updatePatient(@RequestBody PatientDto patientDto) {
        try {
            return new ResponseEntity<>(this.patientService.updatePatient(patientDto), HttpStatus.OK);
        } catch (ResouceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
