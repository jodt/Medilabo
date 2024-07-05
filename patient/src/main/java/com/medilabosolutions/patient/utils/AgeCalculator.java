package com.medilabosolutions.patient.utils;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {

    public static int calculatePatientAge(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, today).getYears();
        } else {
            return 0;
        }
    }

}
