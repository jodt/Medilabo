package com.medilabosolutions.patient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AddressDto {

    private Integer number;

    private String street;

}
