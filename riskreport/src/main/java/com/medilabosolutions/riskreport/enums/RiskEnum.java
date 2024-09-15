package com.medilabosolutions.riskreport.enums;

import lombok.Getter;

@Getter
public enum RiskEnum {

    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("InDanger"),
    EARLY_ONSET("EarlyOnset");

    private final String displayName;

    private RiskEnum(String displayName){
        this.displayName = displayName;
    }
}
