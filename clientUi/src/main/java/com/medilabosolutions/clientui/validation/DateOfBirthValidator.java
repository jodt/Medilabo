package com.medilabosolutions.clientui.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

/**
 * Validates the date of birth entered in a form using the custom ValidDateOfBirth annotation.
 * This validator ensures that the patient's age is less than or equal to 130 and
 * the date of birth is not later than today
 */

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    /**
     * Validates the date of birth
     *
     * @param dateOfBirth to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the patient's age is less than or equal to 130 and the date of birth is not later than today
     */
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {

        if (dateOfBirth == null) {
            return true;
        }

        LocalDate now = LocalDate.now();

        Period period = (Period.between(dateOfBirth, now));

        return (!period.isNegative() && period.getYears() <= 130);
    }
}
