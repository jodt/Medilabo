package com.medilabosolutions.clientui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatientAlreadyRegisteredException extends Exception {


    public PatientAlreadyRegisteredException() {
        super("Patient already exist");
    }
}
