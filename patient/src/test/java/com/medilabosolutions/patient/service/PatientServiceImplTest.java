package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.AddressDto;
import com.medilabosolutions.patient.dto.GenderEnumDto;
import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.enums.GenderEnum;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AddressService addressService;

    private Patient patient;

    private PatientDto patientDto;

    private Address address;

    private AddressDto addressDto;

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
    @DisplayName("Should find patients by criteria")
    void shouldFindPatients() {

        when(this.patientRepository.findAll(ArgumentMatchers.<Example<Patient>>any())).thenReturn(List.of(patient));

        List<PatientDto> patientList = this.patientService.findPatients(patientDto.getLastName(), "", null);

        assertFalse(patientList.isEmpty());
        assertEquals(1, patientList.size());
        assertEquals(patientDto, patientList.get(0));

        verify(this.patientRepository).findAll(ArgumentMatchers.<Example<Patient>>any());
    }

    @Test
    @DisplayName("Should add patient")
    void shouldAddPatient() throws PatientAlreadyRegisteredException {

        when(this.patientRepository.save(any())).thenReturn(patient);
        when(this.addressService.getAddressByNumberAndStreet(1, "High st")).thenReturn(Optional.ofNullable(address));

        Patient patientResult = this.patientService.addPatient(patientDto);

        assertNotNull(patientResult);
        assertEquals(patient, patientResult);

        verify(this.patientRepository).save(any());
        verify(this.addressService).getAddressByNumberAndStreet(1, "High st");
    }

    @Test
    @DisplayName("Should add patient with address not registered in database")
    void shouldAddPatientWithAddressNotRegisteredInDatabase() throws PatientAlreadyRegisteredException {

        when(this.patientRepository.save(any())).thenReturn(patient);
        when(this.addressService.getAddressByNumberAndStreet(1, "High st")).thenReturn(Optional.empty());
        when(this.addressService.saveAddress(address)).thenReturn(address);

        Patient patientResult = this.patientService.addPatient(patientDto);

        assertNotNull(patientResult);
        assertEquals(patient, patientResult);

        verify(this.patientRepository).save(any());
        verify(this.addressService).getAddressByNumberAndStreet(1, "High st");
    }

    @Test
    @DisplayName("Should not add patient -> patient already registered")
    void shouldNotAddPatient() throws PatientAlreadyRegisteredException {

        when(this.patientService.getPatientByLastNameAndFirstNameAndDateOfBirth(patientDto)).thenReturn(Optional.ofNullable(patient));

        assertThrows(PatientAlreadyRegisteredException.class, () -> this.patientService.addPatient(patientDto));

    }

    @Test
    @DisplayName("Should get patient by last name, first name and date of birth")
    void shouldGetPatientByLastNameAndFirstNameAndDateOfBirth() {

        when(this.patientRepository.findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(), patientDto.getFirstName(), patientDto.getDateOfBirth())).thenReturn(Optional.ofNullable(patient));

        Optional<Patient> patientResult = this.patientService.getPatientByLastNameAndFirstNameAndDateOfBirth(patientDto);

        assertTrue(patientResult.isPresent());
        assertEquals(patient, patientResult.get());

        verify(this.patientRepository).findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(), patientDto.getFirstName(), patientDto.getDateOfBirth());
    }

    @Test
    @DisplayName("Should update patient")
    void shouldUpdatePatient() throws ResouceNotFoundException {

        patientDto.setLastName("TestPatientUpdated");

        Patient patientUpdated = Patient.builder()
                .id(1)
                .firstName("Test")
                .lastName("TestPatientUpdated")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(address)
                .gender(GenderEnum.M)
                .phoneNumber("000-000-000")
                .build();

        when(this.patientRepository.findById(1)).thenReturn(Optional.ofNullable(patient));
        when(this.addressService.getAddressByNumberAndStreet(address.getNumber(),address.getStreet())).thenReturn(Optional.ofNullable(address));
        when(this.patientRepository.save(patientUpdated)).thenReturn(patientUpdated);

        Patient patientResult = this.patientService.updatePatient(patientDto);

        assertNotNull(patientUpdated);
        assertEquals(patientUpdated, patientResult);

        verify(this.patientRepository).findById(1);
        verify(this.patientRepository).save(patientUpdated);

    }

    @Test
    @DisplayName("Should not update patient -> patient not found")
    void shouldNotUpdatePatientNotFound() throws ResouceNotFoundException {

        patientDto.setLastName("TestPatientUpdated");

        Patient patientUpdated = Patient.builder()
                .id(1)
                .firstName("Test")
                .lastName("TestPatientUpdated")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(address)
                .gender(GenderEnum.M)
                .phoneNumber("000-000-000")
                .build();

        when(this.patientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResouceNotFoundException.class, () -> this.patientService.updatePatient(patientDto));

        verify(this.patientRepository).findById(1);

    }

    @Test
    @DisplayName("Should get patient By Id")
    void shouldGetPatientById() throws ResouceNotFoundException {

        when(this.patientRepository.findById(1)).thenReturn(Optional.ofNullable(patient));

        PatientDto patientResult = this.patientService.getPatientById(1);

        assertNotNull(patientResult);
        assertEquals(patientDto, patientResult);

        verify(patientRepository).findById(1);

    }

    @Test
    @DisplayName("should not get patient By Id -> patient not found")
    void shouldNotdGetPatientById() throws ResouceNotFoundException {

        when(this.patientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResouceNotFoundException.class, () -> this.patientService.getPatientById(1));

        verify(this.patientRepository).findById(1);
    }
}