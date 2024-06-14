package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.mapper.PatientMapper;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.repository.PatientRepository;
import fr.xebia.extras.selma.Selma;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final AddressService addressService;

    private final PatientMapper patientMapper = Selma.builder(PatientMapper.class).build();

    private static final ExampleMatcher SEARCH_CONDITIONS = ExampleMatcher
            .matching()
            .withMatcher("dateOfBirth", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "gender", "address", "phoneNumber");

    public PatientServiceImpl(PatientRepository patientRepository, AddressService addressService) {
        this.patientRepository = patientRepository;
        this.addressService = addressService;
    }

    @Override
    public List<PatientDto> findAllPatients() {
        return this.patientRepository.findAll().stream().map(patientMapper::asPatientDto).collect(Collectors.toList());
    }

   @Override
    public List<PatientDto> findPatients(String lastName, String firstName, LocalDate dateOfBirth) {

        log.info("Try to collect patients with patient with lastName : {}, firstName : {}, date of birth : {} ", lastName.isEmpty() ? null : lastName , firstName.isEmpty() ? null : firstName, dateOfBirth);

        Patient patient = Patient.builder()
                .lastName(lastName.isEmpty() ? null : lastName)
                .firstName(firstName.isEmpty()? null : firstName)
                .dateOfBirth(dateOfBirth)
                .build();

        Example<Patient> patientExample = Example.of(patient,SEARCH_CONDITIONS);
        List<Patient> patients =  this.patientRepository.findAll(patientExample);

        return patients.stream().map(patientMapper::asPatientDto).collect(Collectors.toList());
    }

    @Override
    public Patient addPatient(PatientDto patientDto) throws PatientAlreadyRegisteredException {

        this.throwExceptionIfPatientAlreadyRegistered(patientDto);

        Patient patientToSave = patientMapper.asPatient(patientDto);
        Optional<Address> addressAlreadyRegistered = Optional.empty();

        this.checkAndSavePatientAddress(patientToSave);

        return this.patientRepository.save(patientToSave);
    }

    @Override
    public Optional<Patient> getPatientByLastNameAndFirstNameAndDateOfBirth(PatientDto patientDto) {
        return this.patientRepository.findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(),patientDto.getFirstName(),patientDto.getDateOfBirth());
    }

    @Override
    public Patient updatePatient(PatientDto patientDto) throws ResouceNotFoundException {
        Patient patientUpdated = patientMapper.asPatient(patientDto);
        log.info("Try to find a patient with id : {}", patientUpdated.getId());
        Optional<Patient> patientToUpdate = this.patientRepository.findById(patientUpdated.getId());
        if (patientToUpdate.isPresent()){
            log.info("Patient with id : {} was found", patientUpdated.getId());
            patientUpdated.setId(patientToUpdate.get().getId());
            this.checkAndSavePatientAddress(patientUpdated);
            log.info(("Patent updated successfully"));
            return patientRepository.save(patientUpdated);
        } else {
            log.error("Patient with id : {} was not found", patientUpdated.getId());
            throw  new ResouceNotFoundException();
        }
    }

    @Override
    public PatientDto getPatientById(Integer id) throws ResouceNotFoundException {
        log.info("Try to find patient with id : {}", id);
        return this.patientRepository.findById(id).map(patientMapper::asPatientDto).orElseThrow(() -> {
            log.error("Patient with id {} not found", id);
            return new ResouceNotFoundException();
        });
    }

    private void throwExceptionIfPatientAlreadyRegistered(PatientDto patientDto) throws PatientAlreadyRegisteredException {
        log.info("Check if patient is already registered");
        Optional<Patient> patient = this.getPatientByLastNameAndFirstNameAndDateOfBirth(patientDto);
        if(patient.isPresent()){
            log.error("Patient is already registered");
            throw new PatientAlreadyRegisteredException();
        }
        log.info("Patient is not yet registered ");
    }

    private Address saveNewAddress(Patient patient) {
        Address newAddress = Address.builder()
                .number(patient.getAddress().getNumber())
                .street(patient.getAddress().getStreet())
                .build();
        this.addressService.saveAddress(newAddress);
        return newAddress;
    }

    private void checkAndSavePatientAddress(Patient patientToSave) {
        if (patientToSave.getAddress() != null && patientToSave.getAddress().getNumber() != null && !patientToSave.getAddress().getStreet().isEmpty()) {
            Optional<Address> addressAlreadyRegistered;
            log.info("check if the address is already registered");
            Integer patientAddressNumber = patientToSave.getAddress().getNumber();
            String patientAddressStreet = patientToSave.getAddress().getStreet();

            addressAlreadyRegistered = this.addressService.getAddressByNumberAndStreet(patientAddressNumber, patientAddressStreet);
            log.info("The address already exists in database : {}", addressAlreadyRegistered.isPresent());

            addressAlreadyRegistered.ifPresentOrElse(
                    address -> {
                        log.info("Add address to patient");
                        patientToSave.setAddress(address);
                    },
                    () -> {
                        log.info("Save address to database");
                        Address newAddress = saveNewAddress(patientToSave);
                        log.info("Add address to patient");
                        patientToSave.setAddress(newAddress);
                    });
        } else {
            patientToSave.setAddress(null);
        }
    }

}
