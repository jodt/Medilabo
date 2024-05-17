package com.medilabosolutions.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResouceNotFoundException extends Exception {

    public ResouceNotFoundException() {
        super();
    }
}
