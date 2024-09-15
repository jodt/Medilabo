package com.medilabosolutions.clientui.security;

import com.medilabosolutions.clientui.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * The CustomAuthenticationSuccessHandler is used to perform actions after successful authentication.
 * In this case, a jwt token is generated and placed in the authentication object.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    /**
     * Places a jwt generated token to the authentication details   after successful authentication, and
     * redirect the user to the home page.
     *
     * @param request        the HttpServletRequest request
     * @param response       the HttpServletResponse response
     * @param authentication containing the user's authentication information
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String token = this.jwtService.generateJwtToken(authentication);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
            authToken.setDetails(token);
        }

        response.sendRedirect("/");
    }


}
