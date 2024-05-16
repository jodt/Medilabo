package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.exception.PatientAlreadyRegisteredException;
import com.medilabosolutions.patient.exception.ResouceNotFoundException;
import com.medilabosolutions.patient.mapper.PatientMapper;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.repository.PatientRepository;
import fr.xebia.extras.selma.Selma;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final AddressService addressService;

    private final PatientMapper patientMapper = Selma.builder(PatientMapper.class).build();

    private static final ExampleMatcher SEARCH_CONDITIONS_MATH_ANY = ExampleMatcher
            .matchingAny()
            .withMatcher("dateOfBirth", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("id", "gender", "address", "phoneNumber");

    private static final ExampleMatcher SEARCH_CONDITIONS_MATH_ALL = ExampleMatcher
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
    public List<PatientDto> findPatients(String lastName, String firstName, LocalDate dateOfBirth, boolean matchAll) {

        Patient patient = Patient.builder()
                .lastName(lastName.isEmpty() ? null : lastName)
                .firstName(firstName.isEmpty()? null : firstName)
                .dateOfBirth(dateOfBirth)
                .build();

        Example<Patient> patientExample = Example.of(patient, matchAll ? SEARCH_CONDITIONS_MATH_ALL : SEARCH_CONDITIONS_MATH_ANY);
        List<Patient> patients =  this.patientRepository.findAll(patientExample);

        return patients.stream().map(patientMapper::asPatientDto).collect(Collectors.toList());
    }

    @Override
    public Patient addPatient(PatientDto patientDto) throws PatientAlreadyRegisteredException {

        this.throwExceptionIfPatientAlreadyRegistered(patientDto);

        Patient patientToSave = patientMapper.asPatient(patientDto);
        Optional<Address> addressAlreadyRegistered = Optional.empty();

        if (patientToSave.getAddress() != null) {
            Integer patientAddressNumber = patientToSave.getAddress().getNumber();
            String patientAddressStreet = patientToSave.getAddress().getStreet();

            addressAlreadyRegistered = this.addressService.getAddressByNumberAndStreet(patientAddressNumber, patientAddressStreet);

            addressAlreadyRegistered.ifPresentOrElse(patientToSave::setAddress, () -> {
                Address newAddress = saveNewAddress(patientToSave);
                patientToSave.setAddress(newAddress);
            });
        }

        return this.patientRepository.save(patientToSave);
    }

    @Override
    public Optional<Patient> getPatientByLastNameAndFirstNameAndDateOfBirth(PatientDto patientDto) {
        return this.patientRepository.findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(),patientDto.getFirstName(),patientDto.getDateOfBirth());
    }

    @Override
    public Patient updatePatient(PatientDto patientDto) throws ResouceNotFoundException {
        Patient patientUpdated = patientMapper.asPatient(patientDto);
        Optional<Patient> patientToUpdate = this.patientRepository.findById(patientUpdated.getId());
        if (patientToUpdate.isPresent()){
            patientUpdated.setId(patientToUpdate.get().getId());
            return patientRepository.save(patientUpdated);
        } else {
            throw  new ResouceNotFoundException();
        }
    }

    @Override
    public PatientDto getPatientById(Integer id) throws ResouceNotFoundException {
        return this.patientRepository.findById(id).map(patientMapper::asPatientDto).orElseThrow(ResouceNotFoundException::new);
    }

    private void throwExceptionIfPatientAlreadyRegistered(PatientDto patientDto) throws PatientAlreadyRegisteredException {
        Optional<Patient> patient = this.getPatientByLastNameAndFirstNameAndDateOfBirth(patientDto);
        if(patient.isPresent()){
            throw new PatientAlreadyRegisteredException();
        }
    }

    private Address saveNewAddress(Patient patient) {
        Address newAddress = Address.builder()
                .number(patient.getAddress().getNumber())
                .street(patient.getAddress().getStreet())
                .build();
        this.addressService.saveAddress(newAddress);
        return newAddress;
    }

}
