package com.medilabosolutions.riskreport.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

/**
 * The FeignRequestInterceptor is a class used to intercept and modify
 * all outgoing requests made via Feign Clients.
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    public FeignRequestInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Intercepts every Feign request and adds an Authorization header containing a jwt token.
     *
     * @param template the feign request to modify
     */
    @Override
    public void apply(RequestTemplate template) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !ObjectUtils.isEmpty(request.getHeader("Authorization"))) {
            template.header("Authorization", request.getHeader("Authorization"));
        }
    }

}
