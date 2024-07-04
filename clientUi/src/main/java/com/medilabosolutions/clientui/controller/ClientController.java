package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.NoteBean;
import com.medilabosolutions.clientui.beans.PatientBean;
import com.medilabosolutions.clientui.exceptions.PatientAlreadyRegisteredException;
import com.medilabosolutions.clientui.exceptions.ResourceNotFoundException;
import com.medilabosolutions.clientui.proxies.NoteProxy;
import com.medilabosolutions.clientui.proxies.PatientProxy;
import com.medilabosolutions.clientui.proxies.RiskReportProxy;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class ClientController {

    private final PatientProxy patientProxy;

    private  final NoteProxy noteProxy;

    private final RiskReportProxy riskReportProxy;

    private static final String ERROR_MESSAGE = "We encountered a problem";
    private static final String SERVICE_INACCESSIBLE_MESSAGE = "service is temporarily inaccessible";

    private static final String SUCCESS_MESSAGE = "Action completed successfully";

    public ClientController(PatientProxy patientProxy, NoteProxy noteProxy, RiskReportProxy riskReportProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
        this.riskReportProxy = riskReportProxy;
    }

    @GetMapping("/")
    public String allPatientByCriteria(Model model,
                                       @RequestParam(defaultValue = "") String firstName,
                                       @RequestParam(defaultValue = "") String lastName,
                                       @RequestParam(defaultValue = "") LocalDate dateOfBirth) {
        log.info("GET / called");
        List<PatientBean> patients = this.patientProxy.getBySearchCriteria(lastName, firstName, dateOfBirth);
        model.addAttribute("patients", patients);
        log.info("Home page displayed");
        return ("homePage");
    }

    @GetMapping("/patient/add")
    public String showAddPatientForm(Model model) {
        log.info("GET /patient/add called");
        model.addAttribute("newPatient", new PatientBean());
        log.info("Patient add form displayed");
        return ("addPatientPage");
    }

    @PostMapping("/patient/add")
    public String addNewPatient(@Valid @ModelAttribute("newPatient") PatientBean newPatient, BindingResult bindingResult) {
        log.info("POST /patient/add called -> start process to add patient");
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("Errors in form validation on field {}", fieldError.getField());
            }
            return ("addPatientPage");
        }
        try {
            this.patientProxy.addPatient(newPatient);
        } catch (PatientAlreadyRegisteredException e) {
            log.error("Error : {} ", e.getMessage());
            return ("redirect:add?error");
        }
        log.info("Patient added successfully");
        return ("redirect:add?success");
    }

    @GetMapping("/patient/infos/{id}")
    public String getPatientInfos(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.info("GET /patient/infos/{} -> start process to view information about patient number {}", id, id);
        try {
            PatientBean patient = this.patientProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            model.addAttribute("newNote", new NoteBean());

            try {
                List<NoteBean> patientNotes = this.noteProxy.findNotesByPatientId(id);
                model.addAttribute("notes", patientNotes);
            } catch (FeignException e) {
                log.error(" passe dans le catch : error" + e);
                model.addAttribute("riskServiceErrorMessage", "Note " + SERVICE_INACCESSIBLE_MESSAGE);
            }

            try {
                String riskLevel = this.riskReportProxy.getRiskPatient(id);
                model.addAttribute("risk", riskLevel);
            } catch (FeignException e) {
                log.error(" passe dans le catch : error" + e);
                model.addAttribute("noteServiceErrorMessage", "Risk " + SERVICE_INACCESSIBLE_MESSAGE);
            }

            log.info("Patient information page displayed");
            return ("patientInfosPage");

        } catch (ResourceNotFoundException e) {
            log.error("Error : {} ", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            log.info("Redirect to home page");
            return "redirect:/";
        }
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdatePatientForm(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.info("GET /patient/update/{} called", id);
        try {
            PatientBean patient = this.patientProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            log.info("Patient update form displayed");
            return ("updatePatientPage");
        } catch (ResourceNotFoundException e) {
            log.error("Error : {} ", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            log.info("Redirect to home page");
            return "redirect:/";
        }
    }

    @PostMapping("/patient/update")
    public String updatePatient(@Valid @ModelAttribute("patient") PatientBean patientUpdated, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("POST /patient/update called -> start process to update patient number {}", patientUpdated.getId());
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("Errors in form validation on field {}", fieldError.getField());
            }
            return ("updatePatientPage");
        }
        try {
            this.patientProxy.updatePatient(patientUpdated);
            redirectAttributes.addFlashAttribute("successMessage", SUCCESS_MESSAGE);
            log.info("Patient updated successfully");
            return ("redirect:/");
        } catch (ResourceNotFoundException e) {
            log.error("Error : {} ", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_MESSAGE);
            log.info("Redirect to home page");
            return ("redirect:/");
        }
    }
}
