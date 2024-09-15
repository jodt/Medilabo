package com.medilabosolutions.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PatientAlreadyRegisteredException extends Exception {

    public PatientAlreadyRegisteredException() {
        super();
    }
}
