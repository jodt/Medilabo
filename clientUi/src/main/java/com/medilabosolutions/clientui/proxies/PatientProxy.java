package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.beans.PatientBean;
import com.medilabosolutions.clientui.exceptions.PatientAlreadyRegisteredException;
import com.medilabosolutions.clientui.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "gateway", url = "${gateway.uri}")
public interface PatientProxy {

    @GetMapping("v1/api/patients/all")
    public List<PatientBean> getAllPatients ();

    @GetMapping("v1/api/patients/findBySearchCriteria")
    public List<PatientBean> getBySearchCriteria(@RequestParam String lastName, @RequestParam String firstName, @RequestParam LocalDate dateOfBirth);

    @PostMapping("v1/api/patients/add")
    public ResponseEntity<PatientBean> addPatient (@RequestBody PatientBean patientBean) throws PatientAlreadyRegisteredException;

    @GetMapping("v1/api/patients/findPatientById/{id}")
    public PatientBean getPatientById(@PathVariable Integer id) throws ResourceNotFoundException;

    @PutMapping("v1/api/patients/update")
    ResponseEntity<Object> updatePatient(@RequestBody PatientBean patientBean) throws ResourceNotFoundException;
}