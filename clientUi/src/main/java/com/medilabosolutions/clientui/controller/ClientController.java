package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.PatientBean;
import com.medilabosolutions.clientui.proxies.PatientProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ClientController {

    private final PatientProxy patientProxy;

    public ClientController(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @GetMapping("/")
    public String allPatientByCriteria(Model model,
                                       @RequestParam(defaultValue = "") String firstName,
                                       @RequestParam(defaultValue = "") String lastName,
                                       @RequestParam(defaultValue = "") LocalDate dateOfBirth) {

        List<PatientBean> patients = this.patientProxy.getBySerachCriteria(lastName,firstName,dateOfBirth);
        model.addAttribute("patients", patients);
        return ("homePage");
    }

    @GetMapping("/addPatient")
    public String showAddPatientForm() {
        return ("addPatientPage");
    }



}
