package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "patient-microservice", url = "localhost:9001")
public interface PatientProxy {

    @GetMapping("v1/api/patients/all")
    public List<PatientBean> getAllPatients ();

    @GetMapping("v1/api/patients/findBySearchCriteria")
    public List<PatientBean> getBySerachCriteria(@RequestParam String lastName, @RequestParam String firstName, @RequestParam LocalDate dateOfBirth);

    @PostMapping("v1/api/patients/add")
    public ResponseEntity<PatientBean> addPatient (@RequestBody PatientBean patientBean);
}