package com.medilabosolutions.clientui.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AddressValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAddress {


    String message() default "The street number and street name fields must be both filled in or both empty";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
