package com.medilabosolutions.riskreport.controller;

import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import com.medilabosolutions.riskreport.service.RiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/api/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{id}")
    public String getRiskPatient(@PathVariable Integer id) throws ResourceNotFoundException {
        log.info("Get v1/api/risk/{} called -> start the process to assess patient risk with id {}", id,id );
        String riskLevel = this.riskService.calculPatientRisk(id);
        log.info("Patient risk assessment completed successfully");
        return riskLevel;
    }

}
