package com.medilabosolutions.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.patient.dto.AddressDto;
import com.medilabosolutions.patient.dto.GenderEnumDto;
import com.medilabosolutions.patient.dto.PatientAgeGenderDto;
import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.enums.GenderEnum;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    private Patient patient;

    private PatientDto patientDto;

    private Address address;

    private AddressDto addressDto;

    private final static String URL_PREFIX = "/v1/api/patients";

    @BeforeEach
    void init() {

        address = Address.builder()
                .number(1)
                .street("High st")
                .build();

        addressDto = AddressDto.builder()
                .number(1)
                .street("High st")
                .build();

        patient = Patient.builder()
                .id(1)
                .firstName("Test")
                .lastName("TestPatient")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(address)
                .gender(GenderEnum.M)
                .phoneNumber("000-000-000")
                .build();

        patientDto = PatientDto.builder()
                .id(1)
                .firstName("Test")
                .lastName("TestPatient")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(addressDto)
                .gender(GenderEnumDto.M)
                .phoneNumber("000-000-000")
                .build();
    }

    @Test
    @DisplayName("Should get patient by search criteria")
    void shouldGetPatientBySearchCriteria() throws Exception {

        when(this.patientService.findPatients(eq(""), eq(""), any())).thenReturn(List.of(patientDto));

        mockMvc.perform((get(URL_PREFIX + "/findBySearchCriteria")
                        .param("lastName", "")
                        .param("firstName", "")
                        .param("dateOfBirth", ""))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].lastName").value("TestPatient"))
                .andExpect((jsonPath("$[0].firstName").value("Test")))
                .andExpect((jsonPath("$[0].dateOfBirth").value("2000-01-01")))
                .andExpect((jsonPath("$[0].gender").value("M")))
                .andExpect((jsonPath("$[0].address.number").value(1)))
                .andExpect((jsonPath("$[0].address.street").value("High st")))
                .andExpect((jsonPath("$[0].phoneNumber").value("000-000-000")))
                .andDo(print());

        verify(this.patientService).findPatients(eq(""), eq(""), any());

    }

    @Test
    @DisplayName("Should get patient by id")
    void shouldGetPatientById() throws Exception {

        when(this.patientService.getPatientById(1)).thenReturn(patientDto);

        mockMvc.perform(get(URL_PREFIX + "/findPatientById/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("TestPatient"))
                .andExpect((jsonPath("$.firstName").value("Test")))
                .andExpect((jsonPath("$.dateOfBirth").value("2000-01-01")))
                .andExpect((jsonPath("$.gender").value("M")))
                .andExpect((jsonPath("$.address.number").value(1)))
                .andExpect((jsonPath("$.address.street").value("High st")))
                .andExpect((jsonPath("$.phoneNumber").value("000-000-000")))
                .andDo(print());

        verify(this.patientService).getPatientById(1);
    }

    @Test
    @DisplayName("Should not get patient by id -> patient not found")
    void shouldNotGetPatientById() throws Exception {

        when(this.patientService.getPatientById(1)).thenThrow(ResouceNotFoundException.class);

        mockMvc.perform(get(URL_PREFIX + "/findPatientById/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(this.patientService).getPatientById(1);
    }

    @Test
    @DisplayName("Should add patient")
    void shouldAddPatient() throws Exception {

        when(this.patientService.addPatient(patientDto)).thenReturn(patient);

        mockMvc.perform(post(URL_PREFIX + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("TestPatient"))
                .andExpect((jsonPath("$.firstName").value("Test")))
                .andExpect((jsonPath("$.dateOfBirth").value("2000-01-01")))
                .andExpect((jsonPath("$.gender").value("M")))
                .andExpect((jsonPath("$.address.number").value(1)))
                .andExpect((jsonPath("$.address.street").value("High st")))
                .andExpect((jsonPath("$.phoneNumber").value("000-000-000")))
                .andDo(print());

        verify(this.patientService).addPatient(patientDto);
    }

    @Test
    @DisplayName("Should not add patient -> patient is already registered")
    void shouldNotAddPatient() throws Exception {

        when(this.patientService.addPatient(patientDto)).thenThrow(PatientAlreadyRegisteredException.class);

        mockMvc.perform(post(URL_PREFIX + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(this.patientService).addPatient(patientDto);
    }

    @Test
    @DisplayName("Should update patient")
    void shouldUpdatePatient() throws Exception {

        when(this.patientService.updatePatient(patientDto)).thenReturn(patient);

        mockMvc.perform(put(URL_PREFIX + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("TestPatient"))
                .andExpect((jsonPath("$.firstName").value("Test")))
                .andExpect((jsonPath("$.dateOfBirth").value("2000-01-01")))
                .andExpect((jsonPath("$.gender").value("M")))
                .andExpect((jsonPath("$.address.number").value(1)))
                .andExpect((jsonPath("$.address.street").value("High st")))
                .andExpect((jsonPath("$.phoneNumber").value("000-000-000")))
                .andDo(print());

        verify(this.patientService).updatePatient(patientDto);

    }

    @Test
    @DisplayName("Should not update patient -> patient not found")
    void shouldNotUpdatePatient() throws Exception {

        when(this.patientService.updatePatient(patientDto)).thenThrow(ResouceNotFoundException.class);

        mockMvc.perform(put(URL_PREFIX + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(this.patientService).updatePatient(patientDto);

    }

    @Test
    @DisplayName("Should get patientAgeGender")
    void shouldGetPatientAgeGender() throws Exception {

        PatientAgeGenderDto patientAgeGenderDto = PatientAgeGenderDto.builder()
                .Age(20)
                .gender(GenderEnumDto.M)
                .build();

        when(this.patientService.getPatientWithAgeAndGender(1)).thenReturn(patientAgeGenderDto);

        mockMvc.perform(get(URL_PREFIX + "/patientAgeGender/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.gender").value("M"))
                .andDo(print());

        verify(this.patientService).getPatientWithAgeAndGender(1);

    }


    @Test
    @DisplayName("Should get patientAgeGender -> ResouceNotFoundException")
    void shouldNotGetPatientAgeGender() throws Exception {

        when(this.patientService.getPatientWithAgeAndGender(1)).thenThrow(ResouceNotFoundException.class);

        mockMvc.perform(get(URL_PREFIX + "/patientAgeGender/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isNotFound());

        verify(this.patientService).getPatientWithAgeAndGender(1);

    }
}