package com.medilabosolutions.patient.controller;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
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
    public ResponseEntity<Patient> addPatient (@RequestBody PatientDto patient) {
        Patient patientAdded = this.patientService.addPatient(patient);
        return new ResponseEntity<>(patientAdded,HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<PatientDto> findPatient(@RequestBody PatientDto patientDto) {
        try {
            PatientDto patientToReturn =  this.patientService.getPatient(patientDto);
            return new ResponseEntity<>(patientToReturn, HttpStatus.OK);
        } catch (ResouceNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
