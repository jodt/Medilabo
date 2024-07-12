package com.medilabosolutions.clientui.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeHttpRequests(authorize ->
                    authorize.anyRequest().authenticated())
            .formLogin(Customizer.withDefaults());

    return http.build();
}

@Bean
    public InMemoryUserDetailsManager userDetailsManager () {

    UserDetails pratician = User.withUsername("doctor")
            .password(passwordEncoder().encode("doctor"))
            .roles("ADMIN")
            .build();

    UserDetails secretary = User.withUsername("secretary")
            .password(passwordEncoder().encode("secretary"))
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(pratician,secretary);

}

@Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

}
