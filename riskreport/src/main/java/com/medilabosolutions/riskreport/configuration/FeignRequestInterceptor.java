package com.medilabosolutions.riskreport.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    public FeignRequestInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate template) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !ObjectUtils.isEmpty(request.getHeader("Authorization"))) {
            template.header("Authorization", request.getHeader("Authorization"));
        }
    }

}
