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

    /**
     * This method is used to retrieve an address by street number and name
     * @param StreetNumber
     * @param streetName
     * @return an optional address
     */
    @Override
    public Optional<Address> getAddressByNumberAndStreet(Integer StreetNumber, String streetName) {
        return this.addressRepository.findByNumberAndStreet(StreetNumber, streetName);
    }

    /**
     * This method is used to save a new address
     * @param address with street number and street name
     * @return the saved address
     */
    @Override
    public Address saveAddress(Address address) {
        return this.addressRepository.save(address);
    }
}
