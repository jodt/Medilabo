package com.medilabosolutions.riskreport.proxies;

import com.medilabosolutions.riskreport.beans.PatientAgeGenderBean;
import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway", url = "${gateway.uri}", contextId = "patient-ms")
public interface PatientProxy {

    @GetMapping("v1/api/patients/patientAgeGender/{id}")
    public PatientAgeGenderBean getPatientAgeGender(@PathVariable Integer id) throws ResourceNotFoundException;

}
