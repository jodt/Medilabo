package com.medilabosolutions.patient.service;

import com.medilabosolutions.patient.dto.AddressDto;
import com.medilabosolutions.patient.model.Address;
import com.medilabosolutions.patient.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;

    @BeforeEach
    void init() {
        address = Address.builder()
                .number(1)
                .street("High st")
                .build();
    }

    @Test
    @DisplayName("Should get address by number and street name")
    void shouldGetAddressByNumberAndStreet() {

        when(this.addressRepository.findByNumberAndStreet(1,"High st")).thenReturn(Optional.ofNullable(address));

        Optional<Address> result = this.addressService.getAddressByNumberAndStreet(1,"High st");

        assertTrue(result.isPresent());
        assertEquals(address,result.get());

        verify(this.addressRepository).findByNumberAndStreet(1, "High st");

    }

    @Test
    @DisplayName("Should save address")
    void shouldSaveAddress() {

        Address adressToSave = Address
                .builder()
                .number(1)
                .street("High st")
                .build();

        when(this.addressRepository.save(adressToSave)).thenReturn(address);

        Address result = this.addressService.saveAddress(address);

        assertNotNull(address);
        assertEquals(address, result);

        verify(this.addressRepository).save(adressToSave);
    }
}