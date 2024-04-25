package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.mapper.PatientMapper;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.model.Patient;
import com.medilabosolutions.patient.repository.PatientRepository;
import fr.xebia.extras.selma.Selma;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final AddressService addressService;

    private final PatientMapper patientMapper = Selma.builder(PatientMapper.class).build();

    public PatientServiceImpl(PatientRepository patientRepository, AddressService addressService) {
        this.patientRepository = patientRepository;
        this.addressService = addressService;
    }

    @Override
    public List<PatientDto> findAllPatients() {
        return this.patientRepository.findAll().stream().map(patientMapper::asPatientDto).collect(Collectors.toList());
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {

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
    public Optional<Patient> getPatient(PatientDto patientDto) {
        return this.patientRepository.findByLastNameAndFirstNameAndDateOfBirth(patientDto.getLastName(),patientDto.getFirstName(),patientDto.getDateOfBirth());
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
