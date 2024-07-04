package com.medilabosolutions.riskreport.proxies;

import com.medilabosolutions.riskreport.beans.PatientBean;
import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient", url = "http://localhost:9001")
public interface PatientProxy {

    @GetMapping("v1/api/patients/findPatientById/{id}")
    public PatientBean getPatientById(@PathVariable Integer id) throws ResourceNotFoundException;

}
