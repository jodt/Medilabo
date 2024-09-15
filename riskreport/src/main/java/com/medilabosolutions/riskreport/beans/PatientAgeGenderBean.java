package com.medilabosolutions.riskreport.beans;

import com.medilabosolutions.riskreport.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PatientAgeGenderBean {

    private GenderEnum gender;

    int age;

}