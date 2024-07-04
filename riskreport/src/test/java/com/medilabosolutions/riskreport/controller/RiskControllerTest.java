package com.medilabosolutions.riskreport.controller;

import com.medilabosolutions.riskreport.service.RiskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RiskController.class)
class RiskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RiskService riskService;

    @Test
    void getRiskPatient() throws Exception {

        when(this.riskService.calculPatientRisk(anyInt())).thenReturn("None");

        mockMvc.perform(get("/v1/api/risk/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("None"));
    }
}