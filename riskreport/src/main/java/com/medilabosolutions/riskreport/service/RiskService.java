package com.medilabosolutions.riskreport.service;

import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;

public interface RiskService {

    public String calculPatientRisk(Integer id) throws ResourceNotFoundException;

}
