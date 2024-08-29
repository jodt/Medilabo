package com.medilabosolutions.clientui.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {

        if (dateOfBirth == null) {
            return true;
        }

        LocalDate now = LocalDate.now();

        Period period = (Period.between(dateOfBirth,now));

        return (!period.isNegative() && period.getYears() <= 130);
    }
}
