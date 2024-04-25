package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<Address> getAddressByNumberAndStreet(Integer number, String street) {
        return this.addressRepository.findByNumberAndStreet(number, street);
    }

    @Override
    public Address saveAddress(Address address) {
        return this.addressRepository.save(address);
    }
}
