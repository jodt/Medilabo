package com.medilabosolutions.clientui.validation;

import com.medilabosolutions.clientui.beans.AddressBean;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressBean> {


    @Override
    public boolean isValid(AddressBean address, ConstraintValidatorContext constraintValidatorContext) {

        if (address == null){
            return true;
        }

        Integer streetNumber = address.getNumber();
        String streetName = address.getStreet();

        return (ObjectUtils.isEmpty(streetName) && (streetNumber == null || streetNumber == 0)) || (!ObjectUtils.isEmpty(streetName) && (streetNumber != null && streetNumber != 0));
    }
}
