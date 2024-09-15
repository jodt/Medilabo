package com.medilabosolutions.clientui.validation;

import com.medilabosolutions.clientui.beans.AddressBean;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

/**
 * Validates the address entered in a form using the custom ValidAddress annotation.
 * This validator ensures that both the street name and street number are either provided or both left empty.
 * The address is considered invalid if only one of these fields is filled.
 */

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressBean> {


    /**
     * Validates the address.
     * The address is considered valid if both the street name and street number are empty or null
     * or if both the street name and street number are provided.
     * The address is considered invalid if only one of the two fields is provided.
     *
     * @param address the address to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if valid , false if invalid
     */
    @Override
    public boolean isValid(AddressBean address, ConstraintValidatorContext constraintValidatorContext) {

        if (address == null) {
            return true;
        }

        Integer streetNumber = address.getNumber();
        String streetName = address.getStreet();

        return (ObjectUtils.isEmpty(streetName) && (streetNumber == null || streetNumber == 0)) || (!ObjectUtils.isEmpty(streetName) && (streetNumber != null && streetNumber != 0));
    }
}
