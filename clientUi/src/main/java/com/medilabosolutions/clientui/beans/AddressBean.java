package com.medilabosolutions.clientui.beans;

import com.medilabosolutions.clientui.validation.ValidAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ValidAddress
public class AddressBean {
    private Integer number;

    private String street;
}
