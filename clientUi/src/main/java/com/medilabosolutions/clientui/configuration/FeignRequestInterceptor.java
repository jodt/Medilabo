package com.medilabosolutions.clientui.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The FeignRequestInterceptor is a class used to intercept and modify
 * all outgoing requests made via Feign Clients.
 */

@Slf4j
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * Intercepts every Feign request and adds an Authorization header containing a jwt token.
     * Injects the "Authorization: Bearer <token>" header in each HTTP request.
     *
     * @param template the feign request to modify
     */
    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getDetails() instanceof String) {
            template.header("Authorization", "Bearer " + authentication.getDetails());
        } else {
            log.warn("No token found in security context.");
        }
    }
}
