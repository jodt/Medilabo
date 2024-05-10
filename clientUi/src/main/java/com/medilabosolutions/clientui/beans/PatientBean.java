package com.medilabosolutions.clientui.beans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientBean {

    private Integer id;

    @NotNull
    @NotBlank(message = "LastName is mandatory")
    private String lastName;

    @NotNull
    @NotBlank(message = "FirstName is mandatory")
    private String firstName;

    @NotNull(message = "Date Of Birth is mandatory")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private GenderEnum gender;

    private AddressBean address;

    private String phoneNumber;


}
