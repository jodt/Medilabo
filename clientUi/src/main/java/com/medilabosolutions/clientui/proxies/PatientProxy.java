package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "patient-microservice", url = "localhost:9001")
public interface PatientProxy {

    @GetMapping("v1/api/patients/all")
    public List<PatientBean> getAllPatients ();

    @GetMapping("v1/api/patients/findBySearchCriteria")
    public List<PatientBean> getBySearchCriteria(@RequestParam String lastName, @RequestParam String firstName, @RequestParam LocalDate dateOfBirth);

    @PostMapping("v1/api/patients/add")
    public ResponseEntity<PatientBean> addPatient (@RequestBody PatientBean patientBean);

    @GetMapping("v1/api/patients/findPatientById/{id}")
    public PatientBean getPatientById(@PathVariable Integer id);

    @PutMapping("v1/api/patients/update")
    ResponseEntity<Object> updatePatient(@RequestBody PatientBean patientBean);
}