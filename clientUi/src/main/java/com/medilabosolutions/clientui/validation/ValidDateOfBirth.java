package com.medilabosolutions.clientui.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to validate the patient's date of birth
 */
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateOfBirth {

    String message() default "Invalid date of birth";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
