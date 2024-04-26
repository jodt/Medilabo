package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "patient-microservice", url = "localhost:9001")
public interface PatientProxy {

    @GetMapping("v1/api/patients/all")
    public List<PatientBean> getAllPatients ();
}
