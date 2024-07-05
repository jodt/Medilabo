package com.medilabosolutions.patient.dto;

import com.medilabosolutions.patient.model.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientAgeGenderDto {

    GenderEnumDto gender;

    int Age;

}
