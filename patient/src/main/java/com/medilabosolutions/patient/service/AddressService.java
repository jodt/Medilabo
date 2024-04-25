package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.model.Address;

import java.util.Optional;

public interface AddressService {

    public Optional<Address> getAddressByNumberAndStreet(Integer number, String street);

    public Address saveAddress(Address address);

}
