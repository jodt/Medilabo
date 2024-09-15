package com.medilabosolutions.clientui.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response response) {
        if(response.status() == 404){
            return new ResourceNotFoundException();
        } else if (response.status() == 409) {
            return new PatientAlreadyRegisteredException();
        }
        return defaultErrorDecoder.decode(invoqueur,response);
    }
}
