package com.medilabosolutions.clientui.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
    public String generateJwtToken(Authentication authentication);
}
