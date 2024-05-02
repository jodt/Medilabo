package com.medilabosolutions.clientui.beans;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientBean {
    @NotNull
    private Integer id;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private LocalDate dateOfBirth;

    private GenderEnum gender;

    private AddressBean address;

    private String phoneNumber;


}
