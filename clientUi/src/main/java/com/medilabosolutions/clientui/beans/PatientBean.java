package com.medilabosolutions.clientui.beans;

import com.medilabosolutions.clientui.validation.ValidDateOfBirth;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @ValidDateOfBirth
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private GenderEnum gender;

    @Valid
    private AddressBean address;

    private String phoneNumber;


}
