package com.medilabosolutions.clientui.beans;

import com.medilabosolutions.clientui.enums.GenderEnum;
import com.medilabosolutions.clientui.validation.ValidDateOfBirth;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 150, message = "Size is limited to 150 characters")
    private String lastName;

    @NotNull
    @NotBlank(message = "FirstName is mandatory")
    @Size(max = 100, message = "Size is limited to 100 characters")
    private String firstName;

    @NotNull(message = "Date Of Birth is mandatory")
    @ValidDateOfBirth
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private GenderEnum gender;

    @Valid
    private AddressBean address;

    @Size(max = 30, message = "Size is limited to 30 characters")
    private String phoneNumber;


}
