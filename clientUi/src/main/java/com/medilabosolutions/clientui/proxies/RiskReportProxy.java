package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway", url = "${gateway.uri}", contextId = "riskreport-ms")
public interface RiskReportProxy {

    @GetMapping("v1/api/risk/{id}")
    public String getRiskPatient(@PathVariable Integer id) throws ResourceNotFoundException;
}
