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
public class PatientAgeGenderBean {

    private GenderEnum gender;

    int age;

}