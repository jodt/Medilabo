package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.AddressBean;
import com.medilabosolutions.clientui.enums.GenderEnum;
import com.medilabosolutions.clientui.beans.NoteBean;
import com.medilabosolutions.clientui.beans.PatientBean;
import com.medilabosolutions.clientui.exceptions.PatientAlreadyRegisteredException;
import com.medilabosolutions.clientui.exceptions.ResourceNotFoundException;
import com.medilabosolutions.clientui.proxies.NoteProxy;
import com.medilabosolutions.clientui.proxies.PatientProxy;
import com.medilabosolutions.clientui.proxies.RiskReportProxy;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@WithMockUser(username = "doctor", roles = {"USER","ADMIN"})
class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientProxy patientProxy;

    @MockBean
    NoteProxy noteProxy;

    @MockBean
    RiskReportProxy riskReportProxy;

    private PatientBean patient;

    private AddressBean address;

    private NoteBean notes;

    private static final String ERROR_MESSAGE = "We encountered a problem";

    private static final String SERVICE_INACCESSIBLE_MESSAGE = "service is temporarily inaccessible";

    private static final String SUCCESS_MESSAGE = "Action completed successfully";

    @BeforeEach
    void init() {

        address = AddressBean.builder()
                .number(1)
                .street("High st")
                .build();

        patient = PatientBean.builder()
                .id(1)
                .firstName("Test")
                .lastName("TestPatient")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(address)
                .gender(GenderEnum.M)
                .phoneNumber("000-000-000")
                .build();

        notes = NoteBean.builder()
                .patientId(1)
                .content("patient note")
                .build();
    }

    @Test
    @DisplayName("should display the home page with the list of patients")
    void shouldGetAllPatientByCriteria() throws Exception {
        List<PatientBean> patientBeanList = new ArrayList<>(Arrays.asList(patient));
        when(this.patientProxy.getPatientsBySearchCriteria(any(), any(), any())).thenReturn(patientBeanList);

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasSize(1)))
                .andExpect(model().attribute("patients", hasItem(patient)))
                .andExpect(view().name("homePage"));

        verify(this.patientProxy).getPatientsBySearchCriteria(any(),any(),any());
    }

    @Test
    @DisplayName("should display the form to add a patient")
    void shouldShowAddPatientForm() throws Exception {

        this.mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("newPatient"))
                .andExpect(view().name("addPatientPage"));
    }

    @Test
    @DisplayName("should add a new patient")
    void shouldAddNewPatient() throws Exception {

        when(patientProxy.addPatient(patient)).thenReturn(new ResponseEntity<PatientBean>(patient, HttpStatus.CREATED));

        this.mockMvc.perform(post("/patient/add")
                        .with(csrf())
                        .flashAttr("newPatient", patient))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add?success"))
                .andDo(print());

        verify(this.patientProxy).addPatient(patient);
    }

    @Test
    @DisplayName("should not add patient -> fields in error")
    void shouldNotAddNewPatient() throws Exception {

        PatientBean invalidPatient = patient;
        invalidPatient.setLastName(null);

        this.mockMvc.perform(post("/patient/add")
                        .with(csrf())
                        .flashAttr("newPatient", invalidPatient))
                .andExpect(status().isOk())
                .andExpect(view().name("addPatientPage"))
                .andExpect(model().attributeHasFieldErrors("newPatient", "lastName"));

        verify(this.patientProxy, never()).addPatient(patient);
    }

    @Test
    @DisplayName("should not add patient -> patient already registered")
    void shouldNotAddNewPatientAlreadyRegistered() throws Exception {

        when(patientProxy.addPatient(patient)).thenThrow(PatientAlreadyRegisteredException.class);

        this.mockMvc.perform(post("/patient/add")
                        .with(csrf())
                        .flashAttr("newPatient", patient))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add?error"));

        verify(this.patientProxy).addPatient(patient);
    }

    @Test
    @DisplayName("should get patient info")
    void shouldGetPatientInfos() throws Exception {

        when(this.patientProxy.getPatientById(patient.getId())).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(patient.getId())).thenReturn(List.of(notes));
        when(this.riskReportProxy.getRiskPatient(patient.getId())).thenReturn("None");

        this.mockMvc.perform(get("/patient/infos/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("patientInfosPage"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("newNote", new NoteBean()))
                .andExpect(model().attribute("notes", hasSize(1)))
                .andExpect(model().attribute("notes", hasItem(notes)))
                .andExpect(model().attribute("risk", "None"))
                .andDo(print());

        verify(this.patientProxy).getPatientById(patient.getId());
        verify(this.noteProxy).findNotesByPatientId(patient.getId());
        verify(this.riskReportProxy).getRiskPatient(patient.getId());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("should get patient info without note and risk -> user don't have ADMIN role")
    void shouldGetPatientInfosWithoutNoteAndRisk() throws Exception {

        when(this.patientProxy.getPatientById(patient.getId())).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(patient.getId())).thenReturn(List.of(notes));
        when(this.riskReportProxy.getRiskPatient(patient.getId())).thenReturn("None");

        this.mockMvc.perform(get("/patient/infos/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("patientInfosPage"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("newNote", new NoteBean()))
                .andExpect(model().attributeDoesNotExist("notes","risk"))
                .andDo(print());

        verify(this.patientProxy).getPatientById(patient.getId());
        verify(this.noteProxy, never()).findNotesByPatientId(patient.getId());
        verify(this.riskReportProxy, never()).getRiskPatient(patient.getId());

    }

    @Test
    @DisplayName("should not get patient info -> ResourceNotFoundException")
    void shouldNotGetPatientInfos() throws Exception {

        when(this.patientProxy.getPatientById(100)).thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(get("/patient/infos/{id}", 100))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", ERROR_MESSAGE))
                .andExpect(redirectedUrl("/"));

        verify(this.patientProxy).getPatientById(100);
    }

    @Test
    @DisplayName("should get patient with error message when risk ms is down")
    void shouldGetPatientInfosWithoutRisk() throws Exception {

        when(this.patientProxy.getPatientById(patient.getId())).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(patient.getId())).thenReturn(List.of(notes));
        when(this.riskReportProxy.getRiskPatient(patient.getId())).thenThrow(FeignException.class);

        this.mockMvc.perform(get("/patient/infos/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("patientInfosPage"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("newNote", new NoteBean()))
                .andExpect(model().attribute("notes", hasSize(1)))
                .andExpect(model().attribute("notes", hasItem(notes)))
                .andExpect(model().attribute("riskServiceErrorMessage", "Risk " + SERVICE_INACCESSIBLE_MESSAGE))
                .andDo(print());

        verify(this.patientProxy).getPatientById(patient.getId());
        verify(this.noteProxy).findNotesByPatientId(patient.getId());
        verify(this.riskReportProxy).getRiskPatient(patient.getId());
    }

    @Test
    @DisplayName("should get patient with error message when note ms is down")
    void shouldGetPatientInfosWithoutNote() throws Exception {

        when(this.patientProxy.getPatientById(patient.getId())).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(patient.getId())).thenThrow(FeignException.class);
        when(this.riskReportProxy.getRiskPatient(patient.getId())).thenThrow(FeignException.class);

        this.mockMvc.perform(get("/patient/infos/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("patientInfosPage"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("newNote", new NoteBean()))
                .andExpect(model().attribute("noteServiceErrorMessage", "Note " + SERVICE_INACCESSIBLE_MESSAGE))
                .andExpect(model().attribute("riskServiceErrorMessage", "Risk " + SERVICE_INACCESSIBLE_MESSAGE))
                .andDo(print());

        verify(this.patientProxy).getPatientById(patient.getId());
        verify(this.noteProxy).findNotesByPatientId(patient.getId());
        verify(this.riskReportProxy).getRiskPatient(patient.getId());
    }

    @Test
    @DisplayName("should display the patient update form")
    void shouldShowUpdatePatientForm() throws Exception {

        when(this.patientProxy.getPatientById(patient.getId())).thenReturn(patient);

        this.mockMvc.perform(get("/patient/update/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("patient", patient))
                .andExpect(view().name("updatePatientPage"));

        verify(this.patientProxy).getPatientById(patient.getId());
    }

    @Test
    @DisplayName("should not display the patient update form -> ResourceNotFoundException")
    void shouldNotShowUpdatePatientForm() throws Exception {

        when(this.patientProxy.getPatientById(100)).thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(get("/patient/update/{id}", 100))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", ERROR_MESSAGE))
                .andExpect(redirectedUrl("/"));

        verify(this.patientProxy).getPatientById(100);
    }

    @Test
    @DisplayName("should update a patient")
    void updatePatient() throws Exception {
        PatientBean patientUpdated = patient;
        patientUpdated.setLastName("lastNameUpdated");

        when(this.patientProxy.updatePatient(patientUpdated)).thenReturn(new ResponseEntity<>(patientUpdated, HttpStatus.OK));

        this.mockMvc.perform(post("/patient/update")
                        .with(csrf())
                        .flashAttr("patient", patientUpdated))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("successMessage", SUCCESS_MESSAGE))
                .andExpect(redirectedUrl("/"));

        verify(this.patientProxy).updatePatient(patientUpdated);
    }

    @Test
    @DisplayName("should not update a patient -> fields in error")
    void shouldNotUpdatePatient() throws Exception {
        PatientBean invalidPatient = patient;
        invalidPatient.setLastName(null);

        this.mockMvc.perform(post("/patient/update")
                        .with(csrf())
                        .flashAttr("newPatient", invalidPatient))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePatientPage"))
                .andExpect(model().attributeHasFieldErrors("patient", "lastName"));

        verify(this.patientProxy, never()).updatePatient(invalidPatient);
    }

    @Test
    @DisplayName("should not update a patient -> ResourceNotFoundException")
    void shouldNotUpdatePatientException() throws Exception {
        PatientBean patientUpdated = patient;
        patientUpdated.setLastName("lastNameUpdated");

        when(this.patientProxy.updatePatient(patientUpdated)).thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(post("/patient/update")
                        .with(csrf())
                        .flashAttr("patient", patientUpdated))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", ERROR_MESSAGE))
                .andExpect(redirectedUrl("/"));

        verify(this.patientProxy).updatePatient(patientUpdated);
    }
}