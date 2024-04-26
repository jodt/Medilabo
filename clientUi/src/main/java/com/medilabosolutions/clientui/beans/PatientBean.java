package com.medilabosolutions.clientui.beans;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientBean {

    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private LocalDate dateOfBirth;

    private GenderEnum gender;

    private AddressBean address;

    private String phoneNumber;


}
