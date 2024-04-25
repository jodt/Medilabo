package com.medilabosolutions.patient.controller;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
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

    @PostMapping ("/add")
    public ResponseEntity<Patient> getAllPatients (@RequestBody PatientDto patient) {
        Patient patientAdded = this.patientService.addPatient(patient);
        return new ResponseEntity<>(patientAdded,HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public Optional<Patient> findPatient(@RequestBody PatientDto patientDto) {
        return this.patientService.getPatient(patientDto);
    }

}
