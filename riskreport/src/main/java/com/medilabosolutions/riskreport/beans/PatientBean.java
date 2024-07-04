package com.medilabosolutions.riskreport.beans;

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

    private GenderEnum gender;

    private LocalDate dateOfBirth;

}