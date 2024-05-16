package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.PatientBean;
import com.medilabosolutions.clientui.proxies.PatientProxy;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ClientController {

    private final PatientProxy patientProxy;
    private static final String ERROR_MESSAGE = "We encountered a problem";
    private static final String SUCCESS_MESSAGE = "Action completed successfully";

    public ClientController(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @GetMapping("/")
    public String allPatientByCriteria(Model model,
                                       @RequestParam(defaultValue = "") String firstName,
                                       @RequestParam(defaultValue = "") String lastName,
                                       @RequestParam(defaultValue = "") LocalDate dateOfBirth) {

        List<PatientBean> patients = this.patientProxy.getBySearchCriteria(lastName,firstName,dateOfBirth);
        model.addAttribute("patients", patients);
        return ("homePage");
    }

    @GetMapping("/patient/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("newPatient", new PatientBean());
        return ("addPatientPage");
    }

    @PostMapping("/patient/add")
    public String addNewPatient(@Valid @ModelAttribute("newPatient") PatientBean newPatient, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ("addPatientPage");
        }
        try {
            this.patientProxy.addPatient(newPatient);
        } catch (FeignException e){
            return ("redirect:add?error");
        }
        return ("redirect:add?success");
    }

    @GetMapping("/patient/infos/{id}")
    public String getPatientInfos(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes){
        try {
            PatientBean patient = this.patientProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            return ("patientInfosPage");
        } catch (FeignException e) {
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            return "redirect:/";
        }
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdatePatientForm(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            PatientBean patient = this.patientProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            return ("updatePatientPage");
        } catch (FeignException e) {
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            return "redirect:/";
        }
    }

    @PostMapping("/patient/update")
    public String updatePatient(@Valid @ModelAttribute("patient") PatientBean patientUpdated, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return ("updatePatientPage");
        }
        try {
            this.patientProxy.updatePatient(patientUpdated);
            redirectAttributes.addFlashAttribute("successMessage", SUCCESS_MESSAGE);
            return ("redirect:/");
        } catch (FeignException e) {
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            return ("redirect:/");
        }
    }
}
