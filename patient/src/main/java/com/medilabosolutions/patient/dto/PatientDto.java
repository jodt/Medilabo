package com.medilabosolutions.patient.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientDto {

    @NotNull
    private Integer id;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderEnumDto gender;

    private AddressDto address;

    private String phoneNumber;

}
