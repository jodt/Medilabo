package com.medilabosolutions.riskreport.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "risk")
@Data
public class RiskTermsProperties {

    private List<String> terms;

}
