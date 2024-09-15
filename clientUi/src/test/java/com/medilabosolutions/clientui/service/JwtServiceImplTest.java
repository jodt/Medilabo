package com.medilabosolutions.clientui.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    JwtEncoder jwtEncoder;

    @Mock
    Authentication authentication;

    @Mock
    GrantedAuthority grantedAuthority;
    @Mock
    Jwt jwt;

    @InjectMocks
    JwtServiceImpl jwtService;


    @Test
    void shouldGenerateJwtToken() {

        when(authentication.getName()).thenReturn("user");

        when(grantedAuthority.getAuthority()).thenReturn("ROLE_USER");
        doReturn(List.of(grantedAuthority)).when(authentication).getAuthorities();

        when(jwt.getTokenValue()).thenReturn("token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtService.generateJwtToken(authentication);

        assertEquals("token", token);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));

    }
}