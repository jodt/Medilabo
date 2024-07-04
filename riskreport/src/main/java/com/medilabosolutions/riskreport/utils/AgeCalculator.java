package com.medilabosolutions.riskreport.utils;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {

    public static int calculPatientAge(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, today).getYears();
        } else {
            return 0;
        }
    }

}
