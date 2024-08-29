package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientAgeGenderDto;
import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.mapper.PatientMapper;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.repository.PatientRepository;
import com.medilabosolutions.patient.utils.AgeCalculator;
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


    /**
     * Retrieve a list of patients based on the search elements provided.
     * It can either perform on a single parameter like the first name if just this element is provided
     * or several parameters like last name + first name, last name + date of birth.....
     * @param lastName
     * @param firstName
     * @param dateOfBirth
     * @return a list of patients
     */
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

    /**
     * Register a new patient
     * @param patientDto which represents a patient
     * @return registered patient
     * @throws PatientAlreadyRegisteredException if patient already registered
     */
    @Override
    public Patient addPatient(PatientDto patientDto) throws PatientAlreadyRegisteredException {

        this.throwExceptionIfPatientAlreadyRegistered(patientDto);

        Patient patientToSave = patientMapper.asPatient(patientDto);

        this.checkAndSavePatientAddress(patientToSave);

        return this.patientRepository.save(patientToSave);
    }

    /**
     * This method is used to retrieve a patient by first and last name
     * @param patientDto which represents a patient
     * @return a optional patient
     */
    @Override
    public Optional<Patient> getPatientByLastNameAndFirstNameAndDateOfBirth(PatientDto patientDto) {
        return this.patientRepository.findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(),patientDto.getFirstName(),patientDto.getDateOfBirth());
    }

    /**
     * Update a patient
     * @param patientDto which represents a patient
     * @return the updated patient
     * @throws ResouceNotFoundException if patient is not found
     */
    @Override
    public Patient updatePatient(PatientDto patientDto) throws ResouceNotFoundException {
        Patient patientUpdated = patientMapper.asPatient(patientDto);
        log.info("Try to find a patient with id : {}", patientUpdated.getId());
        Optional<Patient> patientToUpdate = this.patientRepository.findById(patientUpdated.getId());
        if (patientToUpdate.isPresent()){
            log.info("Patient with id : {} was found", patientUpdated.getId());
            patientUpdated.setId(patientToUpdate.get().getId());
            this.checkAndSavePatientAddress(patientUpdated);
            log.info("Patient updated successfully");
            return patientRepository.save(patientUpdated);
        } else {
            log.error("Patient with id : {} was not found", patientUpdated.getId());
            throw  new ResouceNotFoundException();
        }
    }

    /**
     * Retrieve a patient by this id
     * @param id which is a unique identifier for each patient
     * @return PatientDto which represents the patient
     * @throws ResouceNotFoundException if patient is not found
     */
    @Override
    public PatientDto getPatientById(Integer id) throws ResouceNotFoundException {
        log.info("Try to find patient with id : {}", id);
        return this.patientRepository.findById(id).map(patientMapper::asPatientDto).orElseThrow(() -> {
            log.error("Patient with id {} not found", id);
            return new ResouceNotFoundException();
        });
    }

    /**
     * Retrieve only the age and gender of a patient
     * @param id which is a unique identifier for each patient
     * @return PatientAgeGenderDto which contains the age and gender of the patient
     * @throws ResouceNotFoundException if patient is not found
     */
    @Override
    public  PatientAgeGenderDto getPatientWithAgeAndGender (Integer id) throws ResouceNotFoundException {
        PatientDto patientDto = this.getPatientById(id);
        int patientAge = AgeCalculator.calculatePatientAge(patientDto.getDateOfBirth());

        return PatientAgeGenderDto.builder()
                .Age(patientAge)
                .gender(patientDto.getGender())
                .build();
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

    /**
     * Checks if the address of the new patient is already registered in the database,
     * otherwise added it before associating it with the patient
     * @param patientToSave new patient
     */
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
                        Address newAddress = this.addressService.saveAddress(patientToSave.getAddress());
                        log.info("Add address to patient");
                        patientToSave.setAddress(newAddress);
                    });
        } else {
            patientToSave.setAddress(null);
        }
    }

}
