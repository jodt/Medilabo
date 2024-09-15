package com.medilabosolutions.clientui.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtKey;

    /**
     * This method is used to configure the security filter chain which is responsible
     * for all security (protection of application URLs, redirection to the login form, etc.)
     * Each request is filtered to check if the user can access the requested URL.
     * URLs can be accessible to everyone, or just if the user has sufficient authorizations or if he is authenticated.
     *
     * @param http
     * @return a securityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/assets/**", "/webjars/**", "/actuator/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(formlogin -> formlogin
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler()));
        return http.build();
    }

    /**
     * Configures two users in memory with their name, password and roles
     *
     * @return InMemoryUserDetailsManager
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails pratician = User.withUsername("doctor")
                .password(passwordEncoder().encode("doctor"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails secretary = User.withUsername("secretary")
                .password(passwordEncoder().encode("secretary"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(pratician, secretary);

    }

    /**
     * Returns a jwtEncoder used to encode a jwt token.
     *
     * @return a jwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder((new ImmutableSecret<>(this.jwtKey.getBytes())));
    }

    /**
     * Returns a BCryptPasswordEncoder used ton encode the user password with Bcrypt.
     *
     * @return a BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Ths method is used to perform actions after successful authentication.
     *
     * @return new instance of CustomSuccessHandler
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}
